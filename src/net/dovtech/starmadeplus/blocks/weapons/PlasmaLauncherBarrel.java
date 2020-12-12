package net.dovtech.starmadeplus.blocks.weapons;

import api.config.BlockConfig;
import api.element.block.Blocks;
import net.dovtech.starmadeplus.blocks.BlockElement;
import net.dovtech.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class PlasmaLauncherBarrel extends BlockElement {

    public PlasmaLauncherBarrel(BlockConfig config) {
        super(config, "Plasma Launcher Barrel", BlockManager.TextureType.MODULE);
        blockInfo.setInRecipe(true);
        blockInfo.setShoppable(true);
        blockInfo.setPrice(Blocks.DAMAGE_PULSE_MODULE_0.getInfo().price);
        blockInfo.controlledBy.add(BlockManager.getFromName("Plasma Launcher Computer").getId());

        BlockConfig.addRecipe(blockInfo, Blocks.DAMAGE_PULSE_MODULE_0.getInfo().getProducedInFactory(), (int) Blocks.DAMAGE_PULSE_MODULE_0.getInfo().getFactoryBakeTime(),
                new FactoryResource(100, Blocks.FERTIKEEN_CAPSULE.getId()),
                new FactoryResource(100, Blocks.CRYSTAL_COMPOSITE.getId())
        );
        config.add(blockInfo);
    }
}