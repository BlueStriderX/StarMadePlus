package thederpgamer.starmadeplus.blocks.multiblocks.deepstorage;

import api.config.BlockConfig;
import org.schema.game.common.data.element.ElementKeyMap;
import thederpgamer.starmadeplus.blocks.BlockElement;
import thederpgamer.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class DeepStorageComponent extends BlockElement {

    public DeepStorageComponent() {
        super("Deep Storage Component", BlockManager.TextureType.MULTIBLOCK);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.setPrice(ElementKeyMap.getInfo(ElementKeyMap.STASH_ELEMENT).price * 2);
        blockInfo.setShoppable(true);

        blockInfo.controlledBy.add(BlockManager.getFromName("Deep Storage Controller").getId());

        BlockConfig.addRecipe(blockInfo,ElementKeyMap.getInfo(ElementKeyMap.SHIELD_CAP_ID).getProducedInFactoryType(), (int) ElementKeyMap.getInfo(ElementKeyMap.SHIELD_CAP_ID).getFactoryBakeTime(),
                new FactoryResource(1, ElementKeyMap.STASH_ELEMENT),
                new FactoryResource(15, ElementKeyMap.CARGO_SPACE)
        );

        BlockConfig.add(blockInfo);
    }
}