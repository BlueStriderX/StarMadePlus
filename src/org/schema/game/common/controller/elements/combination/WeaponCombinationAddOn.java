/*
 * WeaponCombinationAddOn.java (modified)
 * Original class created by Schine
 */

package org.schema.game.common.controller.elements.combination;

import javax.vecmath.Vector3f;
import api.mod.StarLoader;
import net.dovtech.starmadeplus.systems.weapons.plasmalauncher.PlasmaLauncherCollectionManager;
import org.schema.common.config.ConfigurationElement;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.ShootingRespose;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamCollectionManager;
import org.schema.game.common.controller.elements.combination.modifier.Modifier;
import org.schema.game.common.controller.elements.combination.modifier.MultiConfigModifier;
import org.schema.game.common.controller.elements.combination.modifier.WeaponUnitModifier;
import org.schema.game.common.controller.elements.missile.dumb.DumbMissileCollectionManager;
import org.schema.game.common.controller.elements.weapon.WeaponCollectionManager;
import org.schema.game.common.controller.elements.weapon.WeaponElementManager;
import org.schema.game.common.controller.elements.weapon.WeaponUnit;
import org.schema.game.common.data.element.ShootContainer;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.schine.graphicsengine.core.Timer;

public class WeaponCombinationAddOn
        extends
        CombinationAddOn<WeaponUnit, WeaponCollectionManager, WeaponElementManager, WeaponCombiSettings> {

    @ConfigurationElement(name = "cannon")
    private static final MultiConfigModifier<WeaponUnitModifier, WeaponUnit, WeaponCombiSettings> weaponCannonUnitModifier = new MultiConfigModifier<WeaponUnitModifier, WeaponUnit, WeaponCombiSettings>() {

        @Override
        public WeaponUnitModifier instance() {
            return new WeaponUnitModifier();
        }
    };
    //	@ConfigurationElement(name = "pulse")
//	private static final MultiConfigModifier<WeaponUnitModifier, WeaponUnit> weaponPulseUnitModifier = new MultiConfigModifier<WeaponUnitModifier, WeaponUnit>() {
//
//		@Override
//		public WeaponUnitModifier instance() {
//			return new WeaponUnitModifier();
//		}
//	};
    @ConfigurationElement(name = "beam")
    private static final MultiConfigModifier<WeaponUnitModifier, WeaponUnit, WeaponCombiSettings> weaponBeamUnitModifier = new MultiConfigModifier<WeaponUnitModifier, WeaponUnit, WeaponCombiSettings>() {

        @Override
        public WeaponUnitModifier instance() {
            return new WeaponUnitModifier();
        }
    };
    @ConfigurationElement(name = "missile")
    private static final MultiConfigModifier<WeaponUnitModifier, WeaponUnit, WeaponCombiSettings> weaponMissileUnitModifier = new MultiConfigModifier<WeaponUnitModifier, WeaponUnit, WeaponCombiSettings>() {

        @Override
        public WeaponUnitModifier instance() {
            return new WeaponUnitModifier();
        }
    };
    @ConfigurationElement(name = "plasma")
    private static final MultiConfigModifier<WeaponUnitModifier, WeaponUnit, WeaponCombiSettings> weaponPlasmaUnitModifier = new MultiConfigModifier<WeaponUnitModifier, WeaponUnit, WeaponCombiSettings>() {
        @Override
        public WeaponUnitModifier instance() {
            return new WeaponUnitModifier();
        }
    };

    public WeaponCombinationAddOn(WeaponElementManager elementManager, GameStateInterface gsinterface) {
        super(elementManager, gsinterface);
    }


    public ShootingRespose handle(WeaponUnitModifier mod, WeaponCollectionManager fireingCollection,
                                  WeaponUnit firingUnit, ControlBlockElementCollectionManager<?, ?, ?> combi,
                                  ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager,
                                  ShootContainer shootContainer,
                                  SimpleTransformableSendableObject lockOntarget) {

        mod.handle(firingUnit, combi, getRatio(fireingCollection, combi));



        final long weaponId = fireingCollection.getUsableId();
        firingUnit.setShotReloading((long) mod.outputReload);

        Vector3f dir = new Vector3f(shootContainer.shootingDirTemp);

        if(mod.outputAimable) {
            dir.set(shootContainer.shootingDirTemp);
        }else {
            dir.set(shootContainer.shootingDirStraightTemp);
        }

        dir.normalize();

        dir.scale(mod.outputSpeed);

        elementManager.getParticleController().addProjectile(
                elementManager.getSegmentController(), shootContainer.weapontOutputWorldPos,
                dir,
                mod.outputDamage,
                mod.outputDistance,
                mod.outputAcidType,
                mod.outputProjectileWidth,
                firingUnit.getPenetrationDepth(mod.outputDamage),
                mod.outputImpactForce,
                weaponId,
                firingUnit.getColor());

        fireingCollection.damageProduced += mod.outputDamage;

        fireingCollection.getElementManager().handleRecoil(fireingCollection, firingUnit, shootContainer.weapontOutputWorldPos, shootContainer.shootingDirTemp, mod.outputRecoil, mod.outputDamage);
        return ShootingRespose.FIRED;

    }

    @Override
    protected String getTag() {
        return "cannon";
    }

    /**
     * machine gun
     */
    @Override
    public ShootingRespose handleCannonCombi(
            WeaponCollectionManager fireingCollection,
            WeaponUnit firingUnit, WeaponCollectionManager combi,
            ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager,
            ShootContainer shootContainer,
            SimpleTransformableSendableObject lockOntarget, PlayerState playerState, Timer timer, float beamTimeout) {

        return handle(weaponCannonUnitModifier.get(combi), fireingCollection, firingUnit, combi, effectCollectionManager, shootContainer, lockOntarget);
    }

    /**
     * sniper
     */
    @Override
    public ShootingRespose handleBeamCombi(WeaponCollectionManager fireingCollection,
                                           WeaponUnit firingUnit, DamageBeamCollectionManager combi,
                                           ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager,
                                           ShootContainer shootContainer,
                                           SimpleTransformableSendableObject lockOntarget, PlayerState playerState, Timer timer, float beamTimeout) {

        return handle(weaponBeamUnitModifier.get(combi), fireingCollection, firingUnit, combi, effectCollectionManager, shootContainer, lockOntarget);
    }

    /**
     * shotgun
     */
    @Override
    public ShootingRespose handleMissileCombi(WeaponCollectionManager fireingCollection,
                                              WeaponUnit firingUnit, DumbMissileCollectionManager combi,
                                              ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager,
                                              ShootContainer shootContainer,
                                              SimpleTransformableSendableObject lockOntarget, PlayerState playerState, Timer timer, float beamTimeout) {

        return handle(weaponMissileUnitModifier.get(combi), fireingCollection, firingUnit, combi, effectCollectionManager, shootContainer, lockOntarget);
    }

    @Override
    public ShootingRespose handlePlasmaCombi(WeaponCollectionManager fireringCollection, WeaponUnit firingUnit, PlasmaLauncherCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager, ShootContainer shootContainer, SimpleTransformableSendableObject lockOntarget, PlayerState playerState, Timer timer, float beamTimeout) {
        return handle(weaponPlasmaUnitModifier.get(combi), fireringCollection, firingUnit, combi, effectCollectionManager, shootContainer, lockOntarget);
    }

    @Override
    public WeaponUnitModifier getGUI(
            WeaponCollectionManager fireringCollection,
            WeaponUnit firingUnit,
            WeaponCollectionManager combi,
            ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        WeaponUnitModifier mod = weaponCannonUnitModifier.get(combi);
        mod.handle(firingUnit, combi, getRatio(fireringCollection, combi));
        return mod;
    }

    @Override
    public WeaponUnitModifier getGUI(
            WeaponCollectionManager fireingCollection,
            WeaponUnit firingUnit,
            DamageBeamCollectionManager combi,
            ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        WeaponUnitModifier mod = weaponBeamUnitModifier.get(combi);
        mod.handle(firingUnit, combi, getRatio(fireingCollection, combi));
        return mod;
    }

    @Override
    public WeaponUnitModifier getGUI(
            WeaponCollectionManager fireringCollection,
            WeaponUnit firingUnit,
            DumbMissileCollectionManager combi,
            ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        WeaponUnitModifier mod = weaponMissileUnitModifier.get(combi);
        mod.handle(firingUnit, combi, getRatio(fireringCollection, combi));
        return mod;
    }

    @Override
    public Modifier<WeaponUnit, WeaponCombiSettings> getGUI(WeaponCollectionManager fireringCollection, WeaponUnit firingUnit, PlasmaLauncherCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        WeaponUnitModifier mod = weaponPlasmaUnitModifier.get(combi);
        mod.handle(firingUnit, combi, getRatio(fireringCollection, combi));
        return mod;
    }


    @Override
    public double calcCannonCombiPowerConsumption(double powerPerBlock, WeaponCollectionManager fireingCollection, WeaponUnit firingUnit,
                                                  WeaponCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        WeaponUnitModifier mod = weaponCannonUnitModifier.get(combi);
        return mod.calculatePowerConsumption(powerPerBlock, firingUnit, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public double calcBeamCombiPowerConsumption(double powerPerBlock, WeaponCollectionManager fireingCollection, WeaponUnit firingUnit,
                                                DamageBeamCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        WeaponUnitModifier mod = weaponBeamUnitModifier.get(combi);
        return mod.calculatePowerConsumption(powerPerBlock, firingUnit, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public double calcMissileCombiPowerConsumption(double powerPerBlock, WeaponCollectionManager fireingCollection, WeaponUnit firingUnit,
                                                   DumbMissileCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        WeaponUnitModifier mod = weaponMissileUnitModifier.get(combi);
        return mod.calculatePowerConsumption(powerPerBlock, firingUnit, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public double calcPlasmaCombiPowerConsumption(double powerPerBlock, WeaponCollectionManager fireingCollection, WeaponUnit firingUnit, PlasmaLauncherCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        return weaponPlasmaUnitModifier.get(combi).calculatePowerConsumption(powerPerBlock, firingUnit, combi, getRatio(fireingCollection, combi));
    }


    @Override
    public double calcCannonCombiReload(WeaponCollectionManager fireingCollection, WeaponUnit firingUnit,
                                        WeaponCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        WeaponUnitModifier mod = weaponCannonUnitModifier.get(combi);
        return mod.calculateReload(firingUnit, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public double calcBeamCombiReload(WeaponCollectionManager fireingCollection, WeaponUnit firingUnit,
                                      DamageBeamCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        WeaponUnitModifier mod = weaponBeamUnitModifier.get(combi);
        return mod.calculateReload(firingUnit, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public double calcMissileCombiReload(WeaponCollectionManager fireingCollection, WeaponUnit firingUnit,
                                         DumbMissileCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        WeaponUnitModifier mod = weaponMissileUnitModifier.get(combi);
        return mod.calculateReload(firingUnit, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public double calcPlasmaCombiReload(WeaponCollectionManager fireingCollection, WeaponUnit firingUnit, PlasmaLauncherCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        return weaponPlasmaUnitModifier.get(combi).calculateReload(firingUnit, combi, getRatio(fireingCollection, combi));
    }


    @Override
    public void calcCannonCombiSettings(WeaponCombiSettings out, WeaponCollectionManager fireingCollection,
                                        WeaponCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        WeaponUnitModifier mod = weaponCannonUnitModifier.get(combi);
        mod.calcCombiSettings(out, fireingCollection, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public void calcBeamCombiSettings(WeaponCombiSettings out, WeaponCollectionManager fireingCollection,
                                      DamageBeamCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        WeaponUnitModifier mod = weaponBeamUnitModifier.get(combi);
        mod.calcCombiSettings(out, fireingCollection, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public void calcMissileCombiPowerSettings(WeaponCombiSettings out, WeaponCollectionManager fireingCollection,
                                              DumbMissileCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        WeaponUnitModifier mod = weaponMissileUnitModifier.get(combi);
        mod.calcCombiSettings(out, fireingCollection, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public void calcPlasmaCombiPowerSettings(WeaponCombiSettings out, WeaponCollectionManager fireingCollection, PlasmaLauncherCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        weaponPlasmaUnitModifier.get(combi).calcCombiSettings(out, fireingCollection, combi, getRatio(fireingCollection, combi));
    }
}