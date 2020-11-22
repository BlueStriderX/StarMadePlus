package net.dovtech.starmadeplus.blocks;

import api.config.BlockConfig;

public class DisplayScreen extends BlockElement {

    public DisplayScreen(BlockConfig config) {
        super(config, "Display Screen", BlockManager.BlockSide.ALL);
        config.add(blockInfo);
    }
}