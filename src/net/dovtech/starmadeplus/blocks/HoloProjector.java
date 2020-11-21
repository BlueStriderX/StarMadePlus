package net.dovtech.starmadeplus.blocks;

import api.config.BlockConfig;

public class HoloProjector extends BlockElement {

    public HoloProjector(BlockConfig config) {
        super(config, "Holo Projector", BlockManager.BlockSide.ALL);
        config.add(blockInfo);
    }
}
