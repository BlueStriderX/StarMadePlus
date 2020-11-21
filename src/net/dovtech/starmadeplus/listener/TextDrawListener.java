package net.dovtech.starmadeplus.listener;

import api.DebugFile;
import api.common.GameClient;
import api.listener.fastevents.TextBoxDrawListener;
import api.utils.game.PlayerUtils;
import api.utils.textures.StarLoaderTexture;
import com.bulletphysics.linearmath.Transform;
import net.dovtech.starmadeplus.StarMadePlus;
import net.dovtech.starmadeplus.image.ImageManager;
import net.dovtech.starmadeplus.image.ScalableImageSubSprite;
import org.schema.game.client.view.SegmentDrawer;
import org.schema.game.client.view.textbox.AbstractTextBox;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import javax.vecmath.Vector3f;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class TextDrawListener implements TextBoxDrawListener {

    private Sprite sprite;
    private Sprite screenSprite;

    @Override
    public void draw(SegmentDrawer.TextBoxSeg.TextBoxElement textBoxElement, AbstractTextBox box) {
        textBoxElement.text.setFont(FontLibrary.getBlenderProHeavy31());
        String text = textBoxElement.realText;
        String newText = text;
        if (text != null) {
            String[] lines = text.split("\n");
            String imageLine = null;

            for (int i = 0; i < lines.length; i++) {
                if (lines[i].toLowerCase().startsWith("<image>") && lines[i].endsWith("</image>") && imageLine == null) {
                    imageLine = lines[i];
                    lines[i] = "";
                }
            }

            if (imageLine != null) {
                String imageString = imageLine.substring(imageLine.indexOf("<image>") + 7, imageLine.indexOf("</image>"));

                String[] args = imageString.split(",");
                float scale = 1.0f;
                float x = 0;
                float y = 0;
                float z = 0;
                String imageURL = null;
                try {
                    for (String s : args) {
                        if (s.startsWith("scale=")) {
                            scale = (float) Math.min(15, Double.parseDouble(s.split("scale=")[1]));
                        } else if (s.startsWith("src=")) {
                            imageURL = s.split("src=")[1];
                        } else if (s.startsWith("x=")) {
                            x = (float) Math.min(30, Double.parseDouble(s.split("x=")[1]));
                        } else if (s.startsWith("y=")) {
                            y = (float) Math.min(30, Double.parseDouble(s.split("y=")[1]));
                        } else if (s.startsWith("z=")) {
                            z = (float) Math.min(30, Double.parseDouble(s.split("z=")[1]));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                boolean blockImage = false;
                BufferedImage image = null;
                if (imageURL != null) {
                    if (StarMadePlus.getInstance().imageFilterMode.equals(StarMadePlus.ImageFilterMode.BLACKLIST)) {
                        for (String blackList : StarMadePlus.getInstance().imageFilter) {
                            //Todo: Figure out how to block specific domains/sites rather than just terms
                            if (imageURL.toLowerCase().contains(blackList.toLowerCase())) {
                                blockImage = true;
                                break;
                            }
                        }
                    } else if (StarMadePlus.getInstance().imageFilterMode.equals(StarMadePlus.ImageFilterMode.WHITELIST)) {
                        blockImage = true;
                        for (String whiteList : StarMadePlus.getInstance().imageFilter) {
                            if (imageURL.toLowerCase().contains(whiteList.toLowerCase())) {
                                blockImage = false;
                                break;
                            }
                        }
                    }

                    if (blockImage) {
                        PlayerUtils.sendMessage(GameClient.getClientPlayerState(), "[ERROR]: Could not load image from URL as the link is being blocked by the server's filter.");
                        return;
                    }

                    try {
                        image = ImageManager.getFromURL(imageURL);
                        DebugFile.log("[INFO] Fetched image from URL '" + imageURL + "'", StarMadePlus.getInstance());
                    } catch (IOException e) {
                        e.printStackTrace();
                        e.printStackTrace();
                        DebugFile.log("[ERROR] Failed to fetch image from URL '" + imageURL + "'. \nThe URL is most likely invalid or the connection is being blocked somehow.", StarMadePlus.getInstance());
                        PlayerUtils.sendMessage(GameClient.getClientPlayerState(), "[ERROR]: Could not load image from URL. Please check to make sure the link is valid.");
                    }
                    if (image != null) {
                        /*
                        if (scaleX != 0 && scaleY != 0) {

                            BufferedImage scaledImage = new BufferedImage((int) (image.getWidth() * scaleX), (int) (image.getHeight() * scaleY), image.getType());
                            Graphics2D graphics2D = scaledImage.createGraphics();
                            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                            graphics2D.drawImage(image, 0, 0, (int) (image.getWidth() * scaleX), (int) (image.getHeight() * scaleY), 0, 0, image.getWidth(), image.getHeight(), null);
                            graphics2D.dispose();
                            image = scaledImage;

                        }
                         */
                        box.getBg().setInvisible(true);
                        sprite = StarLoaderTexture.newSprite(image, StarMadePlus.getInstance(), imageURL);
                        sprite.setPositionCenter(true);
                        Vector3f newPos = new Vector3f(sprite.getPos().x + x, sprite.getPos().y + y, sprite.getPos().z + z);
                        Transform newTransform = textBoxElement.worldpos;
                        newTransform.transform(newPos);
                        Sprite.draw3D(sprite, new ScalableImageSubSprite[]{new ScalableImageSubSprite(scale, newTransform)}, 1, Controller.getCamera());
                    }
                }
            }

            if(!textBoxElement.realText.equals(newText)) {
                StringBuilder builder = new StringBuilder();
                for (String s : lines) builder.append(s).append("\n");
                newText = builder.toString();
                textBoxElement.text.setTextSimple(newText);
            }
        } else {
            if (screenSprite == null) screenSprite = Controller.getResLoader().getSprite("screen-gui-");
            if (!box.getBg().getSprite().getName().equals(screenSprite.getName())) {
                box.getBg().setInvisible(false);
                box.getBg().setSprite(Controller.getResLoader().getSprite("screen-gui-"));
            }
                /*
                Sprite screenSprite = Controller.getResLoader().getSprite("screen-gui-");
                if (!box.getBg().getSprite().equals(screenSprite) || !box.getBg().getSprite().getName().equals(screenSprite.getName())) {
                    box.onInit();
                    textBoxElement.text.setTextSimple(text);
                }
                 */
        }
    }

    @Override
    public void preDrawBackground(SegmentDrawer.TextBoxSeg textBoxSeg, AbstractTextBox abstractTextBox) {

    }
}
