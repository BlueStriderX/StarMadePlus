package net.dovtech.starmadeplus.blocks.resources;

import api.config.BlockConfig;
import net.dovtech.starmadeplus.blocks.BlockElement;

public class PhotonShard extends BlockElement {

    public PhotonShard(BlockConfig config) {
        super(config, "Photon Shard");
        blockInfo.setInRecipe(false);
        blockInfo.canActivate = true;
        blockInfo.shoppable = false;
    }
}