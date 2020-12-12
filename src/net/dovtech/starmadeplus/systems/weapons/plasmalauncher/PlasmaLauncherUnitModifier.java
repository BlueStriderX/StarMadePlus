package net.dovtech.starmadeplus.systems.weapons.plasmalauncher;

import org.schema.common.config.ConfigurationElement;
import org.schema.game.client.data.GameStateInterface;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.combination.modifier.Modifier;
import org.schema.game.common.controller.elements.combination.modifier.tagMod.BasicModifier;
import org.schema.game.common.controller.elements.combination.modifier.tagMod.formula.SetFomula;

public class PlasmaLauncherUnitModifier extends Modifier<PlasmaLauncherUnit, PlasmaLauncherCombinationSettings> {

    @ConfigurationElement(name = "Damage")
    public BasicModifier damageModifier;

    @ConfigurationElement(name = "Reload")
    public BasicModifier reloadModifier;

    @ConfigurationElement(name = "PowerConsumption")
    public BasicModifier powerModifier;

    @ConfigurationElement(name = "Distance")
    public BasicModifier distanceModifier;

    @ConfigurationElement(name = "Speed")
    public BasicModifier speedModifier;

    @ConfigurationElement(name = "Recoil")
    public BasicModifier recoilModifier;

    @ConfigurationElement(name = "ImpactForce")
    public BasicModifier impactForceModifier;

    @ConfigurationElement(name = "ChargeMax")
    public BasicModifier chargeMaxModifier;

    @ConfigurationElement(name = "ChargeSpeed")
    public BasicModifier chargeSpeedModifier;

    @ConfigurationElement(name = "CursorRecoilX")
    public BasicModifier cursorRecoilXModifier;

    @ConfigurationElement(name = "CursorRecoilMinX")
    public BasicModifier cursorRecoilMinXModifier;

    @ConfigurationElement(name = "CursorRecoilMaxX")
    public BasicModifier cursorRecoilMaxXModifier;

    @ConfigurationElement(name = "CursorRecoilDirX")
    public BasicModifier cursorRecoilDirXModifier;

    @ConfigurationElement(name = "CursorRecoilY")
    public BasicModifier cursorRecoilYModifier;

    @ConfigurationElement(name = "CursorRecoilMinY")
    public BasicModifier cursorRecoilMinYModifier;

    @ConfigurationElement(name = "CursorRecoilMaxY")
    public BasicModifier cursorRecoilMaxYModifier;

    @ConfigurationElement(name = "CursorRecoilDirY")
    public BasicModifier cursorRecoilDirYModifier;


    public float outputDamage;
    public float outputDistance;
    public float outputSpeed;
    public float outputReload;
    public float outputPowerConsumption;
    public float outputImpactForce;
    public float outputRecoil;

    public PlasmaLauncherUnitModifier() {

    }

    @Override
    public void handle(PlasmaLauncherUnit input, ControlBlockElementCollectionManager<?, ?, ?> combination, float ratio) {
        outputDamage = damageModifier.getOuput(input.getBaseDamage(), input.size(), input.getCombiBonus(combination.getTotalSize()), input.getEffectBonus(), ratio);

        outputReload = reloadModifier.getOuput(input.getReloadTimeMs(), input.size(), input.getCombiBonus(combination.getTotalSize()), input.getEffectBonus(), ratio);

        float d = distanceModifier.getOuput(input.getDistance(), input.size(), input.getCombiBonus(combination.getTotalSize()), input.getEffectBonus(), ratio);
        outputDistance = distanceModifier.formulas instanceof SetFomula ? d * ((GameStateInterface) input.getSegmentController().getState()).getGameState().getWeaponRangeReference() : d;

        outputSpeed = speedModifier.getOuput(input.getSpeed(), input.size(), input.getCombiBonus(combination.getTotalSize()), input.getEffectBonus(), ratio);

        outputImpactForce = (int) impactForceModifier.getOuput(input.getImpactForce(), input.size(), input.getCombiBonus(combination.getTotalSize()), input.getEffectBonus(), ratio);

        outputPowerConsumption = powerModifier.getOuput(input.getBasePowerConsumption() * input.getExtraConsume(), input.size(), input.getCombiBonus(combination.getTotalSize()), input.getEffectBonus(), ratio);

        outputRecoil = recoilModifier.getOuput(input.getRecoil(), input.size(), input.getCombiBonus(combination.getTotalSize()), input.getEffectBonus(), ratio);
    }

