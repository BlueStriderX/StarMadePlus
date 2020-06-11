package net.dovtech.starmadeplus.blocks;

import api.config.BlockConfig;
import api.element.block.Blocks;
import api.element.usable.PlayerUsableHelper;
import org.schema.game.common.data.element.ElementInformation;

public class MassDriverAmmoBox {

    private ElementInformation blockinfo;

    public MassDriverAmmoBox() {
        blockinfo = BlockConfig.newElement("Mass Driver Ammo Box");
        blockinfo.description = "Supplies ammunition to connected Mass Driver systems.";
        blockinfo.systemBlock = true;
        blockinfo.mainCombinationController = true;
        blockinfo.shoppable = true;
        blockinfo.price = Blocks.MISSILE_CAPACITY_MODULE.getInfo().price;
        blockinfo.inRecipe = true;
        blockinfo.controlledBy.add(new MassDriverComputer().getBlockinfo().getId());
    }

    public ElementInformation getBlockinfo() {
        return blockinfo;
    }

    public long getPlayerUseableID() {
        return PlayerUsableHelper.getPlayerUsableFromBlockID(blockinfo.getId());
    }
}
