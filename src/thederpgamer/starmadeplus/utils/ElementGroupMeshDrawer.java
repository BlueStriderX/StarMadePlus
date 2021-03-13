package thederpgamer.starmadeplus.utils;

import api.utils.draw.ModWorldDrawer;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementCollectionMesh;
import org.schema.schine.graphicsengine.core.Timer;
import thederpgamer.starmadeplus.data.mesh.MeshDrawData;
import javax.vecmath.Vector3f;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class ElementGroupMeshDrawer extends ModWorldDrawer {

    public HashMap<ElementCollection<?, ?, ?>, MeshDrawData> meshMap = new HashMap<>();

    public void addMesh(ElementCollection<?, ?, ?> elementCollection, MeshDrawData drawData) {
        if(!meshMap.containsValue(drawData)) meshMap.put(elementCollection, drawData);
    }

    private ArrayList<ElementCollectionMesh> getMeshList() {
        ArrayList<ElementCollectionMesh> meshList = new ArrayList<>();
        for(MeshDrawData drawData : meshMap.values()) {
            if(drawData.getMesh() != null) meshList.add(drawData.getMesh());
        }
        return meshList;
    }

    @Override
    public void update(Timer timer) {
        for(ElementCollection<?, ?, ?> elementCollection : meshMap.keySet()) {
            if(meshMap.get(elementCollection).getMesh() == null) {
                meshMap.get(elementCollection).setMesh(getDrawLocation(timer.currentTime, elementCollection, meshMap.get(elementCollection)));
            }
            ElementCollectionMesh mesh = meshMap.get(elementCollection).getMesh();
            if(mesh != null) {
                if(mesh.isVisibleFrustum(elementCollection.getSegmentController().getWorldTransformOnClient())) {
                    mesh.markDraw();
                    mesh.draw();
                } else {
                    mesh.clear();
                }
            }
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

    private ElementCollectionMesh getDrawLocation(long time, ElementCollection<?, ?, ?> elementCollection, MeshDrawData drawData) {
        try {
            elementCollection.calculateMesh(time, true);
            ElementCollectionMesh mesh = elementCollection.getMesh();

            Field minField = mesh.getClass().getDeclaredField("localMin");
            Field maxField = mesh.getClass().getDeclaredField("localMax");
            minField.setAccessible(true);
            maxField.setAccessible(true);

            Vector3f min = (Vector3f) minField.get(mesh);
            Vector3f max = (Vector3f) maxField.get(mesh);

            min.set(drawData.getDrawMin().toVector3f());
            max.set(drawData.getDrawMax().toVector3f());

            minField.set(mesh, min);
            maxField.set(mesh, max);

            return mesh;
        } catch(IllegalAccessException | NoSuchFieldException | NullPointerException e) {
            e.printStackTrace();
        }

        return ElementCollection.getMeshInstance();
    }
}
