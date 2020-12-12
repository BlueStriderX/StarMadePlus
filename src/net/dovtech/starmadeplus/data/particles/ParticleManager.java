package net.dovtech.starmadeplus.data.particles;

import api.utils.textures.StarLoaderTexture;
import net.dovtech.starmadeplus.StarMadePlus;
import org.schema.schine.graphicsengine.forms.Sprite;
import javax.imageio.ImageIO;
import java.io.IOException;

public enum ParticleManager {

    PLASMA_BALL;

    private Sprite sprite;
    private String name;

    public static void init(final StarMadePlus instance) {
        StarLoaderTexture.runOnGraphicsThread(new Runnable() {
            @Override
            public void run() {
                synchronized (ParticleManager.class) {
                    for (ParticleManager value : ParticleManager.values()) {
                        String name = value.name().toLowerCase();
                        value.name = name;
                        try {
                            value.sprite = StarLoaderTexture.newSprite(ImageIO.read(StarMadePlus.class.getResourceAsStream("resource/textures/particles/" + name + ".png")), instance, "starmadeplus_" + name);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public Sprite getSprite() {
        return sprite;
    }

    public String getName() {
        return "starmadeplus_" + name;
    }
}
