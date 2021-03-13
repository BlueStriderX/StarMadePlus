package thederpgamer.starmadeplus.blocks.decor;

import api.config.BlockConfig;
import org.schema.game.common.data.element.ElementKeyMap;
import thederpgamer.starmadeplus.StarMadePlus;
import thederpgamer.starmadeplus.blocks.BlockElement;
import thederpgamer.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class DisplayScreen extends BlockElement {

    public DisplayScreen() {
        super("Display Screen", BlockManager.TextureType.MODEL);
    }

    @Override
    public void initialize() {
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.setPrice(ElementKeyMap.getInfo(ElementKeyMap.TEXT_BOX).price);
        blockInfo.setShoppable(true);

        BlockConfig.addRecipe(blockInfo, ElementKeyMap.getInfo(ElementKeyMap.TEXT_BOX).getProducedInFactoryType(), (int) ElementKeyMap.getInfo(ElementKeyMap.TEXT_BOX).getFactoryBakeTime(),
                new FactoryResource(1, ElementKeyMap.TEXT_BOX)
        );

        BlockConfig.assignLod(blockInfo, StarMadePlus.getInstance(), "display_screen", null);
        BlockConfig.add(blockInfo);
    }
}