    public double calculateReload(PlasmaLauncherUnit input, ControlBlockElementCollectionManager<?, ?, ?>  combination, float ratio) {
        return reloadModifier.getOuput(input.getReloadTimeMs(), input.size(), input.getCombiBonus(combination.getTotalSize()), input.getEffectBonus(), ratio);
    }

    public double calculatePowerConsumption(double powerPerBlock, PlasmaLauncherUnit input, ControlBlockElementCollectionManager<?, ?, ?>  combination, float ratio) {
        return powerModifier.getOuput((float) (powerPerBlock * input.getExtraConsume()), input.size(), input.getCombiBonus(combination.getTotalSize()), input.getEffectBonus(), ratio);
    }

    @Override
    public void calcCombiSettings(PlasmaLauncherCombinationSettings out, ControlBlockElementCollectionManager<?, ?, ?> collection, ControlBlockElementCollectionManager<?, ?, ?> combination, float ratio) {
        out.damageChargeMax = chargeMaxModifier.getOuput(((PlasmaLauncherCollectionManager) collection).getDamageChargeMax(), collection.getTotalSize(), combination.getTotalSize(), 0, ratio);
        out.damageChargeSpeed = chargeSpeedModifier.getOuput(((PlasmaLauncherCollectionManager) collection).getDamageChargeSpeed(), collection.getTotalSize(), combination.getTotalSize(), 0, ratio);

        out.cursorRecoilX = cursorRecoilXModifier.getOuput(((PlasmaLauncherCollectionManager) collection).getCursorRecoilX(), collection.getTotalSize(), combination.getTotalSize(), 0, ratio);
        out.cursorRecoilMinX = cursorRecoilMinXModifier.getOuput(((PlasmaLauncherCollectionManager) collection).getCursorRecoilMinX(), collection.getTotalSize(), combination.getTotalSize(), 0, ratio);
        out.cursorRecoilMaxX= cursorRecoilMaxXModifier.getOuput(((PlasmaLauncherCollectionManager) collection).getCursorRecoilMaxX(), collection.getTotalSize(), combination.getTotalSize(), 0, ratio);
        out.cursorRecoilDirX= cursorRecoilDirXModifier.getOuput(((PlasmaLauncherCollectionManager) collection).getCursorRecoilDirX(), collection.getTotalSize(), combination.getTotalSize(), 0, ratio);

        out.cursorRecoilY = cursorRecoilYModifier.getOuput(((PlasmaLauncherCollectionManager) collection).getCursorRecoilY(), collection.getTotalSize(), combination.getTotalSize(), 0, ratio);
        out.cursorRecoilMinY = cursorRecoilMinYModifier.getOuput(((PlasmaLauncherCollectionManager) collection).getCursorRecoilMinY(), collection.getTotalSize(), combination.getTotalSize(), 0, ratio);
        out.cursorRecoilMaxY= cursorRecoilMaxYModifier.getOuput(((PlasmaLauncherCollectionManager) collection).getCursorRecoilMaxY(), collection.getTotalSize(), combination.getTotalSize(), 0, ratio);
        out.cursorRecoilDirY= cursorRecoilDirYModifier.getOuput(((PlasmaLauncherCollectionManager) collection).getCursorRecoilDirY(), collection.getTotalSize(), combination.getTotalSize(), 0, ratio);
    }
}
