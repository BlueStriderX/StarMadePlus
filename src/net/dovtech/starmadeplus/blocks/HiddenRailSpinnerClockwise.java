package net.dovtech.starmadeplus.blocks;

import api.config.BlockConfig;
import api.element.block.Blocks;
import org.schema.game.common.data.element.FactoryResource;

public class HiddenRailSpinnerClockwise extends BlockElement {

    public HiddenRailSpinnerClockwise(BlockConfig config) {
        super(config, "Hidden Rail Spinner Clockwise", BlockManager.BlockSide.TOP_ONLY);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = false;
        blockInfo.setPrice(Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().price);
        blockInfo.setShoppable(true);
        blockInfo.controlling.addAll(Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().controlling);
        blockInfo.controlledBy.addAll(Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().controlledBy);
        blockInfo.setDrawOnlyInBuildMode(true);
        blockInfo.setBlockStyle(Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().getBlockStyle().id);

        BlockConfig.addRecipe(blockInfo, Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().getProducedInFactoryType(), (int) Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, Blocks.RAIL_ROTATOR_CLOCK_WISE.getId()),
                new FactoryResource(1, Blocks.SHOOTOUT_RAIL.getId()),
                new FactoryResource(1, Blocks.BUTTON.getId())
        );

        BlockConfig.addRecipe(blockInfo, Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().getProducedInFactoryType(), (int) Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, Blocks.RAIL_ROTATOR_CLOCK_WISE.getId()),
                new FactoryResource(1, Blocks.PICKUP_RAIL_0.getId()),
                new FactoryResource(1, Blocks.BUTTON.getId())
        );

        BlockConfig.addRecipe(blockInfo, Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().getProducedInFactoryType(), (int) Blocks.RAIL_ROTATOR_CLOCK_WISE.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, Blocks.RAIL_ROTATOR_CLOCK_WISE.getId()),
                new FactoryResource(1, Blocks.PICKUP_POINT.getId()),
                new FactoryResource(1, Blocks.BUTTON.getId())
        );

        BlockConfig.addCustomRail(blockInfo, BlockConfig.RailType.ROTATOR);
        config.add(blockInfo);
    }
}