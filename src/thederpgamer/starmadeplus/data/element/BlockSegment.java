package thederpgamer.starmadeplus.data.element;

import api.common.GameCommon;
import api.listener.events.block.*;
import org.schema.common.util.linAlg.Vector3b;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.Element;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * BlockElement.java
 * <Description>
 *
 * @author TheDerpGamer
 * @since 03/12/2021
 */
public class BlockSegment implements Serializable {

    private short id;
    private byte[] position;
    private byte orientation;
    private int entityId;

    public BlockSegment(short id, Vector3b position, byte orientation, SegmentController entity) {
        this.id = id;
        this.orientation = orientation;
        this.position = new byte[] {position.x, position.y, position.z};
        this.entityId = entity.getId();
    }

    public short getId() {
        return id;
    }

    public Vector3b getPosition() {
        return new Vector3b(position[0], position[1], position[2]);
    }

    public Vector3i getPositionInt() {
        return new Vector3i(position[0], position[1], position[2]);
    }

    public byte getOrientation() {
        return orientation;
    }

    public SegmentController getEntity() {
        return (SegmentController) GameCommon.getGameObject(entityId);
    }

    public ElementInformation getInfo() {
        return ElementKeyMap.getInfo(id);
    }

    public BlockSegment[] getAdjacentBlocks() {
        ArrayList<BlockSegment> elementList = new ArrayList<>();
        SegmentController entity = getEntity();
        for(Vector3i direction : Element.DIRECTIONSi) {
            Vector3i adjacentPos = getPositionInt();
            adjacentPos.add(direction);
            if(entity.getSegmentBuffer().getPointUnsave(adjacentPos) != null) {
                elementList.add(BlockSegment.fromSegmentPiece(entity.getSegmentBuffer().getPointUnsave(adjacentPos)));
            }
        }
        return elementList.toArray(new BlockSegment[6]);
    }

    public BlockSegment[] getMatchingAdjacent() {
        ArrayList<BlockSegment> blocks = new ArrayList<>();
        BlockSegment[] adjacent = getAdjacentBlocks();
        if(adjacent.length > 0) {
            for(BlockSegment b : adjacent) {
                if(b.getId() == getId()) blocks.addAll(Arrays.asList(getMatchingAdjacent()));
            }
        }

        BlockSegment[] matchingAdjacent = new BlockSegment[blocks.size()];
        for(int i = 0; i < matchingAdjacent.length; i ++) matchingAdjacent[i] = blocks.get(i);
        return matchingAdjacent;
    }

    public SegmentPiece toSegmentPiece() {
        return getEntity().getSegmentBuffer().getPointUnsave(getPositionInt());
    }

    public static BlockSegment fromSegmentPiece(SegmentPiece segmentPiece) {
        return new BlockSegment(segmentPiece.getType(), new Vector3b(segmentPiece.x, segmentPiece.y, segmentPiece.z), segmentPiece.getOrientation(), segmentPiece.getSegmentController());
    }

    public static BlockSegment fromEvent(SegmentPieceAddEvent event) {
        return new BlockSegment(event.getNewType(), new Vector3b(event.getX(), event.getY(), event.getZ()), event.getOrientation(), event.getSegmentController());
    }

    public static BlockSegment fromEvent(SegmentPieceRemoveEvent event) {
        return new BlockSegment(event.getType(), new Vector3b(event.getX(), event.getY(), event.getZ()), event.getOrientation(), event.getSegment().getSegmentController());
    }

    public static BlockSegment fromEvent(SegmentPieceActivateByPlayer event) {
        return new BlockSegment(event.getSegmentPiece().getType(), new Vector3b(event.getSegmentPiece().x, event.getSegmentPiece().y, event.getSegmentPiece().z), event.getSegmentPiece().getOrientation(), event.getSegmentPiece().getSegmentController());
    }

    public static BlockSegment fromEvent(SegmentPieceActivateEvent event) {
        return new BlockSegment(event.getSegmentPiece().getType(), new Vector3b(event.getSegmentPiece().x, event.getSegmentPiece().y, event.getSegmentPiece().z), event.getSegmentPiece().getOrientation(), event.getSegmentPiece().getSegmentController());
    }

    public static BlockSegment fromEvent(SegmentPieceDamageEvent event) {
        return new BlockSegment(event.getHitBlock().getType(), new Vector3b(event.getHitBlock().x, event.getHitBlock().y, event.getHitBlock().z), event.getHitBlock().getOrientation(), event.getHitBlock().getSegmentController());
    }

    public static BlockSegment fromEvent(SegmentPieceModifyEvent event) {
        return new BlockSegment(event.getVar6().getType(), new Vector3b(event.getVar6().x, event.getVar6().y, event.getVar6().z), event.getVar6().getOrientation(), event.getVar6().getSegmentController());
    }

    public static BlockSegment fromEvent(SegmentPieceKillEvent event) {
        return new BlockSegment(event.getPiece().getType(), new Vector3b(event.getPiece().x, event.getPiece().y, event.getPiece().z), event.getPiece().getOrientation(), event.getPiece().getSegmentController());
    }
}
