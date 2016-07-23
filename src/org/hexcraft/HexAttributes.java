package org.hexcraft;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
//import org.bukkit.plugin.RegisteredServiceProvider;
import org.hexcraft.chest.DeficiencyGUI;
import org.hexcraft.chest.FoodGUI;
import org.hexcraft.chest.ImmunityGUI;
import org.hexcraft.chest.MasteryGUI;
import org.hexcraft.chest.Overview;
import org.hexcraft.chest.PassiveGUI;
import org.hexcraft.chest.PermEffectGUI;
import org.hexcraft.chest.ReflectGUI;
import org.hexcraft.chest.StrikeGUI;
import org.hexcraft.chest.ToughnessGUI;
import org.hexcraft.chest.TruceGUI;
import org.hexcraft.chest.WeaknessGUI;
import org.hexcraft.hexattributes.Commands;
import org.hexcraft.hexattributes.Config;
import org.hexcraft.hexattributes.ConfigAttributes;
import org.hexcraft.hexattributes.HPlayer;
import org.hexcraft.hexattributes.Lang;
import org.hexcraft.hexattributes.api.AttributeBase;
import org.hexcraft.hexattributes.api.HexAttributesAPI;
import org.hexcraft.hexattributes.listeners.*;
import org.hexcraft.hexattributes.types.Passive;
import org.hexcraft.util.DiscUtil;


public class HexAttributes extends HexCore {
	
	public String dataLayerFolderPath = "plugins" + File.separator + "HexAttributes";
    public String configFilePath = dataLayerFolderPath + File.separator + "config.json";
    public String langFilePath = dataLayerFolderPath + File.separator + "lang.json";
    
    public net.milkbowl.vault.permission.Permission perms = null;
    
    // -- per player settings
    // -- 10 talent slots
    // -- 150 points default
    public Config config;
    public ConfigAttributes configAttributes;
    public Lang lang;
    // -- config per type of attribute...
   
    public Map<UUID, HPlayer> hPlayers = new HashMap<UUID, HPlayer>();
    public List<UUID> removalList = new ArrayList<UUID>();


    // -- UGLY HACK to fix gui, do this until i re-work the system..
    // -- gui register event once..
    
    public boolean b_attributeOverview;
	// -- save Static GUIs for use!!!
	public boolean b_defGui;
	public boolean b_fooGui;
	public boolean b_immGui;
	public boolean b_masGui;
	public boolean b_pasGui;
	public boolean b_perGui;
	public boolean b_refGui;
	public boolean b_strGui;
	public boolean b_touGui;
	public boolean b_truGui;
	public boolean b_weaGui;
	
	
	// -- chest gui stuff!
	public Overview attributeOverview;
	// -- save Static GUIs for use!!!
	public DeficiencyGUI defGui;
	public FoodGUI fooGui;
	public ImmunityGUI immGui;
	public MasteryGUI masGui;
	public PassiveGUI pasGui;
	public PermEffectGUI perGui;
	public ReflectGUI refGui;
	public StrikeGUI strGui;
	public ToughnessGUI touGui;
	public TruceGUI truGui;
	public WeaknessGUI weaGui;

 	
    /*
 	@Override 
 	public GsonBuilder getGsonBuilder() 
 	{ 
 		return this.getGsonBuilderWithoutPreprocessors(); 
 	}
 	*/
    
