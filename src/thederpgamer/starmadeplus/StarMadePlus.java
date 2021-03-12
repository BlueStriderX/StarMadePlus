package thederpgamer.starmadeplus;

import api.DebugFile;
import api.common.GameClient;
import api.common.GameCommon;
import api.config.BlockConfig;
import api.listener.Listener;
import api.listener.events.block.SegmentPieceActivateByPlayer;
import api.listener.events.block.SegmentPieceAddByMetadataEvent;
import api.listener.events.block.SegmentPieceAddEvent;
import api.listener.events.block.SegmentPieceRemoveEvent;
import api.listener.events.player.PlayerJoinWorldEvent;
import api.listener.fastevents.FastListenerCommon;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.mod.config.FileConfiguration;
import api.mod.config.PersistentObjectUtil;
import api.utils.textures.StarLoaderTexture;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.schine.resource.ResourceLoader;
import thederpgamer.starmadeplus.blocks.BlockManager;
import thederpgamer.starmadeplus.blocks.decor.BlueHoloProjector;
import thederpgamer.starmadeplus.blocks.decor.DisplayScreen;
import thederpgamer.starmadeplus.blocks.factory.CrystallizerController;
import thederpgamer.starmadeplus.blocks.multiblocks.deepstorage.DeepStorageComponent;
import thederpgamer.starmadeplus.blocks.multiblocks.deepstorage.DeepStorageController;
import thederpgamer.starmadeplus.blocks.multiblocks.deepstorage.DeepStorageDisplay;
import thederpgamer.starmadeplus.blocks.rails.HiddenRailSpinnerClockwise;
import thederpgamer.starmadeplus.blocks.rails.HiddenRailSpinnerCounterClockwise;
import thederpgamer.starmadeplus.blocks.rails.RailSpinnerClockwise;
import thederpgamer.starmadeplus.blocks.rails.RailSpinnerCounterClockwise;
import thederpgamer.starmadeplus.blocks.resources.PhotonShard;
import thederpgamer.starmadeplus.blocks.systems.StellarLifterController;
import thederpgamer.starmadeplus.data.element.BlockSegment;
import thederpgamer.starmadeplus.data.element.ElementGroup;
import thederpgamer.starmadeplus.data.mesh.MeshDrawData;
import thederpgamer.starmadeplus.data.player.PlayerData;
import thederpgamer.starmadeplus.data.server.ServerDatabase;
import thederpgamer.starmadeplus.listener.RailMoveEvent;
import thederpgamer.starmadeplus.listener.TextDrawEvent;
import thederpgamer.starmadeplus.utils.ElementGroupMeshUtils;
import thederpgamer.starmadeplus.utils.MultiblockUtils;
import org.schema.game.client.controller.PlayerOkCancelInput;
import org.schema.game.client.controller.PlayerTextAreaInput;
import org.schema.game.client.controller.element.world.ClientSegmentProvider;
import org.schema.game.client.controller.manager.ingame.PlayerInteractionControlManager;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.SendableSegmentProvider;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.SpaceStation;
import org.schema.game.common.controller.elements.*;
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
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.ResourceException;
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

    //Instance
    public static void main(String[] args) {

    }
    public StarMadePlus() {
    }
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
    public boolean debugMode = false;
    public int maxDisplayDrawDistance = 50;
    public float maxImageScale = 15;
    public float maxImageOffset = 30;
    public ImageFilterMode imageFilterMode = ImageFilterMode.BLACKLIST;
    public ArrayList<String> imageFilter = new ArrayList<>();
    public int tacticalMapMaxViewDistance = 3;

    //Resources
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
    private final String[] modelNames = new String[] {
            "display_screen"
    };

    @Override
    public void onEnable() {
        instance = this;
        initConfig();
        createLogs();
        registerFastListeners();
        registerListeners();
    }

    @Override
    public void onResourceLoad(ResourceLoader loader) {
        for(String textureName : textureNames) {
            try {
                textures.put(textureName, StarLoaderTexture.newBlockTexture(ImageIO.read(getJarResource("thederpgamer/starmadeplus/resources/textures/blocks/" + textureName + ".png"))));
            } catch(IOException exception) {
                exception.printStackTrace();
            }
        }

        for(String model : modelNames) {
            try {
                loader.getMeshLoader().loadModMesh(this, model, getJarResource("thederpgamer/starmadeplus/resources/models/blocks/" + model + ".zip"), null);
            } catch(ResourceException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBlockConfigLoad(BlockConfig config) {
        //Decor Blocks
        BlockManager.addBlock(new DisplayScreen());
        BlockManager.addBlock(new BlueHoloProjector());

        //Multiblocks
        BlockManager.addBlock(new DeepStorageController());
        BlockManager.addBlock(new DeepStorageDisplay());
        BlockManager.addBlock(new DeepStorageComponent());

        //Rails
        BlockManager.addBlock(new RailSpinnerClockwise());
        BlockManager.addBlock(new RailSpinnerCounterClockwise());
        BlockManager.addBlock(new HiddenRailSpinnerClockwise());
        BlockManager.addBlock(new HiddenRailSpinnerCounterClockwise());

        //Systems
        BlockManager.addBlock(new StellarLifterController());

        //Factories
        new CrystallizerController();

        //Resources
        BlockManager.addBlock(new PhotonShard());
    }

    private void registerFastListeners() {
        FastListenerCommon.textBoxListeners.add(new TextDrawEvent());
        FastListenerCommon.railMoveListeners.add(new RailMoveEvent());
    }

    private void registerListeners() {
        StarLoader.registerListener(PlayerJoinWorldEvent.class, new Listener<PlayerJoinWorldEvent>() {
            @Override
            public void onEvent(PlayerJoinWorldEvent event) {
                if(!ServerDatabase.playerExists(event.getPlayerName())) ServerDatabase.addNewPlayerData(event.getPlayerName());
            }
        }, this);

        StarLoader.registerListener(SegmentPieceActivateByPlayer.class, new Listener<SegmentPieceActivateByPlayer>() {
            @Override
            public void onEvent(final SegmentPieceActivateByPlayer event) {
                final SegmentPiece piece = event.getSegmentPiece();
                if (GameCommon.isDedicatedServer() || GameCommon.isOnSinglePlayer()) {
                    if (piece.getType() == ElementKeyMap.TEXT_BOX) {
                        final PlayerState player = event.getPlayer();
                        ArrayList<Object> playerList = PersistentObjectUtil.getObjects(StarMadePlus.getInstance().getSkeleton(), PlayerData.class);
                        boolean acceptedDisclaimer = false;
                        for(Object object : playerList) {
                            PlayerData playerData = (PlayerData) object;
                            if(playerData.playerName.equals(player.getName()) && playerData.acceptedDisclaimer) { //Player has already accepted disclaimer
                                acceptedDisclaimer = true;
                                String text = piece.getSegment().getSegmentController().getTextMap().get(ElementCollection.getIndex4(piece.getAbsoluteIndex(), piece.getOrientation()));
                                logImage(text, event.getPlayer());
                                break;
                            }
                        }

                        if(!acceptedDisclaimer) { //Player has not accepted disclaimer
                            PlayerOkCancelInput input = new PlayerOkCancelInput("Disclaimer Popup", GameClient.getClientState(), "Accept Disclaimer", disclaimerMessage) {
                                @Override
                                public void onDeactivate() {
                                    pressedSecondOption();
                                }

                                @Override
                                public void pressedOK() { //Player accepts disclaimer
                                    ArrayList<Object> dataObjectList = PersistentObjectUtil.getObjects(getSkeleton(), PlayerData.class);
                                    for(Object dataObject : dataObjectList) {
                                        PlayerData pData = (PlayerData) dataObject;
                                        if(pData.playerName.equals(player.getName())) {
                                            pData.acceptedDisclaimer = true;
                                            PersistentObjectUtil.removeObject(getSkeleton(), pData);
                                            PersistentObjectUtil.addObject(getSkeleton(), pData);
                                            PersistentObjectUtil.save(getSkeleton());
                                            break;
                                        }
                                    }
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

                        PlayerData playerData = ServerDatabase.getPlayerData(event.getPlayer().getName());
                        if(playerData.acceptedDisclaimer) {
                            String text = piece.getSegment().getSegmentController().getTextMap().get(ElementCollection.getIndex4(piece.getAbsoluteIndex(), piece.getOrientation()));
                            logImage(text, event.getPlayer());
                        } else {
                            PlayerOkCancelInput input = new PlayerOkCancelInput("Disclaimer Popup", GameClient.getClientState(), "Accept Disclaimer", disclaimerMessage) {
                                @Override
                                public void onDeactivate() {
                                    pressedSecondOption();
                                }

                                @Override
                                public void pressedOK() { //Player accepts disclaimer
                                    PlayerData playerData = ServerDatabase.getPlayerData(event.getPlayer().getName());
                                    playerData.acceptedDisclaimer = true;
                                    ServerDatabase.updatePlayerData(playerData);

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
                            public String handleAutoComplete(String s, TextCallback callback, String prefix) {
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
                BlockSegment blockElement = BlockSegment.fromEvent(event);
                if(blockElement.getId() == Objects.requireNonNull(BlockManager.getFromName("Holo Table")).getId()) {
                    ElementGroup elementGroup = MultiblockUtils.getElementGroup(blockElement, MultiblockUtils.MultiblockType.SQUARE_FLAT);
                    if(elementGroup != null) {
                        ArrayList<BlockSegment> connections = elementGroup.getConnections(blockElement);
                        if(connections != null && connections.size() > 0) {
                            ArrayList<ElementCollectionMesh> meshes = new ArrayList<>();
                            SegmentController segmentController = blockElement.getEntity();
                            ManagerContainer manager = null;
                            if(segmentController.getType().equals(SimpleTransformableSendableObject.EntityType.SHIP)) {
                                manager = new ShipManagerContainer(segmentController.getState(), (Ship) segmentController);
                            } else if(segmentController.getType().equals(SimpleTransformableSendableObject.EntityType.SPACE_STATION)) {
                                manager = new SpaceStationManagerContainer(segmentController.getState(), (SpaceStation) segmentController);
                            }

                            if(manager != null) {
                                for (BlockSegment connection : connections) {
                                    ElementCollectionMesh mesh = null;
                                    if(connection.getId() == ElementKeyMap.SHIELD_CAP_ID || connection.getId() == ElementKeyMap.SHIELD_REGEN_ID) {
                                        ShieldAddOn shieldAddon = ((ShieldContainerInterface) segmentController).getShieldAddOn();
                                        if(shieldAddon != null && shieldAddon.getShieldLocalAddOn() != null) {
                                            if(connection.getId() == ElementKeyMap.SHIELD_CAP_ID) {
                                                mesh = shieldAddon.sc.getShieldCapacityManager().getInstance().getMesh();
                                            } else if(connection.getId() == ElementKeyMap.SHIELD_REGEN_ID) {
                                                mesh = shieldAddon.sc.getShieldRegenManager().getInstance().getMesh();
                                            }
                                        }
                                    } else if(connection.getId() == ElementKeyMap.REACTOR_MAIN) {
                                        mesh = manager.getMainReactor().getInstance().getMesh();
                                    } else if(connection.getId() == ElementKeyMap.REACTOR_STABILIZER) {
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
                } else if(blockElement.getId() == Objects.requireNonNull(BlockManager.getFromName("Display Screen")).blockInfo.getId()) {
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