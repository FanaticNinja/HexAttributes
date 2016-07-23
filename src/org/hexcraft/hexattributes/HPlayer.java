package org.hexcraft.hexattributes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.hexcraft.HexAttributes;
import org.hexcraft.chest.*;
import org.hexcraft.hexattributes.types.PermEffect;
import org.hexcraft.util.DiscUtil;

public class HPlayer {
	
	public HexAttributes plugin;
	
	public String dataLayerFolderPath;
    public String configFilePath;
    public ConfigHPlayer config;
	public UUID playerUUID;
	public boolean bDenyDamageEvent = false;
	public boolean bNeedsSave = false;
	public Location lastLocation;
	public int meditationCount = 0;
	public long lastDamage = 0;
	public long lastAttack = 0;
	public boolean bRemoval = false;
	public long lastLogin = 0;
	
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

	//public List<String> assignedAttributes;
	
	// -- Truces
	//public boolean bCreeperTruce = false;
	
	public HPlayer(HexAttributes _plugin, UUID uuid){
		this.plugin = _plugin;
		this.playerUUID = uuid;
		
		// -- build strings
		dataLayerFolderPath = "plugins" + File.separator + "HexAttributes" + File.separator + "players";
		configFilePath = dataLayerFolderPath + File.separator + this.playerUUID.toString() + ".json";
		
		// -- we need to load our config, attributes, from json files
		// -- load from json config
		String content = DiscUtil.readCatch(new File(configFilePath));
		
		if(content == null)
		{
			config = new ConfigHPlayer();
			
			// -- set any defaults?
			config.assignedAttributes = new ArrayList<String>();
			//config.pointsBase = 150;
			
			// -- permission based points!
			resetPoints();
			
			
			if (plugin.config.bShowPlayerNoAttributesMessage) {
				//String msg = ChatColor.WHITE + "You have not added any " + ChatColor.AQUA + "Attributes" + ChatColor.WHITE + " yet. ";
				String msg = ChatColor.translateAlternateColorCodes('&', plugin.lang.noAttributes);
				
				if (plugin.config.bShowWeblink == true) {
					//msg += "Check them out here: " + ChatColor.GRAY + "(" + ChatColor.GOLD + plugin.config.WebLinkString + ChatColor.GRAY + ")";
					msg += ChatColor.translateAlternateColorCodes('&', plugin.lang.webLinkString);
				}
				
				//getPlayer().sendMessage(ChatColor.WHITE + "You have not added any " + ChatColor.AQUA + "Attributes" + ChatColor.WHITE + " yet. Check them out here: " + ChatColor.GRAY + "(" + ChatColor.GOLD + "http://hexcraft.org/attributes" + ChatColor.GRAY + ")");
				getPlayer().sendMessage(msg);
				//getPlayer().sendMessage(ChatColor.WHITE + "Use " + ChatColor.GOLD + "/attribute gui" + ChatColor.WHITE + " to start!");
				getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.lang.commandHint));
			}
			
