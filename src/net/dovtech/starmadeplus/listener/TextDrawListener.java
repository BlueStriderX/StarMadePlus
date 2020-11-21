package net.dovtech.starmadeplus.listener;

import api.listener.fastevents.TextBoxDrawListener;
import net.dovtech.starmadeplus.StarMadePlus;
import net.dovtech.starmadeplus.image.ImageManager;
import net.dovtech.starmadeplus.image.ScalableImageSubSprite;
import org.apache.commons.lang3.StringUtils;
import org.schema.game.client.view.SegmentDrawer;
import org.schema.game.client.view.textbox.AbstractTextBox;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.forms.Sprite;

public class TextDrawListener implements TextBoxDrawListener {

    @Override
    public void draw(SegmentDrawer.TextBoxSeg.TextBoxElement textBoxElement, AbstractTextBox box) {

    }

    @Override
    public void preDrawBackground(SegmentDrawer.TextBoxSeg seg, AbstractTextBox abstractTextBox) {
        for (SegmentDrawer.TextBoxSeg.TextBoxElement textBoxElement : seg.v) {
            if (textBoxElement.rawText.contains("<img>")) {
                String str = StringUtils.substringBetween(textBoxElement.rawText, "<img>", "</img>");
                String[] args = str.split(",");
                String src = null;
                float scale = 1.0f;

                for (String arg : args) {
                    if (arg.startsWith("src=")) {
                        src = arg.split("src=")[1];
                    } else if (arg.startsWith("scale=")) {
                        scale = (float) (Math.min(15, Double.parseDouble(arg.split("scale=")[1])));
                    }
                }

                if (scale <= 0) scale = 1.0f;

                boolean blockImage = false;

                if (src != null) {
                    if (StarMadePlus.getInstance().imageFilterMode.equals(StarMadePlus.ImageFilterMode.BLACKLIST)) {
                        for (String s : StarMadePlus.getInstance().imageFilter) {
                            if (src.toLowerCase().contains(s.toLowerCase())) {
                                blockImage = false;
                                break;
                            }
                        }
                    } else {
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
                            Sprite.draw3D(image, new ScalableImageSubSprite[]{new ScalableImageSubSprite(scale, textBoxElement.worldpos)}, 1, Controller.getCamera());
                        }
                    }
                }
            }
        }
    }


}
