package net.dovtech.immersiveplanets;

import api.DebugFile;
import api.common.GameClient;
import api.common.GameCommon;
import api.config.BlockConfig;
import api.entity.StarPlayer;
import api.listener.Listener;
import api.listener.events.draw.PlanetDrawEvent;
import api.listener.events.draw.SegmentDrawEvent;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.mod.config.FileConfiguration;
import api.network.packets.PacketUtil;
import net.dovtech.immersiveplanets.graphics.shape.BoundingSphere;
import net.dovtech.immersiveplanets.network.client.ClientAtmoKillSendPacket;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.data.world.SectorInformation;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.settings.EngineSettings;
import org.schema.schine.graphicsengine.forms.Mesh;
import org.schema.schine.graphicsengine.texture.Material;
import org.schema.schine.graphicsengine.texture.Texture;
import javax.vecmath.Vector3f;
import java.io.IOException;

public class ImmersivePlanets extends StarMod {

    static ImmersivePlanets inst;

    public ImmersivePlanets() {
        inst = this;
    }

    //Other
    private int clientViewDistance = 1000;
    private GameClientState clientState;
    private StarPlayer player;

    //Mesh
    private Mesh planet;
    private Mesh clouds;
    private BoundingSphere outerBoundingSphere;
    private BoundingSphere innerBoundingSphere;

    //Materials
    private Texture cloudsTexture;
    private Material cloudsMaterial;
    private Texture icePlanetTexture;
    private Material icePlanetMaterial;
    private Texture desertPlanetTexture;
    private Material desertPlanetMaterial;


    //Config
    private FileConfiguration config;
    private String[] defaultConfig = {
            "debug-mode: false",
            "sky-offset: 1.15",
            "reduce-draw-on-planets: true",
            "planet-chunk-view-distance: 250"
    };

    //Config Settings
    public boolean debugMode = true;
    public float skyOffset = 1.15f;
    public boolean reduceDrawOnPlanets = true;
    public int planetChunkViewDistance = 250;

    public static void main(String[] args) {

    }

    @Override
    public void onGameStart() {
        inst = this;
        setModName("ImmersivePlanets");
        setModAuthor("Dovtech");
        setModVersion("0.3.2");
        setModDescription("Adds larger and more immersive planets with their own atmospheres and features.");

        if (GameCommon.isOnSinglePlayer() || GameCommon.isDedicatedServer()) initConfig();
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (GameCommon.isClientConnectedToServer() || GameCommon.isOnSinglePlayer()) clientViewDistance = (int) EngineSettings.G_MAX_SEGMENTSDRAWN.getCurrentState();

        registerListeners();

        DebugFile.log("Enabled", this);
    }

    @Override
    public void onBlockConfigLoad(BlockConfig config) {

    }

