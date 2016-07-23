package org.hexcraft.hexattributes.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.hexcraft.HexAttributes;
import org.hexcraft.hexattributes.HPlayer;
import org.hexcraft.hexattributes.api.HexAttributesAPI;
import org.hexcraft.hexattributes.types.Food;

public class OnPlayerItemConsumeEvent extends BaseListener {
	
	public OnPlayerItemConsumeEvent(HexAttributes _plugin) {
		super(_plugin);
	}
	

	@EventHandler
	private void playerEat(PlayerItemConsumeEvent event) {
		
		// -- World Check
		if (!HexAttributesAPI.bValidWorld(event.getPlayer().getWorld())) {
			return;
		}
		/*
		if (plugin.config.bUseWorldWhiteList) {
			if (plugin.config.worldWhitelist != null) {
				if (!plugin.config.worldWhitelist.contains(event.getPlayer().getWorld().getName())) {
					return;
				}
			}
			else {
				plugin.log("Unable to use World White List: null");
			}
		}
		*/
		
		final Player player = event.getPlayer();
		
		final HPlayer hplayer = plugin.hPlayers.get(event.getPlayer().getUniqueId());
		
		
		//event.getPlayer().sendMessage(event.getItem().getData().toString());
		// -- MILK for permanent effect..
		if (event.getItem().getType().name().equals("MILK_BUCKET")) {
			// -- just reapply their effects..??
			
			event.getPlayer().setFoodLevel(Math.min(event.getPlayer().getFoodLevel() + 3, 20));
			// -- and FUCK YOU GLASS BOTTLE BITCH
			plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
                public void run() {
                	hplayer.addPermEffects();
		        }
            }, 5L);
			
		}
		
		
		// -- special case GLASS_BOTTLE
		if (event.getItem().getType().name().equals("POTION")) {
			if (hplayer.config.assignedAttributes.contains("WaterDrinker")) {
				
				// -- FUCK YOU IDs...
				//if (event.getItem().getData().getData() == 0) {
				if (event.getItem().getData().toString().equalsIgnoreCase("POTION(0)")) {
					
					if (event.getPlayer().getFoodLevel() < 20) {
						if (event.getPlayer().getFoodLevel() + 3 > 20) {
							int blah = Math.abs(20 - event.getPlayer().getFoodLevel() + 3);
							event.getPlayer().setFoodLevel(Math.min(20, 20));
							event.getPlayer().setSaturation(blah);
						}
						else {
							event.getPlayer().setFoodLevel(Math.min(event.getPlayer().getFoodLevel() + 3, 20));
						}
						
						// -- and FUCK YOU GLASS BOTTLE BITCH
						plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, new Runnable() {
			                public void run() {
			                	player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
			                	//player.setItemInHand(new ItemStack(Material.AIR));
					        }
			            }, 1L);
					}
					
					
				}
			}
		}
		else
		{
			boolean bFound = false;
			boolean bFoodType = false;
			// -- for stuff..
			for(Food food : plugin.configAttributes.types.food) {
				if (hplayer.config.assignedAttributes.contains(food.name)) {
					//if (strikeChance < 0.05)
					bFoodType = true;
					//event.getPlayer().sendMessage("checking food type with: " + food.name);
					
					
					if (food.foodTypes.contains(event.getItem().getType().name())) {
						
						//event.getPlayer().sendMessage(ChatColor.GRAY + "You are disguested by the taste of: " + ChatColor.AQUA + event.getItem().getType().name() + ChatColor.GRAY + " reason: " + ChatColor.AQUA + food.name);
						event.getPlayer().sendMessage(String.format(plugin.lang.foodRepulse, event.getItem().getType().name(), food.name));
						
						bFound = true;
						break;
					}
					if (bFound) {
						
						break;
					}
					
					
					
				}
			}
			// -- no food type, allow all eating..
			if (bFoodType) {
				if (bFound) {
					event.setCancelled(true);
				}
			}
		}
		
	}

}
