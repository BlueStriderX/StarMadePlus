package net.dovtech.starmadeplus.blocks;

import api.config.BlockConfig;
import api.utils.textures.StarLoaderTexture;
import net.dovtech.starmadeplus.StarMadePlus;
import org.schema.game.common.data.element.ElementInformation;
import javax.imageio.ImageIO;

public abstract class BlockElement {

    public ElementInformation blockInfo;

    public BlockElement(BlockConfig config, String name, BlockManager.BlockSide... textureSides) {
        blockInfo = config.newElement(StarMadePlus.getInstance(), name);
        short[] textureIDs = new short[6];
        if(textureSides.length > 1) {
            try {
                int index;
                for (index = 0; index < textureSides.length; index ++) {
                    String textureName = name.toLowerCase().replaceAll(" ", "-") + "_" + textureSides[index].name().toLowerCase() + ".png";
                    StarLoaderTexture texture = StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream("resource/textures/blocks/" + textureName)));
                    textureIDs[index] = (short) texture.getTextureId();
                }
                blockInfo.setTextureId(textureIDs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(textureSides.length == 1 && textureSides[0] == BlockManager.BlockSide.ALL) {
            try {
                String textureName = name.toLowerCase().replaceAll(" ", "-") + ".png";
                StarLoaderTexture texture = StarLoaderTexture.newBlockTexture(ImageIO.read(StarMadePlus.class.getResourceAsStream("resource/textures/blocks/" + textureName)));
                short textureID = (short) texture.getTextureId();
                blockInfo.setTextureId(new short[] {textureID, textureID, textureID, textureID, textureID, textureID});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