			// -- Save
			saveConfig();
		}
		else
		{
			// -- create config from json
			config = _plugin.gson.fromJson(content, ConfigHPlayer.class);
			//this.world = config.world;
			//this.size = config.size;
			
			if (config.assignedAttributes == null)
			{
				config.assignedAttributes = new ArrayList<String>();
			}
			if (config.assignedAttributes.size() == 0 && plugin.config.bShowPlayerNoAttributesMessage) {
				
				//String msg = ChatColor.WHITE + "You have not added any " + ChatColor.AQUA + "Attributes" + ChatColor.WHITE + " yet. ";
				String msg = ChatColor.translateAlternateColorCodes('&', plugin.lang.noAttributes);
				
				if (plugin.config.bShowWeblink == true) {
					//msg += "Check them out here: " + ChatColor.GRAY + "(" + ChatColor.GOLD + plugin.config.WebLinkString + ChatColor.GRAY + ")";
					msg += ChatColor.translateAlternateColorCodes('&', plugin.lang.webLinkString);
				}
				
				//getPlayer().sendMessage(ChatColor.WHITE + "You have not added any " + ChatColor.AQUA + "Attributes" + ChatColor.WHITE + " yet. Check them out here: " + ChatColor.GRAY + "(" + ChatColor.GOLD + "http://hexcraft.org/attributes" + ChatColor.GRAY + ")");
				getPlayer().sendMessage(msg);
				//getPlayer().sendMessage(ChatColor.WHITE + "Use " + ChatColor.GOLD + "/attribute gui" + ChatColor.WHITE + " to start!");
				getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.lang.commandHint));
			}
		}
		
		// -- npe
		lastLocation = getPlayer().getLocation();
		
		addPermEffects();
	}
	
	public int checkPermModifier(String permNode) {
		
		int tempBaseModifier = 0;
		for(PermissionAttachmentInfo perm : getPlayer().getEffectivePermissions()) {
			
		    String permission = perm.getPermission();
		    
		    if(permission.startsWith(permNode)) {
		    	
		    	String[] numberSplit = permission.split("\\.");
			    if(numberSplit.length == 3) {

			    	
			    	try {
			    		
			    		if (tempBaseModifier < Integer.parseInt(numberSplit[2]))
			    			tempBaseModifier = Integer.parseInt(numberSplit[2]);
			    		
				    } catch(Exception ex) {
				        // not a number
				    }

			    }
		    }
		}
		
		return tempBaseModifier;
	}
	
	public void resetPoints() {
		
		// -- check for perms to modify amounts...

		int tempPointsBase = checkPermModifier("hexattributes.pointsbase.");
		config.pointsBase = Math.max(plugin.config.pointsBase, tempPointsBase);
		config.pointsCurrent = Math.max(plugin.config.pointsBase, tempPointsBase);
		
		config.slotsMax = Math.max(plugin.config.slotsMax, checkPermModifier("hexattributes.slotsmax."));
		config.slotsSpent = 0;
	}
	
	public void saveConfig() {
		// -- Save
		String save = this.plugin.gson.toJson(config);
		DiscUtil.writeCatch(new File(configFilePath), save);
	}
	
	public Player getPlayer() {
		return plugin.getServer().getPlayer(playerUUID);
	}
	
	public float clamp(float val, float min, float max) {
	    return Math.max(min, Math.min(max, val));
	}
	
	public double randomStrikeChance() {
		
		// -- strike chance! 5%
		AttributeInstance swingSpeed = getPlayer().getAttribute(Attribute.GENERIC_ATTACK_SPEED);
    	
    	// their first attack should always be a full attack.
    	if (lastAttack == 0) {
    		lastAttack = (long) (System.currentTimeMillis() - (swingSpeed.getBaseValue() * 1000));
    	}
    	
    	// milliseconds for attack speed
    	// ( 1 / swingSpeed.getValue() * 1000 ) 
    	float swingRechargeMillis = (float) ((1 / swingSpeed.getValue()) * 1000);
    	
    	//plugin.log("swingRechargeMillis: " + swingRechargeMillis);
    	
    	float timeSinceLastAttack = System.currentTimeMillis() - lastAttack;
    	
    	//plugin.log("timeSinceLastAttack: " + timeSinceLastAttack);
    	
    	// -- set percentage based on attack recharge
    	float strikeChanceModifier = clamp(timeSinceLastAttack, 0f, swingRechargeMillis) / swingRechargeMillis;
		
		// math.random * strikeChanceModifier
    	double chance = Math.random();
    	
    	//plugin.log("strikeChanceModifier: " + strikeChanceModifier);
    	//plugin.log("chance: " + chance);
    	//plugin.log("modified: " + chance * strikeChanceModifier);
    	
    	if (plugin.config.bModifyStrikeChanceBasedOnSwingCooldown) {
    		return chance * strikeChanceModifier;
    	}
    	else {
    		return chance;
    	}
    	
		
	}
	
	public double randomReflectChance() {
		return Math.random();
	}
	
	public void removePermEffect(String attribute) {
		// -- if attribute is a permEffect
		for (PermEffect perm : plugin.configAttributes.types.permeffect) {
			if (perm.name.equalsIgnoreCase(attribute)) {
				//PotionEffect permRemove = new PotionEffect(PotionEffectType.NIGHT_VISION, perm.duration, perm.amplifier);
				getPlayer().removePotionEffect(PotionEffectType.getByName(perm.effect));
				break;
			}
		}
	}
	
	public void addPermEffects() {
		Player player = getPlayer();
		// -- permenent effects
		List<PotionEffect> perm = new ArrayList<PotionEffect>();
		//int permLength = 1200;
		for (PermEffect permE : plugin.configAttributes.types.permeffect) {
			if (this.config.assignedAttributes.contains(permE.name)) {
				perm.add(new PotionEffect(PotionEffectType.getByName(permE.effect), permE.duration, permE.amplifier));
				//player.sendMessage("Adding Perm: " + permE.effect + " duration: " + permE.duration);
			}
		}
		
		// -- batch apply
		// -- dont remove all, conflicts with strike and reflect system.
		if (perm != null)
		{
			// -- not for each potion effect, for each potion effect they want to ADD
			/*
			for (PotionEffect effect : player.getActivePotionEffects())
		        player.removePotionEffect(effect.getType());
			*/
			for (PotionEffect effect : perm)
		        player.removePotionEffect(effect.getType());

			player.addPotionEffects(perm);
			
		}
		
	}

}
