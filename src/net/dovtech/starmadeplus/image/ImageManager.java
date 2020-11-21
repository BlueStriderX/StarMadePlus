package net.dovtech.starmadeplus.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class ImageManager {

    private static HashMap<String, BufferedImage> images = new HashMap<>();

    public static BufferedImage getFromURL(String imageURL) throws IOException {
        if(images.containsKey(imageURL)) {
            return images.get(imageURL);
        } else {
            URL url = new URL(imageURL);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("User-Agent", "NING/1.0");
            InputStream stream = urlConnection.getInputStream();
            BufferedImage image = ImageIO.read(stream);
            /*
            Sprite sprite = StarLoaderTexture.newSprite(image, StarMadePlus.getInstance(), imageURL);
            sprite.setPositionCenter(true);
            images.put(imageURL, sprite);
            return sprite;
             */
            images.put(imageURL, image);
            return image;
        }
    }
}