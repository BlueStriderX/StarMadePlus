package net.dovtech.starmadeplus.blocks;

import api.config.BlockConfig;
import api.element.block.Blocks;
import org.schema.game.common.data.element.FactoryResource;

public class HoloProjector extends BlockElement {

    public HoloProjector(BlockConfig config) {
        super(config, "Holo Projector", BlockManager.BlockSide.ALL);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.lodShapeString = "holo projector";
        blockInfo.lodUseDetailCollision = true;
        blockInfo.lodCollisionPhysical = true;
        blockInfo.setPrice(Blocks.DISPLAY_MODULE.getInfo().price);
        blockInfo.setShoppable(true);

        BlockConfig.addRecipe(blockInfo, Blocks.DISPLAY_MODULE.getInfo().getProducedInFactoryType(), (int) Blocks.DISPLAY_MODULE.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, Blocks.DISPLAY_MODULE.getId())
        );

        config.add(blockInfo);
    }
}
