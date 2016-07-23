package org.hexcraft.chest;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.hexcraft.HexAttributes;
import org.hexcraft.hexattributes.HPlayer;


public abstract class GUI implements Listener
{

    private Inventory inventory;
    private String guiName = "Menu";
    private int guiSlots;
    private String getClickedItem;
    private boolean isRightClick;
    public HexAttributes plugin;
    //private HPlayer hplayer;
    
    private int executeOnce;
    
    /**
    * Creates a new GUI display.
    *
    * @param Player to show the GUI.
    * @param The GUI display name.
    * @param The GUI slots. Must be multiple of 9!
    */
    public GUI(String guiName, int guiSlots, HexAttributes _plugin)
    {
        this.guiName = guiName;
        this.guiSlots = guiSlots;
        this.plugin = _plugin;
        //this.hplayer = _hplayer;
        this.executeOnce = 0;
        
        //plugin.log("Creating new GUI Instance: " + guiName);
        
        if (inventory == null) {
        	//plugin.log("Creating new Inventory Instance: " + guiName);
        	inventory = plugin.getServer().createInventory(null, this.guiSlots, this.guiName);
        }
    }
    
    /**
    * Adds items to the GUI.
    */
    public abstract void addItems(HPlayer _hplayer);
       /**
    * Creates item commands when clicked.
     * @param player 
    */
    public abstract void createCommands(Player player);
       /**
    * Creates a GUI item.
    *
    * @param The material type.
    * @param The item display name.
    * @param The item amount.
    * @param The item lore.
    * @param The GUI slot to be placed.
    */

    public void newItem(Material type, String itemDisplayName, int amount, String[] itemLore, int guiSlot)
    {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        item.setAmount(amount);
        meta.setDisplayName(itemDisplayName);

        if (itemLore != null) {
        	meta.setLore(Arrays.asList(itemLore));
        }
        

        item.setItemMeta(meta);

        this.inventory.setItem(guiSlot, item);
    }

    /**
    * Creates a GUI command.
    *
    * @param command
    */
    protected void createCommand(String command, Player player)
    {
    	if (executeOnce != 0){
    		return;
    	}
    	//executeOnce++;
    	
        Bukkit.dispatchCommand(player, command);
        player.playSound(player.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
    }
    
    // -- FIX stuff
    public void destroy() {
    	HandlerList.unregisterAll(this);

        this.guiName = null;
        this.guiSlots = 0;
        this.plugin = null;
        //this.hplayer = null;
        this.executeOnce = 0;

	}

    @EventHandler
    public void onInventoryItemClick(final InventoryClickEvent e)
    {
        // Checking if the inventory is the correct one.
        if (!(e.getInventory().getName().equals(this.guiName)))
        {
            return;
        }
        // Checking if the clicked item exists.
        if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta() || !e.getCurrentItem().getItemMeta().hasDisplayName())
        {
            return;
        }
        this.getClickedItem = e.getCurrentItem().getItemMeta().getDisplayName();
        final Player player = (Player) e.getWhoClicked();
        HPlayer hplayer = plugin.hPlayers.get(player.getUniqueId());
        this.isRightClick = e.isRightClick();

        e.setCancelled(true);
        closeGUI(hplayer);

        // Waiting for the event to get cancelled.
        if (e.isCancelled() == true)
        {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
            {
                public void run()
                {
                    try
                    {
                        createCommands(player);
                        //destroy();
                    }
                    catch (Exception cmdException)
                    {
                        cmdException.printStackTrace();
                    }
                }
            }, 1);
        }
        
        
        
        
        
    }

    /**
    * Gets the GUI name.
    *
    * @return
    */
    public String getGuiName()
    {
        return this.guiName;
    }
    
    /**
    * Opens the GUI.
    */
    public void openGUI(HPlayer hplayer)
    {
    	//plugin.log("Opening GUI for: " + hplayer.getPlayer().getDisplayName());
    	if (inventory == null)
    	{
    		//plugin.log("Inventory is NULL, re-creating: " + hplayer.getPlayer().getDisplayName());
    		inventory = plugin.getServer().createInventory(null, this.guiSlots, this.guiName);
    		addItems(hplayer);
    		hplayer.getPlayer().openInventory(inventory);
    	}
    	else
    	{
    		//plugin.log("Inventory Exists, opening for: " + hplayer.getPlayer().getDisplayName());
    		hplayer.getPlayer().openInventory(inventory);
    	}
    }

    /**
    * Closes the GUI.
    */
    public void closeGUI(HPlayer hplayer)
    {
        hplayer.getPlayer().closeInventory();
        hplayer = null;
    }
    /**
    * Gets the GUI.
    *
    * @return
    */
    public Inventory getGUI()
    {
        return this.inventory;
    }

    /**
    * Gets the clicked GUI item.
    *
    * @return
    */
    public String getClickedItem()
    {
        return this.getClickedItem;
    }
    
    /**
     * Gets right click boolean.
     *
     * @return
     */
     public boolean getRightClick()
     {
         return this.isRightClick;
     }
     
     public void clearGUI() {
    	 this.inventory.clear();
     }
     
}

