package net.dovtech.starmadeplus.blocks;

import api.config.BlockConfig;
import api.element.block.Blocks;
import api.element.usable.PlayerUsableHelper;
import org.schema.game.common.data.element.ElementInformation;

public class MassDriverComputer {

    private ElementInformation blockinfo;

    public MassDriverComputer() {
        blockinfo = BlockConfig.newElement("Mass Driver Computer");
        blockinfo.description = "Use to control a Mass Driver System.";
        blockinfo.systemBlock = true;
        blockinfo.canActivate = true;
        blockinfo.mainCombinationController = true;
        blockinfo.shoppable = true;
        blockinfo.price = Blocks.MINE_LAYER.getInfo().price;
        blockinfo.inRecipe = true;
        blockinfo.controlledBy.add(Blocks.SHIP_CORE.getId());
        blockinfo.controlling.add(new MassDriverLauncher().getBlockinfo().getId());
        blockinfo.controlling.add(new MassDriverAmmoBox().getBlockinfo().getId());
    }

    public ElementInformation getBlockinfo() {
        return blockinfo;
    }

    public long getPlayerUseableID() {
        return PlayerUsableHelper.getPlayerUsableFromBlockID(blockinfo.getId());
    }
}