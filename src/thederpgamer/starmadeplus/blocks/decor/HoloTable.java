package thederpgamer.starmadeplus.blocks.decor;

import api.config.BlockConfig;
import org.schema.game.common.data.element.ElementKeyMap;
import thederpgamer.starmadeplus.StarMadePlus;
import thederpgamer.starmadeplus.blocks.BlockElement;
import thederpgamer.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class HoloTable extends BlockElement {

    public HoloTable() {
        super("Holo Table", BlockManager.TextureType.MODEL);
    }

    @Override
    public void initialize() {
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.setPrice(ElementKeyMap.getInfo(ElementKeyMap.TEXT_BOX).price * 10);
        blockInfo.setShoppable(true);

        BlockConfig.addRecipe(blockInfo, ElementKeyMap.getInfo(ElementKeyMap.TEXT_BOX).getProducedInFactoryType(), (int) ElementKeyMap.getInfo(ElementKeyMap.TEXT_BOX).getFactoryBakeTime(),
                new FactoryResource(1, (short) 975),
                new FactoryResource(9, BlockManager.getFromName("Display Screen").getId())
        );

        BlockConfig.addRecipe(blockInfo, ElementKeyMap.getInfo(ElementKeyMap.TEXT_BOX).getProducedInFactoryType(), (int) ElementKeyMap.getInfo(ElementKeyMap.TEXT_BOX).getFactoryBakeTime(),
                new FactoryResource(1, (short) 1028),
                new FactoryResource(9, BlockManager.getFromName("Display Screen").getId())
        );

        BlockConfig.addRecipe(blockInfo, ElementKeyMap.getInfo(ElementKeyMap.TEXT_BOX).getProducedInFactoryType(), (int) ElementKeyMap.getInfo(ElementKeyMap.TEXT_BOX).getFactoryBakeTime(),
                new FactoryResource(1, (short) 1029),
                new FactoryResource(9, BlockManager.getFromName("Display Screen").getId())
        );

        BlockConfig.assignLod(blockInfo, StarMadePlus.getInstance(), "holo_table", null);
        BlockConfig.add(blockInfo);
    }
}
