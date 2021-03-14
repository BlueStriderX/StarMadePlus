package thederpgamer.starmadeplus.data.drawer;

import com.bulletphysics.linearmath.Transform;
import org.schema.game.common.data.element.ElementCollection;
import org.schema.game.common.data.element.ElementCollectionMesh;
import org.schema.game.common.data.element.ElementKeyMap;
import org.schema.game.common.data.world.Segment;
import thederpgamer.starmadeplus.data.element.BlockSegment;

public class MeshDrawData {

    private BlockSegment controller;
    private BlockSegment[] blocks;
    private ElementCollection<?, ?, ?> elementCollection;

    private ElementCollectionMesh mesh;
    private float meshScale;
    private Transform meshTransform;


    public MeshDrawData(BlockSegment controller, short connectionType, ElementCollection<?, ?, ?> elementCollection) {
        this.meshScale = calculateMeshScale(controller);
        this.meshTransform = calculateMeshTransform(controller);
        this.controller = controller;
        this.blocks = controller.getSlavedBlocks(connectionType);
        this.elementCollection = elementCollection;
        if((mesh = elementCollection.getMesh()) == null) this.mesh = ElementCollection.getMeshInstance();
    }

    public BlockSegment getController() {
        return controller;
    }

    public BlockSegment[] getBlocks() {
        return blocks;
    }

    public ElementCollection<?, ?, ?> getElementCollection() {
        return elementCollection;
    }

    public ElementCollectionMesh getMesh() {
        return mesh;
    }

    public float getMeshScale() {
        return meshScale;
    }

    public Transform getMeshTransform() {
        return meshTransform;
    }

    private float calculateMeshScale(BlockSegment controller) {
        BlockSegment[] activationModules = controller.getSlavedBlocks(ElementKeyMap.ACTIVAION_BLOCK_ID);
        float scale = Segment.HALF_DIM * 0.1f; //No idea if this will work
        if(activationModules != null && activationModules.length > 0) {
            for(BlockSegment activationModule : activationModules) {
                if(activationModule.isActivated()) scale += 0.1f;
            }
        }
        return scale;
    }

    private Transform calculateMeshTransform(BlockSegment controller) {
        Transform transform = new Transform();
        transform.setIdentity();
        transform.origin.set(controller.getPosition().toVector3f());
        return transform;
    }
}
