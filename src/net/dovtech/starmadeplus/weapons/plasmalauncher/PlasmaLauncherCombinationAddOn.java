package net.dovtech.starmadeplus.weapons.plasmalauncher;

import org.schema.common.config.ConfigurationElement;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.ShootingRespose;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamCollectionManager;
import org.schema.game.common.controller.elements.combination.CombinationAddOn;
import org.schema.game.common.controller.elements.combination.modifier.Modifier;
import org.schema.game.common.controller.elements.combination.modifier.MultiConfigModifier;
import org.schema.game.common.controller.elements.missile.dumb.DumbMissileCollectionManager;
import org.schema.game.common.controller.elements.weapon.WeaponCollectionManager;
import org.schema.game.common.data.element.ShootContainer;
import org.schema.game.common.data.player.PlayerState;
import org.schema.game.common.data.world.SimpleTransformableSendableObject;
import org.schema.schine.graphicsengine.core.Timer;
import javax.vecmath.Vector3f;

public class PlasmaLauncherCombinationAddOn extends CombinationAddOn<PlasmaLauncherUnit, PlasmaLauncherCollectionManager, PlasmaLauncherElementManager, PlasmaLauncherCombinationSettings> {

    @ConfigurationElement(name = "cannon")
    private static final MultiConfigModifier<PlasmaLauncherUnitModifier, PlasmaLauncherUnit, PlasmaLauncherCombinationSettings> plasmaCannonUnitModifier = new MultiConfigModifier<PlasmaLauncherUnitModifier, PlasmaLauncherUnit, PlasmaLauncherCombinationSettings>() {
        @Override
        public PlasmaLauncherUnitModifier instance() {
            return new PlasmaLauncherUnitModifier();
        }
    };

    @ConfigurationElement(name = "beam")
    private static final MultiConfigModifier<PlasmaLauncherUnitModifier, PlasmaLauncherUnit, PlasmaLauncherCombinationSettings> plasmaBeamUnitModifier = new MultiConfigModifier<PlasmaLauncherUnitModifier, PlasmaLauncherUnit, PlasmaLauncherCombinationSettings>() {
        @Override
        public PlasmaLauncherUnitModifier instance() {
            return new PlasmaLauncherUnitModifier();
        }
    };

    @ConfigurationElement(name = "missile")
    private static final MultiConfigModifier<PlasmaLauncherUnitModifier, PlasmaLauncherUnit, PlasmaLauncherCombinationSettings> plasmaMissileUnitModifier = new MultiConfigModifier<PlasmaLauncherUnitModifier, PlasmaLauncherUnit, PlasmaLauncherCombinationSettings>() {
        @Override
        public PlasmaLauncherUnitModifier instance() {
            return new PlasmaLauncherUnitModifier();
        }
    };

    @ConfigurationElement(name = "plasma")
    private static final MultiConfigModifier<PlasmaLauncherUnitModifier, PlasmaLauncherUnit, PlasmaLauncherCombinationSettings> plasmaPlasmaUnitModifier = new MultiConfigModifier<PlasmaLauncherUnitModifier, PlasmaLauncherUnit, PlasmaLauncherCombinationSettings>() {
        @Override
        public PlasmaLauncherUnitModifier instance() {
            return new PlasmaLauncherUnitModifier();
        }
    };

    public PlasmaLauncherCombinationAddOn(PlasmaLauncherElementManager elementManager, GameStateInterface stateInterface) {
        super(elementManager, stateInterface);
    }

    public ShootingRespose handle(PlasmaLauncherUnitModifier mod, PlasmaLauncherCollectionManager collection, PlasmaLauncherUnit unit, ControlBlockElementCollectionManager<?, ?, ?> combination, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager, ShootContainer shootContainer, SimpleTransformableSendableObject lockOntarget) {
        mod.handle(unit, combination, getRatio(collection, combination));

        //final long weaponId = collection.getUsableId();
        unit.setShotReloading((long) mod.outputReload);

        Vector3f dir = new Vector3f(shootContainer.shootingDirTemp);

        dir.normalize();
        dir.scale(mod.outputSpeed);

        collection.damageProduced += mod.outputDamage;

        collection.getElementManager().handleRecoil(collection, unit, shootContainer.weapontOutputWorldPos, shootContainer.shootingDirTemp, mod.outputRecoil, mod.outputDamage);
        return ShootingRespose.FIRED;

    }

    @Override
    protected String getTag() {
        return "plasma";
    }

