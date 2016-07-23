package org.hexcraft.chest;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.hexcraft.HexAttributes;
import org.hexcraft.chest.GUI;
import org.hexcraft.hexattributes.HPlayer;
import org.hexcraft.hexattributes.api.AttributeBase;

public class Overview extends GUI {
	
	public Overview(Player player, HexAttributes _plugin, HPlayer _hplayer)
    {
        super("Attributes", 54, _plugin); // Must be multiple of 9!
        
        if (plugin.b_attributeOverview == false) {
        	plugin.b_attributeOverview=true;
        	//plugin.log("Registering Events: " + "Attributes");
        	plugin.getServer().getPluginManager().registerEvents(this, plugin);
        }
		addItems(_hplayer);
        openGUI(_hplayer);
    }
    public void addItems(HPlayer hplayer)
    {
    	// -- lets build some stuff
        String attInfo[] = new String[] {
        	//ChatColor.WHITE + "Sockets: " + ChatColor.AQUA + Math.abs(hplayer.config.slotsSpent) + ChatColor.WHITE + "/" + ChatColor.AQUA + hplayer.config.slotsMax,
        	//ChatColor.WHITE + "Shards: " + ChatColor.AQUA + hplayer.config.pointsCurrent + ChatColor.WHITE + "/" + ChatColor.AQUA + hplayer.config.pointsBase
        	String.format(plugin.lang.sockets, Math.abs(hplayer.config.slotsSpent), hplayer.config.slotsMax),
        	String.format(plugin.lang.shards, hplayer.config.pointsCurrent, hplayer.config.pointsBase)
        };

        newItem(Material.PAPER, plugin.lang.yourAttributes, 1, attInfo, 53);
        newItem(Material.BARRIER, plugin.lang.close, 1, null, 49);
        
        // -- create attribute categories.
        int guiSlot = 0;
        newItem(Material.BOOK, plugin.lang.deficiency, 1, new String[] { plugin.lang.category }, guiSlot++);
        newItem(Material.COOKED_BEEF, plugin.lang.food, 1, new String[] { plugin.lang.category }, guiSlot++);
        newItem(Material.GOLDEN_APPLE, plugin.lang.immunity, 1, new String[] { plugin.lang.category }, guiSlot++);
        newItem(Material.CHAINMAIL_CHESTPLATE, plugin.lang.mastery, 1, new String[] { plugin.lang.category }, guiSlot++);
        newItem(Material.ENCHANTED_BOOK, plugin.lang.passive, 1, new String[] { plugin.lang.category }, guiSlot++);
        newItem(Material.POTION, plugin.lang.permeffect, 1, new String[] { plugin.lang.category }, guiSlot++);
        newItem(Material.NETHER_STAR, plugin.lang.reflect, 1, new String[] { plugin.lang.category }, guiSlot++);
        newItem(Material.DIAMOND_SWORD, plugin.lang.strike, 1, new String[] { plugin.lang.category }, guiSlot++);
        newItem(Material.DIAMOND_CHESTPLATE, plugin.lang.toughness, 1, new String[] { plugin.lang.category }, guiSlot++);
        newItem(Material.MONSTER_EGG, plugin.lang.truce, 1, new String[] { plugin.lang.category }, guiSlot++);
        newItem(Material.PAPER, plugin.lang.weakness, 1, new String[] { plugin.lang.category }, guiSlot++);
        
        
        /*
        for(int x=0; x<hplayer.config.assignedAttributes.size(); x=x+1) {
        	
        }
        */
        
        guiSlot = 18;
        // -- Build GUI for each item..
        for (AttributeBase b : plugin.configAttributes.types.register) {
			if (hplayer.config.assignedAttributes.contains(b.name)) {
				
				// -- lets build some stuff
		        
				String assAttrib[] = null;
				//newItem(Material.NAME_TAG, ChatColor.WHITE + "---- " + ChatColor.GOLD + "Your Attributes" + ChatColor.WHITE + " ----", 1, attInfo, guiSlot + x);
				
				if (b.cost > 0) {
					assAttrib = new String[] {
							String.format(plugin.lang.tooltipInfoPositive, b.name, b.cost),
							String.format(plugin.lang.tooltipAttributeInfo, b.info),
							plugin.lang.tooltipRemove
							//ChatColor.GREEN + b.name + ChatColor.GRAY + "(" + ChatColor.WHITE + "+" + b.cost + ChatColor.GRAY + ")",
				        	//ChatColor.GRAY + b.info,
				        	//ChatColor.RED + "Right-Click To Remove"
				    };
					
					newItem(Material.NAME_TAG, b.name, 1, assAttrib, guiSlot++);
				}
				else {
					assAttrib = new String[] {
							//ChatColor.RED + b.name + ChatColor.GRAY + "(" + ChatColor.WHITE + "" + b.cost + ChatColor.GRAY + ")",
				        	//ChatColor.GRAY + b.info,
				        	//ChatColor.RED + "Right-Click To Remove"
				        	String.format(plugin.lang.tooltipInfoNegative, b.name, b.cost),
				        	String.format(plugin.lang.tooltipAttributeInfo, b.info),
				        	plugin.lang.tooltipRemove
				    };
					newItem(Material.NAME_TAG, b.name, 1, assAttrib, guiSlot++);
				}
				
			}
		}
        
        
    }
    
    public void createCommands(Player player) {
        if (getClickedItem().equals("Go back to Spawn"))
        {
            createCommand("spawn", player);
        }
        else if (getRightClick() == false) {
        	createCommand("attribute gui " + getClickedItem(), player);
        }
        if (getRightClick()) {
        	createCommand("attribute r " + getClickedItem(), player);
        	createCommand("attribute gui", player);
        }
        
    }
    
    @Override
    public void openGUI(HPlayer _hplayer) {
    	
    	// -- we want to clear the items, and repopulate based on changed data...
    	super.clearGUI();
    	addItems(_hplayer);
    	super.openGUI(_hplayer);
    	
    }
}
