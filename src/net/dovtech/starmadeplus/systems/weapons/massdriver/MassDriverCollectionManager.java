package net.dovtech.starmadeplus.systems.weapons.massdriver;

import net.dovtech.starmadeplus.blocks.MassDriverComputer;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelpManager;
import org.schema.game.client.view.gui.shiphud.newhud.HudContextHelperContainer;
import org.schema.game.client.view.gui.structurecontrol.GUIKeyValueEntry;
import org.schema.game.client.view.gui.structurecontrol.ModuleValueEntry;
import org.schema.game.client.view.gui.weapon.WeaponRowElementInterface;
import org.schema.game.common.controller.PlayerUsableInterface;
import org.schema.game.common.controller.SegmentController;
import org.schema.game.common.controller.damage.Damager;
import org.schema.game.common.controller.elements.BlockKillInterface;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.data.SegmentPiece;
import org.schema.game.common.data.player.ControllerStateInterface;
import org.schema.game.common.data.player.ControllerStateUnit;
import org.schema.schine.graphicsengine.core.MouseEvent;
import org.schema.schine.graphicsengine.core.Timer;
import org.schema.schine.input.KeyboardMappings;

public class MassDriverCollectionManager  extends ControlBlockElementCollectionManager<MassDriverUnit, MassDriverCollectionManager, MassDriverElementManager> implements PlayerUsableInterface, BlockKillInterface {

    public MassDriverCollectionManager(SegmentPiece segmentPiece, SegmentController segmentController, MassDriverElementManager massDriverElementManager) {
        super(segmentPiece, (short) 0, segmentController, massDriverElementManager);
    }

    public int getMargin() {
        return 0;
    }

    protected Class<MassDriverUnit> getType() {
        return MassDriverUnit.class;
    }

    public boolean needsUpdate() {
        return false;
    }

    public MassDriverUnit getInstance() {
        return new MassDriverUnit();
    }

    public void onLogicActivate(SegmentPiece segmentPiece, boolean bool, Timer timer) {

    }

    public void handleMouseEvent(ControllerStateUnit controllerStateUnit, MouseEvent mouseEvent) {
    }

    public boolean isDetailedElementCollections() {
        return false;
    }

    public GUIKeyValueEntry[] getGUICollectionStats() {
        return new GUIKeyValueEntry[]{
                new ModuleValueEntry("Total Size", this.getTotalSize())};
    }

    public String getModuleName() {
        return "Mass Driver System";
    }

    public float getSensorValue(SegmentPiece segmentPiece) {
        return 1.0F;
    }

    public WeaponRowElementInterface getWeaponRow() {
        return null;
    }

    public boolean isControllerConnectedTo(long useableID, short ID) {
        return true;
    }

    public boolean isPlayerUsable() {
        return true;
    }

    public long getUsableId() {
        return new MassDriverComputer().getPlayerUseableID();
    }

    public void handleControl(ControllerStateInterface controllerStateInterface, Timer timer) {
        this.getElementManager().handle(controllerStateInterface, timer);
    }

    public CollectionShape requiredNeigborsPerBlock() {
        return CollectionShape.ALL_IN_ONE;
    }

    public void onKilledBlock(long useableID, short ID, Damager damager) {
        this.checkIntegrity(useableID, ID, damager);
    }

    public void handleKeyEvent(ControllerStateUnit controllerStateUnit, KeyboardMappings keyboardMappings) {
    }

    public void addHudConext(ControllerStateUnit controllerStateUnit, HudContextHelpManager hudContextHelpManager, HudContextHelperContainer.Hos hos) {
    }
}
