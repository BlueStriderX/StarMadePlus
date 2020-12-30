package net.dovtech.starmadeplus.weapons.plasmalauncher;

import api.utils.particle.ModParticle;
import api.utils.particle.ModParticleFactory;
import api.utils.particle.ModParticleUtil;
import net.dovtech.starmadeplus.blocks.BlockManager;
import net.dovtech.starmadeplus.data.particles.ParticleManager;
import net.dovtech.starmadeplus.data.particles.PlasmaBallParticle;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelpManager;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelperContainer;
import org.schema.game.common.controller.PlayerUsableInterface;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.damage.effects.InterEffectSet;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.EffectChangeHanlder;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.core.settings.ContextFilter;
import org.schema.schine.input.InputType;

import javax.vecmath.Vector3f;

public class PlasmaLauncherCollectionManager extends ControlBlockElementCollectionManager<PlasmaLauncherUnit, PlasmaLauncherCollectionManager, PlasmaLauncherElementManager> implements PlayerUsableInterface, EffectChangeHanlder {

    public float currentDamageMult = 1;
    public float damageProduced;
    public float damageCharge;
    public float damageSize;
    private float speedMax;
    private float distanceMax;
    private InterEffectSet effectConfiguration = new InterEffectSet(PlasmaLauncherElementManager.basicEffectConfiguration);
    public PlasmaBallParticle particle = null;

    public PlasmaLauncherCollectionManager(SegmentPiece segmentPiece, SegmentController segmentController, PlasmaLauncherElementManager plasmaLauncherElementManager) {
        super(segmentPiece, BlockManager.getFromName("Plasma Launcher Computer").getId(), segmentController, plasmaLauncherElementManager);
    }

    @Override
    public void addHudConext(ControllerStateUnit unit, HudContextHelpManager h, HudContextHelperContainer.Hos hos) {
        h.addHelper(InputType.MOUSE, MouseEvent.ShootButton.PRIMARY_FIRE.getButton(), Lng.str("Charge Plasma"), hos, ContextFilter.IMPORTANT);
    }

    public InterEffectSet getAttackEffectSet() {
        return this.effectConfiguration;
    }

    @Override
    public void handleControlShot(ControllerStateInterface unit, Timer timer) {
        damageProduced = 0;
        super.handleControlShot(unit, timer);
        if(damageProduced > 0 ) {
            PlasmaLauncherCombinationSettings chargeParams = getChargeParams();
            if(chargeParams.cursorRecoilX > 0 || chargeParams.cursorRecoilY > 0) {
                getElementManager().handleCursorRecoil(this, damageProduced, chargeParams);
            }
        }
    }

    @Override
    public float getWeaponSpeed() {
        return speedMax;
    }

    @Override
    public float getWeaponDistance() {
        return distanceMax;
    }

    @Override
    protected Class<PlasmaLauncherUnit> getType() {
        return PlasmaLauncherUnit.class;
    }

    @Override
    public PlasmaLauncherUnit getInstance() {
        return new PlasmaLauncherUnit();
    }

    @Override
    public String getModuleName() {
        return "Plasma Launcher System";
    }

    @Override
    public void onSwitched(boolean on) {
        super.onSwitched(on);
        damageCharge = 0;
        damageSize = 1f;
    }

    public PlasmaLauncherCombinationSettings getChargeParams() {
        getElementManager().getCombiSettings().damageChargeMax = getDamageChargeMax();
        getElementManager().getCombiSettings().damageChargeSpeed = getDamageChargeSpeed();

        getElementManager().getCombiSettings().cursorRecoilX = getCursorRecoilX();
        getElementManager().getCombiSettings().cursorRecoilMinX = getCursorRecoilMinX();
        getElementManager().getCombiSettings().cursorRecoilMaxX = getCursorRecoilMaxX();
        getElementManager().getCombiSettings().cursorRecoilDirX = getCursorRecoilDirX();

        getElementManager().getCombiSettings().cursorRecoilY = getCursorRecoilY();
        getElementManager().getCombiSettings().cursorRecoilMinY = getCursorRecoilMinY();
        getElementManager().getCombiSettings().cursorRecoilMaxY = getCursorRecoilMaxY();
        getElementManager().getCombiSettings().cursorRecoilDirY = getCursorRecoilDirY();

        ControlBlockElementCollectionManager<?, ?, ?> cp = getSupportCollectionManager();
        if (cp != null)
            getElementManager().getAddOn().calcCombiSettings(getElementManager().getCombiSettings(), this, cp, getEffectCollectionManager());
        return getElementManager().getCombiSettings();
    }

