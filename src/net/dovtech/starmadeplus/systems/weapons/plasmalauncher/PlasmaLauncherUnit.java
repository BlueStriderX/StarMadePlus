package net.dovtech.starmadeplus.systems.weapons.plasmalauncher;

import org.schema.game.client.data.GameClientState;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.client.view.gui.structurecontrol.ControllerManagerGUI;
import org.schema.game.common.controller.damage.DamageDealerType;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.combination.modifier.tagMod.DamageUnitInterface;
import org.schema.game.common.data.blockeffects.config.StatusEffectType;
import org.schema.game.common.data.element.CustomOutputUnit;
import org.schema.game.common.data.element.ShootContainer;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.schine.graphicsengine.core.Timer;

public class PlasmaLauncherUnit extends CustomOutputUnit<PlasmaLauncherUnit, PlasmaLauncherCollectionManager, PlasmaLauncherElementManager> implements DamageUnitInterface {

    @Override
    public String toString() {
        return "PlasmaLauncherUnit " + super.toString();
    }

    @Override
    public void doShot(ControllerStateInterface unit, Timer timer, ShootContainer shootContainer) {
        boolean focus = unit.canFocusWeapon();
        unit.getShootingDir(getSegmentController(), shootContainer, getDistanceFull(), getSpeed(), elementCollectionManager.getControllerPos(), focus, true);

        assert(shootContainer.shootingDirTemp.lengthSquared() > 0):shootContainer.shootingDirTemp;

        shootContainer.shootingDirTemp.normalize();
        float speed = getSpeed() * ((GameStateInterface) getSegmentController().getState()).getGameState().isRelativeProjectiles();
        shootContainer.shootingDirTemp.scale(speed);
        PlasmaLauncherElementManager elementManager = elementCollectionManager.getElementManager();
        elementManager.doShot(this, elementCollectionManager, shootContainer, unit.getPlayerState(), timer);
    }

    public float getSpeed() {
        return ((GameStateInterface) getSegmentController().getState()).getGameState().getMaxGalaxySpeed() * PlasmaLauncherElementManager.BASE_SPEED;
    }

    private float getBaseConsume() {
        return ((size() + getEffectBonus()) * PlasmaLauncherElementManager.BASE_POWER_CONSUMPTION);
    }


    public float getDamageWithoutEffect() {
        return ((size()) * getBaseDamage());
    }

    @Override
    public DamageDealerType getDamageType() {
        return DamageDealerType.PROJECTILE;
    }

    @Override
    public float getBasePowerConsumption() {
        return PlasmaLauncherElementManager.BASE_POWER_CONSUMPTION;
    }

    @Override
    public float getPowerConsumption() {
        return getExtraConsume() * getBaseConsume();
    }

    @Override
    public float getPowerConsumptionWithoutEffect() {
        return ((size()) * PlasmaLauncherElementManager.BASE_POWER_CONSUMPTION);
    }

    @Override
    public float getReloadTimeMs() {
        return PlasmaLauncherElementManager.BASE_RELOAD;
    }

    @Override
    public float getInitializationTime() {
        return getReloadTimeMs();
    }

    @Override
    public float getDistanceRaw() {
        return PlasmaLauncherElementManager.BASE_DISTANCE * ((GameStateInterface) getSegmentController().getState()).getGameState().getWeaponRangeReference();
    }

    @Override
    public float getFiringPower() {
        return getDamage();
    }

    @Override
    public float getDamage() {
        float damage = ((size() + getEffectBonus()) * getBaseDamage());
        damage = getConfigManager().apply(StatusEffectType.WEAPON_DAMAGE, getDamageType(), damage);
        return damage;
    }

    @Override
    public float getBaseDamage() {
        return PlasmaLauncherElementManager.BASE_DAMAGE.get(getSegmentController().isUsingPowerReactors()) * elementCollectionManager.currentDamageMult;
    }

    public double getPowerConsumedPerSecondRestingPerBlock() {
        double powCons = PlasmaLauncherElementManager.REACTOR_POWER_CONSUMPTION_RESTING;
        powCons = getConfigManager().apply(StatusEffectType.WEAPON_TOP_OFF_RATE, getDamageType(), powCons);
        return powCons;
    }

    public double getPowerConsumedPerSecondChargingPerBlock() {
        double powCons = PlasmaLauncherElementManager.REACTOR_POWER_CONSUMPTION_CHARGING;
        powCons = getConfigManager().apply(StatusEffectType.WEAPON_CHARGE_RATE, getDamageType(), powCons);
        return powCons;
    }

    @Override
    public double getPowerConsumedPerSecondResting() {
        return elementCollectionManager.getElementManager().calculatePowerConsumptionCombi(getPowerConsumedPerSecondRestingPerBlock(), false, this);
    }

    @Override
    public double getPowerConsumedPerSecondCharging() {
        return elementCollectionManager.getElementManager().calculatePowerConsumptionCombi(getPowerConsumedPerSecondChargingPerBlock(), true, this);
    }

    @Override
    public PowerConsumerCategory getPowerConsumerCategory() {
        return PowerConsumerCategory.PULSE;
    }

    @Override
    public ControllerManagerGUI createUnitGUI(GameClientState gameClientState, ControlBlockElementCollectionManager<?, ?, ?> supportCollection, ControlBlockElementCollectionManager<?, ?, ?> effectCollection) {
        return elementCollectionManager.getElementManager().getGUIUnitValues(this, elementCollectionManager, supportCollection, effectCollection);
    }

    public float getImpactForce() {
        return PlasmaLauncherElementManager.IMPACT_FORCE;
    }

    public float getRecoil() {
        return PlasmaLauncherElementManager.RECOIL;
    }
}
