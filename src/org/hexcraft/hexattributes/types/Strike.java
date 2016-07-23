package org.hexcraft.hexattributes.types;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.hexcraft.HexAttributes;
import com.google.gson.annotations.SerializedName;
import org.hexcraft.hexattributes.HPlayer;

public class Strike {
	public transient HexAttributes plugin;
	
	// -- all
	@SerializedName("name") public String name;
	@SerializedName("info") public String info;

	@SerializedName("cost") public int cost;
	
	// -- support all types, this could lead to pure customization
	@SerializedName("fireTicks") public int fireTicks;
	@SerializedName("harmDamage") public double harmDamage;
	@SerializedName("starveAmount") public int starveAmount;
	@SerializedName("healAmount") public double healAmount;
	@SerializedName("foodAmount") public int foodAmount;
	
	// -- Types..
	@SerializedName("potionEffect") public String potionEffect;
	@SerializedName("potionDuration") public int potionDuration;
	@SerializedName("potionApmplifier") public int potionApmplifier;
	
	// -- ONLY reason is for a quick check for immunity......
	@SerializedName("immunity") public String immunity;	
	
	/*
	public Strike(String name, String info, int cost, int fireTicks, double harmDamage, int starveAmount, double healAmount, int foodAmount, String potionEffect, int potionDuration, int potionApmplifier, String immunity) {
		this.name = name;
		this.info = info;
		this.cost = cost;
		this.fireTicks = fireTicks;
		this.harmDamage = harmDamage;
		this.starveAmount = starveAmount;
		this.healAmount = healAmount;
		this.foodAmount = foodAmount;
		this.potionEffect = potionEffect;
		this.potionDuration = potionDuration;
		this.potionApmplifier = potionApmplifier;
		
		this.immunity = immunity;
	}
	*/

	// TODO - add the supported types..
	public void Execute(Player attacker, LivingEntity target, EntityDamageByEntityEvent event) {
		
		// -- messages
		if (event.getEntity() instanceof Player) {
			Player targetPlayer = (Player) event.getEntity();
			//attacker.sendMessage("" + ChatColor.AQUA + targetPlayer.getName() + ChatColor.GRAY + " has been affected by " + ChatColor.AQUA + name);
			//targetPlayer.sendMessage("" + ChatColor.AQUA + attacker.getName() + ChatColor.GRAY + " has afflicted you with " + ChatColor.AQUA + name);
			
			attacker.sendMessage(String.format(plugin.lang.defendingAffectedBy, targetPlayer.getName(), name));
			targetPlayer.sendMessage(String.format(plugin.lang.afflictedYouWith, attacker.getName(), name));
			
		}
		else
		{
			//attacker.sendMessage("" + ChatColor.AQUA + target.getType().name() + ChatColor.GRAY + " has been affected by " + ChatColor.AQUA + name);
			attacker.sendMessage(String.format(plugin.lang.defendingAffectedBy, target.getType().name(), name));
		}
		
		// -- allow for an "ALL" strike..
		// -- basically allow for any config setting..
		if (fireTicks > 0) {
			target.setFireTicks(fireTicks);
		}
		if (harmDamage > 0) {
			HPlayer hDefender = plugin.hPlayers.get(attacker.getUniqueId());
			// -- adding damage calls the event, which calls the even..
			// -- recursive failure
			target.damage(harmDamage, attacker);
			hDefender.bDenyDamageEvent = true;
		}
		if (starveAmount > 0) {
			if (event.getEntity() instanceof Player) {
				Player targetPlayer = (Player) event.getEntity();
				//targetPlayer.setFoodLevel(targetPlayer.getFoodLevel() - starveAmount);
				targetPlayer.setFoodLevel(Math.max(targetPlayer.getFoodLevel() - starveAmount, 0));
			}
		}
		if (healAmount > 0) {
			// -- instead of target, give player (attacker) food!
			attacker.setHealth(Math.min(attacker.getHealth() + healAmount, 20.0d));
		}
		if (foodAmount > 0) {
			// -- instead of target, give player (attacker) food!
			attacker.setFoodLevel(Math.min(attacker.getFoodLevel() + foodAmount, 20));
		}
		if (potionEffect != null) {
			target.addPotionEffect(new PotionEffect(PotionEffectType.getByName(potionEffect), potionDuration, potionApmplifier));
			
		}
	}
	
}
