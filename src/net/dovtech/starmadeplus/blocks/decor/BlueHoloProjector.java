package net.dovtech.starmadeplus.blocks.decor;

import api.config.BlockConfig;
import api.element.block.Blocks;
import net.dovtech.starmadeplus.StarMadePlus;
import net.dovtech.starmadeplus.blocks.BlockElement;
import net.dovtech.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class BlueHoloProjector extends BlockElement {

    public BlueHoloProjector(BlockConfig config) {
        super(config, "Blue Holo Projector", BlockManager.TextureType.MODEL);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.setPrice(Blocks.DISPLAY_MODULE.getInfo().price * 10);
        blockInfo.setShoppable(true);

        BlockConfig.addRecipe(blockInfo, Blocks.DISPLAY_MODULE.getInfo().getProducedInFactoryType(), (int) Blocks.DISPLAY_MODULE.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, Blocks.BLUE_CONSOLE.getId()),
                new FactoryResource(9, BlockManager.getFromName("Display Screen").getId())
        );

        BlockConfig.addRecipe(blockInfo, Blocks.DISPLAY_MODULE.getInfo().getProducedInFactoryType(), (int) Blocks.DISPLAY_MODULE.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, Blocks.BLUE_CONSOLE_INNER_CORNER.getId()),
                new FactoryResource(9, BlockManager.getFromName("Display Screen").getId())
        );

        BlockConfig.addRecipe(blockInfo, Blocks.DISPLAY_MODULE.getInfo().getProducedInFactoryType(), (int) Blocks.DISPLAY_MODULE.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, Blocks.BLUE_CONSOLE_OUTER_CORNER.getId()),
                new FactoryResource(9, BlockManager.getFromName("Display Screen").getId())
        );

        config.assignLod(blockInfo, StarMadePlus.getInstance(), "BlueHoloProjector", null);
        config.add(blockInfo);
    }
}
