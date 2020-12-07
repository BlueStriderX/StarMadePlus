package net.dovtech.starmadeplus.gui.tacticalmap;

import api.universe.StarSector;
import net.dovtech.starmadeplus.StarMadePlus;
import org.schema.common.util.linAlg.Vector3i;

public class SectorTacticalMap {

    private final Vector3i[][][] mapSpaces = new Vector3i[StarMadePlus.getInstance().tacticalMapMaxViewDistance][StarMadePlus.getInstance().tacticalMapMaxViewDistance][StarMadePlus.getInstance().tacticalMapMaxViewDistance];
    private StarSector current;

    public SectorTacticalMap(StarSector current) {
        this.current = current;

    }


}
