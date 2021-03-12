package thederpgamer.starmadeplus.data.server;

import api.mod.ModSkeleton;
import api.mod.config.PersistentObjectUtil;
import thederpgamer.starmadeplus.StarMadePlus;
import thederpgamer.starmadeplus.data.player.PlayerData;
import java.util.ArrayList;

/**
 * ServerDatabase.java
 * <Description>
 *
 * @author TheDerpGamer
 * @since 03/12/2021
 */
public class ServerDatabase {

    private static final ModSkeleton instance = StarMadePlus.getInstance().getSkeleton();

    public static PlayerData getPlayerData(String playerName) {
        ArrayList<Object> playerDataObjectList = PersistentObjectUtil.getObjects(instance, PlayerData.class);
        for(Object playerDataObject : playerDataObjectList) {
            PlayerData playerData = (PlayerData) playerDataObject;
            if(playerData.playerName.equals(playerName)) return playerData;
        }
        return addNewPlayerData(playerName);
    }

    public static boolean playerExists(String playerName) {
        ArrayList<Object> playerDataObjectList = PersistentObjectUtil.getObjects(instance, PlayerData.class);
        for(Object playerDataObject : playerDataObjectList) {
            PlayerData playerData = (PlayerData) playerDataObject;
            if(playerData.playerName.equals(playerName)) return true;
        }
        return false;
    }

    public static PlayerData addNewPlayerData(String playerName) {
        ArrayList<Object> playerDataObjectList = PersistentObjectUtil.getObjects(instance, PlayerData.class);
        ArrayList<PlayerData> toRemove = new ArrayList<>();
        for(Object playerDataObject : playerDataObjectList) {
            PlayerData pData = (PlayerData) playerDataObject;
            if(pData.playerName.equals(playerName)) toRemove.add(pData);
        }

        for(PlayerData pData : toRemove) PersistentObjectUtil.removeObject(instance, pData);

        PlayerData playerData = new PlayerData(playerName);
        PersistentObjectUtil.addObject(instance, playerData);
        PersistentObjectUtil.save(instance);
        return playerData;
    }

    public static void updatePlayerData(PlayerData playerData) {
        ArrayList<Object> playerDataObjectList = PersistentObjectUtil.getObjects(instance, PlayerData.class);
        ArrayList<PlayerData> toRemove = new ArrayList<>();
        for(Object playerDataObject : playerDataObjectList) {
            PlayerData pData = (PlayerData) playerDataObject;
            if(pData.playerName.equals(playerData.playerName)) toRemove.add(pData);
        }

        for(PlayerData pData : toRemove) PersistentObjectUtil.removeObject(instance, pData);
        PersistentObjectUtil.addObject(instance, playerData);
        PersistentObjectUtil.save(instance);
    }
}
