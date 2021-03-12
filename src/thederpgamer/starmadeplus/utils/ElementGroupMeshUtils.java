package thederpgamer.starmadeplus.utils;

import api.common.GameCommon;
import api.mod.ModSkeleton;
import api.mod.config.PersistentObjectUtil;
import thederpgamer.starmadeplus.StarMadePlus;
import thederpgamer.starmadeplus.data.element.ElementGroup;
import thederpgamer.starmadeplus.data.mesh.MeshDrawData;
import java.util.HashMap;

public class ElementGroupMeshUtils {

    private static final ModSkeleton instance = StarMadePlus.getInstance().getSkeleton();
    public static final HashMap<ElementGroup, MeshDrawData> elementGroupMeshes = new HashMap<>();

    public static void updateMeshData(MeshDrawData drawData) {
        if (GameCommon.isDedicatedServer() || GameCommon.isOnSinglePlayer()) {
            for (Object object : PersistentObjectUtil.getObjects(instance, drawData.getClass())) {
                MeshDrawData meshData = (MeshDrawData) object;
                if (meshData.getMeshId().equals(drawData.getMeshId())) {
                    PersistentObjectUtil.removeObject(instance, meshData);
                    break;
                }
            }
            PersistentObjectUtil.addObject(instance, drawData);
            PersistentObjectUtil.save(instance);
        }
    }
}
