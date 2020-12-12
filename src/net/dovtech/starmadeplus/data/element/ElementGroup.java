package net.dovtech.starmadeplus.data.element;

import api.element.block.StarBlock;
import api.entity.StarEntity;
import it.unimi.dsi.fastutil.longs.LongIterator;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.PositionControl;
import org.schema.game.common.data.element.ElementKeyMap;
import java.util.ArrayList;
import java.util.Iterator;

public class ElementGroup {

    public enum Orientation {FRONT, BACK, TOP, BOTTOM, RIGHT, LEFT}

    private ArrayList<StarBlock> blocks;
    private short id = 0;
    private int orientationInt = 0;

    public ElementGroup(ArrayList<StarBlock> blockList) {
        this.id = blockList.get(0).getId();
        this.orientationInt = blockList.get(0).getInternalSegmentPiece().getFullOrientation();
        this.blocks = new ArrayList<>();
        for(StarBlock block : blockList) {
            if(block.getId() == id && block.getInternalSegmentPiece().getFullOrientation() == orientationInt) {
                blocks.add(block);
            }
        }
    }

    public void addBlock(StarBlock block) {
        blocks.add(block);
    }

    public void removeBlock(StarBlock block) {
        blocks.remove(block);
    }

    public StarBlock getBlockAbsolute(Vector3i absolutePos) {
        for(StarBlock block : blocks) {
            if(block.getLocation().equals(absolutePos)) return block;
        }
        return null;
    }

    public StarBlock getBlockRelative(Vector3i localPos) {
        Vector3i targetPos = new Vector3i();
        targetPos.set(getMin());
        targetPos.add(localPos);
        for(StarBlock block : blocks) {
            if(block.getLocation().equals(targetPos)) return block;
        }
        return null;
    }

    public Vector3i getMin() {
        int minX = 0;
        int minY = 0;
        int minZ = 0;
        Iterator<StarBlock> iterator = blocks.iterator();
        while(iterator.hasNext()) {
            Vector3i pos = iterator.next().getLocation();
            if(pos.x < minX) minX = pos.x;
            if(pos.y < minY) minY = pos.y;
            if(pos.z < minZ) minZ = pos.z;
        }
        return new Vector3i(minX, minY, minZ);
    }

    public Vector3i getMax() {
        int maxX = 0;
        int maxY = 0;
        int maxZ = 0;
        Iterator<StarBlock> iterator = blocks.iterator();
        while(iterator.hasNext()) {
            Vector3i pos = iterator.next().getLocation();
            if(pos.x > maxX) maxX = pos.x;
            if(pos.y > maxY) maxY = pos.y;
            if(pos.z > maxZ) maxZ = pos.z;
        }
        return new Vector3i(maxX, maxY, maxZ);
    }

    public int getSize() {
        return blocks.size();
    }

    public boolean isSolid() {
        Vector3i min = getMin();
        Vector3i max = getMax();

        for(int x = min.x; x < max.x; x ++) {
            for(int y = min.y; y < max.y; y ++) {
                for(int z = min.z; z < max.z; z ++) {
                    StarBlock currentBlock = getBlockAbsolute(new Vector3i(x, y, z));
                    if(currentBlock == null || currentBlock.getId() != id || currentBlock.getInternalSegmentPiece().getFullOrientation() != orientationInt) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public ArrayList<StarBlock> getConnections(short type) {
        ArrayList<StarBlock> connections = new ArrayList<>();
        for(StarBlock block : blocks) {
            for(short id : ElementKeyMap.getInfo(type).controlling) {
                PositionControl control = block.getInternalSegmentPiece().getSegmentController().getControlElementMap().getDirectControlledElements(id, block.getLocation());
                if(control != null && control.getControlMap() != null && control.getControlMap().size() > 0) {
                    LongIterator iterator = control.getControlMap().iterator();
                    while(iterator.hasNext()) {
                        connections.add(new StarBlock(block.getInternalSegmentPiece().getSegmentController().getSegmentBuffer().getPointUnsave(iterator.nextLong())));
                    }
                }
            }
        }
        return connections;
    }

    public Orientation getOrientation() {
        return Orientation.values()[orientationInt];
    }

    public StarEntity getEntity() {
        return blocks.get(0).getEntity();
    }
}