    @Override
    public boolean canUseCollection(ControllerStateInterface stateInterface, Timer timer) {
        PlasmaLauncherCombinationSettings prms = getChargeParams();
        if (stateInterface.isPrimaryShootButtonDown() && prms.damageChargeMax > 0) {
            boolean anyReloading = false;
            for (PlasmaLauncherUnit unit : getElementCollections()) {
                if (!unit.canUse(timer.currentTime, false)) {
                    anyReloading = true;
                    break;
                }
            }

            if (!anyReloading && damageCharge < prms.damageChargeMax) {
                /*
                if (particle == null) {
                    final PlasmaLauncherUnit unit = getInstance();
                    final Vector3f outputPos = unit.getOutput().toVector3f();
                    ModParticleUtil.playClient(outputPos, ParticleManager.PLASMA_BALL.getSprite(), 1, PlasmaLauncherElementManager.PLASMA_BALL_MAX_LIFETIME, outputPos, new ModParticleFactory() {
                        @Override
                        public ModParticle newParticle() {
                            particle = new PlasmaBallParticle(outputPos, unit);
                            return particle;
                        }
                    });
                }
                 */

                damageCharge = Math.min(prms.damageChargeMax, damageCharge + timer.getDelta() * prms.damageChargeSpeed);
                damageSize = Math.min(prms.damageChargeMaxSize, damageSize + timer.getDelta() * prms.damageChargeSpeed);
            }
            return false;
        }
        return super.canUseCollection(stateInterface, timer);
    }

    @Override
    public void onNotShootingButtonDown(ControllerStateInterface stateInterface, Timer timer) {
        super.onNotShootingButtonDown(stateInterface, timer);

        if (damageCharge > 0) {
            this.currentDamageMult = damageCharge;
            handleControlShot(stateInterface, timer);
            damageCharge = 0;
            damageSize = 0;
            this.currentDamageMult = 1;
            particle = null;
        }
    }

    @Override
    public void onChangedCollection() {
        super.onChangedCollection();
        updateInterEffects(PlasmaLauncherElementManager.basicEffectConfiguration, this.effectConfiguration);
        if (!getSegmentController().isOnServer()) {
            ((GameClientState) getSegmentController().getState()).getWorldDrawer().getGuiDrawer().managerChanged(this);
        }

        this.speedMax = 0;
        this.distanceMax = 0;
        ControlBlockElementCollectionManager<?, ?, ?> support = getSupportCollectionManager();
        ControlBlockElementCollectionManager<?, ?, ?> effect = getEffectCollectionManager();
        for (PlasmaLauncherUnit unit : getElementCollections()) {
            if (support != null) {
                PlasmaLauncherUnitModifier mod = (PlasmaLauncherUnitModifier) getElementManager().getAddOn().getGUI(this, unit, support, effect);
                this.speedMax = Math.max(speedMax, mod.outputSpeed);
                this.distanceMax = Math.max(distanceMax, mod.outputDistance);
            } else {
                this.speedMax = Math.max(speedMax, unit.getSpeed());
                this.distanceMax = Math.max(distanceMax, unit.getDistance());
            }
        }

    }

    public float getDamageChargeMax() {
        return PlasmaLauncherElementManager.DAMAGE_CHARGE_MAX;
    }

    public float getDamageChargeSpeed() {
        return PlasmaLauncherElementManager.DAMAGE_CHARGE_SPEED;
    }

    public float getCursorRecoilX() {
        return PlasmaLauncherElementManager.CURSOR_RECOIL_X;
    }

    public float getCursorRecoilMinX() {
        return PlasmaLauncherElementManager.CURSOR_RECOIL_MIN_X;
    }

    public float getCursorRecoilMaxX() {
        return PlasmaLauncherElementManager.CURSOR_RECOIL_MAX_X;
    }

    public float getCursorRecoilDirX() {
        return PlasmaLauncherElementManager.CURSOR_RECOIL_DIR_X;
    }

    public float getCursorRecoilY() {
        return PlasmaLauncherElementManager.CURSOR_RECOIL_Y;
    }

    public float getCursorRecoilMinY() {
        return PlasmaLauncherElementManager.CURSOR_RECOIL_MIN_Y;
    }

    public float getCursorRecoilMaxY() {
        return PlasmaLauncherElementManager.CURSOR_RECOIL_MAX_Y;
    }

    public float getCursorRecoilDirY() {
        return PlasmaLauncherElementManager.CURSOR_RECOIL_DIR_Y;
    }
}
