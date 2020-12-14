package net.dovtech.starmadeplus.listener;

import api.listener.fastevents.TextBoxDrawListener;
import api.utils.textures.StarLoaderTexture;
import com.bulletphysics.linearmath.Transform;
import net.dovtech.starmadeplus.StarMadePlus;
import net.dovtech.starmadeplus.image.ImageManager;
import net.dovtech.starmadeplus.image.ScalableImageSubSprite;
import org.apache.commons.lang3.StringUtils;
import org.schema.game.client.view.SegmentDrawer;
import org.schema.game.client.view.textbox.AbstractTextBox;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.forms.Sprite;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.lang.reflect.Field;

public class TextDrawEvent implements TextBoxDrawListener {

    @Override
    public void preDraw(SegmentDrawer.TextBoxSeg.TextBoxElement textBoxElement, AbstractTextBox abstractTextBox) {
        try {
            Field maxDrawField = abstractTextBox.getClass().getDeclaredField("maxTextDistance");
            maxDrawField.setAccessible(true);
            maxDrawField.set(abstractTextBox, StarMadePlus.getInstance().maxDisplayDrawDistance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void draw(SegmentDrawer.TextBoxSeg.TextBoxElement textBoxElement, AbstractTextBox box) {

    }

    @Override
    public void preDrawBackground(SegmentDrawer.TextBoxSeg seg, AbstractTextBox abstractTextBox) {
        for (SegmentDrawer.TextBoxSeg.TextBoxElement textBoxElement : seg.v) {
            if (textBoxElement.rawText.contains("<img>")) {
                try {
                    abstractTextBox.getBg().setSprite(StarLoaderTexture.newSprite(ImageIO.read(StarMadePlus.class.getResourceAsStream("particletextures/transparent.png")), StarMadePlus.getInstance(), "transparent"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String str = StringUtils.substringBetween(textBoxElement.rawText, "<img>", "</img>");
                String[] args = str.split(",");
                String src = null;
                boolean hideText = true;
                float scale = 1.0f;
                float xOffset = 0.0f;
                float yOffset = 0.0f;
                float zOffset = 0.0f;
                try {
                    for (String arg : args) {
                        if (arg.startsWith("src=")) {
                            src = arg.split("src=")[1];
                        } else if (arg.startsWith("scale=")) {
                            scale = Math.min(StarMadePlus.getInstance().maxImageScale, Float.parseFloat(arg.split("scale=")[1]));
                        } else if (arg.startsWith("hide_text=")) {
                            hideText = Boolean.parseBoolean(arg.split("hide_text=")[1]);
                        } else if (arg.startsWith("x=")) {
                            xOffset = Math.min(StarMadePlus.getInstance().maxImageOffset, Float.parseFloat(arg.split("x=")[1]));
                        } else if (arg.startsWith("y=")) {
                            yOffset = Math.min(StarMadePlus.getInstance().maxImageOffset, Float.parseFloat(arg.split("y=")[1]));
                        } else if (arg.startsWith("z=")) {
                            zOffset = Math.min(StarMadePlus.getInstance().maxImageOffset, Float.parseFloat(arg.split("z=")[1]));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (scale <= 0) scale = 1.0f;

                scale = (scale / 100) * -1; //Fix scaling

                if (hideText) {
                    textBoxElement.text.setTextSimple("");
                }

                boolean blockImage = false;

                if (src != null) {
                    if (StarMadePlus.getInstance().imageFilterMode.equals(StarMadePlus.ImageFilterMode.BLACKLIST)) {
                        for (String s : StarMadePlus.getInstance().imageFilter) {
                            if (src.toLowerCase().contains(s.toLowerCase())) {
                                blockImage = true;
                                break;
                            }
                        }
                    } else if (StarMadePlus.getInstance().imageFilterMode.equals(StarMadePlus.ImageFilterMode.WHITELIST)) {
                        blockImage = true;
                        for (String s : StarMadePlus.getInstance().imageFilter) {
                            if (src.toLowerCase().contains(s.toLowerCase())) {
                                blockImage = false;
                                break;
                            }
                        }
                    }

                    if (!blockImage) {
                        Sprite image = ImageManager.getImage(src);
                        if (image != null) {
                            Transform newTransform = textBoxElement.worldpos;
                            newTransform.origin.x = newTransform.origin.x + xOffset;
                            newTransform.origin.y = newTransform.origin.y + yOffset;
                            newTransform.origin.z = newTransform.origin.z + zOffset;
                            ScalableImageSubSprite[] subSprite = new ScalableImageSubSprite[]{new ScalableImageSubSprite(scale, newTransform)};
                            Sprite.draw3D(image, subSprite, 1, Controller.getCamera());
                        }
                    }
                }
            } else {
                abstractTextBox.getBg().setInvisible(false);
                abstractTextBox.getBg().setSprite(Controller.getResLoader().getSprite("screen-gui-"));
            }
        }
    }
}
