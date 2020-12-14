package net.dovtech.starmadeplus.data.particles;

import api.utils.particle.ModParticle;
import net.dovtech.starmadeplus.data.physics.projectile.PlasmaBallProjectile;
import net.dovtech.starmadeplus.systems.weapons.plasmalauncher.PlasmaLauncherUnit;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;

public class PlasmaBallParticle extends ModParticle {

    private float size;
    private Vector3f direction;
    private Vector4f color;
    private PlasmaLauncherUnit unit;
    private PlasmaBallProjectile projectile;

    public PlasmaBallParticle(Vector3f direction, PlasmaLauncherUnit unit) {
        this.size = 1.0f;
        this.direction = direction;
        this.unit = unit;
        this.color = unit.getColor();
    }

    @Override
    public void spawn() {
        projectile = new PlasmaBallProjectile(unit);
        super.spawn();
    }

    @Override
    public void update(long currentTime) {
        if(!projectile.isFired()) {
            if(unit.elementCollectionManager.damageCharge < unit.elementCollectionManager.getDamageChargeMax()) {
                sizeByPercent(this, unit.elementCollectionManager.damageCharge, unit.elementCollectionManager.damageSize, unit.elementCollectionManager.damageSize + 0.1f);
            }
        } else {
            if(velocity.equals(new Vector3f(0, 0, 0))) {
                velocity.set(direction);
            } else {
                boolean collision = projectile.checkCollision(unit.getSegmentController(), position, true);
                if(collision) {
                    projectile.handleCollision();
                    //Todo: Explosion effect
                    this.die();
                } else {
                    rotate(this, ticksLived / 150f);
                    projectile.setPosition(position);
                }
            }
        }
    }
}