	@Override
	public void onEnable()
	{
		if ( ! preEnable()) return;
		
		// -- load from json config
		String content = DiscUtil.readCatch(new File(configFilePath));
		
		// -- check for defaults
		if(content == null)
		{
			config = new Config();
			config.Axes = new ArrayList<Material>();
			config.Axes.add(Material.WOOD_AXE);
			config.Axes.add(Material.STONE_AXE);
			config.Axes.add(Material.IRON_AXE);
			config.Axes.add(Material.DIAMOND_AXE);
			
			config.Swords = new ArrayList<Material>();
			config.Swords.add(Material.WOOD_SWORD);
			config.Swords.add(Material.STONE_SWORD);
			config.Swords.add(Material.IRON_SWORD);
			config.Swords.add(Material.DIAMOND_SWORD);
			
			config.Projectiles = new ArrayList<Material>();
			config.Projectiles.add(Material.ARROW);
			
			config.Armors = new ArrayList<Material>();
			config.Armors.add(Material.LEATHER_HELMET);
			config.Armors.add(Material.LEATHER_CHESTPLATE);
			config.Armors.add(Material.LEATHER_LEGGINGS);
			config.Armors.add(Material.LEATHER_BOOTS);
			config.Armors.add(Material.IRON_HELMET);
			config.Armors.add(Material.IRON_CHESTPLATE);
			config.Armors.add(Material.IRON_LEGGINGS);
			config.Armors.add(Material.IRON_BOOTS);
			config.Armors.add(Material.CHAINMAIL_HELMET);
			config.Armors.add(Material.CHAINMAIL_CHESTPLATE);
			config.Armors.add(Material.CHAINMAIL_LEGGINGS);
			config.Armors.add(Material.CHAINMAIL_BOOTS);
			config.Armors.add(Material.GOLD_HELMET);
			config.Armors.add(Material.GOLD_CHESTPLATE);
			config.Armors.add(Material.GOLD_LEGGINGS);
			config.Armors.add(Material.GOLD_BOOTS);
			config.Armors.add(Material.DIAMOND_HELMET);
			config.Armors.add(Material.DIAMOND_CHESTPLATE);
			config.Armors.add(Material.DIAMOND_LEGGINGS);
			config.Armors.add(Material.DIAMOND_BOOTS);
			
			config.Mobs = new ArrayList<EntityType>();
			config.Mobs.add(EntityType.SLIME);
			config.Mobs.add(EntityType.MAGMA_CUBE);
			config.Mobs.add(EntityType.BLAZE);
			config.Mobs.add(EntityType.SILVERFISH);
			config.Mobs.add(EntityType.SPIDER);
			config.Mobs.add(EntityType.CAVE_SPIDER);
			config.Mobs.add(EntityType.SKELETON);
			config.Mobs.add(EntityType.ZOMBIE);
			config.Mobs.add(EntityType.GHAST);
			config.Mobs.add(EntityType.WITCH);
			config.Mobs.add(EntityType.CREEPER);
			
			config.bUsePerms = true;
			config.bDelayRespecAfterDamage = true;
			config.timeRespecAfterDamage = 150;
			config.configVersion = this.getDescription().getVersion();
			config.bHideAttributesWithNoPerms = false;
			
			config.bUseWorldWhiteList = false;
			config.worldWhitelist = new ArrayList<String>();
			config.worldWhitelist.add("world");
			config.worldWhitelist.add("world_nether");
			config.worldWhitelist.add("world_the_end");
			
			config.bShowWeblink = true;
			//config.WebLinkString = "http://hexcraft.org/attributes";
			
			config.bShowPlayerNoAttributesMessage = true;
			
			config.meleeStrikeChance = 0.10f;
			config.rangedStrikeChance = 0.33f;
			config.meleeReflectChance = 0.10f;
			config.rangedReflectChance = 0.33f;
			config.bModifyStrikeChanceBasedOnSwingCooldown = true;
			
			config.pointsBase = 150;
			config.slotsMax = 10;

			// -- Save
			String save = gson.toJson(config);
			DiscUtil.writeCatch(new File(configFilePath), save);
		}
		else
		{
			// -- create config from json
			config = gson.fromJson(content, Config.class);
			
			// -- check config version
			if (config.configVersion == null) {
				
				// -- add new perms
				config.bUsePerms = true;
				config.bDelayRespecAfterDamage = true;
				config.timeRespecAfterDamage = 150;
				config.configVersion = this.getDescription().getVersion();
				
				this.log("Updating config to version: " + config.configVersion);
				this.log("Adding: config.bUsePerms(" + config.bUsePerms + ")");
				this.log("Adding: config.bDelayRespecAfterDamage(" + config.bDelayRespecAfterDamage + ")");
				this.log("Adding: config.timeRespecAfterDamage(" + config.timeRespecAfterDamage + ")");
				this.log("Adding: config.configVersion(" + config.configVersion + ")");
				
				// -- Save
				String save = gson.toJson(config);
				DiscUtil.writeCatch(new File(configFilePath), save);
			}
			else if (config.configVersion.equals("0.1.2")) {
				
				// -- multiworld support
				config.worldWhitelist = new ArrayList<String>();
				config.worldWhitelist.add("world");
				config.worldWhitelist.add("world_nether");
				config.worldWhitelist.add("world_the_end");
				config.configVersion = this.getDescription().getVersion();
				config.bHideAttributesWithNoPerms = false;
				
				this.log("Updating config to version: " + config.configVersion);
				this.log("Adding: config.bHideAttributesWithNoPerms(false)");
				this.log("Adding: config.worldWhitelist(world)");
				this.log("Adding: config.worldWhitelist(world_nether)");
				this.log("Adding: config.worldWhitelist(world_the_end)");
				
				// -- Save
				String save = gson.toJson(config);
				DiscUtil.writeCatch(new File(configFilePath), save);
				
			}
			else if (config.configVersion.equals("0.1.3")) {
				
				// -- multiworld support
				config.configVersion = this.getDescription().getVersion();
				config.bShowWeblink = true;
				//config.WebLinkString = "http://hexcraft.org/attributes";
				
				
				this.log("Updating config to version: " + config.configVersion);
				this.log("Adding: config.bShowWeblink(true)");
				//this.log("Adding: config.WebLinkString(http://hexcraft.org/attributes)");
				
				// -- Save
				String save = gson.toJson(config);
				DiscUtil.writeCatch(new File(configFilePath), save);
				
			}
			else if (config.configVersion.equals("0.1.5")) {
				config.configVersion = this.getDescription().getVersion();
				config.bShowPlayerNoAttributesMessage = true;
				this.log("Updating config to version: " + config.configVersion);
				this.log("Adding: config.bShowPlayerNoAttributesMessage(true)");
				
				// -- Save
				String save = gson.toJson(config);
				DiscUtil.writeCatch(new File(configFilePath), save);
			}
			else if (config.configVersion.equals("0.1.7")) {
				config.configVersion = this.getDescription().getVersion();
				config.meleeStrikeChance = 0.10f;
				config.rangedStrikeChance = 0.33f;
				config.meleeReflectChance = 0.10f;
				config.rangedReflectChance = 0.33f;
				config.bModifyStrikeChanceBasedOnSwingCooldown = true;
				
				this.log("Updating config to version: " + config.configVersion);
				
				// -- Save
				String save = gson.toJson(config);
				DiscUtil.writeCatch(new File(configFilePath), save);
			}
			else if (config.configVersion.equals("0.1.9")) {
				config.configVersion = this.getDescription().getVersion();
				
				config.pointsBase = 150;
				config.slotsMax = 10;
				
				this.log("Updating config to version: " + config.configVersion);
				
				// -- Save
				String save = gson.toJson(config);
				DiscUtil.writeCatch(new File(configFilePath), save);
			}
			else if (config.configVersion.equals("0.2.0") || config.configVersion.equals("0.2.1")) {
				config.configVersion = this.getDescription().getVersion();
				
				config.bUseWorldWhiteList = false;
				
				this.log("Updating config to version: " + config.configVersion);
				
				// -- Save
				String save = gson.toJson(config);
				DiscUtil.writeCatch(new File(configFilePath), save);
			}
			
		}
		
		// -- language
		String langContent = DiscUtil.readCatch(new File(langFilePath));
		
		if (langContent == null) {
			lang = new Lang();

			// -- default
			lang.noAttributes = "&fYou have not added any &3Attributes&f yet. ";
			lang.webLinkString = "Check them out here: &7(&6http://hexcraft.org/attributes&7)";
			lang.commandHint = "&fUse &6/attribute gui&f to start!";
			lang.unableChangeDamage = "&4Unable to change attributes due to damage taken: &7(&b%s remaining&7)";
			lang.removedAddedFromSign = "&aRemoved all attributes, adding attributes from signs.";
			lang.attributeNotFound = "&4Attribute not found &7(&b%s&7)";
			
			lang.noPermissionForAttribute = "&4You do not have permission for: %s";
			lang.alreadyHaveAttribute = "&4You already have %s";
			lang.addedAttribute = "&aAdded attribute&f: %s";
			lang.notEnoughPoints = "&4Not enough points to add attribute %s";
			lang.noMoreSockets = "&4No more attribute sockets.";
			lang.addedFromSign = "&aFinished adding attributes from signs.";
			
			lang.removedAllAttributes = "&aRemoved all attributes.";
			lang.attributeInfo = "&aRemoved all attributes.";
			lang.removedAttribute = "&aRemoved attribute %s";
			lang.helpRemovalNonNegative = "&cYou must remove a positive attribute before you may remove a negative attribute due to the lack of shards. %s.";
			lang.helpUnassigned = "&cYou can't remove an attribute you don't have %s";
			
			lang.yourAttributes = "&f---- &6Your Attributes&f ---- ";
			lang.sockets = "&fSockets: &b%s&f/&b%s";
			lang.shards = "&fShards: &b%s&f/&b%s";
			lang.attributeCommands = "&f---- &6Attribute Commands&f ---- ";
			lang.cmdGui = "&b/attribute gui &7(&fGui Command System&7)";
			lang.cmdAdd = "&b/attribute a,add <attribute> &7(&fadd attributes&7)";
			lang.cmdRemove = "&b/attribute r,remove <attribute|all> &7(&fremove attributes&7)";
			lang.cmdList = "&b/attribute l,list &7(&flist your attributes&7)";
			lang.cmdInfo = "&b/attribute i,info <attribute> &7(&fget info about attribute&7)";
			
			lang.targetStrikeImmunity = "Target has %s Attribute.";
			lang.strikeImmunityReason = "&b%s&7 is immune to &b%s&7 reason: &b%s";
			lang.notifyStrikeImmunity = "&b%s&7 failed &b%s&7 reason: &b%s";
			
			lang.foodRepulse = "&7You are disguested by the taste of: &b%s&7 reason: &b%s";
			
			lang.defendingAffectedBy = "&b%s&7 has been affected by &b%s";
			lang.afflictedYouWith = "&b%s&7 has afflicted you with &b%s";
			
			lang.close = "Close";
			lang.back = "Back";
			lang.category = "Attribute Category";
			lang.deficiency = "Deficiency";
			lang.food = "Food";
			lang.immunity = "Immunity";
			lang.mastery = "Mastery";
			lang.passive = "Passive";
			lang.permeffect = "PermEffect";
			lang.reflect = "Reflect";
			lang.strike = "Strike";
			lang.toughness = "Toughness";
			lang.truce = "Truce";
			lang.weakness = "Weakness";
			lang.tooltipInfoPositive = "&a%s&7(&f+%s&7)";
			lang.tooltipAttributeInfo = "&7%s";
			lang.tooltipRemove = "&cRight-Click To Remove";
			lang.tooltipInfoNegative = "&c%s&7(&f%s&7)";
			lang.tooltipAdd = "&cLeft-Click to Add";
			
			lang.notUnlocked = "";

			// -- Save
			String save = gson.toJson(lang);
			DiscUtil.writeCatch(new File(langFilePath), save);

			// -- prepare lang stuff...
			fixLangColors();
			
			
		}
		else {
			// -- create config from json			
			lang = gson.fromJson(langContent, Lang.class);
			
			fixLangColors();
		}
		
		// -- Load Attributes
		configAttributes = new ConfigAttributes(this);
		
		// -- after attribute load, create dynamic perms!
		for (AttributeBase a : configAttributes.types.register) {
			Permission perm = new Permission("hexattributes.type." + a.name);
			perm.addParent("hexattributes.type.*", true);
			this.getServer().getPluginManager().addPermission(perm);
		}
		
		// -- vault hook
		//setupPermissions();
		
		// -- commands
		this.getCommand("attribute").setExecutor(new Commands(this));
		this.getCommand("att").setExecutor(new Commands(this));
		
		
		// -- lets start the Listeners!!
		new OnPlayerJoin(this);
		new OnEntityDamageByEntityEvent(this);
		new OnEntityTargetEvent(this);
		new OnEntityDamageEvent(this);
		new OnPotionSplashEvent(this);
		new OnPlayerItemConsumeEvent(this);
		new OnClickSign(this);
		new OnPlayerChangeWorld(this);
		
		updateTick();
		delaySave();
		PassiveStuff();
		playerRemoval();
		
		// -- API stuff
		new HexAttributesAPI(this);
		
		postEnable();
	}
	