    @Override
    public double calcCannonCombiPowerConsumption(double powerPerBlock, PlasmaLauncherCollectionManager fireingCollection, PlasmaLauncherUnit firingUnit, WeaponCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        return plasmaCannonUnitModifier.get(combi).calculatePowerConsumption(powerPerBlock, firingUnit, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public double calcBeamCombiPowerConsumption(double powerPerBlock, PlasmaLauncherCollectionManager fireingCollection, PlasmaLauncherUnit firingUnit, DamageBeamCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        return plasmaBeamUnitModifier.get(combi).calculatePowerConsumption(powerPerBlock, firingUnit, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public double calcMissileCombiPowerConsumption(double powerPerBlock, PlasmaLauncherCollectionManager fireingCollection, PlasmaLauncherUnit firingUnit, DumbMissileCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        return plasmaMissileUnitModifier.get(combi).calculatePowerConsumption(powerPerBlock, firingUnit, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public double calcPlasmaCombiPowerConsumption(double powerPerBlock, PlasmaLauncherCollectionManager fireingCollection, PlasmaLauncherUnit firingUnit, PlasmaLauncherCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        return plasmaPlasmaUnitModifier.get(combi).calculatePowerConsumption(powerPerBlock, firingUnit, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public double calcCannonCombiReload(PlasmaLauncherCollectionManager fireingCollection, PlasmaLauncherUnit firingUnit, WeaponCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        return plasmaCannonUnitModifier.get(combi).calculateReload(firingUnit, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public double calcBeamCombiReload(PlasmaLauncherCollectionManager fireingCollection, PlasmaLauncherUnit firingUnit, DamageBeamCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        return plasmaBeamUnitModifier.get(combi).calculateReload(firingUnit, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public double calcMissileCombiReload(PlasmaLauncherCollectionManager fireingCollection, PlasmaLauncherUnit firingUnit, DumbMissileCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        return plasmaMissileUnitModifier.get(combi).calculateReload(firingUnit, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public double calcPlasmaCombiReload(PlasmaLauncherCollectionManager fireingCollection, PlasmaLauncherUnit firingUnit, PlasmaLauncherCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        return plasmaPlasmaUnitModifier.get(combi).calculateReload(firingUnit, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public void calcCannonCombiSettings(PlasmaLauncherCombinationSettings out, PlasmaLauncherCollectionManager fireingCollection, WeaponCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        plasmaCannonUnitModifier.get(combi).calcCombiSettings(out, fireingCollection, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public void calcBeamCombiSettings(PlasmaLauncherCombinationSettings out, PlasmaLauncherCollectionManager fireingCollection, DamageBeamCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        plasmaBeamUnitModifier.get(combi).calcCombiSettings(out, fireingCollection, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public void calcMissileCombiPowerSettings(PlasmaLauncherCombinationSettings out, PlasmaLauncherCollectionManager fireingCollection, DumbMissileCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        plasmaMissileUnitModifier.get(combi).calcCombiSettings(out, fireingCollection, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public void calcPlasmaCombiPowerSettings(PlasmaLauncherCombinationSettings out, PlasmaLauncherCollectionManager fireingCollection, PlasmaLauncherCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        plasmaPlasmaUnitModifier.get(combi).calcCombiSettings(out, fireingCollection, combi, getRatio(fireingCollection, combi));
    }

    @Override
    public ShootingRespose handleCannonCombi(PlasmaLauncherCollectionManager fireringCollection, PlasmaLauncherUnit firingUnit, WeaponCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager, ShootContainer shootContainer, SimpleTransformableSendableObject lockOntarget, PlayerState playerState, Timer timer, float beamTimeout) {
        return handle(plasmaCannonUnitModifier.get(combi), fireringCollection, firingUnit, combi, effectCollectionManager, shootContainer, lockOntarget);
    }

    @Override
    public ShootingRespose handleBeamCombi(PlasmaLauncherCollectionManager fireringCollection, PlasmaLauncherUnit firingUnit, DamageBeamCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager, ShootContainer shootContainer, SimpleTransformableSendableObject lockOntarget, PlayerState playerState, Timer timer, float beamTimeout) {
        return handle(plasmaBeamUnitModifier.get(combi), fireringCollection, firingUnit, combi, effectCollectionManager, shootContainer, lockOntarget);
    }

    @Override
    public ShootingRespose handleMissileCombi(PlasmaLauncherCollectionManager fireringCollection, PlasmaLauncherUnit firingUnit, DumbMissileCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager, ShootContainer shootContainer, SimpleTransformableSendableObject lockOntarget, PlayerState playerState, Timer timer, float beamTimeout) {
        return handle(plasmaMissileUnitModifier.get(combi), fireringCollection, firingUnit, combi, effectCollectionManager, shootContainer, lockOntarget);
    }

    @Override
    public ShootingRespose handlePlasmaCombi(PlasmaLauncherCollectionManager fireringCollection, PlasmaLauncherUnit firingUnit, PlasmaLauncherCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager, ShootContainer shootContainer, SimpleTransformableSendableObject lockOntarget, PlayerState playerState, Timer timer, float beamTimeout) {
        return handle(plasmaPlasmaUnitModifier.get(combi), fireringCollection, firingUnit, combi, effectCollectionManager, shootContainer, lockOntarget);
    }

    @Override
    public Modifier<PlasmaLauncherUnit, PlasmaLauncherCombinationSettings> getGUI(PlasmaLauncherCollectionManager fireringCollection, PlasmaLauncherUnit firingUnit, WeaponCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        PlasmaLauncherUnitModifier mod = plasmaCannonUnitModifier.get(combi);
        mod.handle(firingUnit, combi, getRatio(fireringCollection, combi));
        return mod;
    }

    @Override
    public Modifier<PlasmaLauncherUnit, PlasmaLauncherCombinationSettings> getGUI(PlasmaLauncherCollectionManager fireringCollection, PlasmaLauncherUnit firingUnit, DamageBeamCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        PlasmaLauncherUnitModifier mod = plasmaBeamUnitModifier.get(combi);
        mod.handle(firingUnit, combi, getRatio(fireringCollection, combi));
        return mod;
    }

    @Override
    public Modifier<PlasmaLauncherUnit, PlasmaLauncherCombinationSettings> getGUI(PlasmaLauncherCollectionManager fireringCollection, PlasmaLauncherUnit firingUnit, DumbMissileCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        PlasmaLauncherUnitModifier mod = plasmaMissileUnitModifier.get(combi);
        mod.handle(firingUnit, combi, getRatio(fireringCollection, combi));
        return mod;
    }

    @Override
    public Modifier<PlasmaLauncherUnit, PlasmaLauncherCombinationSettings> getGUI(PlasmaLauncherCollectionManager fireringCollection, PlasmaLauncherUnit firingUnit, PlasmaLauncherCollectionManager combi, ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager) {
        PlasmaLauncherUnitModifier mod = plasmaPlasmaUnitModifier.get(combi);
        mod.handle(firingUnit, combi, getRatio(fireringCollection, combi));
        return mod;
    }
}