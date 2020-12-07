package net.dovtech.starmadeplus.blocks.decor;

import api.config.BlockConfig;
import api.element.block.Blocks;
import net.dovtech.starmadeplus.StarMadePlus;
import net.dovtech.starmadeplus.blocks.BlockElement;
import net.dovtech.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class DisplayScreen extends BlockElement {

    public DisplayScreen(BlockConfig config) {
        super(config, "Display Screen", BlockManager.TextureType.MODEL);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.setPrice(Blocks.DISPLAY_MODULE.getInfo().price);
        blockInfo.setShoppable(true);

        BlockConfig.addRecipe(blockInfo, Blocks.DISPLAY_MODULE.getInfo().getProducedInFactoryType(), (int) Blocks.DISPLAY_MODULE.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, Blocks.DISPLAY_MODULE.getId())
        );

        config.assignLod(blockInfo, StarMadePlus.getInstance(), "displayscreen", null);
        config.add(blockInfo);
    }
}