package thederpgamer.starmadeplus.utils;

import org.schema.common.util.linAlg.Vector3b;
import thederpgamer.starmadeplus.data.element.BlockSegment;
import thederpgamer.starmadeplus.data.element.ElementGroup;
import java.util.ArrayList;
import java.util.Arrays;

public class MultiblockUtils {

    public enum MultiblockType {SQUARE_FLAT}

    public static ElementGroup getElementGroup(BlockSegment block, MultiblockType type) {
        ElementGroup elementGroup = new ElementGroup((ArrayList<BlockSegment>) Arrays.asList(block.getMatchingAdjacent()));
        elementGroup.addBlock(block);
        if(elementGroup.isSolid()) {
            if(type.equals(MultiblockType.SQUARE_FLAT)) {
                Vector3b min = elementGroup.getMin();
                Vector3b max = elementGroup.getMax();
                if(min.x == max.x || min.y == max.y || min.z == max.z) {
                    return elementGroup;
                }
            }
        }
        return null;
    }
}
