package dovtech.starmadeplus.controller;

import dovtech.starmadeplus.gui.faction.relations.NewFactionRelationDialog;
import org.schema.game.client.controller.manager.ingame.faction.FactionControlManager;
import org.schema.game.client.data.GameClientState;
import org.schema.game.common.data.player.faction.Faction;
import org.schema.game.common.data.player.faction.FactionPermission;
import org.schema.schine.common.language.Lng;

public class NewFactionControlManager extends FactionControlManager {

    public NewFactionControlManager(GameClientState gameClientState) {
        super(gameClientState);
    }

    @Override
    public void openRelationShipInput(int toId) {
        if(getState().getPlayer().getFactionId() == 0) {
            getState().getController().popupAlertTextMessage(Lng.str("Cannot modify:\nYou are not in any faction!"), 0);
            return;
        }

        Faction from = getState().getFactionManager().getFaction(getState().getPlayer().getFactionId());
        Faction to = getState().getFactionManager().getFaction(toId);
        if(from == null) {
            getState().getController().popupAlertTextMessage(Lng.str("Your faction is corrupted\nand does not exist!"), 0);
            return;
        }
        if(to == null) {
            getState().getController().popupAlertTextMessage(Lng.str("Target faction is corrupted\nand does not exist!"), 0);
            return;
        }
        if(from == to) {
            getState().getController().popupAlertTextMessage(Lng.str("Your faction can't have\nrelations with itself."), 0);
            return;
        }
        if(to.getIdFaction() < 0) {
            getState().getController().popupAlertTextMessage(Lng.str("Cannot modify relations\nto a NPC faction!"), 0);
            return;
        }
        FactionPermission factionPermission = from.getMembersUID().get(getState().getPlayer().getName());
        if(factionPermission == null || !factionPermission.hasRelationshipPermission(from)) {
            getState().getController().popupAlertTextMessage(Lng.str("Cannot change relation:\nPermission denied!"), 0);
            return;
        }
        suspend(true);

        (new NewFactionRelationDialog(getState(), from, to)).activate();
    }
}