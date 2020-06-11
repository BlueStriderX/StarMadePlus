package net.dovtech.starmadeplus;

import api.DebugFile;
import api.config.BlockConfig;
import api.element.block.Blocks;
import api.element.block.FactoryType;
import api.entity.Entity;
import api.listener.Listener;
import api.listener.events.EntityDamageEvent;
import api.listener.events.Event;
import api.listener.events.register.ElementRegisterEvent;
import api.mod.StarLoader;
import api.mod.StarMod;
import api.systems.Shield;
import net.dovtech.starmadeplus.blocks.MassDriverAmmoBox;
import net.dovtech.starmadeplus.blocks.MassDriverComputer;
import net.dovtech.starmadeplus.blocks.MassDriverLauncher;
import net.dovtech.starmadeplus.systems.weapons.DamagePattern;
import net.dovtech.starmadeplus.systems.weapons.massdriver.MassDriverElementManager;
import org.apache.commons.io.FileUtils;
import org.schema.game.common.controller.elements.ManagerModuleCollection;
import org.schema.game.common.data.element.ElementInformation;
import org.schema.game.common.data.element.FactoryResource;
import org.schema.game.common.data.world.Segment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StarMadePlus extends StarMod {

    static StarMadePlus inst;
    public StarMadePlus() {
        inst = this;
    }

    //Config
    private File configFolder;
    private File config;
    private String[] defaultConfig = {
            "debugMode: false",
            "bleedthroughNerf: 0.1",
            "massDriversEnabled: true",
            "massDriverShieldBypass: true",
            "massDriverDamagePattern: SHATTER",
            "massDriverArmorPiercing: 1.20"
    };

    //Config Settings
    private boolean debug;
    private double bleedthroughNerf;
    private boolean massDriversEnabled;
    private boolean massDriverShieldBypass;
    private DamagePattern massDriverDamagePattern;
    private double massDriverArmorPiercing;

    public static void main(String[] args) {

    }

    @Override
    public void onGameStart() {
        this.modName = "StarMadePlus";
        this.modAuthor = "DovTech";
        this.modVersion = "0.1.6";
        this.modDescription = "Overhauls and adds to StarMade's systems for a 'Vanilla+' feel";
    }

    @Override
    public void onEnable() {
        super.onEnable();

        this.configFolder = new File("/modconfigs/StarMadePlus");
        if(!configFolder.exists()) configFolder.mkdir();
        this.config = new File(configFolder.getPath() + "/config.yml");
        if(!config.exists()) getConfig().saveDefault(defaultConfig);

        try {
            setValuesFromConfig();
        } catch(Exception e) {
            e.printStackTrace();
            DebugFile.log("[ERROR]: Could not load StarMadePlus config! Disabling...", this);
            super.onDisable();
        }

        try {
            changeConfig();
        } catch(Exception e) {
            e.printStackTrace();
            DebugFile.log("[ERROR]: Could not modify /data/config/blockBehaviorConfig.xml! Skipping for now, but this may cause unintended behavior!", this);
        }

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
                    for(Segment segment : event.getSegmentsHit()) {
                        for(int i = 0; i < segment.getSize(); i ++) {
                            event.getHandler().doDamageOnBlock(segment.getSegmentData().getType(i), Blocks.fromId(segment.getSegmentData().getType(i)).getInfo(), segment, i);
                            if(debug) DebugFile.log("[DEBUG]: Applied " + bleedthroughDamage + " bleedthrough damage to segment at " + segment.pos.toString(), this.getMod());
                        }
                    }
                }
            }
        });

        StarLoader.registerListener(ElementRegisterEvent.class, new Listener() {
            @Override
            public void onEvent(Event e) {
                ElementRegisterEvent event = (ElementRegisterEvent) e;
                event.addModuleCollection(new ManagerModuleCollection(new MassDriverElementManager(event.getSegmentController()), new MassDriverComputer().getBlockinfo().getId(), new MassDriverLauncher().getBlockinfo().getId()));
            }
        });

        DebugFile.log("Enabled", this);
    }

    @Override
    public void onBlockConfigLoad(BlockConfig config) {

        //Mass Driver Computer
        MassDriverComputer massDriverComputer = new MassDriverComputer();
        ElementInformation massDriverComputerInfo = massDriverComputer.getBlockinfo();
        FactoryResource[] massDriverComputerRecipe = {
                new FactoryResource(3, Blocks.CANNON_COMPUTER.getId()),
                new FactoryResource(5, Blocks.WARHEAD.getId())
        };
        BlockConfig.addRecipe(massDriverComputerInfo, FactoryType.ADVANCED, 10, massDriverComputerRecipe);
        config.add(massDriverComputerInfo);

        //Mass Driver Launcher
        MassDriverLauncher massDriverLauncher = new MassDriverLauncher();
        ElementInformation massDriverLauncherInfo = massDriverLauncher.getBlockinfo();
        FactoryResource[] massDriverLauncherRecipe = {
                new FactoryResource(10, Blocks.CANNON_BARREL.getId()),
                new FactoryResource(10, Blocks.WARHEAD.getId())
        };
        BlockConfig.addRecipe(massDriverLauncherInfo, FactoryType.ADVANCED, 10, massDriverLauncherRecipe);
        config.add(massDriverLauncherInfo);

        //Mass Driver Ammo Box
        MassDriverAmmoBox massDriverAmmoBox = new MassDriverAmmoBox();
        ElementInformation massDriverAmmoBoxInfo = massDriverAmmoBox.getBlockinfo();
        FactoryResource[] massDriverAmmoBoxRecipe = {
                new FactoryResource(1, Blocks.MISSILE_CAPACITY_MODULE.getId()),
                new FactoryResource(10, Blocks.WARHEAD.getId())
        };
        BlockConfig.addRecipe(massDriverAmmoBoxInfo, FactoryType.ADVANCED, 10, massDriverAmmoBoxRecipe);
        config.add(massDriverAmmoBoxInfo);
    }

    private void changeConfig() throws IOException {
        File blockBehaviorConfigfile = new File("data" + File.separator + "config" + File.separator + "blockBehaviorConfig.xml");
        FileUtils.copyURLToFile(this.getClass().getResource("/blockBehaviorConfig.xml"), blockBehaviorConfigfile);
    }

    private void setValuesFromConfig() {
        debug = Boolean.parseBoolean(getConfig().getString("debugMode"));
        bleedthroughNerf = getConfig().getDouble("bleedthroughNerf");
        massDriversEnabled = Boolean.parseBoolean(getConfig().getString("massDriversEnabled"));
        massDriverShieldBypass = Boolean.parseBoolean(getConfig().getString("massDriverShieldBypass"));
        massDriverDamagePattern = DamagePattern.valueOf(getConfig().getString("massDriverDamagePattern"));
        massDriverArmorPiercing = getConfig().getDouble("massDriverArmorPiercing");
    }

    public static StarMadePlus getInstance() {
        return inst;
    }
}
