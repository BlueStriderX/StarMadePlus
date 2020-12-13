package net.dovtech.starmadeplus;

import api.DebugFile;
import api.common.GameClient;
import api.common.GameCommon;
import api.config.BlockConfig;
import api.element.block.Blocks;
import api.element.block.StarBlock;
import api.listener.Listener;
import api.listener.events.block.SegmentPieceActivateByPlayer;
import api.listener.events.block.SegmentPieceAddByMetadataEvent;
import api.listener.events.block.SegmentPieceAddEvent;
import api.listener.events.block.SegmentPieceRemoveEvent;
import api.listener.events.register.ManagerContainerRegisterEvent;
import api.listener.fastevents.FastListenerCommon;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.mod.config.FileConfiguration;
import api.mod.config.PersistentObjectUtil;
import api.utils.textures.StarLoaderTexture;
import net.dovtech.starmadeplus.blocks.BlockManager;
import net.dovtech.starmadeplus.blocks.decor.DisplayScreen;
import net.dovtech.starmadeplus.blocks.weapons.PlasmaLauncherBarrel;
import net.dovtech.starmadeplus.blocks.weapons.PlasmaLauncherComputer;
import net.dovtech.starmadeplus.data.element.ElementGroup;
import net.dovtech.starmadeplus.data.mesh.MeshDrawData;
import net.dovtech.starmadeplus.listener.RailMoveEvent;
import net.dovtech.starmadeplus.listener.TextDrawEvent;
import net.dovtech.starmadeplus.systems.weapons.plasmalauncher.PlasmaLauncherElementManager;
import net.dovtech.starmadeplus.utils.ElementGroupMeshUtils;
import net.dovtech.starmadeplus.utils.MultiblockUtils;
import org.schema.game.client.controller.PlayerOkCancelInput;
import org.schema.game.client.controller.PlayerTextAreaInput;
import org.schema.game.client.controller.element.world.ClientSegmentProvider;
import org.schema.game.client.controller.manager.ingame.PlayerInteractionControlManager;
import org.schema.game.client.view.GameResourceLoader;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SendableSegmentProvider;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.SpaceStation;
import org.schema.game.common.controller.elements.*;
import org.schema.game.common.controller.elements.combination.*;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.SendableGameState;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementCollectionMesh;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.game.network.objects.remote.RemoteTextBlockPair;
import org.schema.game.network.objects.remote.TextBlockPair;
import org.schema.schine.common.TextCallback;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.ResourceException;
import org.schema.schine.graphicsengine.core.settings.PrefixNotFoundException;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import javax.imageio.ImageIO;
import javax.vecmath.Vector3f;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class StarMadePlus extends StarMod {

    //Other
    private static StarMadePlus instance;

    public enum ImageFilterMode {BLACKLIST, WHITELIST}

    public enum LogType {DEBUG, INFO, WARNING, ERROR, SEVERE}

    public HashMap<String, StarLoaderTexture> textures = new HashMap<>();
    private final String disclaimerMessage =
            "By pressing the ACCEPT button, you hereby acknowledge any and all responsibility for the images you\n" +
                    "post and that the creators of StarMadePlus, the StarLoader team, Schine, the Server or its owners,\n" +
                    "or any other person/entity that is not you cannot be held liable for anything you post.";

    //Logs
    private final String logPath = "moddata/StarMadePlus/logs/";
    private File adminLogFile;
    private File imageLogFile;

    //Config
    private final String[] defaultConfig = {
            "debug-mode: false",
            "max-display-draw-distance: 75",
            "max-image-scale: 15",
            "max-image-offset: 30",
            "image-filter-mode: blacklist",
            "image-filter: porn,hentai,sex,nsfw",
            "tactical-map-max-view-distance: 3"
    };
    private final String[] textureNames = new String[] {
            //Weapons
            "plasma_launcher_computer_front",
            "plasma_launcher_computer_back",
            "plasma_launcher_computer_top",
            "plasma_launcher_computer_bottom",
            "plasma_launcher_computer_sides",
            "plasma_launcher_barrel_front",
            "plasma_launcher_barrel_sides_horizontal",
            "plasma_launcher_barrel_sides_vertical"
    };
    public boolean debugMode = false;
    public int maxDisplayDrawDistance = 50;
    public float maxImageScale = 15;
    public float maxImageOffset = 30;
    public ImageFilterMode imageFilterMode = ImageFilterMode.BLACKLIST;
    public ArrayList<String> imageFilter = new ArrayList<>();
    public int tacticalMapMaxViewDistance = 3;


    public StarMadePlus() {
        instance = this;
    }

    public static void main(String[] args) {
    }

    @Override
    public void onGameStart() {
        setModName("StarMadePlus");
        setModVersion("0.6.2");
        setModAuthor("Dovtech");
        setModDescription("Minor tweaks and additions to improve the base game.");
        setModSMVersion("0.202.108");
    }

    @Override
    public void onBlockConfigLoad(BlockConfig config) {
        //Decor Blocks
        BlockManager.addBlock(new DisplayScreen(config));
        //BlockManager.addBlock(new BlueHoloProjector(config));

        //Multiblocks
        //BlockManager.addBlock(new DeepStorageController(config));
        //BlockManager.addBlock(new DeepStorageDisplay(config));
        //BlockManager.addBlock(new DeepStorageComponent(config));

        //Rails
        //BlockManager.addBlock(new RailSpinnerClockwise(config));
        //BlockManager.addBlock(new RailSpinnerCounterClockwise(config));
        //BlockManager.addBlock(new HiddenRailSpinnerClockwise(config));
        //BlockManager.addBlock(new HiddenRailSpinnerCounterClockwise(config));

        //Systems
        //BlockManager.addBlock(new StellarLifterController(config));
        //new CrystallizerController(config);
        BlockManager.addBlock(new PlasmaLauncherComputer(config));
        BlockManager.addBlock(new PlasmaLauncherBarrel(config));

        //Resources
        //BlockManager.addBlock(new PhotonShard(config));

        BlockConfig.registerComputerModulePair(BlockManager.getFromName("Plasma Launcher Computer").getId(), BlockManager.getFromName("Plasma Launcher Barrel").getId());
    }

    @Override
    public void onEnable() {
        registerOverwrites();
        registerFastListeners();
        registerListeners();
        loadBlockModels();
        loadTextures();
        if (GameCommon.isDedicatedServer() || GameCommon.isOnSinglePlayer()) {
            initConfig();
            createLogs();
        }
        super.onEnable();
    }

    private void registerOverwrites() {
        overwriteClass(CombinationAddOn.class, false);
        overwriteClass(WeaponCombinationAddOn.class, false);
        overwriteClass(MissileCombinationAddOn.class, false);
        overwriteClass(BeamCombinationAddOn.class, false);
        overwriteClass(DamageBeamCombinationAddOn.class, false);
    }

    private void registerFastListeners() {
        FastListenerCommon.textBoxListeners.add(new TextDrawEvent());
        FastListenerCommon.railMoveListeners.add(new RailMoveEvent());
    }

    private void registerListeners() {
        StarLoader.registerListener(ManagerContainerRegisterEvent.class, new Listener<ManagerContainerRegisterEvent>() {
            @Override
            public void onEvent(ManagerContainerRegisterEvent event) {
                event.addModuleCollection(new ManagerModuleCollection(new PlasmaLauncherElementManager(event.getSegmentController()), BlockManager.getFromName("Plasma Launcher Computer").getId(), BlockManager.getFromName("Plasma Launcher Barrel").getId()));
            }
        }, this);

        StarLoader.registerListener(SegmentPieceActivateByPlayer.class, new Listener<SegmentPieceActivateByPlayer>() {
            @Override
            public void onEvent(final SegmentPieceActivateByPlayer event) {
                final SegmentPiece piece = event.getSegmentPiece();
                if (GameCommon.isDedicatedServer() || GameCommon.isOnSinglePlayer()) {
                    if (piece.getType() == Blocks.DISPLAY_MODULE.getId()) {
                        final PlayerState player = event.getPlayer();
                        ArrayList<Object> playerList = PersistentObjectUtil.getObjects(StarMadePlus.getInstance(), String.class);
                        boolean acceptedDisclaimer = false;
                        for (Object object : playerList) {
                            String string = (String) object;
                            if (string.equals("[ACCEPTED DISCLAIMER]: " + player.getName())) { //Player has already accepted disclaimer
                                acceptedDisclaimer = true;
                                String text = piece.getSegment().getSegmentController().getTextMap().get(ElementCollection.getIndex4(piece.getAbsoluteIndex(), piece.getOrientation()));
                                logImage(text, event.getPlayer());
                                break;
                            }
                        }
                        if (!acceptedDisclaimer) { //Player has not accepted disclaimer
                            PlayerOkCancelInput input = new PlayerOkCancelInput("Disclaimer Popup", GameClient.getClientState(), "Accept Disclaimer", disclaimerMessage) {
                                @Override
                                public void onDeactivate() {
                                    pressedSecondOption();
                                }

                                @Override
                                public void pressedOK() { //Player accepts disclaimer
                                    PersistentObjectUtil.addObject(StarMadePlus.getInstance(), "[ACCEPTED DISCLAIMER]: " + player.getName());
                                    PersistentObjectUtil.save(StarMadePlus.getInstance());
                                    String text = piece.getSegment().getSegmentController().getTextMap().get(ElementCollection.getIndex4(piece.getAbsoluteIndex(), piece.getOrientation()));
                                    logImage(text, event.getPlayer());
                                }

                                @Override
                                public void pressedSecondOption() { //Player does not accept disclaimer
                                    long index = piece.getTextBlockIndex();
                                    int pos = -1;
                                    for (int i = 0; i < piece.getSegmentController().getTextBlocks().size(); i++) {
                                        if (piece.getSegmentController().getTextBlocks().get(i) == index) {
                                            pos = i;
                                            break;
                                        }
                                    }
                                    if (pos != -1) piece.getSegmentController().getTextBlocks().remove(pos);
                                    event.setCanceled(true);
                                }
                            };
                            input.setDeactivateOnEscape(false);
                            input.getInputPanel().onInit();
                            input.getInputPanel().setCancelButton(false);

                            input.getInputPanel().setOkButton(true);
                            input.getInputPanel().setOkButtonText("ACCEPT");

                            input.getInputPanel().setSecondOptionButton(true);
                            input.getInputPanel().setSecondOptionButtonText("DECLINE");

                            input.getInputPanel().background.setPos(470.0F, 35.0F, 0.0F);
                            input.getInputPanel().background.setWidth((float) (GLFrame.getWidth() - 435));
                            input.getInputPanel().background.setHeight((float) (GLFrame.getHeight() - 70));
                            input.activate();
                        }

                    } else if (piece.getType() == Objects.requireNonNull(BlockManager.getFromName("Display Screen")).blockInfo.getId()) {

                        final PlayerState player = event.getPlayer();
                        ArrayList<Object> playerList = PersistentObjectUtil.getObjects(StarMadePlus.getInstance(), String.class);
                        boolean acceptedDisclaimer = false;
                        for (Object object : playerList) {
                            String string = (String) object;
                            if (string.equals("[ACCEPTED DISCLAIMER]: " + player.getName())) { //Player has already accepted disclaimer
                                acceptedDisclaimer = true;
                                String text = piece.getSegment().getSegmentController().getTextMap().get(ElementCollection.getIndex4(piece.getAbsoluteIndex(), piece.getOrientation()));
                                logImage(text, event.getPlayer());
                                break;
                            }
                        }
                        if (!acceptedDisclaimer) { //Player has not accepted disclaimer
                            PlayerOkCancelInput input = new PlayerOkCancelInput("Disclaimer Popup", GameClient.getClientState(), "Accept Disclaimer", disclaimerMessage) {
                                @Override
                                public void onDeactivate() {
                                    pressedSecondOption();
                                }

                                @Override
                                public void pressedOK() { //Player accepts disclaimer
                                    PersistentObjectUtil.addObject(StarMadePlus.getInstance(), "[ACCEPTED DISCLAIMER]: " + player.getName());
                                    PersistentObjectUtil.save(StarMadePlus.getInstance());
                                    String text = piece.getSegment().getSegmentController().getTextMap().get(ElementCollection.getIndex4(piece.getAbsoluteIndex(), piece.getOrientation()));
                                    logImage(text, event.getPlayer());
                                }

                                @Override
                                public void pressedSecondOption() { //Player does not accept disclaimer
                                    long index = piece.getTextBlockIndex();
                                    int pos = -1;
                                    for (int i = 0; i < piece.getSegmentController().getTextBlocks().size(); i++) {
                                        if (piece.getSegmentController().getTextBlocks().get(i) == index) {
                                            pos = i;
                                            break;
                                        }
                                    }
                                    if (pos != -1) piece.getSegmentController().getTextBlocks().remove(pos);
                                    event.setCanceled(true);
                                }
                            };
                            input.setDeactivateOnEscape(false);
                            input.getInputPanel().onInit();
                            input.getInputPanel().setCancelButton(false);

                            input.getInputPanel().setOkButton(true);
                            input.getInputPanel().setOkButtonText("ACCEPT");

                            input.getInputPanel().setSecondOptionButton(true);
                            input.getInputPanel().setSecondOptionButtonText("DECLINE");

                            input.getInputPanel().background.setPos(470.0F, 35.0F, 0.0F);
                            input.getInputPanel().background.setWidth((float) (GLFrame.getWidth() - 435));
                            input.getInputPanel().background.setHeight((float) (GLFrame.getHeight() - 70));
                            input.activate();
                        }

                        final PlayerInteractionControlManager cm = event.getControlManager();

                        String text = piece.getSegment().getSegmentController().getTextMap().get(ElementCollection.getIndex4(piece.getAbsoluteIndex(), piece.getOrientation()));
                        logImage(text, event.getPlayer());

                        if (text == null) {
                            text = "";
                        }

                        final PlayerTextAreaInput t = new PlayerTextAreaInput("EDIT_DISPLAY_BLOCK_POPUP", cm.getState(), 400, 300, SendableGameState.TEXT_BLOCK_LIMIT, SendableGameState.TEXT_BLOCK_LINE_LIMIT + 1, "Edit Holo Projector",
                                "",
                                text, FontLibrary.FontSize.SMALL) {
                            @Override
                            public void onDeactivate() {
                                cm.suspend(false);
                            }

                            @Override
                            public String[] getCommandPrefixes() {
                                return null;
                            }

                            @Override
                            public boolean onInput(String entry) {
                                SendableSegmentProvider ss = ((ClientSegmentProvider) piece.getSegment().getSegmentController().getSegmentProvider()).getSendableSegmentProvider();

                                TextBlockPair f = new TextBlockPair();

                                f.block = ElementCollection.getIndex4(piece.getAbsoluteIndex(), piece.getOrientation());
                                f.text = entry;
                                System.err.println("[CLIENT]Text entry:\n\"" + f.text + "\"");
                                ss.getNetworkObject().textBlockResponsesAndChangeRequests.add(new RemoteTextBlockPair(f, false));

                                return true;
                            }

                            @Override
                            public String handleAutoComplete(String s, TextCallback callback, String prefix) throws PrefixNotFoundException {
                                return null;
                            }

                            @Override
                            public boolean isOccluded() {
                                return false;
                            }


                            @Override
                            public void onFailedTextCheck(String msg) {
                            }


                        };

                        t.getTextInput().setAllowEmptyEntry(true);
                        t.getInputPanel().onInit();
                        t.activate();
                    }
                }
            }
        }, this);

        StarLoader.registerListener(SegmentPieceAddEvent.class, new Listener<SegmentPieceAddEvent>() {
            @Override
            public void onEvent(SegmentPieceAddEvent event) {
                StarBlock newBlock = new StarBlock(event.getSegmentController().getSegmentBuffer().getPointUnsave(event.getAbsIndex()));
                if (newBlock.getId() == BlockManager.getFromName("Holo Table").getId()) {
                    ElementGroup elementGroup = MultiblockUtils.getElementGroup(newBlock, MultiblockUtils.MultiblockType.SQUARE_FLAT);
                    if (elementGroup != null) {
                        ArrayList<StarBlock> connections = elementGroup.getConnections(newBlock.getId());
                        if (connections != null && connections.size() > 0) {
                            ArrayList<ElementCollectionMesh> meshes = new ArrayList<>();
                            SegmentController segmentController = newBlock.getInternalSegmentPiece().getSegmentController();
                            ManagerContainer manager = null;
                            if(segmentController.getType().equals(SimpleTransformableSendableObject.EntityType.SHIP)) {
                                manager = new ShipManagerContainer(segmentController.getState(), (Ship) segmentController);
                            } else if(segmentController.getType().equals(SimpleTransformableSendableObject.EntityType.SPACE_STATION)) {
                                manager = new SpaceStationManagerContainer(segmentController.getState(), (SpaceStation) segmentController);
                            }

                            if(manager != null) {
                                for (StarBlock connection : connections) {
                                    ElementCollectionMesh mesh = null;
                                    if(connection.getId() == Blocks.SHIELD_CAPACITOR.getId() || connection.getId() == Blocks.SHIELD_RECHARGER.getId()) {
                                        ShieldAddOn shieldAddon = ((ShieldContainerInterface) segmentController).getShieldAddOn();
                                        if(shieldAddon != null && shieldAddon.getShieldLocalAddOn() != null) {
                                            if(connection.getId() == Blocks.SHIELD_CAPACITOR.getId()) {
                                                mesh = shieldAddon.sc.getShieldCapacityManager().getInstance().getMesh();
                                            } else if(connection.getId() == Blocks.SHIELD_RECHARGER.getId()) {
                                                mesh = shieldAddon.sc.getShieldRegenManager().getInstance().getMesh();
                                            }
                                        }
                                    } else if(connection.getId() == Blocks.REACTOR_POWER.getId()) {
                                        mesh = manager.getMainReactor().getInstance().getMesh();
                                    } else if(connection.getId() == Blocks.REACTOR_STABILIZER.getId()) {
                                        mesh = manager.getStabilizer().getInstance().getMesh();
                                    }
                                    if(mesh != null && !meshes.contains(mesh)) {
                                        meshes.add(mesh);
                                    }
                                }

                                try {
                                    for (ElementCollectionMesh mesh : meshes) {
                                        Field minField = mesh.getClass().getDeclaredField("min");
                                        Field maxField = mesh.getClass().getDeclaredField("max");
                                        minField.setAccessible(true);
                                        maxField.setAccessible(true);

                                        Vector3f min = (Vector3f) minField.get(mesh);
                                        Vector3f max = (Vector3f) maxField.get(mesh);

                                        MeshDrawData drawData;
                                        if(ElementGroupMeshUtils.elementGroupMeshes.containsKey(elementGroup)) {
                                            drawData = ElementGroupMeshUtils.elementGroupMeshes.get(elementGroup);
                                            ElementGroupMeshUtils.elementGroupMeshes.remove(elementGroup);
                                        } else {
                                            drawData = new MeshDrawData(elementGroup);
                                        }

                                        min.set(drawData.getDrawMin().toVector3f());
                                        max.set(drawData.getDrawMax().toVector3f());
                                        minField.set(mesh, min);
                                        maxField.set(mesh, max);
                                        ElementGroupMeshUtils.elementGroupMeshes.put(elementGroup, drawData);
                                        mesh.markDraw();
                                        mesh.draw();
                                    }
                                } catch (NoSuchFieldException | IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else if (newBlock.getId() == Objects.requireNonNull(BlockManager.getFromName("Display Screen")).blockInfo.getId()) {
                    long indexAndOrientation = ElementCollection.getIndex4(event.getAbsIndex(), event.getOrientation());
                    event.getSegmentController().getTextBlocks().add(indexAndOrientation);
                }
            }
        }, this);

        StarLoader.registerListener(SegmentPieceRemoveEvent.class, new Listener<SegmentPieceRemoveEvent>() {
            @Override
            public void onEvent(SegmentPieceRemoveEvent event) {
                if (event.getType() == Objects.requireNonNull(BlockManager.getFromName("Display Screen")).blockInfo.getId()) {
                    Segment segment = event.getSegment();
                    long absoluteIndex = segment.getAbsoluteIndex(event.getX(), event.getY(), event.getZ());
                    long indexAndOrientation = ElementCollection.getIndex4(absoluteIndex, event.getOrientation());
                    event.getSegment().getSegmentController().getTextBlocks().remove(indexAndOrientation);
                    event.getSegment().getSegmentController().getTextMap().remove(indexAndOrientation);
                }
            }
        }, this);

        StarLoader.registerListener(SegmentPieceAddByMetadataEvent.class, new Listener<SegmentPieceAddByMetadataEvent>() {
            @Override
            public void onEvent(SegmentPieceAddByMetadataEvent event) {
                if (event.getType() == Objects.requireNonNull(BlockManager.getFromName("Display Screen")).blockInfo.getId()) {
                    event.getSegment().getSegmentController().getTextBlocks().add(event.getIndexAndOrientation());
                }
            }
        }, this);
    }

    private void initConfig() {
        FileConfiguration config = getConfig("config");
        config.saveDefault(defaultConfig);

        this.debugMode = config.getConfigurableBoolean("debug-mode", false);
        this.maxDisplayDrawDistance = config.getConfigurableInt("max-display-draw-distance", 75);
        this.maxImageScale = config.getConfigurableFloat("max-image-scale", 15);
        this.maxImageOffset = config.getConfigurableFloat("max-image-offset", 30);
        this.imageFilterMode = ImageFilterMode.valueOf(config.getString("image-filter-mode").toUpperCase());
        this.imageFilter = config.getList("image-filter");
        this.tacticalMapMaxViewDistance = config.getInt("tactical-map-max-view-distance");

        DebugFile.log("Loaded Config", this);
    }

    private void createLogs() {
        Date dateTime = new Date();
        File logFolder = new File(logPath);
        try {
            if (!logFolder.exists()) logFolder.mkdirs();
            (adminLogFile = new File(logPath + "adminLog_" + dateTime.toString() + ".log")).createNewFile();
            (imageLogFile = new File(logPath + "imageLog_" + dateTime.toString() + ".log")).createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            DebugFile.logError(new Throwable("Exception encountered while trying to create log files in " + logPath + "!"), this);
        }
    }

    private void loadBlockModels() {
        final GameResourceLoader resLoader = (GameResourceLoader) Controller.getResLoader();

        String[] models = new String[] {
                "displayscreen"
        };

        for (final String model : models) {
            StarLoaderTexture.runOnGraphicsThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        resLoader.getMeshLoader().loadModMesh(StarMadePlus.this, model, StarMadePlus.class.getResourceAsStream("resource/models/" + model + ".zip"), null);
                    } catch (ResourceException | IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void loadTextures() {
        try {
            for(String textureName : textureNames) {
                textures.put(textureName, StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream("resource/textures/blocks/" + textureName + ".png"))));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logAdmin(String message, LogType logType) {
        if (GameCommon.isDedicatedServer() || GameCommon.isOnSinglePlayer()) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(adminLogFile, true));
                switch (logType) {
                    case DEBUG:
                        if (debugMode) writer.append("\n[DEBUG]: ").append(message);
                        break;
                    case INFO:
                        writer.append("\n[INFO]: ").append(message);
                        break;
                    case WARNING:
                        writer.append("\n[WARNING]: ").append(message);
                        break;
                    case ERROR:
                        writer.append("\n[ERROR]: ").append(message);
                        break;
                    case SEVERE:
                        writer.append("\n[SEVERE]: ").append(message);
                        break;
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                DebugFile.logError(new Throwable("Exception encountered while trying to write to admin log!"), this);
            }
        }
    }

    public void logImage(String text, PlayerState player) {
        if (GameCommon.isDedicatedServer() || GameCommon.isOnSinglePlayer()) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(imageLogFile, true));
                writer.append(player.getName()).append(" posted an image ").append(text).append("on a display module.");
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                DebugFile.logError(new Throwable("Exception encountered while trying to write to image log!"), this);
            }
        }
    }

    public static StarMadePlus getInstance() {
        return instance;
    }
}