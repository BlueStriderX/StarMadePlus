package net.dovtech.starmadeplus.blocks.multiblocks.deepstorage;

import api.config.BlockConfig;
import api.element.block.Blocks;
import net.dovtech.starmadeplus.blocks.BlockElement;
import net.dovtech.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class DeepStorageComponent extends BlockElement {

    public DeepStorageComponent(BlockConfig config) {
        super(config, "Deep Storage Component", BlockManager.TextureType.MULTIBLOCK);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.setPrice(Blocks.STORAGE.getInfo().price * 2);
        blockInfo.setShoppable(true);

        blockInfo.controlledBy.add(BlockManager.getFromName("Deep Storage Controller").getId());

        BlockConfig.addRecipe(blockInfo, Blocks.SHIELD_CAPACITOR.getInfo().getProducedInFactoryType(), (int) Blocks.SHIELD_CAPACITOR.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, Blocks.STORAGE.getId()),
                new FactoryResource(15, Blocks.CARGO_SPACE_0.getId())
        );

        config.add(blockInfo);
    }
}