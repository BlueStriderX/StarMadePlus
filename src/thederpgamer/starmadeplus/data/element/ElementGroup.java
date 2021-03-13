package thederpgamer.starmadeplus.data.element;

import it.unimi.dsi.fastutil.longs.LongIterator;
import org.schema.common.util.linAlg.Vector3b;
import org.schema.game.common.controller.PositionControl;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.element.ElementKeyMap;
import java.util.ArrayList;
import java.util.Iterator;

public class ElementGroup {

    public enum Orientation {FRONT, BACK, TOP, BOTTOM, RIGHT, LEFT}

    private ArrayList<BlockSegment> blocks;
    private short id;
    private int orientationInt;

    public ElementGroup(short id, ArrayList<BlockSegment> blockList) {
        this.id = id;
        this.orientationInt = blockList.get(0).getOrientation();
        this.blocks = new ArrayList<>();
        for(BlockSegment block : blockList) {
            if(block.getId() == id && block.getOrientation() == orientationInt) {
                blocks.add(block);
            }
        }
    }

    public void addBlock(BlockSegment block) {
        blocks.add(block);
    }

    public void removeBlock(BlockSegment block) {
        blocks.remove(block);
    }

    public BlockSegment getBlockAbsolute(Vector3b absolutePos) {
        for(BlockSegment block : blocks) {
            if(block.getPosition().equals(absolutePos)) return block;
        }
        return null;
    }

    public BlockSegment getBlockRelative(Vector3b localPos) {
        Vector3b targetPos = new Vector3b();
        targetPos.set(getMin());
        targetPos.add(localPos);
        for(BlockSegment block : blocks) {
            if(block.getPosition().equals(targetPos)) return block;
        }
        return null;
    }

    public Vector3b getMin() {
        int minX = 0;
        int minY = 0;
        int minZ = 0;
        Iterator<BlockSegment> iterator = blocks.iterator();
        while(iterator.hasNext()) {
            Vector3b pos = iterator.next().getPosition();
            if(pos.x < minX) minX = pos.x;
            if(pos.y < minY) minY = pos.y;
            if(pos.z < minZ) minZ = pos.z;
        }
        return new Vector3b(minX, minY, minZ);
    }

    public Vector3b getMax() {
        int maxX = 0;
        int maxY = 0;
        int maxZ = 0;
        Iterator<BlockSegment> iterator = blocks.iterator();
        while(iterator.hasNext()) {
            Vector3b pos = iterator.next().getPosition();
            if(pos.x > maxX) maxX = pos.x;
            if(pos.y > maxY) maxY = pos.y;
            if(pos.z > maxZ) maxZ = pos.z;
        }
        return new Vector3b(maxX, maxY, maxZ);
    }

    public int getSize() {
        return blocks.size();
    }

    public boolean isSolid() {
        Vector3b min = getMin();
        Vector3b max = getMax();

        for(int x = min.x; x < max.x; x ++) {
            for(int y = min.y; y < max.y; y ++) {
                for(int z = min.z; z < max.z; z ++) {
                    BlockSegment currentBlock = getBlockAbsolute(new Vector3b(x, y, z));
                    if(currentBlock == null || currentBlock.getId() != id || currentBlock.getOrientation() != orientationInt) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public ArrayList<BlockSegment> getConnections(BlockSegment blockSegment) {
        ArrayList<BlockSegment> connections = new ArrayList<>();
        for(BlockSegment block : blocks) {
            for(short id : ElementKeyMap.getInfo(blockSegment.getId()).controlling) {
                PositionControl control = block.getEntity().getControlElementMap().getControlledElements(id, block.getPositionInt());
                if(control != null && control.getControlMap() != null && control.getControlMap().size() > 0) {
                    LongIterator iterator = control.getControlMap().iterator();
                    while(iterator.hasNext()) {
                        connections.add(BlockSegment.fromSegmentPiece(block.getEntity().getSegmentBuffer().getPointUnsave(iterator.nextLong())));
                    }
                }
            }
        }
        return connections;
    }

    public Orientation getOrientation() {
        if(orientationInt >= Orientation.values().length) {
            return Orientation.FRONT;
        } else {
            return Orientation.values()[orientationInt];
        }
    }

    public SegmentController getEntity() {
        return blocks.get(0).getEntity();
    }
}
