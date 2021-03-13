package thederpgamer.starmadeplus.blocks.factory;

import api.config.BlockConfig;
import api.utils.textures.StarLoaderTexture;
import thederpgamer.starmadeplus.StarMadePlus;
import thederpgamer.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.ElementInformation;

public abstract class FactoryElement {

    public ElementInformation blockInfo;
    private final String[] blockSideNames = {"front", "back", "top", "bottom", "left", "right"};

    public FactoryElement(String name) {
        short[] textureIDs = new short[6];
        try {
            for(int i = 0; i < 6; i ++) {
                String textureName = name.toLowerCase().replaceAll(" ", "_") + "_" + blockSideNames[i];
                StarLoaderTexture texture = StarMadePlus.getInstance().textureMap.get(textureName);
                textureIDs[i] = (short) texture.getTextureId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        blockInfo = BlockConfig.newFactory(StarMadePlus.getInstance(), name, textureIDs);
        BlockManager.factories.put(name, BlockConfig.customFactories.get(blockInfo.getId()));
    }
}
