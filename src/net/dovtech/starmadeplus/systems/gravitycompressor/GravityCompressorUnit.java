package net.dovtech.starmadeplus.systems.gravitycompressor;

import org.schema.game.client.data.GameClientState;
import org.schema.game.client.view.gui.structurecontrol.ControllerManagerGUI;
import org.schema.game.common.controller.elements.ControlBlockElementCollectionManager;
import org.schema.game.common.data.element.ElementCollection;

/**
 * GravityCompressorUnit.java
 * <Description>
 * ================================
 * Created: 12/14/2020
 * Author: Garret
 */
public class GravityCompressorUnit extends ElementCollection<GravityCompressorUnit, GravityCompressorCollectionManager, GravityCompressorElementManager> {

    @Override
    public ControllerManagerGUI createUnitGUI(GameClientState state, ControlBlockElementCollectionManager<?, ?, ?> supportCollection, ControlBlockElementCollectionManager<?, ?, ?> effectCollection) {
        return elementCollectionManager.getElementManager().getGUIUnitValues(this, elementCollectionManager, supportCollection, effectCollection);
    }
}
