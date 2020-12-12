package net.dovtech.starmadeplus.systems.weapons.plasmalauncher;

import api.utils.particle.ModParticle;
import api.utils.particle.ModParticleFactory;
import api.utils.particle.ModParticleUtil;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;
import it.unimi.dsi.fastutil.shorts.ShortOpenHashSet;
import net.dovtech.starmadeplus.blocks.BlockManager;
import net.dovtech.starmadeplus.data.particles.ParticleManager;
import net.dovtech.starmadeplus.data.particles.PlasmaBallParticle;
import org.schema.common.config.ConfigurationElement;
import org.schema.common.util.StringTools;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.camera.InShipCamera;
import org.schema.game.client.view.gui.structurecontrol.ControllerManagerGUI;
import org.schema.game.client.view.gui.structurecontrol.ModuleValueEntry;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.Ship;
import org.schema.game.common.controller.damage.effects.InterEffectSet;
import org.schema.game.common.controller.elements.*;
import org.schema.game.common.controller.elements.combination.CombinationAddOn;
import org.schema.game.common.controller.elements.config.FloatReactorDualConfigElement;
import org.schema.game.common.controller.elements.effectblock.EffectElementManager;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ShootContainer;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.game.common.data.player.PlayerState;
import org.schema.schine.common.language.Lng;
import org.schema.schine.graphicsengine.core.Controller;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.graphicsengine.forms.font.FontLibrary;
import org.schema.schine.graphicsengine.forms.gui.GUITextOverlay;
import org.schema.schine.input.InputState;
import org.schema.schine.network.objects.NetworkObject;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.io.IOException;

public class PlasmaLauncherElementManager extends UsableCombinableControllableElementManager<PlasmaLauncherUnit, PlasmaLauncherCollectionManager, PlasmaLauncherElementManager, PlasmaLauncherCombinationSettings> implements NTSenderInterface, NTReceiveInterface, BlockActivationListenerInterface, WeaponElementManagerInterface {

    private final PlasmaLauncherCombinationSettings combinationSettings = new PlasmaLauncherCombinationSettings();
    private final WeaponStatisticsData weaponStatisticsData = new WeaponStatisticsData();
    private final DrawReloadListener drawReloadListener = new DrawReloadListener();
    private final ShootContainer shootContainer = new ShootContainer();
    private PlasmaLauncherCombinationAddOn addOn;
    private static GUITextOverlay chargesText;
    public static final Vector4f chargeColor = new Vector4f(0.8F, 0.5F, 0.3F, 0.4F);

    @ConfigurationElement(name = "Damage")
    public static FloatReactorDualConfigElement BASE_DAMAGE = new FloatReactorDualConfigElement();

    @ConfigurationElement(name = "Distance")
    public static float BASE_DISTANCE = 500;

    @ConfigurationElement(name = "Speed")
    public static float BASE_SPEED = 7.5f;

    @ConfigurationElement(name = "ReloadMs")
    public static float BASE_RELOAD = 1200;

    @ConfigurationElement(name = "ImpactForce")
    public static float IMPACT_FORCE = 0.05f;

    @ConfigurationElement(name = "Recoil")
    public static float RECOIL = 0.2f;

    @ConfigurationElement(name = "CursorRecoilX")
    public static float CURSOR_RECOIL_X = 0.0001f;

    @ConfigurationElement(name = "CursorRecoilMinX")
    public static float CURSOR_RECOIL_MIN_X = 0.001f;

    @ConfigurationElement(name = "CursorRecoilMaxX")
    public static float CURSOR_RECOIL_MAX_X = 0.1f;

    @ConfigurationElement(name = "CursorRecoilDirX")
    public static float CURSOR_RECOIL_DIR_X = 0.0f;

    @ConfigurationElement(name = "CursorRecoilY")
    public static float CURSOR_RECOIL_Y = 0.0001f;

