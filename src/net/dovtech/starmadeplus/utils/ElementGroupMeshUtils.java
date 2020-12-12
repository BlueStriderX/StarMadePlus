package net.dovtech.starmadeplus.utils;

import api.common.GameCommon;
import api.mod.config.PersistentObjectUtil;
import net.dovtech.starmadeplus.StarMadePlus;
import net.dovtech.starmadeplus.data.element.ElementGroup;
import net.dovtech.starmadeplus.data.mesh.MeshDrawData;
import java.util.HashMap;

public class ElementGroupMeshUtils {

    private static final StarMadePlus instance = StarMadePlus.getInstance();
    public static final HashMap<ElementGroup, MeshDrawData> elementGroupMeshes = new HashMap<>();

    public static void updateMeshData(MeshDrawData drawData) {
        if (GameCommon.isDedicatedServer() || GameCommon.isOnSinglePlayer()) {
            for (Object object : PersistentObjectUtil.getObjects(instance, drawData.getClass())) {
                MeshDrawData meshData = (MeshDrawData) object;
                if (drawData.getMeshId().equals(drawData.getMeshId())) {
                    PersistentObjectUtil.removeObject(instance, meshData);
                    break;
                }
            }
            PersistentObjectUtil.addObject(instance, drawData);
            PersistentObjectUtil.save(instance);
        }
    }
}
