package net.dovtech.starmadeplus.blocks.multiblocks.deepstorage;

import api.config.BlockConfig;
import api.element.block.Blocks;
import net.dovtech.starmadeplus.blocks.BlockElement;
import net.dovtech.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class DeepStorageDisplay extends BlockElement {

    public DeepStorageDisplay(BlockConfig config) {
        super(config, "Deep Storage Display", BlockManager.TextureType.MULTIBLOCK);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.setPrice((long) (Blocks.STORAGE.getInfo().price * 2.5));
        blockInfo.setShoppable(true);

        blockInfo.controlledBy.add(BlockManager.getFromName("Deep Storage Controller").getId());

        BlockConfig.addRecipe(blockInfo, Blocks.SHIELD_CAPACITOR.getInfo().getProducedInFactoryType(), (int) Blocks.SHIELD_CAPACITOR.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, BlockManager.getFromName("Deep Storage Component").getId()),
                new FactoryResource(1, BlockManager.getFromName("Display Screen").getId())
        );

        BlockConfig.addRecipe(blockInfo, Blocks.SHIELD_CAPACITOR.getInfo().getProducedInFactoryType(), (int) Blocks.SHIELD_CAPACITOR.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, BlockManager.getFromName("Deep Storage Component").getId()),
                new FactoryResource(1, Blocks.DISPLAY_MODULE.getId())
        );

        config.add(blockInfo);
    }
}
