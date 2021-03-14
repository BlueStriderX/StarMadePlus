package thederpgamer.starmadeplus.data.element;

import api.common.GameCommon;
import api.listener.events.block.*;
import it.unimi.dsi.fastutil.longs.LongIterator;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.PositionControl;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.element.Element;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.ElementKeyMap;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * BlockElement.java
 * <Description>
 *
 * @author TheDerpGamer
 * @since 03/12/2021
 */
public class BlockSegment {

    private short id;
    private Vector3i position;
    private byte orientation;
    private int entityId;

    public BlockSegment(short id, Vector3i position, byte orientation, SegmentController entity) {
        this.id = id;
        this.orientation = orientation;
        this.position = position;
        this.entityId = entity.getId();
    }

    public short getId() {
        return id;
    }

    public Vector3i getPosition() {
        return position;
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

    public boolean isActivated() {
        return getInfo().canActivate && toSegmentPiece().isActive();
    }

    public BlockSegment[] getSlavedBlocks() {
        ArrayList<BlockSegment> slavedBlocksList = new ArrayList<>();
        SegmentController entity = getEntity();
        for(short id : ElementKeyMap.getInfo(getId()).controlling) {
            PositionControl control = entity.getControlElementMap().getControlledElements(id, position);
            if(control != null && control.getControlMap() != null && control.getControlMap().size() > 0) {
                LongIterator iterator = control.getControlMap().iterator();
                while(iterator.hasNext()) {
                    slavedBlocksList.add(BlockSegment.fromSegmentPiece(entity.getSegmentBuffer().getPointUnsave(iterator.nextLong())));
                }
            }
        }
        BlockSegment[] slavedBlocks = new BlockSegment[slavedBlocksList.size()];
        for(int i = 0; i < slavedBlocks.length; i ++) slavedBlocks[i] = slavedBlocksList.get(i);
        return slavedBlocks;
    }

    public BlockSegment[] getSlavedBlocks(short type) {
        BlockSegment[] allSlaved = getSlavedBlocks();
        ArrayList<BlockSegment> slavedBlocksList = new ArrayList<>();
        for(BlockSegment blockSegment : allSlaved) {
            if(blockSegment.getId() == type) slavedBlocksList.add(blockSegment);
        }
        BlockSegment[] slavedBlocks = new BlockSegment[slavedBlocksList.size()];
        for(int i = 0; i < slavedBlocks.length; i ++) slavedBlocks[i] = slavedBlocksList.get(i);
        return slavedBlocks;
    }

    public BlockSegment[] getAdjacentBlocks(SegmentController entity) {
        ArrayList<BlockSegment> elementList = new ArrayList<>();
        for(Vector3i direction : Element.DIRECTIONSi) {
            Vector3i adjacentPos = position;
            adjacentPos.add(direction);
            try {
                elementList.add(BlockSegment.fromSegmentPiece(entity.getSegmentBuffer().getPointUnsave(adjacentPos)));
            } catch (NullPointerException exception) {
                exception.printStackTrace();
            }
        }
        return elementList.toArray(new BlockSegment[6]);
    }

    public BlockSegment[] getMatchingAdjacent(SegmentController entity) {
        ArrayList<BlockSegment> blocks = new ArrayList<>();
        BlockSegment[] adjacent = getAdjacentBlocks(entity);
        if(adjacent.length > 0) {
            for(BlockSegment b : adjacent) {
                if(b.getId() == getId() && b.getPosition() != getPosition() && !blocks.contains(b)) blocks.addAll(Arrays.asList(b.getMatchingAdjacent(entity)));
            }
        }

        BlockSegment[] matchingAdjacent = new BlockSegment[blocks.size()];
        for(int i = 0; i < matchingAdjacent.length; i ++) matchingAdjacent[i] = blocks.get(i);
        return matchingAdjacent;
    }

    public SegmentPiece toSegmentPiece() {
        return getEntity().getSegmentBuffer().getPointUnsave(getPosition());
    }

    public static BlockSegment fromSegmentPiece(SegmentPiece segmentPiece) {
        return new BlockSegment(segmentPiece.getType(), new Vector3i(segmentPiece.x, segmentPiece.y, segmentPiece.z), segmentPiece.getOrientation(), segmentPiece.getSegmentController());
    }

    public static BlockSegment fromEvent(SegmentPieceAddEvent event) {
        return new BlockSegment(event.getNewType(), new Vector3i(event.getX(), event.getY(), event.getZ()), event.getOrientation(), event.getSegmentController());
    }

    public static BlockSegment fromEvent(SegmentPieceRemoveEvent event) {
        return new BlockSegment(event.getType(), new Vector3i(event.getX(), event.getY(), event.getZ()), event.getOrientation(), event.getSegment().getSegmentController());
    }

    public static BlockSegment fromEvent(SegmentPieceActivateByPlayer event) {
        return new BlockSegment(event.getSegmentPiece().getType(), new Vector3i(event.getSegmentPiece().x, event.getSegmentPiece().y, event.getSegmentPiece().z), event.getSegmentPiece().getOrientation(), event.getSegmentPiece().getSegmentController());
    }

    public static BlockSegment fromEvent(SegmentPieceActivateEvent event) {
        return new BlockSegment(event.getSegmentPiece().getType(), new Vector3i(event.getSegmentPiece().x, event.getSegmentPiece().y, event.getSegmentPiece().z), event.getSegmentPiece().getOrientation(), event.getSegmentPiece().getSegmentController());
    }

    public static BlockSegment fromEvent(SegmentPieceDamageEvent event) {
        return new BlockSegment(event.getHitBlock().getType(), new Vector3i(event.getHitBlock().x, event.getHitBlock().y, event.getHitBlock().z), event.getHitBlock().getOrientation(), event.getHitBlock().getSegmentController());
    }

    public static BlockSegment fromEvent(SegmentPieceModifyEvent event) {
        return new BlockSegment(event.getVar6().getType(), new Vector3i(event.getVar6().x, event.getVar6().y, event.getVar6().z), event.getVar6().getOrientation(), event.getVar6().getSegmentController());
    }

    public static BlockSegment fromEvent(SegmentPieceKillEvent event) {
        return new BlockSegment(event.getPiece().getType(), new Vector3i(event.getPiece().x, event.getPiece().y, event.getPiece().z), event.getPiece().getOrientation(), event.getPiece().getSegmentController());
    }
}
