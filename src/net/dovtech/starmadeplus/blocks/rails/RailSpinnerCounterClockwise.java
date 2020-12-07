package net.dovtech.starmadeplus.blocks.rails;

import api.config.BlockConfig;
import api.element.block.Blocks;
import net.dovtech.starmadeplus.blocks.BlockElement;
import net.dovtech.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class RailSpinnerCounterClockwise extends BlockElement {

    public RailSpinnerCounterClockwise(BlockConfig config) {
        super(config, "Rail Spinner CounterClock Wise", BlockManager.TextureType.TOP_BOTTOM);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.setPrice(Blocks.RAIL_ROTATOR_COUNTER_CLOCK_WISE.getInfo().price);
        blockInfo.setShoppable(true);
        blockInfo.controlling.addAll(Blocks.RAIL_ROTATOR_COUNTER_CLOCK_WISE.getInfo().controlling);
        blockInfo.controlledBy.addAll(Blocks.RAIL_ROTATOR_COUNTER_CLOCK_WISE.getInfo().controlledBy);
        blockInfo.setBlockStyle(Blocks.RAIL_ROTATOR_COUNTER_CLOCK_WISE.getInfo().getBlockStyle().id);

        BlockConfig.addRecipe(blockInfo, Blocks.RAIL_ROTATOR_COUNTER_CLOCK_WISE.getInfo().getProducedInFactoryType(), (int) Blocks.RAIL_ROTATOR_COUNTER_CLOCK_WISE.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, Blocks.RAIL_ROTATOR_COUNTER_CLOCK_WISE.getId()),
                new FactoryResource(1, Blocks.BUTTON.getId())
        );

        BlockConfig.addCustomRail(blockInfo, BlockConfig.RailType.ROTATOR);
        config.add(blockInfo);
    }
}