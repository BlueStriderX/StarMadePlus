package net.dovtech.starmadeplus;

import api.DebugFile;
import api.config.BlockConfig;
import api.element.block.Blocks;
import api.entity.Entity;
import api.listener.Listener;
import api.listener.events.EntityDamageEvent;
import api.listener.events.Event;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.systems.Shield;
import org.apache.commons.io.FileUtils;
import org.schema.game.common.data.world.Segment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StarMadePlus extends StarMod {

    static StarMadePlus inst;
    public StarMadePlus() {
        inst = this;
    }
    private boolean debug;
    private double bleedthroughNerf = 0.1;

    public static void main(String[] args) {

    }

    @Override
    public void onGameStart() {
        this.modName = "StarMadePlus";
        this.modAuthor = "DovTech";
        this.modVersion = "0.1.3";
        this.modDescription = "Overhauls and adds to StarMade's systems for a 'Vanilla+' feel";

        try {
            changeConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEnable() {
        super.onEnable();
        DebugFile.log("Enabled", this);
        debug = true;

        StarLoader.registerListener(EntityDamageEvent.class, new Listener() {
            @Override
            public void onEvent(Event e) {
                EntityDamageEvent event = (EntityDamageEvent) e;
                if(event.wasShotByEntity()) {
                    Entity entity = event.getEntity();
                    ArrayList<Shield> shields = entity.getShields();
                    Shield hitShield = null;
                    for(Shield shield : shields) {
                        if(shield.getInternalShield().containsInRadius(event.getHandler().getPiece().x, event.getHandler().getPiece().y, event.getHandler().getPiece().z)) {
                            hitShield = shield;
                            break;
                        }
                    }

                    assert hitShield != null;

                    double bleedthrough = 0.0;

                    if((hitShield.getInternalShield().getShields() / hitShield.getInternalShield().getShieldCapacity()) <= 0.3) {
                        bleedthrough = 0.7;
                    } else if(hitShield.getCurrentShields() / hitShield.getMaxCapacity() > 0.3) {
                        bleedthrough = 1.0 - (hitShield.getInternalShield().getShields() / hitShield.getInternalShield().getShieldCapacity());
                    }

                    double damage = event.getProjectile().getDamage(0);

                    double bleedthroughDamage = (damage * bleedthrough) - (bleedthroughNerf * damage);
                    event.getShotHandler().dmg = (float) bleedthroughDamage;
                    if(debug) DebugFile.log("[DEBUG]: Bleedthrough damage is " + bleedthroughDamage, this.getMod());
                    for(Segment segment : event.getShotHandler().segmentsHit) {
                        for(int i = 0; i < segment.getSize(); i ++) {
                            event.getHandler().doDamageOnBlock(segment.getSegmentData().getType(i), Blocks.fromId(segment.getSegmentData().getType(i)).getInfo(), segment, i);
                            if(debug) DebugFile.log("[DEBUG]: Applied " + bleedthroughDamage + " bleedthrough damage to segment at " + segment.pos.toString(), this.getMod());
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onBlockConfigLoad(BlockConfig config) {
        /*
        Terminal terminal = new Terminal();
        ElementInformation terminalInfo = terminal.getBlockInfo();
        FactoryResource[] terminalRecipe = {
                new FactoryResource(1, Blocks.DISPLAY_MODULE.getId()),
                new FactoryResource(5, Blocks.ACTIVATION_MODULE.getId()),
                new FactoryResource(2, Blocks.SENSOR.getId()),
                new FactoryResource(1, Blocks.BOBBY_AI_MODULE.getId()),
        };
        BlockConfig.addRecipe(terminalInfo, FactoryType.ADVANCED, 10, terminalRecipe);
        config.add(terminalInfo);
         */
    }

    private void changeConfig() throws IOException {
        File blockBehaviorConfigfile = new File("data" + File.separator + "data" + File.separator + "blockBehaviorConfig.xml");
        FileUtils.copyURLToFile(this.getClass().getResource("/blockBehaviorConfig.xml"), blockBehaviorConfigfile);
    }

    public static StarMadePlus getInstance() {
        return inst;
    }
}
