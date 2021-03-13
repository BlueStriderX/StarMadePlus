package thederpgamer.starmadeplus;

import api.common.GameClient;
import api.common.GameCommon;
import api.config.BlockConfig;
import api.listener.Listener;
import api.listener.events.block.SegmentPieceActivateByPlayer;
import api.listener.events.block.SegmentPieceAddByMetadataEvent;
import api.listener.events.block.SegmentPieceAddEvent;
import api.listener.events.block.SegmentPieceRemoveEvent;
import api.listener.events.draw.RegisterWorldDrawersEvent;
import api.listener.events.player.PlayerJoinWorldEvent;
import api.listener.fastevents.FastListenerCommon;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.mod.config.FileConfiguration;
import api.mod.config.PersistentObjectUtil;
import api.utils.textures.StarLoaderTexture;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.resource.ResourceLoader;
import thederpgamer.starmadeplus.blocks.BlockManager;
import thederpgamer.starmadeplus.blocks.decor.HoloTable;
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
import thederpgamer.starmadeplus.utils.ElementGroupMeshDrawer;
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
import java.io.IOException;
import java.util.ArrayList;
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

    public ElementGroupMeshDrawer meshDrawer;
    public HashMap<String, Sprite> spriteMap = new HashMap<>();
    public HashMap<String, StarLoaderTexture> textureMap = new HashMap<>();
    private final String disclaimerMessage =
            "By pressing the ACCEPT button, you hereby acknowledge any and all responsibility for the images you\n" +
                    "post and that the creators of StarMadePlus, the StarLoader team, Schine, the Server or its owners,\n" +
                    "or any other person/entity that is not you cannot be held liable for anything you post.";

    //Logs
    private final String logPath = "moddata/StarMadePlus/logs";;

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
    private final String[] spriteNames = new String[] {
            "transparent"
    };
    private final String[] textureNames = new String[] {
            "hidden_rail_spinner_clock_wise_sides",
            "hidden_rail_spinner_clock_wise_top",
            "hidden_rail_spinner_counter_clock_wise_sides",
            "hidden_rail_spinner_counter_clock_wise_top",
            "rail_spinner_clock_wise_bottom",
            "rail_spinner_clock_wise_sides",
            "rail_spinner_clock_wise_top",
            "rail_spinner_counter_clock_wise_bottom",
            "rail_spinner_counter_clock_wise_sides",
            "rail_spinner_counter_clock_wise_top"
    };
    private final String[] modelNames = new String[] {
            "display_screen",
            "holo_table"
    };

    @Override
    public void onEnable() {
        instance = this;
        initConfig();
        registerFastListeners();
        registerListeners();
    }

    @Override
    public void onResourceLoad(ResourceLoader loader) {
        for(String spriteName :  spriteNames) {
            try {
                spriteMap.put(spriteName, StarLoaderTexture.newSprite(ImageIO.read(getJarResource("thederpgamer/starmadeplus/resources/sprites/" + spriteName + ".png")), this, spriteName));
            } catch (IOException exception) {
                try {
                    spriteMap.put(spriteName, StarLoaderTexture.newSprite(ImageIO.read(getJarResource("thederpgamer/starmadeplus/resources/sprites/missing_sprite.png")), this, spriteName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                exception.printStackTrace();
            }
        }

        for(String textureName : textureNames) {
            try {
                textureMap.put(textureName, StarLoaderTexture.newBlockTexture(ImageIO.read(getJarResource("thederpgamer/starmadeplus/resources/textures/blocks/" + textureName + ".png"))));
            } catch(IOException exception) {
                try {
                    textureMap.put(textureName, StarLoaderTexture.newBlockTexture(ImageIO.read(getJarResource("thederpgamer/starmadeplus/resources/textures/missing_texture.png"))));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                exception.printStackTrace();
            }
        }

        for(String modelName : modelNames) {
            try {
                loader.getMeshLoader().loadModMesh(this, modelName, getJarResource("thederpgamer/starmadeplus/resources/models/blocks/" + modelName + ".zip"), null);
            } catch(ResourceException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBlockConfigLoad(BlockConfig config) {
        //Decor Blocks
        BlockManager.addBlock(new DisplayScreen());
        BlockManager.addBlock(new HoloTable());

        //Multi-Blocks
        BlockManager.addBlock(new DeepStorageController());
        BlockManager.addBlock(new DeepStorageDisplay());
        BlockManager.addBlock(new DeepStorageComponent());

        //Rails
        BlockManager.addBlock(new RailSpinnerClockwise());
        BlockManager.addBlock(new RailSpinnerCounterClockwise());
        BlockManager.addBlock(new HiddenRailSpinnerClockwise());
        BlockManager.addBlock(new HiddenRailSpinnerCounterClockwise());

        //Systems
        //BlockManager.addBlock(new StellarLifterController());

        //Resources
        BlockManager.addBlock(new PhotonShard());

        //Factories
        new CrystallizerController();

        BlockManager.initializeBlocks();
    }

    private void registerFastListeners() {
        FastListenerCommon.textBoxListeners.add(new TextDrawEvent());
        FastListenerCommon.railMoveListeners.add(new RailMoveEvent());
    }

    private void registerListeners() {
        StarLoader.registerListener(RegisterWorldDrawersEvent.class, new Listener<RegisterWorldDrawersEvent>() {
            @Override
            public void onEvent(RegisterWorldDrawersEvent event) {
                event.getModDrawables().add(meshDrawer = new ElementGroupMeshDrawer());
            }
        }, this);

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
                            ArrayList<ElementCollection> collections = new ArrayList<>();
                            SegmentController segmentController = blockElement.getEntity();
                            ManagerContainer manager = null;
                            if(segmentController.getType().equals(SimpleTransformableSendableObject.EntityType.SHIP)) {
                                manager = new ShipManagerContainer(segmentController.getState(), (Ship) segmentController);
                            } else if(segmentController.getType().equals(SimpleTransformableSendableObject.EntityType.SPACE_STATION)) {
                                manager = new SpaceStationManagerContainer(segmentController.getState(), (SpaceStation) segmentController);
                            }

                            if(manager != null) {
                                for (BlockSegment connection : connections) {
                                    ElementCollection collection = null;
                                    if(connection.getId() == ElementKeyMap.SHIELD_CAP_ID || connection.getId() == ElementKeyMap.SHIELD_REGEN_ID) {
                                        ShieldAddOn shieldAddon = ((ShieldContainerInterface) segmentController).getShieldAddOn();
                                        if(shieldAddon != null && shieldAddon.getShieldLocalAddOn() != null) {
                                            if(connection.getId() == ElementKeyMap.SHIELD_CAP_ID) {
                                                collection = shieldAddon.sc.getShieldCapacityManager().getInstance();
                                            } else if(connection.getId() == ElementKeyMap.SHIELD_REGEN_ID) {
                                                collection = shieldAddon.sc.getShieldRegenManager().getInstance();
                                            }
                                        }
                                    } else if(connection.getId() == ElementKeyMap.REACTOR_MAIN) {
                                        collection = manager.getMainReactor().getInstance();
                                    } else if(connection.getId() == ElementKeyMap.REACTOR_STABILIZER) {
                                        collection = manager.getStabilizer().getInstance();
                                    }
                                    if(collection != null && !collections.contains(collection)) {
                                        collections.add(collection);
                                    }
                                }

                                for(ElementCollection collection : collections) {
                                    MeshDrawData drawData = new MeshDrawData(elementGroup);
                                    meshDrawer.addMesh(drawData, collection);
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

    public static StarMadePlus getInstance() {
        return instance;
    }
}