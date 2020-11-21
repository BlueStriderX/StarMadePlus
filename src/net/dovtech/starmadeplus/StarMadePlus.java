package net.dovtech.starmadeplus;

import api.DebugFile;
import api.common.GameCommon;
import api.config.BlockConfig;
import api.listener.Listener;
import api.listener.events.block.SegmentPieceActivateByPlayer;
import api.listener.events.block.SegmentPieceAddByMetadataEvent;
import api.listener.events.block.SegmentPieceAddEvent;
import api.listener.events.block.SegmentPieceRemoveEvent;
import api.listener.fastevents.FastListenerCommon;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.mod.config.FileConfiguration;
import net.dovtech.starmadeplus.blocks.BlockManager;
import net.dovtech.starmadeplus.blocks.HoloProjector;
import net.dovtech.starmadeplus.listener.TextDrawListener;
import org.schema.game.client.controller.PlayerTextAreaInput;
import org.schema.game.client.controller.element.world.ClientSegmentProvider;
import org.schema.game.client.controller.manager.ingame.PlayerInteractionControlManager;
import org.schema.game.common.controller.SendableSegmentProvider;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.SendableGameState;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.world.Segment;
import org.schema.game.network.objects.remote.RemoteTextBlockPair;
import org.schema.game.network.objects.remote.TextBlockPair;
import org.schema.schine.common.TextCallback;
import org.schema.schine.graphicsengine.core.settings.PrefixNotFoundException;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import java.util.ArrayList;
import java.util.Objects;

public class StarMadePlus extends StarMod {

    private static StarMadePlus instance;
    public enum GameMode {CLIENT, SERVER, SINGLEPLAYER}
    public enum ImageFilterMode{BLACKLIST, WHITELIST}

    //Config
    private String[] defaultConfig = {
            "debug-mode: false",
            "image-filter-mode: blacklist",
            "image-filter: porn, hentai, sex, nsfw"
    };
    public boolean debugMode = false;
    public ImageFilterMode imageFilterMode = ImageFilterMode.BLACKLIST;
    public ArrayList<String> imageFilter = new ArrayList<>();


    public StarMadePlus() {
        instance = this;
    }

    public static void main(String[] args) { }

    @Override
    public void onGameStart() {
        setModName("StarMadePlus");
        setModVersion("0.3.2");
        setModAuthor("Dovtech");
        setModDescription("Minor tweaks and additions to improve the base game.");
    }

    @Override
    public void onBlockConfigLoad(BlockConfig config) {
        BlockManager.addBlock(new HoloProjector(config));
    }

    @Override
    public void onEnable() {
        registerFastListeners();
        registerListeners();
        if (getGameState().equals(GameMode.SERVER) || getGameState().equals(GameMode.SINGLEPLAYER)) {
            initConfig();
        }
    }

    private void registerFastListeners() {
        FastListenerCommon.getTextBoxListeners().add(new TextDrawListener());
    }

    private void registerListeners() {

        StarLoader.registerListener(SegmentPieceActivateByPlayer.class, new Listener<SegmentPieceActivateByPlayer>() {
            @Override
            public void onEvent(SegmentPieceActivateByPlayer event) {
                final SegmentPiece piece = event.getSegmentPiece();
                if (piece.getType() == Objects.requireNonNull(BlockManager.getFromName("Holo Projector")).blockInfo.getId()) {
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
        }, this);

        //Register text blocks on block place
        StarLoader.registerListener(SegmentPieceAddEvent.class, new Listener<SegmentPieceAddEvent>() {
            @Override
            public void onEvent(SegmentPieceAddEvent event) {
                if (event.getNewType() == Objects.requireNonNull(BlockManager.getFromName("Holo Projector")).blockInfo.getId()) {
                    long indexAndOrientation = ElementCollection.getIndex4(event.getAbsIndex(), event.getOrientation());
                    event.getSegmentController().getTextBlocks().add(indexAndOrientation);
                }
            }
        }, this);

        //Remove text block on block remove
        StarLoader.registerListener(SegmentPieceRemoveEvent.class, new Listener<SegmentPieceRemoveEvent>() {
            @Override
            public void onEvent(SegmentPieceRemoveEvent event) {
                if(event.getType() == Objects.requireNonNull(BlockManager.getFromName("Holo Projector")).blockInfo.getId()) {
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
                if(event.getType() == Objects.requireNonNull(BlockManager.getFromName("Holo Projector")).blockInfo.getId()) {
                    event.getSegment().getSegmentController().getTextBlocks().add(event.getIndexAndOrientation());
                }
            }
        }, this);
    }

    private void initConfig() {
        FileConfiguration config = getConfig("config");
        config.saveDefault(defaultConfig);

        this.debugMode = Boolean.parseBoolean(config.getString("debug-mode"));
        this.imageFilterMode = ImageFilterMode.valueOf(config.getString("image-filter-mode").toUpperCase());
        this.imageFilter = config.getList("image-filter");

        DebugFile.log("Loaded Config", this);
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

    public static StarMadePlus getInstance() {
        return instance;
    }
}