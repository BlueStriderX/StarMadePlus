package net.dovtech.starmadeplus.listener;

import api.common.GameClient;
import api.listener.fastevents.TextBoxDrawListener;
import api.utils.game.PlayerUtils;
import api.utils.textures.StarLoaderTexture;
import net.dovtech.starmadeplus.StarMadePlus;
import net.dovtech.starmadeplus.image.ImageManager;
import net.dovtech.starmadeplus.image.ScalableImageSubSprite;
import org.apache.commons.lang3.StringUtils;
import org.schema.game.client.view.SegmentDrawer;
import org.schema.game.client.view.textbox.AbstractTextBox;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.forms.Sprite;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

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
                        Sprite image = getImage(src);
                        if (image != null) {
                            Sprite.draw3D(image, new ScalableImageSubSprite[]{new ScalableImageSubSprite(scale, textBoxElement.worldpos)}, 1, Controller.getCamera());
                        }
                    }
                }
            }
        }
    }

    @Nullable
    private static Sprite getImage(String url) {
        Sprite bufferedImage = ImageManager.imgCache.get(url);
        if (bufferedImage != null) {
            return bufferedImage;
        } else {
            fetchImage(url);
            return null;
        }
    }

    private static void fetchImage(final String url) {
        if (!ImageManager.downloadingImages.contains(url)) {
            new Thread() {
                @Override
                public void run() {
                    ImageManager.downloadingImages.add(url);
                    final BufferedImage bufferedImage = fromURL(url);
                    StarLoaderTexture.runOnGraphicsThread(new Runnable() {
                        @Override
                        public void run() {
                            Sprite sprite = StarLoaderTexture.newSprite(bufferedImage, StarMadePlus.getInstance(), url + System.currentTimeMillis());
                            ImageManager.imgCache.put(url, sprite);
                        }
                    });
                    ImageManager.downloadingImages.remove(url);
                }
            }.start();
        }
    }

    private static BufferedImage fromURL(String u) {
        BufferedImage image = null;
        try {
            URL url = new URL(u);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "NING/1.0");
            InputStream stream = urlConnection.getInputStream();
            image = ImageIO.read(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
