package org.hexcraft.chest;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.hexcraft.HexAttributes;
import org.hexcraft.hexattributes.HPlayer;
import org.hexcraft.hexattributes.types.*;

public class ToughnessGUI extends GUI {

	public ToughnessGUI(Player player, HexAttributes _plugin, HPlayer _hplayer)
    {
        super(_plugin.lang.toughness + " Attributes", 54, _plugin); // Must be multiple of 9!
        
        if (plugin.b_touGui == false) {
        	plugin.b_touGui=true;
        	//plugin.log("Registering Events: " + "Toughness Attributes");
        	plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
		addItems(_hplayer);
        openGUI(_hplayer);
    }
	
	public void addItems(HPlayer _hplayer)
    {
		// -- Back Command
		newItem(Material.BARRIER, plugin.lang.back, 1, null, 49);
		
        // -- list out attributes
        int guiSlot = 0;
        for (Toughness a : plugin.configAttributes.types.toughness) {
        	
        	String info = null;
        	if (a.cost > 0) {
        		info = String.format(plugin.lang.tooltipInfoPositive, a.name, a.cost);
			}
			else
			{
				info = String.format(plugin.lang.tooltipInfoNegative, a.name, a.cost);
			}
        	
        	List<String> assAttrib = new ArrayList<String> ();
        	assAttrib.add(info);
        	assAttrib.add(String.format(plugin.lang.tooltipAttributeInfo, a.info));
        	assAttrib.add(plugin.lang.tooltipAdd);
		        	
        	// -- check if they want perms or not
			if (plugin.config.bUsePerms ==  true) {
				if (!_hplayer.getPlayer().hasPermission("hexattributes.type." + a.name)) {
					// -- check if we should hide the item or not
					if (plugin.config.bHideAttributesWithNoPerms == false) {
						// -- Display item, but show no perms in tooltip
						assAttrib.add(plugin.lang.notUnlocked);
						
						String[] temp = new String[assAttrib.size()];
						temp = assAttrib.toArray(temp);
						
						newItem(Material.PAPER, a.name, 1, temp, guiSlot++);
		        	}	
				}
				else {
					String[] temp = new String[assAttrib.size()];
					temp = assAttrib.toArray(temp);
					newItem(Material.PAPER, a.name, 1, temp, guiSlot++);
				}
			}
			else {
				String[] temp = new String[assAttrib.size()];
				temp = assAttrib.toArray(temp);
				newItem(Material.PAPER, a.name, 1, temp, guiSlot++);
			}
        }
    }
    
	public void createCommands(Player player)
    {
    	if (getClickedItem().equals(plugin.lang.back))
        {
            createCommand("attribute gui", player);
        }
        else {
        	// -- attempt add
        	createCommand("attribute a " + getClickedItem(), player);
        	createCommand("attribute gui", player);
        }

    }
	
}
