package org.hexcraft.hexattributes.types;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.hexcraft.HexAttributes;
import com.google.gson.annotations.SerializedName;
import org.hexcraft.hexattributes.HPlayer;

public class Passive {
	public transient HexAttributes plugin;
	
	// -- all
	@SerializedName("name") public String name;
	@SerializedName("info") public String info;
	@SerializedName("cost") public int cost;
	
	@SerializedName("condition") public String condition;
	
	@SerializedName("feed") public int feed;
	@SerializedName("starve") public int starve;
	@SerializedName("heal") public double heal;
	@SerializedName("harm") public double harm;
	
	public void ApplyPassive(Player player) {
		if (feed > 0){
			player.setFoodLevel(Math.min(player.getFoodLevel() + feed, 20));
		}
		if (starve > 0){
			player.setFoodLevel(Math.max(player.getFoodLevel() - starve, 0));
		}
		if (heal > 0){
			player.setHealth(Math.min(player.getHealth() + heal, player.getMaxHealth()));
		}
		if (harm > 0){
			// -- TODO: add checks for invincible flag (world guard) or godmode (essentials)..
			player.damage(harm);
		}
	}
	
	public void Execute(Player player, HPlayer hplayer) {
		
		if (condition.equals("Lava")) {
			if (player.getLocation().getBlock().getType().equals(Material.STATIONARY_LAVA)
					|| player.getLocation().getBlock().getType().equals(Material.LAVA)) {
				ApplyPassive(player);
			}
		}
		else if (condition.equals("Sunlight")) {
			
			if (player.getWorld().getBlockAt(player.getLocation()).getLightFromSky() >= 13 
    				&& player.getWorld().getTime() >= 0 
    				&& player.getWorld().getTime() <= 12500) {
				ApplyPassive(player);
            }
			
		}
		else if (condition.equals("Water")) {
			if (player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER)
					|| player.getLocation().getBlock().getType().equals(Material.WATER)) {
				ApplyPassive(player);
			}
		}
		else if (condition.equals("Light")) {
			if (player.getWorld().getBlockAt(player.getLocation()).getLightLevel() >= 13 ) {
				ApplyPassive(player);
            }
		}
		else if (condition.equals("Darkness")) {
			if (player.getWorld().getBlockAt(player.getLocation()).getLightLevel() <= 5 ) {
				ApplyPassive(player);
            }
		}
		else if (condition.equals("Meditation")) {
			
			if (hplayer.meditationCount >= 3) {
				ApplyPassive(player);
			}
			
		}
		else if (condition.equals("Always")) {
			ApplyPassive(player);
		}
		else if (condition.equals("Land")) {
			if (!player.getLocation().getBlock().getType().equals(Material.STATIONARY_WATER) || 
					!player.getLocation().getBlock().getType().equals(Material.WATER) || 
					!player.getLocation().getBlock().getType().equals(Material.LAVA) || 
					!player.getLocation().getBlock().getType().equals(Material.STATIONARY_LAVA)) {
				ApplyPassive(player);
			}
			
		}
		
	}

}
