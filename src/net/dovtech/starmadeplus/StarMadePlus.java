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
import net.dovtech.starmadeplus.blocks.BlockManager;
import net.dovtech.starmadeplus.blocks.DisplayScreen;
import net.dovtech.starmadeplus.listener.TextDrawListener;
import org.schema.game.client.controller.PlayerOkCancelInput;
import org.schema.game.client.controller.PlayerTextAreaInput;
import org.schema.game.client.controller.element.world.ClientSegmentProvider;
import org.schema.game.client.controller.manager.ingame.PlayerInteractionControlManager;
import org.schema.game.common.controller.SendableSegmentProvider;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.SendableGameState;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.Segment;
import org.schema.game.network.objects.remote.RemoteTextBlockPair;
import org.schema.game.network.objects.remote.TextBlockPair;
import org.schema.schine.common.TextCallback;
import org.schema.schine.graphicsengine.core.GLFrame;
import org.schema.schine.graphicsengine.core.settings.PrefixNotFoundException;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class StarMadePlus extends StarMod {

    //Other
    private static StarMadePlus instance;
    public enum GameMode {CLIENT, SERVER, SINGLEPLAYER}
    public enum ImageFilterMode{BLACKLIST, WHITELIST}
    public enum LogType {DEBUG, INFO, WARNING, ERROR, SEVERE}
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
            "image-filter: porn,hentai,sex,nsfw"
    };
    public boolean debugMode = false;
    public int maxDisplayDrawDistance = 50;
    public float maxImageScale = 15;
    public float maxImageOffset = 30;
    public ImageFilterMode imageFilterMode = ImageFilterMode.BLACKLIST;
    public ArrayList<String> imageFilter = new ArrayList<>();


    public StarMadePlus() {
        instance = this;
    }

    public static void main(String[] args) { }

    @Override
    public void onGameStart() {
        setModName("StarMadePlus");
        setModVersion("0.3.6");
        setModAuthor("Dovtech");
        setModDescription("Minor tweaks and additions to improve the base game.");
    }

    @Override
    public void onBlockConfigLoad(BlockConfig config) {
        BlockManager.addBlock(new DisplayScreen(config));
    }

    @Override
    public void onEnable() {
        registerFastListeners();
        registerListeners();
        if (getGameState().equals(GameMode.SERVER) || getGameState().equals(GameMode.SINGLEPLAYER)) {
            initConfig();
            createLogs();
        }
    }

    private void registerFastListeners() {
        FastListenerCommon.getTextBoxListeners().add(new TextDrawListener());
    }

    private void registerListeners() {
        StarLoader.registerListener(SegmentPieceActivateByPlayer.class, new Listener<SegmentPieceActivateByPlayer>() {
            @Override
            public void onEvent(final SegmentPieceActivateByPlayer event) {
                if (getGameState().equals(GameMode.SERVER) || getGameState().equals(GameMode.SINGLEPLAYER)) {
                    final SegmentPiece piece = event.getSegmentPiece();
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
                                    for(int i = 0; i < piece.getSegmentController().getTextBlocks().size(); i ++) {
                                        if(piece.getSegmentController().getTextBlocks().get(i) == index) {
                                            pos = i;
                                            break;
                                        }
                                    }
                                    if(pos != -1) piece.getSegmentController().getTextBlocks().remove(pos);
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

        //Register text blocks on block place
        StarLoader.registerListener(SegmentPieceAddEvent.class, new Listener<SegmentPieceAddEvent>() {
            @Override
            public void onEvent(SegmentPieceAddEvent event) {
                if (event.getNewType() == Objects.requireNonNull(BlockManager.getFromName("Display Screen")).blockInfo.getId()) {
                    long indexAndOrientation = ElementCollection.getIndex4(event.getAbsIndex(), event.getOrientation());
                    event.getSegmentController().getTextBlocks().add(indexAndOrientation);
                }
            }
        }, this);

        //Remove text block on block remove
        StarLoader.registerListener(SegmentPieceRemoveEvent.class, new Listener<SegmentPieceRemoveEvent>() {
            @Override
            public void onEvent(SegmentPieceRemoveEvent event) {
                if(event.getType() == Objects.requireNonNull(BlockManager.getFromName("Display Screen")).blockInfo.getId()) {
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
                if(event.getType() == Objects.requireNonNull(BlockManager.getFromName("Display Screen")).blockInfo.getId()) {
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
        if(getGameState().equals(GameMode.SERVER) || getGameState().equals(GameMode.SINGLEPLAYER)) {
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
        if(getGameState().equals(GameMode.SERVER) || getGameState().equals(GameMode.SINGLEPLAYER)) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(imageLogFile, true));
                writer.append(player.getName()).append(" posted an image ").append(text).append("on a display module at ").append(String.valueOf(System.currentTimeMillis()));
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