package org.hexcraft.hexattributes.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.hexcraft.HexAttributes;
import org.hexcraft.hexattributes.HPlayer;
import org.hexcraft.hexattributes.api.HexAttributesAPI;

public class OnPlayerChangeWorld extends BaseListener {

	public OnPlayerChangeWorld(HexAttributes _plugin) {
		super(_plugin);
		// TODO Auto-generated constructor stub
	}
	
	@EventHandler
	private void onChangeWorld(PlayerChangedWorldEvent event) {
		
		// -- World Check
		if (!plugin.config.bUseWorldWhiteList) {
			return;
		}
		if (plugin.config.worldWhitelist == null) {
			return;
		}
		if (!plugin.config.worldWhitelist.contains(event.getPlayer().getWorld().getName())) {
			
			// -- check if they are moving from a supported world
			if (plugin.config.worldWhitelist.contains(event.getFrom().getName())) {
				// -- moving from a supported to non supported world
				// -- remove any perms left active.
				
				Player player = event.getPlayer();				
				HPlayer hplayer = plugin.hPlayers.get(player.getUniqueId());
				
				for (String s : hplayer.config.assignedAttributes) {
					// -- update permEffects..
					hplayer.removePermEffect(s);
				}
				
				
				// -- now remove them from hplayer
				hplayer.bRemoval = true;
				hplayer.lastLogin = System.currentTimeMillis();
			}
			
			
			return;
		}
		else
		{
			// -- changing world into supported attribute world
			if (plugin.hPlayers.get(event.getPlayer().getUniqueId()) == null )
			{
				plugin.hPlayers.put(event.getPlayer().getUniqueId(), new HPlayer(plugin, event.getPlayer().getUniqueId()));
			}
			else
			{
				// -- unmark removal.
				HPlayer hPlayer = plugin.hPlayers.get(event.getPlayer().getUniqueId());
				hPlayer.bRemoval = false;
				
				// -- re-apply perms
				hPlayer.addPermEffects();
			}
			
		}
		
		
		
	}

}
