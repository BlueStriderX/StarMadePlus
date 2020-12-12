package net.dovtech.starmadeplus.utils;

import api.element.block.StarBlock;
import net.dovtech.starmadeplus.data.element.ElementGroup;
import org.schema.common.util.linAlg.Vector3i;
import java.util.ArrayList;
import java.util.Arrays;

public class MultiblockUtils {

    private enum BlockSide {FRONT, BACK, TOP, BOTTOM, RIGHT, LEFT}
    public enum MultiblockType {SQUARE_FLAT}

    public static ElementGroup getElementGroup(StarBlock block, MultiblockType type) {
        ElementGroup elementGroup = new ElementGroup(getMatchingAdjacent(block));
        elementGroup.addBlock(block);
        if(elementGroup.isSolid()) {
            if(type.equals(MultiblockType.SQUARE_FLAT)) {
                Vector3i min = elementGroup.getMin();
                Vector3i max = elementGroup.getMax();
                if(min.x == max.x || min.y == max.y || min.z == max.z) {
                    return elementGroup;
                }
            }
        }
        return null;
    }

    public static ArrayList<StarBlock> getMatchingAdjacent(StarBlock block) {
        ArrayList<StarBlock> blocks = new ArrayList<>();
        StarBlock[] adjacent = block.getAdjacentBlocks();
        if(adjacent.length == 0) {
            blocks.addAll(Arrays.asList(adjacent));
        } else {
            for(StarBlock b : adjacent) {
                blocks.addAll(getMatchingAdjacent(b));
            }
        }
        return blocks;
    }
}
