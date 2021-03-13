package thederpgamer.starmadeplus.blocks.multiblocks.deepstorage;

import api.config.BlockConfig;
import org.schema.game.common.data.element.ElementKeyMap;
import thederpgamer.starmadeplus.blocks.BlockElement;
import thederpgamer.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class DeepStorageController extends BlockElement {

    public DeepStorageController() {
        super("Deep Storage Controller", BlockManager.TextureType.MULTIBLOCK);
    }

    @Override
    public void initialize() {
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.setPrice(ElementKeyMap.getInfo(ElementKeyMap.STASH_ELEMENT).price * 3);
        blockInfo.setShoppable(true);

        blockInfo.controlledBy.add(ElementKeyMap.STASH_ELEMENT);
        blockInfo.controlledBy.add(ElementKeyMap.RAIL_LOAD);
        blockInfo.controlledBy.add(ElementKeyMap.SHIPYARD_COMPUTER);
        blockInfo.controlledBy.add(ElementKeyMap.SHOP_BLOCK_ID);
        blockInfo.controlledBy.add(ElementKeyMap.FACTORY_BASIC_ID);
        blockInfo.controlledBy.add(ElementKeyMap.FACTORY_STANDARD_ID);
        blockInfo.controlledBy.add(ElementKeyMap.FACTORY_ADVANCED_ID);
        blockInfo.controlledBy.add(ElementKeyMap.FACTORY_CAPSULE_ASSEMBLER_ID);
        blockInfo.controlledBy.add(ElementKeyMap.FACTORY_MICRO_ASSEMBLER_ID);

        blockInfo.controlling.add(BlockManager.getFromName("Deep Storage Display").getId());
        blockInfo.controlling.add(ElementKeyMap.STASH_ELEMENT);
        blockInfo.controlling.add(ElementKeyMap.RAIL_UNLOAD);
        blockInfo.controlling.add(ElementKeyMap.SHIPYARD_COMPUTER);
        blockInfo.controlling.add(ElementKeyMap.SHOP_BLOCK_ID);
        blockInfo.controlling.add(ElementKeyMap.FACTORY_BASIC_ID);
        blockInfo.controlling.add(ElementKeyMap.FACTORY_STANDARD_ID);
        blockInfo.controlling.add(ElementKeyMap.FACTORY_ADVANCED_ID);
        blockInfo.controlling.add(ElementKeyMap.FACTORY_CAPSULE_ASSEMBLER_ID);
        blockInfo.controlling.add(ElementKeyMap.FACTORY_MICRO_ASSEMBLER_ID);

        BlockConfig.addRecipe(blockInfo, ElementKeyMap.getInfo(ElementKeyMap.SHIELD_CAP_ID).getProducedInFactoryType(), (int) ElementKeyMap.getInfo(ElementKeyMap.SHIELD_CAP_ID).getFactoryBakeTime(),
                new FactoryResource(1, ElementKeyMap.STASH_ELEMENT),
                new FactoryResource(5, BlockManager.getFromName("Deep Storage Component").getId()),
                new FactoryResource(1, ElementKeyMap.TEXT_BOX)
        );

        BlockConfig.addRecipe(blockInfo, ElementKeyMap.getInfo(ElementKeyMap.SHIELD_CAP_ID).getProducedInFactoryType(), (int) ElementKeyMap.getInfo(ElementKeyMap.SHIELD_CAP_ID).getFactoryBakeTime(),
                new FactoryResource(1, ElementKeyMap.STASH_ELEMENT),
                new FactoryResource(5, BlockManager.getFromName("Deep Storage Component").getId()),
                new FactoryResource(1, BlockManager.getFromName("Display Screen").getId())
        );

        BlockConfig.add(blockInfo);
    }
}