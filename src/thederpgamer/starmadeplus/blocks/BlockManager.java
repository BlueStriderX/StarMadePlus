package thederpgamer.starmadeplus.blocks;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockManager {

    public enum TextureType {ALL, MODEL, MULTIBLOCK, TOP_BOTTOM, TOP_ONLY, COMPUTER, MODULE, FRONT, BACK, TOP, BOTTOM, RIGHT, LEFT}

    private static ArrayList<BlockElement> blocks = new ArrayList<>();
    public static HashMap<String, Integer> factories = new HashMap<>();

    public static BlockElement getFromID(short ID) {
        for(BlockElement blockElement : blocks) {
            if(blockElement.blockInfo.getId() == ID) return blockElement;
        }
        return null;
    }

    public static BlockElement getFromName(String blockName) {
        for(BlockElement blockElement : blocks) {
            if(blockElement.blockInfo.getName().toLowerCase().equals(blockName.toLowerCase())) return blockElement;
        }
        return null;
    }

    public static void addBlock(BlockElement blockElement) {
        blocks.add(blockElement);
    }
}
