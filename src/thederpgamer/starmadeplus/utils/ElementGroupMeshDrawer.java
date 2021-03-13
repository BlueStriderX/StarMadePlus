package thederpgamer.starmadeplus.utils;

import api.common.GameClient;
import api.utils.draw.ModWorldDrawer;
import com.bulletphysics.linearmath.Transform;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementCollectionMesh;
import org.schema.schine.graphicsengine.core.Timer;
import thederpgamer.starmadeplus.data.mesh.MeshDrawData;
import javax.vecmath.Vector3f;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class ElementGroupMeshDrawer extends ModWorldDrawer {

    public HashMap<ElementCollection, HashMap<MeshDrawData, ElementCollectionMesh>> meshMap = new HashMap<>();

    public void addMesh(MeshDrawData drawData, ElementCollection elementCollection) {
        HashMap<MeshDrawData, ElementCollectionMesh> meshes;
        if(meshMap.containsKey(elementCollection)) {
            meshes = meshMap.get(elementCollection);
        } else {
            meshes = new HashMap<>();
        }
        meshes.put(drawData, getDrawLocation(drawData, elementCollection));
        meshMap.put(elementCollection, meshes);
    }

    private ArrayList<ElementCollectionMesh> getMeshList() {
        ArrayList<ElementCollectionMesh> meshList = new ArrayList<>();
        for(HashMap<MeshDrawData, ElementCollectionMesh> meshes : meshMap.values()) {
            meshList.addAll(meshes.values());
        }
        return meshList;
    }

    @Override
    public void update(Timer timer) {
        for(ElementCollectionMesh mesh : getMeshList()) {
            if(mesh.isVisibleFrustum(getClientTransform())) mesh.draw();
        }
    }

    @Override
    public void cleanUp() {
        for(ElementCollectionMesh mesh : getMeshList()) mesh.clear();
    }

    @Override
    public boolean isInvisible() {
        return false;
    }

    @Override
    public void onInit() {

    }

    private ElementCollectionMesh getDrawLocation(MeshDrawData drawData, ElementCollection elementCollection) {
        ElementCollectionMesh mesh = (elementCollection.hasMesh()) ? elementCollection.getMesh() : new ElementCollectionMesh();
        try {
            Field minField = mesh.getClass().getDeclaredField("min");
            Field maxField = mesh.getClass().getDeclaredField("max");
            minField.setAccessible(true);
            maxField.setAccessible(true);

            Vector3f min = (Vector3f) minField.get(mesh);
            Vector3f max = (Vector3f) maxField.get(mesh);

            min.set(drawData.getDrawMin().toVector3f());
            max.set(drawData.getDrawMax().toVector3f());

            minField.set(mesh, min);
            maxField.set(mesh, max);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return mesh;
    }

    private Transform getClientTransform() {
        Transform clientTransform = new Transform();
        GameClient.getClientPlayerState().getWordTransform(clientTransform);
        return clientTransform;
    }
}