    @ConfigurationElement(name = "CursorRecoilMinY")
    public static float CURSOR_RECOIL_MIN_Y = 0.001f;

    @ConfigurationElement(name = "CursorRecoilMaxY")
    public static float CURSOR_RECOIL_MAX_Y = 0.1f;

    @ConfigurationElement(name = "CursorRecoilDirY")
    public static float CURSOR_RECOIL_DIR_Y = 0.0f;

    @ConfigurationElement(name = "CursorRecoilSpeedIn")
    public static float CURSOR_RECOIL_IN = 1.0f;

    @ConfigurationElement(name = "CursorRecoilSpeedInAddMod")
    public static float CURSOR_RECOIL_IN_ADD = 1.0f;

    @ConfigurationElement(name = "CursorRecoilSpeedInPowMult")
    public static float CURSOR_RECOIL_IN_POW_MULT = 1.0f;

    @ConfigurationElement(name = "CursorRecoilSpeedOut")
    public static float CURSOR_RECOIL_OUT = 5.0f;

    @ConfigurationElement(name = "CursorRecoilSpeedOutAddMod")
    public static float CURSOR_RECOIL_OUT_ADD = 1.0f;

    @ConfigurationElement(name = "CursorRecoilSpeedOutPowMult")
    public static float CURSOR_RECOIL_OUT_POW_MULT = 1.0f;

    @ConfigurationElement(name = "PowerConsumption")
    public static float BASE_POWER_CONSUMPTION = 10;

    @ConfigurationElement(name = "ReactorPowerConsumptionResting")
    public static float REACTOR_POWER_CONSUMPTION_RESTING = 10;

    @ConfigurationElement(name = "ReactorPowerConsumptionCharging")
    public static float REACTOR_POWER_CONSUMPTION_CHARGING = 10;

    @ConfigurationElement(name = "AdditionalPowerConsumptionPerUnitMult")
    public static float ADDITIONAL_POWER_CONSUMPTION_PER_UNIT_MULT = 0.1f;

    @ConfigurationElement(name = "EffectConfiguration")
    public static InterEffectSet basicEffectConfiguration = new InterEffectSet();

    @ConfigurationElement(name = "DamageChargeMax")
    public static float DAMAGE_CHARGE_MAX = 0.1f;

    @ConfigurationElement(name = "DamageChargeSpeed")
    public static float DAMAGE_CHARGE_SPEED = 0.035f;

    @ConfigurationElement(name = "DamageChargeMaxSize")
    public static float DAMAGE_CHARGE_MAX_SIZE = 7.5f;

    @ConfigurationElement(name = "PlasmaBallMaxLifetime")
    public static int PLASMA_BALL_MAX_LIFETIME = 750;

    public PlasmaLauncherElementManager(SegmentController segmentController) {
        super(BlockManager.getFromName("Plasma Launcher Computer").getId(), BlockManager.getFromName("Plasma Launcher Barrel").getId(), segmentController);
    }

    public void handleRecoil(PlasmaLauncherCollectionManager collection, PlasmaLauncherUnit unit, Vector3f outputPos, Vector3f shootingDir, float outputRecoil, float damage) {
        if (getSegmentController().railController.getRoot().getPhysicsDataContainer().getObject() instanceof RigidBody) {
            if (outputRecoil * damage != 0) {
                Vector3f dir = new Vector3f(shootingDir);
                dir.negate();
                boolean negateTorque = true;
                getSegmentController().railController.getRoot().hitWithPhysicalRecoil(outputPos, dir, outputRecoil * damage, negateTorque);
            }
        }
    }

