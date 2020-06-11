package net.dovtech.starmadeplus.blocks;

import api.config.BlockConfig;
import api.element.block.Blocks;
import api.element.usable.PlayerUsableHelper;
import org.schema.game.common.data.element.ElementInformation;

public class MassDriverLauncher {

    private ElementInformation blockinfo;

    public MassDriverLauncher() {
        blockinfo = BlockConfig.newElement("Mass Driver Launcher");
        blockinfo.description = "Link to a Mass Driver Computer to create a Mass Driver weapon.";
        blockinfo.systemBlock = true;
        blockinfo.canActivate = true;
        blockinfo.mainCombinationController = false;
        blockinfo.shoppable = true;
        blockinfo.price = Blocks.MINE_CORE.getInfo().price;
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
