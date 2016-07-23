package org.hexcraft.hexattributes.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.hexcraft.HexAttributes;
import org.hexcraft.hexattributes.HPlayer;
import org.hexcraft.hexattributes.api.HexAttributesAPI;
import org.hexcraft.hexattributes.types.Reflect;
import org.hexcraft.hexattributes.types.Strike;
import org.hexcraft.hexattributes.types.Truce;
import org.hexcraft.util.Cooldown;

public class OnEntityDamageByEntityEvent extends BaseListener {
	
	public OnEntityDamageByEntityEvent(HexAttributes _plugin) {
		super(_plugin);
	}
	
	private boolean bHasStrikeImmunity(Player p, Strike strike, LivingEntity target) {
		
		if (target instanceof Player) {
			Player tp = (Player) target;
			HPlayer targetPlayer = plugin.hPlayers.get(tp.getUniqueId());
			
			if (targetPlayer != null)
			{
				if (targetPlayer.config.assignedAttributes.contains(strike.immunity)) {
					//p.sendMessage("Target has " + strike.immunity + " Attribute.");
					p.sendMessage(String.format(plugin.lang.targetStrikeImmunity, strike.immunity));
					
					
					//p.sendMessage("" + ChatColor.AQUA + tp.getName() + ChatColor.GRAY + " is immune to " + ChatColor.AQUA + strike.name + ChatColor.GRAY + " reason: " + ChatColor.AQUA + strike.immunity);
					p.sendMessage(String.format(plugin.lang.strikeImmunityReason, tp.getName(), strike.name, strike.immunity));
					//tp.sendMessage("" + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " failed " + ChatColor.AQUA + strike.name + ChatColor.GRAY + " reason: " + ChatColor.AQUA + strike.immunity);
					tp.sendMessage(String.format(plugin.lang.notifyStrikeImmunity, p.getName(), strike.name, strike.immunity));
					
					return true;
				}
			}
		}
		
		// -- not all code paths return a value..
		return false;
	}
	
