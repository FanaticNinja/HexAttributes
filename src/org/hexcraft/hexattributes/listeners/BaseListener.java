package org.hexcraft.hexattributes.listeners;

import org.bukkit.event.Listener;
import org.hexcraft.HexAttributes;

//-- I don't feel like typing this over and over and over for different classes that implement listener..
//-- so, we will just extend the base class.
public class BaseListener implements Listener {

	public HexAttributes plugin;
	
	public BaseListener(HexAttributes _plugin)
	{
		this.plugin = _plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
}