	/*
	private boolean setupPermissions() {
        RegisteredServiceProvider<net.milkbowl.vault.permission.Permission> rsp = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    */
	
	private void fixLangColors() {
		
		lang.noAttributes = ChatColor.translateAlternateColorCodes('&', lang.noAttributes);
		lang.webLinkString = ChatColor.translateAlternateColorCodes('&', lang.webLinkString);
		lang.commandHint = ChatColor.translateAlternateColorCodes('&', lang.commandHint);
		lang.unableChangeDamage = ChatColor.translateAlternateColorCodes('&', lang.unableChangeDamage);
		lang.removedAddedFromSign = ChatColor.translateAlternateColorCodes('&', lang.removedAddedFromSign);
		lang.attributeNotFound = ChatColor.translateAlternateColorCodes('&', lang.attributeNotFound);
		
		lang.noPermissionForAttribute = ChatColor.translateAlternateColorCodes('&', lang.noPermissionForAttribute);
		lang.alreadyHaveAttribute = ChatColor.translateAlternateColorCodes('&', lang.alreadyHaveAttribute);
		lang.addedAttribute = ChatColor.translateAlternateColorCodes('&', lang.addedAttribute);
		lang.notEnoughPoints = ChatColor.translateAlternateColorCodes('&', lang.notEnoughPoints);
		lang.noMoreSockets = ChatColor.translateAlternateColorCodes('&', lang.noMoreSockets);
		lang.addedFromSign = ChatColor.translateAlternateColorCodes('&', lang.addedFromSign);
		
		lang.removedAllAttributes = ChatColor.translateAlternateColorCodes('&', lang.removedAllAttributes);
		lang.attributeInfo = ChatColor.translateAlternateColorCodes('&', lang.attributeInfo);
		lang.removedAttribute = ChatColor.translateAlternateColorCodes('&', lang.removedAttribute);
		lang.helpRemovalNonNegative = ChatColor.translateAlternateColorCodes('&', lang.helpRemovalNonNegative);
		lang.helpUnassigned = ChatColor.translateAlternateColorCodes('&', lang.helpUnassigned);
		
		lang.yourAttributes = ChatColor.translateAlternateColorCodes('&', lang.yourAttributes);
		lang.sockets = ChatColor.translateAlternateColorCodes('&', lang.sockets);
		lang.shards = ChatColor.translateAlternateColorCodes('&', lang.shards);
		lang.attributeCommands = ChatColor.translateAlternateColorCodes('&', lang.attributeCommands);
		lang.cmdGui = ChatColor.translateAlternateColorCodes('&', lang.cmdGui);
		lang.cmdAdd = ChatColor.translateAlternateColorCodes('&', lang.cmdAdd);
		lang.cmdRemove = ChatColor.translateAlternateColorCodes('&', lang.cmdRemove);
		lang.cmdList = ChatColor.translateAlternateColorCodes('&', lang.cmdList);
		lang.cmdInfo = ChatColor.translateAlternateColorCodes('&', lang.cmdInfo);
		
		lang.targetStrikeImmunity = ChatColor.translateAlternateColorCodes('&', lang.targetStrikeImmunity);
		lang.strikeImmunityReason = ChatColor.translateAlternateColorCodes('&', lang.strikeImmunityReason);
		lang.notifyStrikeImmunity = ChatColor.translateAlternateColorCodes('&', lang.notifyStrikeImmunity);
		
		lang.foodRepulse = ChatColor.translateAlternateColorCodes('&', lang.foodRepulse);
		
		lang.defendingAffectedBy = ChatColor.translateAlternateColorCodes('&', lang.defendingAffectedBy);
		lang.afflictedYouWith = ChatColor.translateAlternateColorCodes('&', lang.afflictedYouWith);
		
		lang.close = ChatColor.translateAlternateColorCodes('&', lang.close);
		lang.back = ChatColor.translateAlternateColorCodes('&', lang.back);
		lang.category = ChatColor.translateAlternateColorCodes('&', lang.category);
		lang.deficiency = ChatColor.translateAlternateColorCodes('&', lang.deficiency);
		lang.food = ChatColor.translateAlternateColorCodes('&', lang.food);
		lang.immunity = ChatColor.translateAlternateColorCodes('&', lang.immunity);
		lang.mastery = ChatColor.translateAlternateColorCodes('&', lang.mastery);
		lang.passive = ChatColor.translateAlternateColorCodes('&', lang.passive);
		lang.permeffect = ChatColor.translateAlternateColorCodes('&', lang.permeffect);
		lang.reflect = ChatColor.translateAlternateColorCodes('&', lang.reflect);
		lang.strike = ChatColor.translateAlternateColorCodes('&', lang.strike);
		lang.toughness = ChatColor.translateAlternateColorCodes('&', lang.toughness);
		lang.truce = ChatColor.translateAlternateColorCodes('&', lang.truce);
		lang.weakness = ChatColor.translateAlternateColorCodes('&', lang.weakness);
		lang.tooltipInfoPositive = ChatColor.translateAlternateColorCodes('&', lang.tooltipInfoPositive);
		lang.tooltipAttributeInfo = ChatColor.translateAlternateColorCodes('&', lang.tooltipAttributeInfo);
		lang.tooltipRemove = ChatColor.translateAlternateColorCodes('&', lang.tooltipRemove);
		lang.tooltipInfoNegative = ChatColor.translateAlternateColorCodes('&', lang.tooltipInfoNegative);
		lang.tooltipAdd = ChatColor.translateAlternateColorCodes('&', lang.tooltipAdd);
		
		lang.notUnlocked = ChatColor.translateAlternateColorCodes('&', lang.notUnlocked);

	}
	
