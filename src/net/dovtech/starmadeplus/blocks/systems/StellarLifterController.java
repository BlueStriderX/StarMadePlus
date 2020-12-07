package net.dovtech.starmadeplus.blocks.systems;

import api.config.BlockConfig;
import api.element.block.Blocks;
import api.element.block.FactoryType;
import net.dovtech.starmadeplus.blocks.BlockElement;
import net.dovtech.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class StellarLifterController extends BlockElement {

    public StellarLifterController(BlockConfig config) {
        super(config, "Stellar Lifter Controller", BlockManager.TextureType.COMPUTER);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = Blocks.TRACTOR_BEAM_COMPUTER.getInfo().canActivate;
        blockInfo.setPrice(Blocks.TRACTOR_BEAM_COMPUTER.getInfo().price * 5);
        blockInfo.setShoppable(true);
        blockInfo.controlling.add(BlockManager.getFromName("Stellar Lifter Module").getId());
        blockInfo.controlledBy.addAll(Blocks.TRACTOR_BEAM_COMPUTER.getInfo().controlledBy);

        BlockConfig.addRecipe(blockInfo, FactoryType.ADVANCED.getId(), (int) Blocks.SHIELD_CAPACITOR.getInfo().getFactoryBakeTime(),
                new FactoryResource(10, Blocks.TRACTOR_BEAM_COMPUTER.getId()),
                new FactoryResource(30, Blocks.TRACTOR_BEAM.getId()),
                new FactoryResource(15, Blocks.GRAVITY_UNIT.getId()),
                new FactoryResource(1, BlockManager.getFromName("Photon Shard").getId())

        );

        config.add(blockInfo);
    }
}
