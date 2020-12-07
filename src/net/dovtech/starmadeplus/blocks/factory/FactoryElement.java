package net.dovtech.starmadeplus.blocks.factory;

import api.config.BlockConfig;
import api.utils.textures.StarLoaderTexture;
import net.dovtech.starmadeplus.StarMadePlus;
import net.dovtech.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.ElementInformation;

public abstract class FactoryElement {

    public ElementInformation blockInfo;
    private final String[] blockSideNames = {"front", "back", "top", "bottom", "left", "right"};

    public FactoryElement(BlockConfig config, String name) {
        short[] textureIDs = new short[6];
        try {
            for(int i = 0; i < 6; i ++) {
                String textureName = name.toLowerCase().replaceAll(" ", "_") + "_" + blockSideNames[i];
                StarLoaderTexture texture = StarMadePlus.getInstance().textures.get(textureName);
                textureIDs[i] = (short) texture.getTextureId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        blockInfo = config.newFactory(StarMadePlus.getInstance(), name, textureIDs);
        BlockManager.customFactories.put(name, BlockConfig.customFactories.get(blockInfo.getId()));
    }
}
