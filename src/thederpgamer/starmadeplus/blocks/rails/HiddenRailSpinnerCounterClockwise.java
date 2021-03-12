package thederpgamer.starmadeplus.blocks.rails;

import api.config.BlockConfig;
import org.schema.game.common.data.element.ElementKeyMap;
import thederpgamer.starmadeplus.blocks.BlockElement;
import thederpgamer.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class HiddenRailSpinnerCounterClockwise extends BlockElement {

    public HiddenRailSpinnerCounterClockwise() {
        super("Hidden Rail Spinner Counter Clock Wise", BlockManager.TextureType.TOP_ONLY);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.setPrice(ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).price);
        blockInfo.setShoppable(true);
        blockInfo.controlling.addAll(ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).controlling);
        blockInfo.controlledBy.addAll(ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).controlledBy);
        blockInfo.setBlended(true);
        blockInfo.setDrawOnlyInBuildMode(true);
        blockInfo.setBlockStyle(ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).getBlockStyle().id);

        BlockConfig.addRecipe(blockInfo, ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).getProducedInFactoryType(), (int) ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).getFactoryBakeTime(),
                new FactoryResource(1, ElementKeyMap.RAIL_BLOCK_CCW),
                new FactoryResource(1, ElementKeyMap.EXIT_SHOOT_RAIL),
                new FactoryResource(1, ElementKeyMap.LOGIC_BUTTON_NORM)
        );

        BlockConfig.addRecipe(blockInfo, ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).getProducedInFactoryType(), (int) ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).getFactoryBakeTime(),
                new FactoryResource(1, ElementKeyMap.RAIL_BLOCK_CCW),
                new FactoryResource(1, ElementKeyMap.PICKUP_RAIL),
                new FactoryResource(1,  ElementKeyMap.LOGIC_BUTTON_NORM)
        );

        BlockConfig.addRecipe(blockInfo, ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).getProducedInFactoryType(), (int)ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).getFactoryBakeTime(),
                new FactoryResource(1, ElementKeyMap.RAIL_BLOCK_CCW),
                new FactoryResource(1, ElementKeyMap.PICKUP_AREA),
                new FactoryResource(1, ElementKeyMap.LOGIC_BUTTON_NORM)
        );

        BlockConfig.addCustomRail(blockInfo, BlockConfig.RailType.ROTATOR);
        BlockConfig.add(blockInfo);
    }
}