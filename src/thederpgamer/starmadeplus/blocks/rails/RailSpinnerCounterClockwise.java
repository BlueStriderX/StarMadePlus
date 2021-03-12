package thederpgamer.starmadeplus.blocks.rails;

import api.config.BlockConfig;
import org.schema.game.common.data.element.ElementKeyMap;
import thederpgamer.starmadeplus.blocks.BlockElement;
import thederpgamer.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class RailSpinnerCounterClockwise extends BlockElement {

    public RailSpinnerCounterClockwise() {
        super("Rail Spinner Counter Clock Wise", BlockManager.TextureType.TOP_BOTTOM);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.setPrice(ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).price);
        blockInfo.setShoppable(true);
        blockInfo.controlling.addAll(ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).controlling);
        blockInfo.controlledBy.addAll(ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).controlledBy);
        blockInfo.setBlockStyle(ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).getBlockStyle().id);

        BlockConfig.addRecipe(blockInfo, ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).getProducedInFactoryType(), (int) ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CCW).getFactoryBakeTime(),
                new FactoryResource(1, ElementKeyMap.RAIL_BLOCK_CCW),
                new FactoryResource(1, ElementKeyMap.LOGIC_BUTTON_NORM)
        );

        BlockConfig.addCustomRail(blockInfo, BlockConfig.RailType.ROTATOR);
        BlockConfig.add(blockInfo);
    }
}