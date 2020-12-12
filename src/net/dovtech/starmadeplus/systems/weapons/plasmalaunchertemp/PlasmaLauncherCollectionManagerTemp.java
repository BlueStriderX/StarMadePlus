package net.dovtech.starmadeplus.systems.weapons.plasmalaunchertemp;

import api.utils.game.module.CustomModuleUtils;
import net.dovtech.starmadeplus.blocks.BlockManager;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.weapon.WeaponCollectionManager;
import org.schema.game.common.data.SegmentPiece;

public class PlasmaLauncherCollectionManagerTemp extends WeaponCollectionManager {

    public PlasmaLauncherCollectionManagerTemp(SegmentPiece segmentPiece, SegmentController segmentController, PlasmaLauncherElementManagerTemp elementManager) {
        super(segmentPiece, segmentController, elementManager);
        CustomModuleUtils.setCollectionManager(this, BlockManager.getFromName("Plasma Launcher Barrel").getId());
    }

    @Override
    public PlasmaLauncherUnitTemp getInstance() {
        return new PlasmaLauncherUnitTemp();
    }
}