    private void registerListeners() {

        StarLoader.registerListener(SegmentDrawEvent.class, new Listener<SegmentDrawEvent>() {
            @Override
            public void onEvent(SegmentDrawEvent event) {
                event.getDrawer().showUpdateNotification = false;
            }
        });

        StarLoader.registerListener(PlanetDrawEvent.class, new Listener<PlanetDrawEvent>() {
            @Override
            public void onEvent(PlanetDrawEvent event) {
                if(clientState == null || player == null) {
                    clientState = GameClient.getClientState();
                    player = new StarPlayer(GameClient.getClientPlayerState());
                }
                try {
                    if(player.getSector().getInternalSector().getSectorType().equals(SectorInformation.SectorType.PLANET)) {
                        if (outerBoundingSphere == null || innerBoundingSphere == null) {
                            float skyRadius = event.getDodecahedron().radius * skyOffset;

                            outerBoundingSphere = new BoundingSphere(skyRadius, event.getSphere().getPos());
                            outerBoundingSphere.onInit();
                            //Todo: Add a check so that the client cannot enter the sphere through build mode
                            innerBoundingSphere = new BoundingSphere(Math.max(skyRadius * 0.85f, event.getPlanetInfo().getRadius()), event.getSphere().getPos());
                            innerBoundingSphere.onInit();


                        }

                        if (outerBoundingSphere != null && innerBoundingSphere != null) {
                            if (debugMode) outerBoundingSphere.draw();
                            if (debugMode) innerBoundingSphere.draw();

                            Vector3f clientPos = Controller.getCamera().getPos();
                            if (outerBoundingSphere.isPositionInRadius(clientPos) && !innerBoundingSphere.isPositionInRadius(clientPos)) {
                                if (debugMode) DebugFile.log("[DEBUG] Client is within atmosphere of planet at " + event.getSector().toString());

                                if(clientState.isInAnyBuildMode() && !player.getPlayerState().isGodMode()) {
                                    Vector3f clientBuildModePos = player.getPlayerState().getBuildModePosition().getWorldTransformOnClient().origin;
                                    if(outerBoundingSphere.isPositionInRadius(clientBuildModePos) && !innerBoundingSphere.isPositionInRadius(clientBuildModePos)) {
                                        Vector3f pos = player.getPlayerState().getBuildModePosition().getWorldTransformOnClient().origin;
                                        String oldPosString = pos.toString();
                                        float pushBack = outerBoundingSphere.getDistanceToCenter(pos);
                                        pos.sub(new Vector3f(pushBack, pushBack, pushBack));
                                        String newPosString = pos.toString();
                                        player.getPlayerState().getBuildModePosition().getWorldTransformOnClient().origin.set(pos);
                                        if(debugMode) {
                                            DebugFile.log("[DEBUG] Pushed client build mode camera out of debug sphere");
                                            DebugFile.log("OldPos: " + oldPosString);
                                            DebugFile.log("NewPos : " + newPosString);
                                        }
                                    }
                                }

                                if (player.getCurrentEntity() != null || player.getPlayerState().isSitting() || player.getPlayerState().isGodMode()) {
                                    Vector3f velocity = player.getCurrentEntity().getVelocity();
                                    velocity.scale(0.35f);
                                    player.getCurrentEntity().setVelocity(velocity);
                                } else {
                                    if(GameCommon.isClientConnectedToServer() || GameCommon.isOnSinglePlayer()) {
                                        PacketUtil.sendPacketToServer(new ClientAtmoKillSendPacket(player.getSector().getCoordinates()));
                                    }
                                }
                                if(reduceDrawOnPlanets) {
                                    if (!EngineSettings.G_MAX_SEGMENTSDRAWN.getCurrentState().equals(planetChunkViewDistance)) {
                                        EngineSettings.G_MAX_SEGMENTSDRAWN.setCurrentState(planetChunkViewDistance);
                                        if (debugMode) DebugFile.log("[DEBUG] Set view distance to " + planetChunkViewDistance);
                                    }
                                }
                            } else if (outerBoundingSphere.isPositionInRadius(clientPos) || innerBoundingSphere.isPositionInRadius(clientPos)) {
                                event.getDodecahedron().draw();
                            } else if (!outerBoundingSphere.isPositionInRadius(clientPos) && !innerBoundingSphere.isPositionInRadius(clientPos)) {
                                event.getDodecahedron().cleanUp();

                                if (!EngineSettings.G_MAX_SEGMENTSDRAWN.getCurrentState().equals(clientViewDistance)) {
                                    EngineSettings.G_MAX_SEGMENTSDRAWN.setCurrentState(clientViewDistance);
                                    if (debugMode) DebugFile.log("[DEBUG] Set view distance to " + clientViewDistance);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                    /*
                    if (clouds == null) {
                        clouds = new Mesh();
                        clouds.setPos(event.getSphere().getPos());
                        Vector3f scale = event.getSphere().getScale();
                        scale.scale(3.5f)
                        clouds.setScale(scale);
                        Transform transform = event.getSphere().getTransform();
                        transform.origin.scale(3.5f);
                        clouds.setTransform(transform);
                        clouds.updateBound();
                        clouds.setBoundingSphereRadius(planetRadius);
                        cloudsMaterial = new Material();
                        String cloudsTexturePath = "clouds.png";
                        cloudsTexture = Controller.getTexLoader().getTexture2D(cloudsTexturePath, true);
                        cloudsMaterial.setTexture(cloudsTexture);
                    }

                    if (clouds != null && cloudsMaterial != null && cloudsTexture != null) {
                        clouds.setMaterial(cloudsMaterial);
                        clouds.draw();
                    }


                    if (event.getPlanetType().equals(SectorInformation.PlanetType.ICE)) {
                        if (icePlanetMaterial == null || icePlanetTexture == null) {
                            icePlanetMaterial = new Material();
                            String icePlanetTexturePath = "ice-planet.png";
                            icePlanetTexture = Controller.getTexLoader().getTexture2D(icePlanetTexturePath,true);
                            icePlanetMaterial.setTexture(icePlanetTexture);
                            icePlanetMaterial.attach(0);
                        } else {
                            event.getSphere().setMaterial(icePlanetMaterial);
                            event.getSphere().draw();
                        }
                    } else if (event.getPlanetType().equals(SectorInformation.PlanetType.DESERT)) {
                        if (desertPlanetMaterial == null || desertPlanetTexture == null) {
                            desertPlanetMaterial = new Material();
                            String desertPlanetTexturePath = "desert-planet.png";
                            desertPlanetTexture = Controller.getTexLoader().getTexture2D(desertPlanetTexturePath, true);
                            desertPlanetMaterial.setTexture(desertPlanetTexture);
                            desertPlanetMaterial.attach(0);
                        } else {
                            event.getSphere().setMaterial(desertPlanetMaterial);
                            event.getSphere().draw();
                        }
                    }

                     */
            }
        });

        DebugFile.log("Registered Listeners!", this);
    }

    private void initConfig() {
        this.config = getConfig("config");
        this.config.saveDefault(defaultConfig);

        this.debugMode = config.getBoolean("debug-mode");
        this.skyOffset = (float) config.getDouble("sky-offset");
        this.reduceDrawOnPlanets = config.getBoolean("reduce-draw-on-planets");
        this.planetChunkViewDistance = config.getInt("planet-chunk-view-distance");
    }

    public static ImmersivePlanets getInstance() {
        return inst;
    }

    public FileConfiguration getConfig() {
        return this.config;
    }
}
