package thederpgamer.starmadeplus.blocks.factory;

import api.config.BlockConfig;
import org.schema.game.common.data.element.ElementKeyMap;

public class CrystallizerController extends FactoryElement {

    public CrystallizerController() {
        super("Crystallizer Controller");
        blockInfo.shoppable = true;
        blockInfo.price = ElementKeyMap.getInfo(ElementKeyMap.FACTORY_ADVANCED_ID).price * 15;

        BlockConfig.add(blockInfo);
    }
}
