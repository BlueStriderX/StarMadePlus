package net.dovtech.starmadeplus.data.physics.projectile;

import api.DebugFile;
import net.dovtech.starmadeplus.StarMadePlus;
import net.dovtech.starmadeplus.systems.weapons.plasmalauncher.PlasmaLauncherCollectionManager;
import net.dovtech.starmadeplus.systems.weapons.plasmalauncher.PlasmaLauncherUnit;
import org.schema.game.common.controller.SegmentController;

public class PlasmaBallProjectile extends WeaponProjectile {

    private PlasmaLauncherCollectionManager collectionManager;

    public PlasmaBallProjectile(PlasmaLauncherUnit unit) {
        super(unit);
        this.collectionManager = unit.elementCollectionManager;
    }

    @Override
    public boolean isFired() {
        return (!isShootButtonDown() && collectionManager.damageCharge > 0 && collectionManager.damageCharge <= collectionManager.getDamageChargeMax());
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
