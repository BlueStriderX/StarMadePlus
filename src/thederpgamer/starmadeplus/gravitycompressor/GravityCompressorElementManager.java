package thederpgamer.starmadeplus.gravitycompressor;

import com.bulletphysics.linearmath.Transform;
import org.schema.game.client.view.gui.structurecontrol.ControllerManagerGUI;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.UsableControllableElementManager;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.schine.graphicsengine.core.Timer;

/**
 * GravityCompressorElementManager.java
 * <Description>
 * ================================
 * Created: 12/14/2020
 * Author: Garret
 */
public class GravityCompressorElementManager extends UsableControllableElementManager<GravityCompressorUnit, GravityCompressorCollectionManager, GravityCompressorElementManager> {

    public GravityCompressorElementManager(short i, short i1, SegmentController segmentController) {
        super(i, i1, segmentController);
    }

    @Override
    public ControllerManagerGUI getGUIUnitValues(GravityCompressorUnit gravityCompressorUnit, GravityCompressorCollectionManager gravityCompressorCollectionManager, ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager, ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager1) {
        return null;
    }

    @Override
    protected String getTag() {
        return null;
    }

    @Override
    public GravityCompressorCollectionManager getNewCollectionManager(SegmentPiece segmentPiece, Class<GravityCompressorCollectionManager> aClass) {
        return null;
    }

    @Override
    public String getManagerName() {
        return null;
    }

    @Override
    protected void playSound(GravityCompressorUnit gravityCompressorUnit, Transform transform) {

    }

    @Override
    public void handle(ControllerStateInterface controllerStateInterface, Timer timer) {

    }
}
