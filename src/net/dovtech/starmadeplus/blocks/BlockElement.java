package net.dovtech.starmadeplus.blocks;

import api.config.BlockConfig;
import api.utils.textures.StarLoaderTexture;
import net.dovtech.starmadeplus.StarMadePlus;
import org.schema.game.common.data.element.ElementInformation;

public abstract class BlockElement {

    public ElementInformation blockInfo;
    private final String[] blockSideNames = {"front", "back", "top", "bottom", "right", "left"};

    public BlockElement(BlockConfig config, String name, BlockManager.TextureType... textureSides) {
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
        } else if(textureSides.length == 1 && textureSides[0] == BlockManager.TextureType.COMPUTER) {
            try {
                for(int i = 0; i < 6; i ++) {
                    String textureName = name.toLowerCase().replaceAll(" ", "_") + "_" + blockSideNames[i];
                    StarLoaderTexture texture = StarMadePlus.getInstance().textures.get(textureName);
                    textureIDs[i] = (short) texture.getTextureId();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        blockInfo = config.newElement(StarMadePlus.getInstance(), name, textureIDs);
    }

    public short getId() {
        return blockInfo.getId();
    }
}
