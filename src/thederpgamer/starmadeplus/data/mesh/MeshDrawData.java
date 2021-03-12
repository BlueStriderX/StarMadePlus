package thederpgamer.starmadeplus.data.mesh;

import org.schema.common.util.linAlg.Vector3b;
import thederpgamer.starmadeplus.data.element.ElementGroup;
import org.schema.common.util.linAlg.Vector3i;
import java.io.Serializable;
import java.util.UUID;

public class MeshDrawData implements Serializable {

    private String meshId;
    private int[] drawMin;
    private int[] drawMax;

    public MeshDrawData(ElementGroup elementGroup) {
        meshId = UUID.randomUUID().toString();

        Vector3b min = elementGroup.getMin();
        Vector3b max = elementGroup.getMax();
        Vector3i displayOffset = new Vector3i();
        switch (elementGroup.getOrientation()) {
            case FRONT:
                displayOffset.add(0, 0, 1);
                break;
            case BACK:
                displayOffset.add(0, 0, -1);
                break;
            case TOP:
                displayOffset.add(0, 1, 0);
                break;
            case BOTTOM:
                displayOffset.add(0, -1, 0);
                break;
            case RIGHT:
                displayOffset.add(1, 0, 0);
                break;
            case LEFT:
                displayOffset.add(-1, 0, 0);
                break;
        }
        drawMin = new int[] {
                min.x + displayOffset.x,
                min.y + displayOffset.y,
                min.z + displayOffset.z
        };

        drawMax = new int[] {
                max.x + displayOffset.x,
                max.y + displayOffset.y,
                max.z + displayOffset.z
        };
    }

    public String getMeshId() {
        return meshId;
    }

    public Vector3i getDrawMin() {
        return new Vector3i(drawMin[0], drawMin[1], drawMin[2]);
    }

    public Vector3i getDrawMax() {
        return new Vector3i(drawMax[0], drawMax[1], drawMax[2]);
    }
}
