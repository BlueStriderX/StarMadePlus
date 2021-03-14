package thederpgamer.starmadeplus.utils;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.schema.game.common.controller.elements.ManagerContainer;
import org.schema.game.common.controller.elements.ShieldAddOn;
import org.schema.game.common.controller.elements.ShieldContainerInterface;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamCollectionManager;
import org.schema.game.common.controller.elements.beam.damageBeam.DamageBeamElementManager;
import org.schema.game.common.controller.elements.missile.MissileCollectionManager;
import org.schema.game.common.controller.elements.missile.MissileElementManager;
import org.schema.game.common.controller.elements.weapon.WeaponCollectionManager;
import org.schema.game.common.controller.elements.weapon.WeaponElementManager;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementKeyMap;
import thederpgamer.starmadeplus.data.element.BlockSegment;

public class MultiblockUtils {

    public static ElementCollection<?, ?, ?> getElementCollection(ManagerContainer<?> manager, BlockSegment connection) {
        if(connection.getId() == ElementKeyMap.REACTOR_MAIN) {
            return manager.getMainReactor().getInstance();
        } else if(connection.getId() == ElementKeyMap.REACTOR_STABILIZER) {
            return manager.getStabilizer().getInstance();
        } else if(connection.getId() == ElementKeyMap.SHIELD_CAP_ID || connection.getId() == ElementKeyMap.SHIELD_REGEN_ID) {
            ShieldAddOn shieldAddon = ((ShieldContainerInterface) connection.getEntity()).getShieldAddOn();
            if(shieldAddon != null && shieldAddon.getShieldLocalAddOn() != null) {
                if(connection.getId() == ElementKeyMap.SHIELD_CAP_ID) {
                    return shieldAddon.sc.getShieldCapacityManager().getInstance();
                } else if(connection.getId() == ElementKeyMap.SHIELD_REGEN_ID) {
                    return shieldAddon.sc.getShieldRegenManager().getInstance();
                }
            }
        } else if(connection.getId() == ElementKeyMap.WEAPON_CONTROLLER_ID) {
            ObjectArrayList<?> weapons = manager.getWeapons();
            for(Object object : weapons) {
                if(object instanceof WeaponElementManager) {
                    WeaponElementManager elementManager = (WeaponElementManager) object;
                    for(WeaponCollectionManager collectionManager : elementManager.getCollectionManagers()) {
                        if(collectionManager.getControllerPos().equals(connection.getPosition())) return collectionManager.getInstance();
                    }
                }
            }
        } else if(connection.getId() == ElementKeyMap.DAMAGE_BEAM_COMPUTER) {
            ObjectArrayList<?> weapons = manager.getWeapons();
            for(Object object : weapons) {
                if(object instanceof DamageBeamElementManager) {
                    DamageBeamElementManager elementManager = (DamageBeamElementManager) object;
                    for(DamageBeamCollectionManager collectionManager : elementManager.getCollectionManagers()) {
                        if(collectionManager.getControllerPos().equals(connection.getPosition())) return collectionManager.getInstance();
                    }
                }
            }
        } else if(connection.getId() == ElementKeyMap.MISSILE_DUMB_CONTROLLER_ID) {
            ObjectArrayList<?> weapons = manager.getWeapons();
            for(Object object : weapons) {
                if(object instanceof MissileElementManager) {
                    MissileElementManager<?, ?, ?> elementManager = (MissileElementManager<?, ?, ?>) object;
                    for(Object cmObject : elementManager.getCollectionManagers()) {
                        if(cmObject instanceof MissileCollectionManager) {
                            MissileCollectionManager<?, ?, ?> collectionManager = (MissileCollectionManager<?, ?, ?>) cmObject;
                            if(collectionManager.getControllerPos().equals(connection.getPosition())) return collectionManager.getInstance();
                        }
                    }
                }
            }
        }
        return null;
    }
}
