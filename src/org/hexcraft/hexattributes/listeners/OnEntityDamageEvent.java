package org.hexcraft.hexattributes.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import org.hexcraft.HexAttributes;
import org.hexcraft.hexattributes.HPlayer;
import org.hexcraft.hexattributes.api.HexAttributesAPI;
import org.hexcraft.hexattributes.types.Immunity;

public class OnEntityDamageEvent extends BaseListener {

	public OnEntityDamageEvent(HexAttributes _plugin) {
		super(_plugin);
		// TODO Auto-generated constructor stub
	}

	@EventHandler
	private void damageEvent(EntityDamageEvent event) {
		
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
		
		if (event.getEntity() instanceof Player) {
			
			Player player = (Player) event.getEntity();
            HPlayer h = plugin.hPlayers.get(player.getUniqueId());
			
            if (h != null ) {
            	
            	
            	
            	//player.sendMessage("damage type: " + event.getCause().name());
            	boolean bImmunecheck = false;
            	for(Immunity i : plugin.configAttributes.types.immunity) {
            		
            		if (i.immunityDamageCause != null)
    				{
            			//player.sendMessage("cause: " + i.immunityDamageCause);
            			if (h.config.assignedAttributes.contains(i.name)) {
            				
            				//player.sendMessage("we contain it!!");
            				
            				if (i.immunityDamageCause.contains(event.getCause().name())) {
            					//player.sendMessage("canceling damage event");
            					bImmunecheck = true;
            					break;
            				}
            				
        				}
            			
            			
    				}
            		
    				
				}
            	if (bImmunecheck) {
            		event.setCancelled(true);
            	}
            	else {
            		h.lastDamage = System.currentTimeMillis();
            	}
            	
            	
            }
            
		}
		
		
		
	}
	
	

}
