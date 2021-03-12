package thederpgamer.starmadeplus.data.player;

import api.common.GameCommon;
import java.io.Serializable;

/**
 * PlayerData.java
 * <Description>
 *
 * @author TheDerpGamer
 * @since 03/12/2021
 */
public class PlayerData implements Serializable {

    public String playerName;
    public boolean acceptedDisclaimer;
    public int factionId;

    public PlayerData(String playerName) {
        this.playerName = playerName;
        this.acceptedDisclaimer = false;
        this.factionId = GameCommon.getPlayerFromName(playerName).getFactionId();
    }
}
