package thederpgamer.starmadeplus.gravitycompressor;

import thederpgamer.starmadeplus.blocks.BlockManager;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.power.reactor.PowerConsumer;
import org.schema.game.common.data.SegmentPiece;
import org.schema.schine.graphicsengine.core.Timer;

/**
 * GravityCompressorCollectionManager.java
 * <Description>
 * ================================
 * Created: 12/14/2020
 * Author: Garret
 */
public class GravityCompressorCollectionManager extends ControlBlockElementCollectionManager<GravityCompressorUnit, GravityCompressorCollectionManager, GravityCompressorElementManager> implements PowerConsumer {

    public GravityCompressorCollectionManager(SegmentPiece segmentPiece, SegmentController segmentController, GravityCompressorElementManager elementManager) {
        super(segmentPiece, BlockManager.getFromName("Gravity Compressor Computer").getId(), segmentController, elementManager);
    }

    @Override
    protected Class<GravityCompressorUnit> getType() {
        return null;
    }

    @Override
    public GravityCompressorUnit getInstance() {
        return null;
    }

    @Override
    public String getModuleName() {
        return null;
    }

    @Override
    public double getPowerConsumedPerSecondResting() {
        return 0;
    }

    @Override
    public double getPowerConsumedPerSecondCharging() {
        return 0;
    }

    @Override
    public boolean isPowerCharging(long l) {
        return false;
    }

    @Override
    public void setPowered(float v) {

    }

    @Override
    public float getPowered() {
        return 0;
    }

    @Override
    public PowerConsumerCategory getPowerConsumerCategory() {
        return null;
    }

    @Override
    public void reloadFromReactor(double v, Timer timer, float v1, boolean b, float v2) {

    }

    @Override
    public boolean isPowerConsumerActive() {
        return false;
    }

    @Override
    public void dischargeFully() {

    }
}
