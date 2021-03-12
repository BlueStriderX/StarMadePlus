package thederpgamer.starmadeplus.blocks.rails;

import api.config.BlockConfig;
import org.schema.game.common.data.element.ElementKeyMap;
import thederpgamer.starmadeplus.blocks.BlockElement;
import thederpgamer.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class RailSpinnerClockwise extends BlockElement {

    public RailSpinnerClockwise() {
        super("Rail Spinner Clock Wise", BlockManager.TextureType.TOP_BOTTOM);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.setPrice(ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CW).price);
        blockInfo.setShoppable(true);
        blockInfo.controlling.addAll(ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CW).controlling);
        blockInfo.controlledBy.addAll(ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CW).controlledBy);
        blockInfo.setBlockStyle(ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CW).getBlockStyle().id);

        BlockConfig.addRecipe(blockInfo, ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CW).getProducedInFactoryType(), (int) ElementKeyMap.getInfo(ElementKeyMap.RAIL_BLOCK_CW).getFactoryBakeTime(),
                new FactoryResource(1, ElementKeyMap.RAIL_BLOCK_CW),
                new FactoryResource(1, ElementKeyMap.LOGIC_BUTTON_NORM)
        );

        BlockConfig.addCustomRail(blockInfo, BlockConfig.RailType.ROTATOR);
        BlockConfig.add(blockInfo);
    }
}
