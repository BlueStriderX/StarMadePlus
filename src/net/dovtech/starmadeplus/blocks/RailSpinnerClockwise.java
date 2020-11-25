package net.dovtech.starmadeplus.blocks;

import api.config.BlockConfig;
import api.element.block.Blocks;
import org.schema.game.common.data.element.FactoryResource;

public class RailSpinnerClockwise extends BlockElement {

    public RailSpinnerClockwise(BlockConfig config) {
        super(config, "Rail Spinner Clock Wise", BlockManager.BlockSide.TOP_BOTTOM);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.setPrice(Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().price);
        blockInfo.setShoppable(true);
        blockInfo.controlling.addAll(Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().controlling);
        blockInfo.controlledBy.addAll(Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().controlledBy);
        blockInfo.setBlockStyle(Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().getBlockStyle().id);

        BlockConfig.addRecipe(blockInfo, Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().getProducedInFactoryType(), (int) Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, Blocks.RAIL_ROTATOR_CLOCK_WISE.getId()),
                new FactoryResource(1, Blocks.BUTTON.getId())
        );

        BlockConfig.addCustomRail(blockInfo, BlockConfig.RailType.ROTATOR);
        config.add(blockInfo);
    }
}
