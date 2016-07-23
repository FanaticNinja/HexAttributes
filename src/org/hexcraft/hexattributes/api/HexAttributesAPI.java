package org.hexcraft.hexattributes.api;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.hexcraft.HexAttributes;
import org.hexcraft.hexattributes.HPlayer;

public class HexAttributesAPI {
	
	/*
		HexAttributesAPI hexAPI = (HexAttributesAPI) Bukkit.getServer().getPluginManager().getPlugin("HexAttributes");
		api.yourMethod();
		Object variable = api.yourvariable;
	 */
	
	private static HexAttributes plugin;
	
	public HexAttributesAPI(HexAttributes _plugin) {
		plugin = _plugin;
	}
	
	public static boolean bHasAttribute(UUID uuid, String attributeName) {
		
		if (plugin.hPlayers.get(uuid) == null )
		{
			return false;
		}
		else
		{
			// -- player exists
			HPlayer hPlayer = plugin.hPlayers.get(uuid);
			return hPlayer.config.assignedAttributes.contains(attributeName);			
		}
	}
	
	public static List<String> getAssignedAttributes(UUID uuid) {
		
		HPlayer hPlayer = plugin.hPlayers.get(uuid);
		if (hPlayer == null )
		{
			return null;
		}
		else
		{
			// -- player exists
			return hPlayer.config.assignedAttributes;			
		}
		
	}
	
	/** Returns an AttributeBase class: name, cost, info... */
	public static AttributeBase getAttributeInfo(String attributeName) {
		
		AttributeBase aTemp = null;
		for (AttributeBase a : plugin.configAttributes.types.register) {
			if (a.name.equalsIgnoreCase(attributeName)) {
				aTemp = a;
				break;
			}
		}

		return aTemp;
		
	}
	
	/** Returns true on valid world or bUseWorldWhiteList == false */
	public static boolean bValidWorld(World world) {
		
		if (plugin.config.bUseWorldWhiteList) {
			if (plugin.config.worldWhitelist != null) {
				if (!plugin.config.worldWhitelist.contains(world.getName())) {
					return false;
				}
				else {
					return true;
				}
			}
			else {
				plugin.log("Unable to use World White List: null");
				return false;
			}
		}
		else {
			// -- if they don't want to use the whitelist, always return true;
			return true;
		}
	}
	
	public static boolean bCanRespec(UUID uuid) {
		
		HPlayer hPlayer = plugin.hPlayers.get(uuid);
		if (hPlayer != null) {
			if ((hPlayer.lastDamage + plugin.config.timeRespecAfterDamage) > System.currentTimeMillis()) {
				return false;
			}
			else {
				return true;
			}
		}
		else {
			return false;
		}
		
	}
	
	
	public static boolean bRemoveAttributeFromPlayer(UUID uuid, String attributeName) {
		
		HPlayer hPlayer = plugin.hPlayers.get(uuid);
		if (hPlayer == null) {
			return false;
		}
		
		AttributeBase a = getAttributeInfo(attributeName);
		if (a == null) {
			return false;
		}
		
		if (hPlayer.config.assignedAttributes.contains(attributeName)) {
			
			// -- make sure player can't go negative..
			// -- if 10 > ABS(-20)
			// -- allow
			// -- else free up points first
			if (a.cost < 0)	
			{
				if (hPlayer.config.pointsCurrent >= Math.abs(a.cost)) {
					hPlayer.config.slotsSpent -= 1;
					hPlayer.config.pointsCurrent += a.cost;
					
					
					hPlayer.config.assignedAttributes.remove(attributeName);
					hPlayer.bNeedsSave = true;
					
					// -- update permEffects..
					hPlayer.removePermEffect(attributeName);
					
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				
				hPlayer.config.slotsSpent -= 1;
				hPlayer.config.pointsCurrent += a.cost;
				
				
				hPlayer.config.assignedAttributes.remove(attributeName);
				hPlayer.bNeedsSave = true;
				
				hPlayer.removePermEffect(attributeName);
				
				return true;
				
			}
			
			
		}
		else {
			return false;
		}
		
	}
	
	public static boolean bAddAttributeToPlayer(UUID uuid, String attributeName) {
		
		HPlayer hPlayer = plugin.hPlayers.get(uuid);
		if (hPlayer == null) {
			return false;
		}
		
		AttributeBase a = getAttributeInfo(attributeName);
		if (a == null) {
			return false;
		}
		
		
		// -- check if they want perms or not
		if (plugin.config.bUsePerms ==  true) {
			if (!hPlayer.getPlayer().hasPermission("hexattributes.type." + attributeName)) {
				//player.sendMessage(String.format(plugin.lang.noPermissionForAttribute, attributeColor));
				return false;
			}
		}
		
		if (hPlayer.config.assignedAttributes.contains(attributeName)) {
			return false;
		}
		else {
			
			
			// -- cost system..
			// -- perm based slots??
			if (hPlayer.config.slotsSpent < hPlayer.config.slotsMax) {
				
				// -- if our points left is more than the cost..
				if (hPlayer.config.pointsCurrent >= a.cost) {
					
					// -- we have the points, and slots remaining.
					// -- so lets reduce num points, and apply attributes to player
					hPlayer.config.slotsSpent += 1;
					hPlayer.config.pointsCurrent -= a.cost;
					
					hPlayer.config.assignedAttributes.add(attributeName);
					hPlayer.bNeedsSave = true;

					//hPlayer.sendMessage(String.format(plugin.lang.addedAttribute, attributeColor));

					// -- update permEffects..
					hPlayer.addPermEffects();
					
					return true;
					
				}
				else {
					return false;
				}
			}
			else {
				return false;
			}
		}
		
		
	}
	

}
