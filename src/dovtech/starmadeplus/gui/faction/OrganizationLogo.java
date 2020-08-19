package dovtech.starmadeplus.gui.faction;

import org.schema.game.common.data.player.PlayerState;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.PlayerNotFountException;
import org.schema.schine.graphicsengine.forms.Sprite;

public class OrganizationLogo {

    private String fileName;
    private int uploaderID;
    private Sprite sprite;
    private String orgID;

    public OrganizationLogo(int uploaderID, String orgID, String fileName) {
        this.uploaderID = uploaderID;
        this.orgID = orgID;
        this.fileName = fileName;
    }

    public PlayerState getUploaderState() {
        try {
            return GameServerState.instance.getPlayerFromStateId(uploaderID);
        } catch (PlayerNotFountException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getUploaderID() {
        return uploaderID;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public String getOrgID() {
        return orgID;
    }

    public String getFileName() {
        return fileName;
    }
}
