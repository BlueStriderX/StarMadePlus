package net.dovtech.starmadeplus.utils;

import api.element.block.StarBlock;
import api.entity.StarEntity;
import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.Element;
import java.util.ArrayList;
import java.util.HashMap;

public class MultiblockUtils {

    private enum BlockSide {FRONT, BACK, TOP, BOTTOM, RIGHT, LEFT}
    public enum MultiblockType {SQUARE_FLAT}


    public static int checkMultiblock(StarEntity entity, StarBlock block, MultiblockType type) {
        Vector3i absolutePos = block.getLocation();
        Vector3i tempPos = new Vector3i();
        short id = block.getId();
        int size = 1;

        if(type.equals(MultiblockType.SQUARE_FLAT)) {

        }
    }

    public static ArrayList<StarBlock> getAllMatching(StarBlock block) {
        ArrayList<StarBlock> blocks = new ArrayList<>();
        LongOpenHashSet hashSet = block.getInternalSegmentPiece().getSegmentController().getControlElementMap().getAllControlledElements(block.getId());
        if(hashSet != null && hashSet.size() > 0) {
            LongIterator iterator = hashSet.iterator();
            while(iterator.hasNext()) {
                SegmentPiece segmentPiece = block.getInternalSegmentPiece().getSegmentController().getSegmentBuffer().getPointUnsave(iterator.nextLong());
                blocks.add(new StarBlock(segmentPiece));
            }
        }
        return blocks;
    }

    public static

    private static HashMap<BlockSide, StarBlock> getAdjacent(StarBlock block) {
        HashMap<BlockSide, StarBlock> adjacent = new HashMap<>();
        Vector3i absPos = block.getLocation();
        Vector3i tempPos = new Vector3i();
        for(int i = 0; i < 6; ++ i) {
            tempPos.set(absPos);
            tempPos.add(Element.DIRECTIONSi[i]);
            SegmentPiece segmentPiece = block.getInternalSegmentPiece().getSegmentController().getSegmentBuffer().getPointUnsave(tempPos);
            if(segmentPiece == null) {
                adjacent.put(BlockSide.values()[i], null);
            } else {
                adjacent.put(BlockSide.values()[i], new StarBlock(segmentPiece));
            }
        }

        return adjacent;
    }
}
