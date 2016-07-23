package org.hexcraft.hexattributes.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.hexcraft.HexAttributes;

import org.hexcraft.hexattributes.HPlayer;
import org.hexcraft.hexattributes.api.HexAttributesAPI;

public class OnPlayerJoin extends BaseListener {
	
	public OnPlayerJoin(HexAttributes _plugin) {
		super(_plugin);
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		
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
		if (plugin.hPlayers.get(event.getPlayer().getUniqueId()) == null )
		{
			plugin.hPlayers.put(event.getPlayer().getUniqueId(), new HPlayer(plugin, event.getPlayer().getUniqueId()));
		}
		else
		{
			// -- unmark removal.
			HPlayer hPlayer = plugin.hPlayers.get(event.getPlayer().getUniqueId());
			hPlayer.bRemoval = false;
		}
	}
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent event) {
		
		// -- still remove if we need to
		if (plugin.hPlayers.get(event.getPlayer().getUniqueId()) != null )
		{
			// -- instead of instant remove, lets target them for removal later..
			HPlayer hPlayer = plugin.hPlayers.get(event.getPlayer().getUniqueId());
			hPlayer.bRemoval = true;
			hPlayer.lastLogin = System.currentTimeMillis();
		}	
		
	}

}
