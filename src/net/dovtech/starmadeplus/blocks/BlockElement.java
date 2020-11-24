package net.dovtech.starmadeplus.blocks;

import api.config.BlockConfig;
import api.utils.textures.StarLoaderTexture;
import net.dovtech.starmadeplus.StarMadePlus;
import org.schema.game.client.view.cubes.shapes.BlockStyle;
import org.schema.game.common.data.element.ElementInformation;
import javax.imageio.ImageIO;

public abstract class BlockElement {

    public ElementInformation blockInfo;

    public BlockElement(BlockConfig config, String name, BlockManager.BlockSide... textureSides) {
        short[] textureIDs = new short[6];
        if(textureSides.length == 6) {
            try {
                int index;
                for (index = 0; index < textureSides.length; index ++) {
                    String textureName = name.toLowerCase().replaceAll(" ", "-") + "_" + textureSides[index].name().toLowerCase() + ".png";
                    StarLoaderTexture texture = StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream("resource/textures/blocks/" + textureName)));
                    textureIDs[index] = (short) texture.getTextureId();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(textureSides.length == 1 && textureSides[0] == BlockManager.BlockSide.ALL) {
            try {
                String textureName = name.toLowerCase().replaceAll(" ", "-") + ".png";
                StarLoaderTexture texture = StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream("resource/textures/blocks/" + textureName)));
                short textureID = (short) texture.getTextureId();
                textureIDs = new short[] {textureID, textureID, textureID, textureID, textureID, textureID};
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (textureSides.length == 1 && textureSides[0] == BlockManager.BlockSide.TOP_ONLY) {
                try {
                    String topName = name.toLowerCase().replaceAll(" ", "-") + "_top.png";
                    String sidesName = name.toLowerCase().replaceAll(" ", "-") + "_sides.png";

                    StarLoaderTexture topTexture = StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream("resource/textures/blocks/" + topName)));
                    StarLoaderTexture sidesTexture = StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream("resource/textures/blocks/" + sidesName)));

                    textureIDs[0] = (short) sidesTexture.getTextureId();
                    textureIDs[1] = (short) sidesTexture.getTextureId();
                    textureIDs[2] = (short) topTexture.getTextureId();
                    textureIDs[3] = (short) sidesTexture.getTextureId();
                    textureIDs[4] = (short) sidesTexture.getTextureId();
                    textureIDs[5] = (short) sidesTexture.getTextureId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        } else if(textureSides.length == 1 && textureSides[0] == BlockManager.BlockSide.TOP_BOTTOM) {
            try {
                String topName = name.toLowerCase().replaceAll(" ", "-") + "_top.png";
                String bottomName = name.toLowerCase().replaceAll(" ", "-") + "_bottom.png";
                String sidesName = name.toLowerCase().replaceAll(" ", "-") + "_sides.png";

                StarLoaderTexture topTexture = StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream("resource/textures/blocks/" + topName)));
                StarLoaderTexture bottomTexture = StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream("resource/textures/blocks/" + bottomName)));
                StarLoaderTexture sidesTexture = StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream("resource/textures/blocks/" + sidesName)));

                textureIDs[0] = (short) sidesTexture.getTextureId();
                textureIDs[1] = (short) sidesTexture.getTextureId();
                textureIDs[2] = (short) topTexture.getTextureId();
                textureIDs[3] = (short) bottomTexture.getTextureId();
                textureIDs[4] = (short) sidesTexture.getTextureId();
                textureIDs[5] = (short) sidesTexture.getTextureId();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        blockInfo = config.newElement(StarMadePlus.getInstance(), name, textureIDs);
    }
}
