package net.dovtech.starmadeplus.systems.weapons.massdriver;

import com.bulletphysics.linearmath.Transform;
import net.dovtech.starmadeplus.blocks.MassDriverComputer;
import net.dovtech.starmadeplus.blocks.MassDriverLauncher;
import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.structurecontrol.ControllerManagerGUI;
import org.schema.game.client.view.gui.structurecontrol.ModuleValueEntry;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.controller.elements.UsableControllableElementManager;
import org.schema.game.common.controller.elements.power.reactor.PowerConsumer;
import org.schema.game.common.controller.observer.DrawerObserver;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.schine.graphicsengine.core.Timer;

public class MassDriverElementManager extends UsableControllableElementManager<MassDriverUnit, MassDriverCollectionManager, MassDriverElementManager> implements PowerConsumer {

    public MassDriverElementManager(SegmentController segmentController) {
        super(new MassDriverComputer().getBlockinfo().getId(), new MassDriverLauncher().getBlockinfo().getId(), segmentController);
        if (!segmentController.isOnServer()) {
            this.addObserver((DrawerObserver)segmentController.getState());
        }

    }

    public void onControllerChange() {
    }


    public ControllerManagerGUI getGUIUnitValues(MassDriverUnit massDriverUnit, MassDriverCollectionManager massDriverCollectionManager, ControlBlockElementCollectionManager<?, ?, ?> collectionManager, ControlBlockElementCollectionManager<?, ?, ?> controlBlockElementCollectionManager) {
        return ControllerManagerGUI.create((GameClientState)this.getState(),
                "Mass Driver Unit",
                massDriverUnit,

                new ModuleValueEntry("BU size",
                        massDriverUnit.size()),
                new ModuleValueEntry("BB size",
                        massDriverUnit.getBBTotalSize())
        );
    }

    public boolean canHandle(ControllerStateInterface controllerStateInterface) {
        return true;
    }

    protected String getTag() {
        return "mainreactor";
    }

    public MassDriverCollectionManager getNewCollectionManager(SegmentPiece segmentPiece, Class<MassDriverCollectionManager> massDriverCollectionManager) {
        return new MassDriverCollectionManager(segmentPiece, this.getSegmentController(), this);
    }

    protected void playSound(MassDriverUnit massDriverUnit, Transform transform) {
    }

    public void handle(ControllerStateInterface controllerStateInterface, Timer timer) {
    }

    public double getPowerConsumedPerSecondResting() {
        return this.totalSize;
    }

    public double getPowerConsumedPerSecondCharging() {
        return this.totalSize*2;
    }

    public boolean isPowerCharging(long useableID) {
        return false;
    }
    private float powered = 0;
    public void setPowered(float powered) {
        this.powered = powered;
    }

    public float getPowered() {
        return this.powered;
    }

    public void reloadFromReactor(double var1, Timer timer, float var4, boolean bool, float var6) {
    }

    public PowerConsumerCategory getPowerConsumerCategory() {
        return PowerConsumerCategory.CANNONS;
    }

    public boolean isPowerConsumerActive() {
        return true;
    }

    public String getName() {
        return "MassDriverElementManager";
    }

    public String getManagerName() {
        return "MassDriverElementManager";
    }

    public void dischargeFully() {
    }
}
