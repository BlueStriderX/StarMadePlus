package net.dovtech.starmadeplus.blocks.weapons;

import api.config.BlockConfig;
import api.element.block.Blocks;
import net.dovtech.starmadeplus.blocks.BlockElement;
import net.dovtech.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class PlasmaLauncherComputer extends BlockElement {

    public PlasmaLauncherComputer(BlockConfig config) {
        super(config, "Plasma Launcher Computer", BlockManager.TextureType.COMPUTER);
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = Blocks.TRACTOR_BEAM_COMPUTER.getInfo().canActivate;
        blockInfo.setShoppable(true);
        blockInfo.setPrice(Blocks.DAMAGE_PULSE_COMPUTER_0.getInfo().price);
        blockInfo.controlling.add(BlockManager.getFromName("Plasma Launcher Barrel").getId());
        blockInfo.controlledBy.addAll(Blocks.DAMAGE_PULSE_COMPUTER_0.getInfo().controlledBy);

        BlockConfig.addRecipe(blockInfo, Blocks.DAMAGE_PULSE_COMPUTER_0.getInfo().getProducedInFactory(), (int) Blocks.DAMAGE_PULSE_COMPUTER_0.getInfo().getFactoryBakeTime(),
                new FactoryResource(500, Blocks.FERTIKEEN_CAPSULE.getId()),
                new FactoryResource(500, Blocks.CRYSTAL_COMPOSITE.getId())
        );
        config.add(blockInfo);
    }
}