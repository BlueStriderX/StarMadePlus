package net.dovtech.starmadeplus;

import api.DebugFile;
import api.common.GameClient;
import api.common.GameCommon;
import api.config.BlockConfig;
import api.element.block.Blocks;
import api.listener.Listener;
import api.listener.events.block.SegmentPieceActivateByPlayer;
import api.listener.events.block.SegmentPieceAddByMetadataEvent;
import api.listener.events.block.SegmentPieceAddEvent;
import api.listener.events.block.SegmentPieceRemoveEvent;
import api.listener.fastevents.FastListenerCommon;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.mod.config.FileConfiguration;
import api.mod.config.PersistentObjectUtil;
import api.utils.textures.StarLoaderTexture;
import net.dovtech.starmadeplus.blocks.BlockManager;
import net.dovtech.starmadeplus.blocks.decor.DisplayScreen;
import net.dovtech.starmadeplus.blocks.multiblocks.deepstorage.DeepStorageComponent;
import net.dovtech.starmadeplus.blocks.multiblocks.deepstorage.DeepStorageController;
import net.dovtech.starmadeplus.blocks.multiblocks.deepstorage.DeepStorageDisplay;
import net.dovtech.starmadeplus.listener.RailMoveEvent;
import net.dovtech.starmadeplus.listener.TextDrawEvent;
import org.schema.game.client.controller.PlayerOkCancelInput;
import org.schema.game.client.controller.PlayerTextAreaInput;
import org.schema.game.client.controller.element.world.ClientSegmentProvider;
import org.schema.game.client.controller.manager.ingame.PlayerInteractionControlManager;
import org.schema.game.client.view.GameResourceLoader;
import org.schema.game.common.controller.SendableSegmentProvider;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.SendableGameState;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.Segment;
import org.schema.game.network.objects.remote.RemoteTextBlockPair;
import org.schema.game.network.objects.remote.TextBlockPair;
import org.schema.schine.common.TextCallback;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.ResourceException;
import org.schema.schine.graphicsengine.core.settings.PrefixNotFoundException;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

public class StarMadePlus extends StarMod {

    //Other
    private static StarMadePlus instance;

    public enum GameMode {CLIENT, SERVER, SINGLEPLAYER}

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
    private String[] defaultConfig = {
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


    public StarMadePlus() {
        instance = this;
    }

    public static void main(String[] args) {
    }

    @Override
    public void onGameStart() {
        setModName("StarMadePlus");
        setModVersion("0.5.4");
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

        //Resources
        //BlockManager.addBlock(new PhotonShard(config));
    }

    @Override
    public void onEnable() {
        registerFastListeners();
        registerListeners();
        loadBlockModels();
        loadTextures();
        if (getGameState().equals(GameMode.SERVER) || getGameState().equals(GameMode.SINGLEPLAYER)) {
            initConfig();
            createLogs();
        }
    }

    private void registerFastListeners() {
        FastListenerCommon.textBoxListeners.add(new TextDrawEvent());
        FastListenerCommon.railMoveListeners.add(new RailMoveEvent());
    }

    private void registerListeners() {
        StarLoader.registerListener(SegmentPieceActivateByPlayer.class, new Listener<SegmentPieceActivateByPlayer>() {
            @Override
            public void onEvent(final SegmentPieceActivateByPlayer event) {
                final SegmentPiece piece = event.getSegmentPiece();
                if (piece.getType() == BlockManager.getFromName("Holo Table")) {

                } else {
                    if (getGameState().equals(GameMode.SERVER) || getGameState().equals(GameMode.SINGLEPLAYER)) {
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
            }
        }, this);

        StarLoader.registerListener(SegmentPieceAddEvent.class, new Listener<SegmentPieceAddEvent>() {
            @Override
            public void onEvent(SegmentPieceAddEvent event) {
                if (event.getNewType() == Objects.requireNonNull(BlockManager.getFromName("Display Screen")).blockInfo.getId()) {
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

        for(final String model : models) {
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
            //String texturesFolder = "/resource/textures/blocks/";
            URL url = StarMadePlus.class.getResource("resource/textures/blocks");
            File texturesFolder = new File(url.getFile());
            for(File file : Objects.requireNonNull(texturesFolder.listFiles())) {
                textures.put(file.getName().split(".png")[0], StarLoaderTexture.newBlockTexture(ImageIO.read(file)));
            }

            /*
            textures.put("hidden-rail-spinner-clockwise_sides", StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream(texturesFolder + "hidden-rail-spinner-clockwise_sides.png"))));
            textures.put("hidden-rail-spinner-clockwise_top", StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream(texturesFolder + "hidden-rail-spinner-clockwise_top.png"))));

            textures.put("hidden-rail-spinner-counterclockwise_sides", StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream(texturesFolder + "hidden-rail-spinner-counterclockwise_sides.png"))));
            textures.put("hidden-rail-spinner-counterclockwise_top", StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream(texturesFolder + "hidden-rail-spinner-counterclockwise_top.png"))));

            textures.put("rail-spinner-clockwise_bottom", StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream(texturesFolder + "rail-spinner-clockwise_bottom.png"))));
            textures.put("rail-spinner-clockwise_sides", StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream(texturesFolder + "rail-spinner-clockwise_sides.png"))));
            textures.put("rail-spinner-clockwise_top", StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream(texturesFolder + "rail-spinner-clockwise_top.png"))));

            textures.put("rail-spinner-counterclockwise_bottom", StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream(texturesFolder + "rail-spinner-counterclockwise_bottom.png"))));
            textures.put("rail-spinner-counterclockwise_sides", StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream(texturesFolder + "rail-spinner-counterclockwise_sides.png"))));
            textures.put("rail-spinner-counterclockwise_top", StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream(texturesFolder + "rail-spinner-counterclockwise_top.png"))));
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GameMode getGameState() {
        if (GameCommon.isDedicatedServer()) {
            return GameMode.SERVER;
        } else if (GameCommon.isOnSinglePlayer()) {
            return GameMode.SINGLEPLAYER;
        } else if (GameCommon.isClientConnectedToServer()) {
            return GameMode.CLIENT;
        } else {
            return GameMode.CLIENT;
        }
    }

    public void logAdmin(String message, LogType logType) {
        if (getGameState().equals(GameMode.SERVER) || getGameState().equals(GameMode.SINGLEPLAYER)) {
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
        if (getGameState().equals(GameMode.SERVER) || getGameState().equals(GameMode.SINGLEPLAYER)) {
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