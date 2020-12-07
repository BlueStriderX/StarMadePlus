package net.dovtech.starmadeplus.blocks;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockManager {

    public enum TextureType {ALL, MODEL, MULTIBLOCK, TOP_BOTTOM, TOP_ONLY, COMPUTER, FRONT, BACK, TOP, BOTTOM, RIGHT, LEFT}

    private static ArrayList<BlockElement> blocks = new ArrayList<>();
    public static HashMap<String, Integer> customFactories = new HashMap<>();

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
