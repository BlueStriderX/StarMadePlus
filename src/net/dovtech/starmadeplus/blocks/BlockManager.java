package net.dovtech.starmadeplus.blocks;

import java.util.ArrayList;

public class BlockManager {

    public enum BlockSide {ALL, TOP_BOTTOM, TOP_ONLY, FRONT, BACK, TOP, BOTTOM, LEFT, RIGHT}

    private static ArrayList<BlockElement> blocks = new ArrayList<>();

    public static BlockElement getFromID(short ID) {
        for(BlockElement block : blocks) {
            if(block.blockInfo.getId() == ID) return block;
        }
        return null;
    }

    public static BlockElement getFromName(String blockName) {
        for(BlockElement block : blocks) {
            if(block.blockInfo.getName().toLowerCase().equals(blockName.toLowerCase())) return block;
        }
        return null;
    }

    public static void addBlock(BlockElement block) {
        blocks.add(block);
    }
}