    public void handleCursorRecoil(PlasmaLauncherCollectionManager fireingCollection, float damage, PlasmaLauncherCombinationSettings settings) {
        if (getSegmentController().isClientOwnObject()) {
            if (Controller.getCamera() instanceof InShipCamera) {
                ((InShipCamera) Controller.getCamera()).addRecoil(Math.min(settings.cursorRecoilMaxX, Math.max(settings.cursorRecoilMinX, damage * settings.cursorRecoilX)), Math.min(settings.cursorRecoilMaxY, Math.max(settings.cursorRecoilMinY, damage * settings.cursorRecoilY)), settings.cursorRecoilDirX, settings.cursorRecoilDirY, CURSOR_RECOIL_IN, CURSOR_RECOIL_IN_ADD, CURSOR_RECOIL_IN_POW_MULT, CURSOR_RECOIL_OUT, CURSOR_RECOIL_OUT_ADD, CURSOR_RECOIL_OUT_POW_MULT);
            }
        }
    }

    @Override
    public int onActivate(SegmentPiece segmentPiece, boolean oldActive, boolean active) {
        long absPos = segmentPiece.getAbsoluteIndex();
        for (int i = 0; i < getCollectionManagers().size(); i++) {
            for (PlasmaLauncherUnit unit : getCollectionManagers().get(i).getElementCollections()) {
                if (unit.contains(absPos)) {
                    unit.setMainPiece(segmentPiece, active);
                    return active ? 1 : 0;
                }
            }
        }
        return active ? 1 : 0;
    }

    @Override
    public void updateActivationTypes(ShortOpenHashSet types) {
        types.add(BlockManager.getFromName("Plasma Launcher Barrel").getId());
    }

    @Override
    public void updateFromNT(NetworkObject networkObject) {

    }

    @Override
    public void updateToFullNT(NetworkObject networkObject) {

    }

    @Override
    public PlasmaLauncherCombinationSettings getCombiSettings() {
        return combinationSettings;
    }

    @Override
    public ControllerManagerGUI getGUIUnitValues(PlasmaLauncherUnit firingUnit, PlasmaLauncherCollectionManager collection, ControlBlockElementCollectionManager<?, ?, ?> supportCol, ControlBlockElementCollectionManager<?, ?, ?> effectCollection) {
        getStatistics(firingUnit, collection, supportCol, effectCollection, weaponStatisticsData);
        return ControllerManagerGUI.create((GameClientState) getState(), Lng.str("Plasma Launcher Unit"), firingUnit,
                new ModuleValueEntry(Lng.str("Total Damage/WeaponProjectile"), StringTools.formatPointZero(weaponStatisticsData.damage / weaponStatisticsData.split)),
                new ModuleValueEntry(Lng.str("WeaponProjectile Speed"), StringTools.formatPointZero(weaponStatisticsData.speed)),
                new ModuleValueEntry(Lng.str("Range"), StringTools.formatPointZero(weaponStatisticsData.distance)),
                new ModuleValueEntry(Lng.str("Shots"), StringTools.formatPointZero(weaponStatisticsData.split)),
                new ModuleValueEntry(Lng.str("Reload(ms)"), StringTools.formatPointZero(weaponStatisticsData.reload)),
                new ModuleValueEntry(Lng.str("PowerConsumptionResting"), firingUnit.getPowerConsumedPerSecondResting()),
                new ModuleValueEntry(Lng.str("PowerConsumptionCharging"), firingUnit.getPowerConsumedPerSecondCharging()),
                new ModuleValueEntry(Lng.str("Effect Ratio(%):"), StringTools.formatPointZero(weaponStatisticsData.effectRatio)));
    }

    @Override
    protected String getTag() {
        return "plasma";
    }

    @Override
    public PlasmaLauncherCollectionManager getNewCollectionManager(SegmentPiece segmentPiece, Class<PlasmaLauncherCollectionManager> clazz) {
        return new PlasmaLauncherCollectionManager(segmentPiece, getSegmentController(), this);
    }

    @Override
    public String getManagerName() {
        return "Plasma Launcher System Collective";
    }

    @Override
    protected void playSound(PlasmaLauncherUnit plasmaLauncherUnit, Transform transform) {

    }