	private void PassiveStuff()
	{
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            
            public void run() {
            	
            	for(Entry<UUID, HPlayer> entry : hPlayers.entrySet()) {
            		
					UUID uid = entry.getKey();
					HPlayer hplayer = entry.getValue();
					
					/*
					// -- World Check
					// -- technically, they should only be an HPlayer if they are in a world.
            		if (!config.worldWhitelist.contains(hplayer.getPlayer().getWorld().getName())) {
            			return;
            		}
            		*/
					
					if (hplayer.bRemoval == false) {					
						
						for(Passive passive : configAttributes.types.passive) {
		    				if (hplayer.config.assignedAttributes.contains(passive.name)) {
		    					Player player = getServer().getPlayer(uid);
		    					passive.Execute(player, hplayer);
		    				}
						}
						
						// -- handle meditation stuff here..
						// -- so its not a for each passive 
						
						if (hplayer.lastLocation.getBlockX() == hplayer.getPlayer().getLocation().getBlockX()
								&& hplayer.lastLocation.getBlockY() == hplayer.getPlayer().getLocation().getBlockY()
								&& hplayer.lastLocation.getBlockZ() == hplayer.getPlayer().getLocation().getBlockZ()){
							
							
							if (hplayer.meditationCount >= 3) {
								
								//ApplyPassive(player);
							}
							else
							{
								hplayer.meditationCount += 1;
							}
						}
						else
						{
							hplayer.lastLocation = hplayer.getPlayer().getLocation();
							hplayer.meditationCount = 0;
						}
					}
					
					
					
					
				}
            	
            }
        }, 60L, 60L);
	}
	
	// -- update tick.. for stuf..
	// -- might now need this
	public void updateTick()
	{
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				// -- for each hplayer
				for(Entry<UUID, HPlayer> entry : hPlayers.entrySet()) {
					HPlayer player = entry.getValue();
					if (player.bRemoval == false) {
						
						player.addPermEffects();
					}

				}
				
			}
		}, 0L, 600L);
	}
	
	// -- delaySave?
	// -- 5 minutes
	public void delaySave()
	{
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				// -- for each hplayer
				for(Entry<UUID, HPlayer> entry : hPlayers.entrySet()) {
					//UUID uid = entry.getKey();
					HPlayer player = entry.getValue();
				    if (player.bNeedsSave == true) {
				    	player.saveConfig();
				    }
				}
				
			}
		}, 0L, 600L);
	}
	
	// -- 5 minute remove player
	public void playerRemoval()
	{
		this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				
				for (Iterator<Entry<UUID, HPlayer>> iterator = hPlayers.entrySet().iterator(); iterator.hasNext(); ) {
				    Entry<UUID, HPlayer> entry = iterator.next();
				    //UUID uid = entry.getKey();
					HPlayer hplayer = entry.getValue();
					
					if (hplayer.bRemoval == true) {
						if ((hplayer.lastLogin + 150000) <= System.currentTimeMillis()) {
							iterator.remove();
						}
					}
					
					
				}
				
				
			}
		}, 0L, 600L);
	}
	
}
