package thederpgamer.starmadeplus.blocks.systems;

import api.config.BlockConfig;
import org.schema.game.common.data.element.ElementKeyMap;
import thederpgamer.starmadeplus.blocks.BlockElement;
import thederpgamer.starmadeplus.blocks.BlockManager;
import org.schema.game.common.data.element.FactoryResource;

public class StellarLifterController extends BlockElement {

    public StellarLifterController() {
        super("Stellar Lifter Controller", BlockManager.TextureType.COMPUTER);
    }

    @Override
    public void initialize() {
        blockInfo.setInRecipe(true);
        blockInfo.canActivate = ElementKeyMap.getInfo(ElementKeyMap.TRACTOR_BEAM_COMPUTER).canActivate;
        blockInfo.setPrice(ElementKeyMap.getInfo(ElementKeyMap.TRACTOR_BEAM_COMPUTER).price * 5);
        blockInfo.setShoppable(true);
        blockInfo.controlling.add(BlockManager.getFromName("Stellar Lifter Module").getId());
        blockInfo.controlledBy.addAll(ElementKeyMap.getInfo(ElementKeyMap.TRACTOR_BEAM_COMPUTER).controlledBy);

        BlockConfig.addRecipe(blockInfo, 5, (int) ElementKeyMap.getInfo(ElementKeyMap.SHIELD_CAP_ID).getFactoryBakeTime(),
                new FactoryResource(10, ElementKeyMap.TRACTOR_BEAM_COMPUTER),
                new FactoryResource(30, ElementKeyMap.TRACTOR_BEAM),
                new FactoryResource(15, ElementKeyMap.GRAVITY_ID),
                new FactoryResource(1, BlockManager.getFromName("Photon Shard").getId())

        );
        BlockConfig.add(blockInfo);
    }
}
