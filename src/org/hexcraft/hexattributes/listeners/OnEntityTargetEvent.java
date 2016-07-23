package org.hexcraft.hexattributes.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.hexcraft.HexAttributes;
import org.hexcraft.hexattributes.HPlayer;
import org.hexcraft.hexattributes.api.HexAttributesAPI;
import org.hexcraft.hexattributes.types.Truce;
import org.hexcraft.util.Cooldown;
import org.hexcraft.util.PlayerCooldown;

public class OnEntityTargetEvent extends BaseListener {

	public OnEntityTargetEvent(HexAttributes _plugin) {
		super(_plugin);
		
	}
	
	@EventHandler
	public void spiderFix(EntityTargetEvent event) {
		
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
		
		if (event.getTarget() instanceof Player) {
			
			Player player = (Player) event.getTarget();
            HPlayer h = plugin.hPlayers.get(player.getUniqueId());
            if (h != null ) {
            	
            	//player.sendMessage(event.getEntityType().name());
            	
            	boolean bFound = false;
            	for (Truce t : plugin.configAttributes.types.truce) {
            		if (h.config.assignedAttributes.contains(t.name)) {
            			for (String n : t.entities) {
                			if (event.getEntityType().name().equals(n)) {
                				SetTruceSpider(player, t.name, event);
                				bFound = true;
                				break;
                			}
                		}
                		if (bFound) {
                			break;
                		}
            		}
            	}
            	
            }
		}
	}
	
	@EventHandler
	public void EntityTargetEvent(EntityTargetLivingEntityEvent event) {
		
		// -- World Check
		if (!HexAttributesAPI.bValidWorld(event.getEntity().getWorld())) {
			return;
		}
		/*
		if (!plugin.config.worldWhitelist.contains(event.getEntity().getWorld().getName())) {
			return;
		}
		*/
		if (event.getTarget() instanceof Player) {
			Player player = (Player) event.getTarget();
            HPlayer h = plugin.hPlayers.get(player.getUniqueId());
            if (h != null ) {
            	
            	//player.sendMessage(event.getEntityType().name());
            	
            	boolean bFound = false;
            	for (Truce t : plugin.configAttributes.types.truce) {
            		if (h.config.assignedAttributes.contains(t.name)) {
            			for (String n : t.entities) {
                			if (event.getEntityType().name().equals(n)) {
                				SetTruce(player, t.name, event);
                				bFound = true;
                				break;
                			}
                		}
                		if (bFound) {
                			break;
                		}
            		}
            	}
            	
            }
		}
	}
	
	private void SetTruceSpider(Player player, String truceType, EntityTargetEvent event)
	{
		HPlayer h = plugin.hPlayers.get(player.getUniqueId());
		
		if (h.config.assignedAttributes.contains(truceType)) {
			PlayerCooldown pc = Cooldown.getCooldown(truceType, player.getName());
			if(pc==null) {
				// -- truce has not been broken.
				event.setCancelled(true);
			}
			else{
				if(pc.isOver()) {
					// -- no need for reset of timer
					// pc.reset();
					
					// -- truce was broken, but now has reset
					event.setCancelled(true);
				}
			}
		}
	}
	
	private void SetTruce(Player player, String truceType, EntityTargetLivingEntityEvent event)
	{
		HPlayer h = plugin.hPlayers.get(player.getUniqueId());
		
		if (h.config.assignedAttributes.contains(truceType)) {
			PlayerCooldown pc = Cooldown.getCooldown(truceType, player.getName());
			if(pc==null) {
				// -- truce has not been broken.
				event.setCancelled(true);
			}
			else{
				if(pc.isOver()) {
					// -- no need for reset of timer
					// pc.reset();
					
					// -- truce was broken, but now has reset
					event.setCancelled(true);
				}
			}
		}
	}
	
	

}
