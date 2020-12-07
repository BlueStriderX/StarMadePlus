package net.dovtech.starmadeplus.data;

import api.element.block.StarBlock;
import org.schema.common.util.linAlg.Vector3i;
import java.util.ArrayList;
import java.util.Arrays;

public class ElementGroup {

    private ArrayList<StarBlock> blockList;
    private short id = 0;

    public ElementGroup(StarBlock... blocks) {
        this.blockList = new ArrayList<>();
        if(blocks != null && blocks.length > 0) {
            this.blockList.addAll(Arrays.asList(blocks));
            this.id = blockList.get(0).getId();
        }
    }

    public void addBlocks(StarBlock... blocks) {
        if(blocks != null && blocks.length > 0) {
            for(StarBlock block : blocks) {
                if(block.getId() == id) blockList.add(block);
            }
        }
    }

    public void removeBlocks(StarBlock... blocks) {
        if(blocks != null && blocks.length > 0) {
            for(StarBlock block : blocks) {
                if(block.getId() == id) blockList.remove(block);
            }
        }
    }

    public StarBlock getBlockAbsolute(Vector3i absolutePos) {
        for(StarBlock block : blockList) {
            if(block.getLocation().equals(absolutePos)) return block;
        }
        return null;
    }

    public StarBlock getBlockRelative(Vector3i localPos) {
        Vector3i targetPos = new Vector3i();
        targetPos.set(getMin());
        targetPos.add(localPos);
        for(StarBlock block : blockList) {
            if(block.getLocation().equals(targetPos)) return block;
        }
        return null;
    }

    public Vector3i getMin() {
        Vector3i min = blockList.get(0).getLocation();
        for(StarBlock block : blockList) {
            if(min.compareTo(block.getLocation()) < 0) {
                min = block.getLocation();
            }
        }
        return min;
    }

    public Vector3i getMax() {
        Vector3i max = blockList.get(0).getLocation();
        for(StarBlock block : blockList) {
            if(max.compareTo(block.getLocation()) > 0) {
                max = block.getLocation();
            }
        }
        return max;
    }

    public int getSize() {
        return blockList.size();
    }

    public boolean isSolid() {
        Vector3i min = getMin();
        Vector3i max = getMax();

        for(int x = min.x; x < max.x; x ++) {
            for(int y = min.y; y < max.y; y ++) {
                for(int z = min.z; z < max.z; z ++) {
                    StarBlock currentBlock = getBlockAbsolute(new Vector3i(x, y, z));
                    if(currentBlock == null || currentBlock.getId() != id) return false;
                }
            }
        }

        return true;
    }
}