    @Override
    public void handle(ControllerStateInterface unit, Timer timer) {
        if (!unit.isFlightControllerActive()) {
            return;
        }
        if (getCollectionManagers().isEmpty()) {
            return;
        }
        try {
            if (!convertDeligateControls(unit, shootContainer.controlledFromOrig, shootContainer.controlledFrom)) {
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        int unpowered = 0;
        getPowerManager().sendNoPowerHitEffectIfNeeded();
        for (int i = 0; i < getCollectionManagers().size(); i++) {
            PlasmaLauncherCollectionManager collection = getCollectionManagers().get(i);
            boolean selected = unit.isSelected(collection.getControllerElement(), shootContainer.controlledFrom);
            boolean aiSelected = unit.isAISelected(collection.getControllerElement(), shootContainer.controlledFrom, i, getCollectionManagers().size(), collection);
            if (selected && aiSelected) {
                boolean controlling = shootContainer.controlledFromOrig.equals(shootContainer.controlledFrom);
                controlling |= getControlElementMap().isControlling(shootContainer.controlledFromOrig, collection.getControllerPos(), controllerId);
                if (controlling) {
                    if (!collection.allowedOnServerLimit()) {
                        continue;
                    }
                    if (shootContainer.controlledFromOrig.equals(Ship.core)) {
                        unit.getControlledFrom(shootContainer.controlledFromOrig);
                    }
                    collection.handleControlShot(unit, timer);
                }
            }
        }
        if (unpowered > 0 && clientIsOwnShip()) {
            ((GameClientState) getState()).getController().popupInfoTextMessage(Lng.str("WARNING!\n \nWeapon Elements unpowered: %s", unpowered), 0);
        }
        if (getCollectionManagers().isEmpty() && clientIsOwnShip()) {
            ((GameClientState) getState()).getController().popupInfoTextMessage(Lng.str("WARNING!\n \nNo weapon controllers."), 0);
        }
    }

    public void doShot(final PlasmaLauncherUnit unit, PlasmaLauncherCollectionManager collection, final ShootContainer shootContainer, PlayerState playerState, Timer timer) {
        ManagerModuleCollection<?, ?, ?> effectModuleCollection = null;
        collection.setEffectTotal(0);
        if (collection.getEffectConnectedElement() != Long.MIN_VALUE) {
            short connectedType = 0;
            connectedType = (short) ElementCollection.getType(collection.getEffectConnectedElement());
            effectModuleCollection = getManagerContainer().getModulesControllerMap().get(connectedType);
            ControlBlockElementCollectionManager<?, ?, ?> effect = CombinationAddOn.getEffect(collection.getEffectConnectedElement(), effectModuleCollection, getSegmentController());
            if (effect != null) {
                collection.setEffectTotal(effect.getTotalSize());
            }
        }
        if (collection.getSlaveConnectedElement() != Long.MIN_VALUE) {
            short connectedType = 0;
            connectedType = (short) ElementCollection.getType(collection.getSlaveConnectedElement());
            ManagerModuleCollection<?, ?, ?> managerModuleCollection = getManagerContainer().getModulesControllerMap().get(connectedType);
            ShootingRespose handled = handleAddOn(this, collection, unit, managerModuleCollection, effectModuleCollection, shootContainer, null, playerState, timer, -1);
            handleResponse(handled, unit, shootContainer.weapontOutputWorldPos);
        } else {
            boolean canUse = unit.canUse(timer.currentTime, false);
            if (canUse) {
                final long weaponId = collection.getUsableId();
                if (isUsingPowerReactors() || consumePower(unit.getPowerConsumption())) {
                    unit.setStandardShotReloading();

                    handleRecoil(collection, unit, shootContainer.weapontOutputWorldPos, shootContainer.shootingDirTemp, unit.getRecoil(), unit.getDamage());
                    collection.damageProduced += unit.getDamage();

                    ModParticleUtil.playClient(shootContainer.weapontOutputWorldPos, ParticleManager.PLASMA_BALL.getSprite(), 1, PLASMA_BALL_MAX_LIFETIME, shootContainer.shootingDirTemp, new ModParticleFactory() {
                        @Override
                        public ModParticle newParticle() {
                            return new PlasmaBallParticle(shootContainer.shootingDirTemp, unit);
                        }
                    });
                    //Todo:Spawn WeaponProjectile


                    handleResponse(ShootingRespose.FIRED, unit, shootContainer.weapontOutputWorldPos);
                } else {
                    handleResponse(ShootingRespose.NO_POWER, unit, shootContainer.weapontOutputWorldPos);
                }
            } else {
                handleResponse(ShootingRespose.RELOADING, unit, shootContainer.weapontOutputWorldPos);
            }
        }
    }

    @Override
    public double calculateWeaponDamageIndex() {
        double dps = 0;
        for (PlasmaLauncherCollectionManager collection : getCollectionManagers()) {
            ControlBlockElementCollectionManager<?, ?, ?> supportCollectionManager = collection.getSupportCollectionManager();
            ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager = collection.getEffectCollectionManager();
            for (PlasmaLauncherUnit unit : collection.getElementCollections()) {
                getStatistics(unit, collection, supportCollectionManager, effectCollectionManager, weaponStatisticsData);
                dps += weaponStatisticsData.damage / (weaponStatisticsData.reload / 1000d);
            }
        }
        return dps;
    }

    @Override
    public double calculateWeaponRangeIndex() {
        double range = 0;
        double c = 0;
        for (PlasmaLauncherCollectionManager collection : getCollectionManagers()) {
            ControlBlockElementCollectionManager<?, ?, ?> supportCollectionManager = collection.getSupportCollectionManager();
            ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager = collection.getEffectCollectionManager();
            for (PlasmaLauncherUnit unit : collection.getElementCollections()) {
                getStatistics(unit, collection, supportCollectionManager, effectCollectionManager, weaponStatisticsData);
                range += weaponStatisticsData.distance;
                c++;
            }
        }
        return c > 0 ? range / c : 0;
    }

    @Override
    public double calculateWeaponHitPropabilityIndex() {
        double range = 0;
        for (PlasmaLauncherCollectionManager collection : getCollectionManagers()) {
            ControlBlockElementCollectionManager<?, ?, ?> supportCollectionManager = collection.getSupportCollectionManager();
            ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager = collection.getEffectCollectionManager();
            for (PlasmaLauncherUnit unit : collection.getElementCollections()) {
                getStatistics(unit, collection, supportCollectionManager, effectCollectionManager, weaponStatisticsData);
                range += (PlasmaLauncherElementManager.BASE_SPEED * weaponStatisticsData.split) / (weaponStatisticsData.reload / 1000d);
            }
        }
        return range;
    }

    @Override
    public double calculateWeaponSpecialIndex() {
        double special = 0;
        for (PlasmaLauncherCollectionManager collection : getCollectionManagers()) {
            ControlBlockElementCollectionManager<?, ?, ?> supportCollectionManager = collection.getSupportCollectionManager();
            ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager = collection.getEffectCollectionManager();
            for (PlasmaLauncherUnit unit : collection.getElementCollections()) {
                getStatistics(unit, collection, supportCollectionManager, effectCollectionManager, weaponStatisticsData);
            }
        }
        return special;
    }

    @Override
    public double calculateWeaponPowerConsumptionPerSecondIndex() {
        double powerConsumption = 0;
        for (PlasmaLauncherCollectionManager collection : getCollectionManagers()) {
            ControlBlockElementCollectionManager<?, ?, ?> supportCollectionManager = collection.getSupportCollectionManager();
            ControlBlockElementCollectionManager<?, ?, ?> effectCollectionManager = collection.getEffectCollectionManager();
            for (PlasmaLauncherUnit unit : collection.getElementCollections()) {
                getStatistics(unit, collection, supportCollectionManager, effectCollectionManager, weaponStatisticsData);
                powerConsumption += unit.getPowerConsumption();
            }
        }
        return powerConsumption;
    }

    @Override
    public CombinationAddOn<PlasmaLauncherUnit, PlasmaLauncherCollectionManager, ? extends PlasmaLauncherElementManager, PlasmaLauncherCombinationSettings> getAddOn() {
        return addOn;
    }

    public WeaponStatisticsData getStatistics(PlasmaLauncherUnit firingUnit, PlasmaLauncherCollectionManager collection, ControlBlockElementCollectionManager<?, ?, ?> supportCollection, ControlBlockElementCollectionManager<?, ?, ?> effectCollection, WeaponStatisticsData output) {
        if (effectCollection != null) {
            collection.setEffectTotal(effectCollection.getTotalSize());
        } else {
            collection.setEffectTotal(0);
        }
        output.damage = firingUnit.getDamage();
        output.speed = firingUnit.getSpeed();
        output.distance = firingUnit.getDistance();
        output.reload = firingUnit.getReloadTimeMs();
        output.powerConsumption = firingUnit.getPowerConsumption();
        output.split = 1;
        output.mode = 1;
        output.effectRatio = 0;
        if (supportCollection != null) {
            PlasmaLauncherUnitModifier gui = (PlasmaLauncherUnitModifier) getAddOn().getGUI(collection, firingUnit, supportCollection, effectCollection);
            output.damage = gui.outputDamage;
            output.speed = gui.outputSpeed;
            output.distance = gui.outputDistance;
            output.reload = gui.outputReload;
            output.powerConsumption = gui.outputPowerConsumption;
        }
        if (effectCollection != null) {
            EffectElementManager<?, ?, ?> effect = (EffectElementManager<?, ?, ?>) effectCollection.getElementManager();
            output.effectRatio = CombinationAddOn.getRatio(collection, effectCollection);
        }
        return output;
    }

    public class DrawReloadListener implements ReloadListener {

        @Override
        public String onDischarged(InputState state, Vector3i iconPos, Vector3i iconSize, Vector4f reloadColor, boolean backwards, float percent) {
            drawReload(state, iconPos, iconSize, reloadColor, backwards, percent);
            return null;
        }

        @Override
        public String onReload(InputState state, Vector3i iconPos, Vector3i iconSize, Vector4f reloadColor, boolean backwards, float percent) {
            drawReload(state, iconPos, iconSize, reloadColor, backwards, percent);
            return null;
        }

        @Override
        public String onFull(InputState state, Vector3i iconPos, Vector3i iconSize, Vector4f reloadColor, boolean backwards, float percent, long controllerPos) {
            return null;
        }

        @Override
        public void drawForElementCollectionManager(InputState state, Vector3i iconPos, Vector3i iconSize, Vector4f reloadcolor, long controllerPos) {
            PlasmaLauncherCollectionManager collection = getCollectionManagersMap().get(controllerPos);
            if (collection != null) {
                PlasmaLauncherCombinationSettings combinationSettings = collection.getChargeParams();
                if (combinationSettings.damageChargeMax > 0 && collection.damageCharge > 0) {
                    if (chargesText == null) {
                        chargesText = new GUITextOverlay(10, 10, FontLibrary.FontSize.MEDIUM, (InputState) getState());
                        chargesText.onInit();
                    }
                    float p = Math.min(collection.damageCharge / combinationSettings.damageChargeMax, 0.99999f);
                    drawReload(state, iconPos, iconSize, chargeColor, false, p, true, collection.damageCharge, (int) combinationSettings.damageChargeMax, -1, chargesText);
                }
            }
        }
    }

    @Override
    public void drawReloads(Vector3i iconPos, Vector3i iconSize, long controllerPos) {
        handleReload(iconPos, iconSize, controllerPos, drawReloadListener);
    }
}
