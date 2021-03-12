package thederpgamer.starmadeplus.blocks;

import api.config.BlockConfig;
import api.utils.textures.StarLoaderTexture;
import thederpgamer.starmadeplus.StarMadePlus;
import org.schema.game.common.data.element.ElementInformation;

public abstract class BlockElement {

    public ElementInformation blockInfo;

    public BlockElement(String name, BlockManager.TextureType... textureSides) {
        short[] textureIDs = new short[6];
        if(textureSides.length == 6) {
            try {
                int index;
                for (index = 0; index < textureSides.length; index ++) {
                    String textureName = name.toLowerCase().replaceAll(" ", "_") + "_" + textureSides[index].name().toLowerCase();
                    StarLoaderTexture texture = StarMadePlus.getInstance().textures.get(textureName);
                    textureIDs[index] = (short) texture.getTextureId();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(textureSides.length == 1 && textureSides[0] == BlockManager.TextureType.ALL) {
            try {
                String textureName = name.toLowerCase().replaceAll(" ", "_");
                StarLoaderTexture texture = StarMadePlus.getInstance().textures.get(textureName);
                short textureID = (short) texture.getTextureId();
                textureIDs = new short[] {textureID, textureID, textureID, textureID, textureID, textureID};
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (textureSides.length == 1 && textureSides[0] == BlockManager.TextureType.TOP_ONLY) {
                try {
                    String topName = name.toLowerCase().replaceAll(" ", "_") + "_top";
                    String sidesName = name.toLowerCase().replaceAll(" ", "_") + "_sides";

                    StarLoaderTexture topTexture =  StarMadePlus.getInstance().textures.get(topName);
                    StarLoaderTexture sidesTexture = StarMadePlus.getInstance().textures.get(sidesName);

                    textureIDs[0] = (short) sidesTexture.getTextureId();
                    textureIDs[1] = (short) sidesTexture.getTextureId();
                    textureIDs[2] = (short) topTexture.getTextureId();
                    textureIDs[3] = (short) sidesTexture.getTextureId();
                    textureIDs[4] = (short) sidesTexture.getTextureId();
                    textureIDs[5] = (short) sidesTexture.getTextureId();
                } catch (Exception e) {
                    e.printStackTrace();
                }
        } else if(textureSides.length == 1 && textureSides[0] == BlockManager.TextureType.TOP_BOTTOM) {
            try {
                String topName = name.toLowerCase().replaceAll(" ", "_") + "_top";
                String bottomName = name.toLowerCase().replaceAll(" ", "_") + "_bottom";
                String sidesName = name.toLowerCase().replaceAll(" ", "_") + "_sides";

                StarLoaderTexture topTexture = StarMadePlus.getInstance().textures.get(topName);
                StarLoaderTexture bottomTexture = StarMadePlus.getInstance().textures.get(bottomName);
                StarLoaderTexture sidesTexture = StarMadePlus.getInstance().textures.get(sidesName);

                textureIDs[0] = (short) sidesTexture.getTextureId();
                textureIDs[1] = (short) sidesTexture.getTextureId();
                textureIDs[2] = (short) topTexture.getTextureId();
                textureIDs[3] = (short) bottomTexture.getTextureId();
                textureIDs[4] = (short) sidesTexture.getTextureId();
                textureIDs[5] = (short) sidesTexture.getTextureId();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(textureSides.length == 1 && textureSides[0] == BlockManager.TextureType.MODULE) {
            try {
                String frontName = name.toLowerCase().replaceAll(" ", "_") + "_front";
                String sidesHorizontalName = name.toLowerCase().replaceAll(" ", "_") + "_sides_horizontal";
                String sidesVerticalName = name.toLowerCase().replaceAll(" ", "_") + "_sides_vertical";

                StarLoaderTexture frontTexture = StarMadePlus.getInstance().textures.get(frontName);
                StarLoaderTexture sidesHorizontalTexture = StarMadePlus.getInstance().textures.get(sidesHorizontalName);
                StarLoaderTexture sidesVerticalTexture = StarMadePlus.getInstance().textures.get(sidesVerticalName);

                textureIDs[0] = (short) frontTexture.getTextureId();
                textureIDs[1] = (short) sidesHorizontalTexture.getTextureId();
                textureIDs[2] = (short) sidesVerticalTexture.getTextureId();
                textureIDs[3] = (short) sidesVerticalTexture.getTextureId();
                textureIDs[4] = (short) sidesHorizontalTexture.getTextureId();
                textureIDs[5] = (short) sidesHorizontalTexture.getTextureId();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if(textureSides.length == 1 && textureSides[0] == BlockManager.TextureType.COMPUTER) {
            try {
                String frontName = name.toLowerCase().replaceAll(" ", "_") + "_front";
                String backName = name.toLowerCase().replaceAll(" ", "_") + "_back";
                String topName = name.toLowerCase().replaceAll(" ", "_") + "_top";
                String bottomName = name.toLowerCase().replaceAll(" ", "_") + "_bottom";
                String sidesName = name.toLowerCase().replaceAll(" ", "_") + "_sides";

                StarLoaderTexture frontTexture = StarMadePlus.getInstance().textures.get(frontName);
                StarLoaderTexture backTexture = StarMadePlus.getInstance().textures.get(backName);
                StarLoaderTexture topTexture = StarMadePlus.getInstance().textures.get(topName);
                StarLoaderTexture bottomTexture = StarMadePlus.getInstance().textures.get(bottomName);
                StarLoaderTexture sidesTexture = StarMadePlus.getInstance().textures.get(sidesName);

                textureIDs[0] = (short) backTexture.getTextureId();
                textureIDs[1] = (short) frontTexture.getTextureId();
                textureIDs[2] = (short) topTexture.getTextureId();
                textureIDs[3] = (short) bottomTexture.getTextureId();
                textureIDs[4] = (short) sidesTexture.getTextureId();
                textureIDs[5] = (short) sidesTexture.getTextureId();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        blockInfo = BlockConfig.newElement(StarMadePlus.getInstance(), name, textureIDs);
    }

    public short getId() {
        return blockInfo.getId();
    }
}
