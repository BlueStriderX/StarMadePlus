package net.dovtech.starmadeplus.systems.weapons.plasmalaunchertemp;

import api.utils.game.module.CustomModuleUtils;
import api.utils.particle.ModParticle;
import api.utils.particle.ModParticleFactory;
import api.utils.particle.ModParticleUtil;
import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
import net.dovtech.starmadeplus.blocks.BlockManager;
import net.dovtech.starmadeplus.data.particles.ParticleManager;
import net.dovtech.starmadeplus.data.particles.PlasmaBallParticle;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.weapon.WeaponCollectionManager;
import org.schema.game.common.controller.elements.weapon.WeaponElementManager;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ShootContainer;
import org.schema.game.common.data.player.PlayerState;
import org.schema.schine.graphicsengine.core.Timer;
import javax.vecmath.Vector3f;

public class PlasmaLauncherElementManagerTemp extends WeaponElementManager {

    private float projectileSize = 1f;
    private float plasmaSpeed = 60f;

    public PlasmaLauncherElementManagerTemp(SegmentController segmentController) {
        super(segmentController);
        CustomModuleUtils.setElementManager(this, BlockManager.getFromName("Plasma Launcher Computer").getId(), BlockManager.getFromName("Plasma Launcher Barrel").getId());
    }

    @Override
    protected String getTag() {
        return "mainreactor";
    }

    @Override
    public PlasmaLauncherCollectionManagerTemp getNewCollectionManager(SegmentPiece piece, Class<WeaponCollectionManager> collectionManagerClass) {
        return new PlasmaLauncherCollectionManagerTemp(piece, this.getSegmentController(), this);
    }

    @Override
    public void updateActivationTypes(ShortOpenHashSet set) {
        set.add(BlockManager.getFromName("Plasma Launcher Barrel").getId());
    }

    public void doShot(final PlasmaLauncherUnitTemp unit, PlasmaLauncherCollectionManagerTemp collection, final ShootContainer shootContainer, PlayerState playerState, Timer timer) {
        final Vector3f shootDir = new Vector3f(shootContainer.shootingDirTemp);
        shootDir.add(new Vector3f(0, plasmaSpeed,0));

        ModParticleUtil.playClient(shootContainer.weapontOutputWorldPos, ParticleManager.PLASMA_BALL.getSprite(), 1, 1000, shootDir, new ModParticleFactory() {
            @Override
            public ModParticle newParticle() {
                return new PlasmaBallParticle(shootDir, projectileSize, unit.getColor());
            }
        });

        super.doShot(unit, collection, shootContainer, playerState, timer);
    }
}
