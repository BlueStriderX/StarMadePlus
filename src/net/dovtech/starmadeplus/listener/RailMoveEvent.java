package net.dovtech.starmadeplus.listener;

import api.listener.fastevents.RailMoveListener;
import net.dovtech.starmadeplus.blocks.BlockManager;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.rails.RailController;
import org.schema.game.common.controller.rails.RailRelation;
import org.schema.game.common.data.SegmentPiece;

public class RailMoveEvent implements RailMoveListener {

    private short railSpinnerClockwiseID;
    private short railSpinnerCounterClockwiseID;
    private short hiddenRailSpinnerClockwiseID;
    private short hiddenRailSpinnerCounterClockwiseID;

    @Override
    public void onRailRotate(RailController railController, SegmentPiece railBlock, SegmentPiece railDocker) {
        if(railSpinnerClockwiseID == 0) getRailIDs();
        short id = railBlock.getType();
        if(id == railSpinnerClockwiseID || id == hiddenRailSpinnerClockwiseID || id == railSpinnerCounterClockwiseID || id == hiddenRailSpinnerCounterClockwiseID) {
            if(railBlock.isActive() && railController.previous.doneInRotationServer() && railController.previous.rotationCode.equals(RailRelation.RotationType.NONE) && !railController.previous.continueRotation && railController.next.size() == 0) {
                /*
                RailRelation newRelation = new RailRelation(railBlock, railDocker);
                newRelation.rotationCode = rotationType;
                newRelation.railContactToGo = railBlock.getAbsolutePos(new Vector3i());
                railController.next.add(newRelation);
                 */
                railController.previous.rotationCode = getNextRotation(railController.previous.rotationCode);
                railController.previous.railContactToGo = railBlock.getAbsolutePos(new Vector3i());
                railController.previous.continueRotation = true;
            } else {
                //railController.previous.continueRotation = false;
            }
        }
    }

    @Override
    public void onRailUndock(RailController railController, SegmentPiece railBlock, SegmentPiece railDocker) {

    }

    @Override
    public void onRailDock(RailController railController, SegmentPiece railBlock, SegmentPiece railDocker) {

    }

    private void getRailIDs() {
        railSpinnerClockwiseID = BlockManager.getFromName("Rail Spinner Clock Wise").blockInfo.getId();
        railSpinnerCounterClockwiseID = BlockManager.getFromName("Rail Spinner CounterClock Wise").blockInfo.getId();
        hiddenRailSpinnerClockwiseID = BlockManager.getFromName("Hidden Rail Spinner Clock Wise").blockInfo.getId();
        hiddenRailSpinnerCounterClockwiseID = BlockManager.getFromName("Hidden Rail Spinner CounterClock Wise").blockInfo.getId();
    }

    private RailRelation.RotationType getNextRotation(RailRelation.RotationType previousRotation) {
        switch (previousRotation) {
            case NONE:
                return RailRelation.RotationType.CW_90;
            case CW_45:
                return RailRelation.RotationType.CW_135;
            case CW_90:
                return RailRelation.RotationType.CW_180;
            case CW_135:
                return RailRelation.RotationType.CW_225;
            case CW_180:
                return RailRelation.RotationType.CW_270;
            case CW_225:
                return RailRelation.RotationType.CW_315;
            case CW_270:
                return RailRelation.RotationType.CW_360;
            case CW_315:
                return RailRelation.RotationType.CW_45;
            case CW_360:
                return RailRelation.RotationType.CW_90;
            case CCW_45:
                return RailRelation.RotationType.CCW_135;
            case CCW_90:
                return RailRelation.RotationType.CCW_180;
            case CCW_135:
                return RailRelation.RotationType.CCW_225;
            case CCW_180:
                return RailRelation.RotationType.CCW_270;
            case CCW_225:
                return RailRelation.RotationType.CCW_315;
            case CCW_270:
                return RailRelation.RotationType.CCW_360;
            case CCW_315:
                return RailRelation.RotationType.CCW_45;
            case CCW_360:
                return RailRelation.RotationType.CCW_90;
            default:
                return RailRelation.RotationType.NONE;
        }
    }
}
