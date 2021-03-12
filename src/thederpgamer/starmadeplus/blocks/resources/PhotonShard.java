package thederpgamer.starmadeplus.blocks.resources;

import thederpgamer.starmadeplus.blocks.BlockElement;

public class PhotonShard extends BlockElement {

    public PhotonShard() {
        super("Photon Shard");
        blockInfo.setInRecipe(false);
        blockInfo.canActivate = true;
        blockInfo.shoppable = false;
    }
}