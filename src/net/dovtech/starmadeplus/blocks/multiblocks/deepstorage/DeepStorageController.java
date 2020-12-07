package net.dovtech.starmadeplus.blocks.multiblocks.deepstorage;

import api.config.BlockConfig;
import api.element.block.Blocks;
import net.dovtech.starmadeplus.blocks.BlockElement;
import net.dovtech.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class DeepStorageController extends BlockElement {

    public DeepStorageController(BlockConfig config) {
        super(config, "Deep Storage Controller", BlockManager.TextureType.MULTIBLOCK);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = true;
        blockInfo.setPrice(Blocks.STORAGE.getInfo().price * 3);
        blockInfo.setShoppable(true);

        blockInfo.controlledBy.add(Blocks.STORAGE.getId());
        blockInfo.controlledBy.add(Blocks.RAIL_LOAD_0.getId());
        blockInfo.controlledBy.add(Blocks.SHIPYARD_COMPUTER_0.getId());
        blockInfo.controlledBy.add(Blocks.SHOP_MODULE.getId());
        blockInfo.controlledBy.add(Blocks.BASIC_FACTORY.getId());
        blockInfo.controlledBy.add(Blocks.STANDARD_FACTORY.getId());
        blockInfo.controlledBy.add(Blocks.ADVANCED_FACTORY.getId());
        blockInfo.controlledBy.add(Blocks.CAPSULE_REFINERY.getId());
        blockInfo.controlledBy.add(Blocks.MICRO_ASSEMBLER.getId());

        blockInfo.controlling.add(BlockManager.getFromName("Deep Storage Display").getId());
        blockInfo.controlling.add(Blocks.STORAGE.getId());
        blockInfo.controlling.add(Blocks.RAIL_UNLOAD_0.getId());
        blockInfo.controlling.add(Blocks.SHIPYARD_COMPUTER_0.getId());
        blockInfo.controlling.add(Blocks.SHOP_MODULE.getId());
        blockInfo.controlling.add(Blocks.BASIC_FACTORY.getId());
        blockInfo.controlling.add(Blocks.STANDARD_FACTORY.getId());
        blockInfo.controlling.add(Blocks.ADVANCED_FACTORY.getId());
        blockInfo.controlling.add(Blocks.CAPSULE_REFINERY.getId());
        blockInfo.controlling.add(Blocks.MICRO_ASSEMBLER.getId());

        BlockConfig.addRecipe(blockInfo, Blocks.SHIELD_CAPACITOR.getInfo().getProducedInFactoryType(), (int) Blocks.SHIELD_CAPACITOR.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, Blocks.STORAGE.getId()),
                new FactoryResource(5, BlockManager.getFromName("Deep Storage Component").getId()),
                new FactoryResource(1, Blocks.DISPLAY_MODULE.getId())
        );

        BlockConfig.addRecipe(blockInfo, Blocks.SHIELD_CAPACITOR.getInfo().getProducedInFactoryType(), (int) Blocks.SHIELD_CAPACITOR.getInfo().getFactoryBakeTime(),
                new FactoryResource(1, Blocks.STORAGE.getId()),
                new FactoryResource(5, BlockManager.getFromName("Deep Storage Component").getId()),
                new FactoryResource(1, BlockManager.getFromName("Display Screen").getId())
        );

        config.add(blockInfo);
    }
}