	private boolean bHasReflectImmunity(Player p, Reflect strike, LivingEntity target) {
		
		if (target instanceof Player) {
			Player tp = (Player) target;
			HPlayer targetPlayer = plugin.hPlayers.get(tp.getUniqueId());
			
			if (targetPlayer != null)
			{
				if (targetPlayer.config.assignedAttributes.contains(strike.immunity)) {
					//p.sendMessage("Target has " + strike.immunity + " Attribute.");
					p.sendMessage(String.format(plugin.lang.targetStrikeImmunity, strike.immunity));
					
					//p.sendMessage("" + ChatColor.AQUA + tp.getName() + ChatColor.GRAY + " is immune to " + ChatColor.AQUA + strike.name + ChatColor.GRAY + " reason: " + ChatColor.AQUA + strike.immunity);
					//tp.sendMessage("" + ChatColor.AQUA + p.getName() + ChatColor.GRAY + " failed " + ChatColor.AQUA + strike.name + ChatColor.GRAY + " reason: " + ChatColor.AQUA + strike.immunity);
					
					p.sendMessage(String.format(plugin.lang.strikeImmunityReason, tp.getName(), strike.name, strike.immunity));
					tp.sendMessage(String.format(plugin.lang.notifyStrikeImmunity, p.getName(), strike.name, strike.immunity));
					
					
					return true;
				}
			}
		}
		
		// -- not all code paths return a value..
		return false;
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		
		// -- World Check
		if (!HexAttributesAPI.bValidWorld(event.getEntity().getWorld())) {
			return;
		}
		
		// -- player specific stuff...
		if (event.getDamager() instanceof Player) {
			
            Entity entity = event.getEntity();
            Player player = (Player) event.getDamager();

            HPlayer h = plugin.hPlayers.get(player.getUniqueId());
            if (h != null ) {
            	
            	if (h.bDenyDamageEvent == true) {
            		h.bDenyDamageEvent = false;
    				event.setDamage(2.0d);
    			}
            	else
            	{
            		
            		// -- Strike and Reflect!
                	// -- 5% melee chance, 33% ranged
                	// -- make sure its a player or some living thing.. i guess..
                	if (entity instanceof LivingEntity) {
                		
                		LivingEntity target = (LivingEntity) entity;
                		
                		for(Strike strike : plugin.configAttributes.types.strike) {
            				if (h.config.assignedAttributes.contains(strike.name)) {
            					
            					// -- instead of being 5% attack change
            					// with the new mechanics, attacks cannot spam, with full damage
            					// we should modify the strike chance based on strikeChanceModifier
            					
            					//if (h.strikeChance() < 0.05){
            					if (h.randomStrikeChance() > (1 - plugin.config.meleeStrikeChance)){
            						
            						if (!bHasStrikeImmunity(player, strike, target)){
        								strike.Execute(player, target, event);	
                        			}
            					}
            				}
        				}
                		
                	}
                	
        			// -- a simple check for NPE
                	// -- weapon checks for mastery, deficiency, etc..
                	// -- reserve base damage!!!!!
                	double baseDamage = event.getDamage();
                	double attackMod = 0.0;
                	double deffendMod = 0.0;
                	
                	ItemStack mainHand = player.getInventory().getItemInMainHand();
                	if (mainHand != null) {
                		
                		if (plugin.config.Axes.contains(mainHand.getType()))
                		{
                    		if (h.config.assignedAttributes.contains("AxeMastery"))
            				{
                    			attackMod += (baseDamage * 0.25);
                    		}
            				if (h.config.assignedAttributes.contains("AxeDeficiency"))
            				{
            					attackMod += (baseDamage * -0.25);
            				}
            				// -- check if they have Toughness or Weakness
                    		if (event.getEntity() instanceof Player) {
                    			
                    			Player defplayer = (Player) event.getEntity();
                    			HPlayer hDefender = plugin.hPlayers.get(defplayer.getUniqueId());
                    			
                                if (hDefender != null ) {
                                	
                                	// -- melee toughness/weakness
                            		if (hDefender.config.assignedAttributes.contains("AxeToughness"))
                    				{
                            			deffendMod += (baseDamage * -0.25);
                    				}
                            		if (hDefender.config.assignedAttributes.contains("AxeWeakness"))
                    				{
                            			deffendMod += (baseDamage * 0.25);
                    				}
                            		
                                }
                    		}
                        }
                		else if (plugin.config.Swords.contains(mainHand.getType())) {
                            
                			// -- check for player
                    		if (h.config.assignedAttributes.contains("SwordMastery"))
            				{
                    			attackMod += (baseDamage * 0.25);
            				}
            				if (h.config.assignedAttributes.contains("SwordDeficiency"))
            				{
            					attackMod += (baseDamage * -0.25);
            				}
            				// -- check if they have Toughness or Weakness
                    		if (event.getEntity() instanceof Player) {
                    			
                    			Player defplayer = (Player) event.getEntity();
                    			HPlayer hDefender = plugin.hPlayers.get(defplayer.getUniqueId());
                    			
                                if (hDefender != null ) {
                                	
                                	// -- melee toughness/weakness
                            		if (hDefender.config.assignedAttributes.contains("SwordToughness"))
                    				{
                            			deffendMod += (baseDamage * -0.25);
                    				}
                            		if (hDefender.config.assignedAttributes.contains("SwordWeakness"))
                    				{
                            			deffendMod += (baseDamage * 0.25);
                    				}                            		
                                }
                    		}
                        }
                		else if (mainHand.getType().equals(Material.AIR)) {
                			
                			
                			if (h.config.assignedAttributes.contains("UnarmedMastery"))
            				{
                    			attackMod += (baseDamage * 0.25);
                    		}
            				if (h.config.assignedAttributes.contains("UnarmedDeficiency"))
            				{
            					attackMod += (baseDamage * -0.25);
            				}
            				// -- check if they have Toughness or Weakness
                    		if (event.getEntity() instanceof Player) {
                    			
                    			
                    			Player defplayer = (Player) event.getEntity();
                    			HPlayer hDefender = plugin.hPlayers.get(defplayer.getUniqueId());
                    			
                                if (hDefender != null ) {
                                	
                                	
                                	// -- melee toughness/weakness
                            		if (hDefender.config.assignedAttributes.contains("UnarmedToughness"))
                    				{
                            			deffendMod += (baseDamage * -0.25);
                    				}
                            		if (hDefender.config.assignedAttributes.contains("UnarmedWeakness"))
                    				{
                            			deffendMod += (baseDamage * 0.25);
                    				}
                            		
                                }
                    		}

                		}
                		
                    }
                	
                	// -- angelic and demonic...
                	//double angelicDamage = 0.0;
                	if (event.getEntity() instanceof Player) {
                		
                		// -- if defending player is demonic
                		Player defplayer = (Player) event.getEntity();
            			HPlayer hDefender = plugin.hPlayers.get(defplayer.getUniqueId());
            			
                        if (hDefender != null ) {
                        	
                        	// -- melee toughness/weakness
                    		if (hDefender.config.assignedAttributes.contains("Demonic"))
            				{
                    			// -- if attacking player is angelic
                    			if (h.config.assignedAttributes.contains("Angelic"))
                				{
                    				// -- do MORE damage
                    				attackMod += (baseDamage * 0.5);
                        		}
            				}
                    		// -- melee toughness/weakness
                    		if (hDefender.config.assignedAttributes.contains("Angelic"))
            				{
                    			// -- if attacking player is angelic
                    			if (h.config.assignedAttributes.contains("Demonic"))
                				{
                    				// -- do MORE damage
                    				//attackMod += (baseDamage * 0.5);
                    				deffendMod += (baseDamage * -0.5);
                        		}
            				}
                        }
                	}
                	
                	// -- add the damages up example: (10 + 2.5 + -2.5)
            		event.setDamage(baseDamage + attackMod + deffendMod);
            		
            		// -- better reflect system
            		if (event.getEntity() instanceof Player) {
            			
            			Player defplayer = (Player) event.getEntity();
            			HPlayer hDefender = plugin.hPlayers.get(defplayer.getUniqueId());
            			
            			if (hDefender != null) {
            				
            				LivingEntity attacker = (LivingEntity) player;
                			
                			for(Reflect reflect : plugin.configAttributes.types.reflect) {
                				
                				if (hDefender.config.assignedAttributes.contains(reflect.name)) {
                					
                					//if (hDefender.strikeChance() < 0.05){
                					if (hDefender.randomReflectChance() > (1-plugin.config.meleeReflectChance)){
                						
                						if (!bHasReflectImmunity(defplayer, reflect, attacker)){
                							reflect.Execute(defplayer, attacker, event);
                            			}
                					}
                				}
                			}
            				
            			}
            			
            		}
        			
            		
                	
                	// -- Peaceful break by combat...
                	if (plugin.config.Mobs.contains(event.getEntity().getType())) {
                	
                		
                		boolean bFound = false;
                    	for (Truce t : plugin.configAttributes.types.truce) {
                    		if (h.config.assignedAttributes.contains(t.name)) {
                    			for (String n : t.entities) {
                        			if (event.getEntityType().name().equals(n)) {
                        				Cooldown.addCooldown(t.name, player.getName(), 30000);
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

            	h.lastAttack = System.currentTimeMillis();
    		}
        }
		// -- living attack target, and NOT player..
		// -- defender must also be a player
		else if (event.getDamager() instanceof LivingEntity && !(event.getDamager() instanceof Player) && event.getEntity() instanceof Player) {
		
			
			
			
			LivingEntity attacker = (LivingEntity) event.getDamager();
			Player defplayer = (Player) event.getEntity();
			HPlayer hDefender = plugin.hPlayers.get(defplayer.getUniqueId());
			
			//Player attackingPlayer = null;
			
			// -- special use
			//if (attacker instanceof Player) {
				//attackingPlayer = (Player) attacker;
			//}
			
			// -- better reflect system
			for(Reflect reflect : plugin.configAttributes.types.reflect) {
				
				if (hDefender.config.assignedAttributes.contains(reflect.name)) {
					
					//if (strikeChance < 0.05)
					if (hDefender.randomReflectChance() > (1-plugin.config.meleeReflectChance)){
						
						//defplayer.sendMessage("you have reflect: " + strike.getReflect());
						
						
						if (!bHasReflectImmunity(defplayer, reflect, attacker)){
							reflect.Execute(defplayer, attacker, event);
            			}
					}
				}
			}
			
			// -- lets check for cave spider poison...
			//defplayer.sendMessage("cavespider damage: " + event.getCause().name());
			
		}
		// -- arrow attacks player
		else if(event.getDamager() instanceof Arrow){
			
			//Bukkit.getServer().broadcastMessage("Damage From Arrow");
			
			final Arrow arrow = (Arrow) event.getDamager();
			if(arrow.getShooter() instanceof Player){
				
				// -- if shooter has attribute for bows
				Player player = (Player) arrow.getShooter();
	            HPlayer h = plugin.hPlayers.get(player.getUniqueId());
	            if (h != null ) {
	            	
	            	
	            	
	            	LivingEntity target = (LivingEntity) event.getEntity();
	            	
	            	// -- bow strike!!
	            	for(Strike strike : plugin.configAttributes.types.strike) {
        				if (h.config.assignedAttributes.contains(strike.name)) {
        					//if (h.strikeChance() < 0.33){
        					if (h.randomStrikeChance() > (1-plugin.config.rangedStrikeChance)){
        						
        						if (!bHasStrikeImmunity(player, strike, target)){
    								strike.Execute(player, target, event);	
                    			}
        					}
        				}
	            	}
	            	
	            	// -- reflect
	            	if (event.getEntity() instanceof Player) {
	            		Player defplayer = (Player) event.getEntity();
            			HPlayer hDefender = plugin.hPlayers.get(defplayer.getUniqueId());
	    	            
	    				// -- instance of livingentity
	    				//player.sendMessage("testing reflect shot");
	                	for(Reflect reflect : plugin.configAttributes.types.reflect) {
	        				
	        				if (hDefender.config.assignedAttributes.contains(reflect.name)) {
	        					//player.sendMessage("contains reflect: " + reflect.name);
	        					//if (strikeChance < 0.05)
	        					if (hDefender.randomReflectChance() > (1-plugin.config.meleeReflectChance)){
	        						
	        						if (!bHasReflectImmunity(defplayer, reflect, player)){
	        							//player.sendMessage("execute: " + reflect.name);
	        							reflect.Execute(defplayer, player, event);
	                    			}
	        					}
	        				}
	        			}
	            	}
	            	
	            	
	            	
	            	// -- reserve base damage!!!!!
                	double baseDamage = event.getDamage();
                	double attackMod = 0.0;
                	double deffendMod = 0.0;
	            	
	            	if (h.config.assignedAttributes.contains("ArcheryMastery"))
					{
	            		attackMod += (baseDamage * 0.25);
					}
					if (h.config.assignedAttributes.contains("ArcheryDeficiency"))
					{
						attackMod += (baseDamage * -0.25);
					}
	            	
	            	// -- check if they have Toughness or Weakness
            		if (event.getEntity() instanceof Player) {
            			
            			Player defplayer = (Player) event.getEntity();
            			HPlayer hDefender = plugin.hPlayers.get(defplayer.getUniqueId());
            			
                        if (hDefender != null ) {
                    		if (hDefender.config.assignedAttributes.contains("ArcheryToughness"))
            				{
                    			deffendMod += (baseDamage * -0.25);
            				}
                    		if (hDefender.config.assignedAttributes.contains("ArcheryWeakness"))
            				{
                    			deffendMod += (baseDamage * 0.25);
            				}
                        }
            		}
            		
            		// -- angelic and demonic...
                	//double angelicDamage = 0.0;
                	if (event.getEntity() instanceof Player) {
                		// -- if defending player is demonic
                		Player defplayer = (Player) event.getEntity();
            			HPlayer hDefender = plugin.hPlayers.get(defplayer.getUniqueId());
            			
                        if (hDefender != null ) {
                        	
                        	// -- melee toughness/weakness
                    		if (hDefender.config.assignedAttributes.contains("Demonic"))
            				{
                    			// -- if attacking player is angelic
                    			if (h.config.assignedAttributes.contains("Angelic"))
                				{
                    				// -- do MORE damage
                    				attackMod += (baseDamage * 0.5);
                    				//deffendMod += (baseDamage * -0.5);
                        		}
            				}
                    		// -- melee toughness/weakness
                    		if (hDefender.config.assignedAttributes.contains("Angelic"))
            				{
                    			// -- if attacking player is angelic
                    			if (h.config.assignedAttributes.contains("Demonic"))
                				{
                    				// -- do MORE damage
                    				//attackMod += (baseDamage * 0.5);
                    				deffendMod += (baseDamage * -0.5);
                        		}
            				}
                        }
                	}
	            	
            		// -- add the damages up example: (10 + 2.5 + -2.5)
            		event.setDamage(baseDamage + attackMod + deffendMod);
            		
            		
            		
            		// -- Peaceful break by combat...
                	if (plugin.config.Mobs.contains(event.getEntity().getType())) {
                		
                		boolean bFound = false;
                    	for (Truce t : plugin.configAttributes.types.truce) {
                    		if (h.config.assignedAttributes.contains(t.name)) {
                    			for (String n : t.entities) {
                        			if (event.getEntityType().name().equals(n)) {
                        				Cooldown.addCooldown(t.name, player.getName(), 30000);
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
            		
                	h.lastAttack = System.currentTimeMillis();
            		
	            }
	            
			}
			else {

				// -- defending player
				if (event.getEntity() instanceof Player) {
					
					Player player = (Player) event.getEntity();
		            HPlayer h = plugin.hPlayers.get(player.getUniqueId());
		            
		            
		            if (arrow.getShooter() instanceof LivingEntity) {
		            	
		            	LivingEntity target = (LivingEntity) arrow.getShooter();
			            
			            
						// -- instance of livingentity
						//player.sendMessage("testing reflect shot");
		            	for(Reflect reflect : plugin.configAttributes.types.reflect) {
		    				
		    				if (h.config.assignedAttributes.contains(reflect.name)) {
		    					//player.sendMessage("contains reflect: " + reflect.name);
		    					//if (h.randomChance() < 0.33){
		    					if (h.randomReflectChance() > (1-plugin.config.rangedReflectChance)){
		    						
		    						if (!bHasReflectImmunity(player, reflect, target)){
		    							//player.sendMessage("execute: " + reflect.name);
		    							reflect.Execute(player, target, event);
		                			}
		    					}
		    				}
		    			}
		            }
		            
				}
				
			}

		}

	}
	
}
