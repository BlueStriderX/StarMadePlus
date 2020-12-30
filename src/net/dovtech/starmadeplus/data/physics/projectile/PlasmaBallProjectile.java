package net.dovtech.starmadeplus.data.physics.projectile;

import api.DebugFile;
import net.dovtech.starmadeplus.StarMadePlus;
import net.dovtech.starmadeplus.weapons.plasmalauncher.PlasmaLauncherCollectionManager;
import net.dovtech.starmadeplus.weapons.plasmalauncher.PlasmaLauncherUnit;
import org.schema.game.common.controller.SegmentController;

public class PlasmaBallProjectile extends WeaponProjectile {

    public PlasmaBallProjectile(PlasmaLauncherUnit unit) {
        super(unit);
    }

    @Override
    public void handleCollision() {
        SegmentController hitEntity = rayCast.getSegment().getSegmentController();
        if(hitEntity.getId() != owner.getId() && !owner.getDockingController().isInAnyDockingRelation(hitEntity) && !hitEntity.getDockingController().isInAnyDockingRelation(owner)) {
            if(StarMadePlus.getInstance().debugMode) {
                DebugFile.log("[DEBUG]: Plamsa Ball from entity " + owner.getName() + " made contact with " + hitEntity.getName() + ".", StarMadePlus.getInstance());
            }
            float damage = unit.getDamage();
            //Todo: Plasma Ball impact and damage
        }
    }
}
