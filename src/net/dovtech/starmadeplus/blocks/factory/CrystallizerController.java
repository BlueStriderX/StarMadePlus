package net.dovtech.starmadeplus.blocks.factory;

import api.config.BlockConfig;
import api.element.block.Blocks;

public class CrystallizerController extends FactoryElement {

    public CrystallizerController(BlockConfig config) {
        super(config, "Crystallizer Controller");
        blockInfo.shoppable = true;
        blockInfo.price = Blocks.ADVANCED_FACTORY.getInfo().price * 15;

        config.add(blockInfo);
    }
}
