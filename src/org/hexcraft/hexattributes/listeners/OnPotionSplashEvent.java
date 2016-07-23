package org.hexcraft.hexattributes.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PotionSplashEvent;

import org.hexcraft.HexAttributes;
import org.hexcraft.hexattributes.HPlayer;
import org.hexcraft.hexattributes.api.HexAttributesAPI;
import org.hexcraft.hexattributes.types.Immunity;

public class OnPotionSplashEvent extends BaseListener {

	public OnPotionSplashEvent(HexAttributes _plugin) {
		super(_plugin);
	}
	
	@EventHandler
	public void splashEvent(PotionSplashEvent event) {
		
		// -- World Check
		if (!HexAttributesAPI.bValidWorld(event.getEntity().getWorld())) {
			return;
		}
		/*
		if (plugin.config.bUseWorldWhiteList) {
			if (plugin.config.worldWhitelist != null) {
				if (!plugin.config.worldWhitelist.contains(event.getEntity().getWorld().getName())) {
					return;
				}
			}
			else {
				plugin.log("Unable to use World White List: null");
			}
		}
		*/
		for (LivingEntity entity : event.getAffectedEntities()) {
			
			if (entity instanceof Player) {
				Player player = (Player) entity;
				HPlayer h = plugin.hPlayers.get(player.getUniqueId());
				
				for(Immunity i : plugin.configAttributes.types.immunity) {
            		
            		if (i.immunityPotionTypes != null)
    				{
            			
            			if (h.config.assignedAttributes.contains(i.name)) {
            				
            				if (i.immunityPotionTypes.contains(event.getPotion().getEffects())) {
            					event.setCancelled(true);
            				}
            				
        				}
            			
            			
    				}
            		
    				
				}
				
			}
			
		}
		
	}

}
