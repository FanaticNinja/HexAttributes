package org.hexcraft.hexattributes.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.hexcraft.HexAttributes;
import org.hexcraft.hexattributes.HPlayer;
import org.hexcraft.hexattributes.api.AttributeBase;
import org.hexcraft.hexattributes.api.HexAttributesAPI;

public class OnClickSign extends BaseListener {
	
	public OnClickSign(HexAttributes _plugin) {
		super(_plugin);
		// TODO Auto-generated constructor stub
	}
	
	private boolean CheckForSign(Block block) {
		if (block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
			return true;
		}
		return false;
	}
	
	private void addAttribute(HPlayer hplayer, List<String> newAttributes) {
		Player player = hplayer.getPlayer();
		
		
		int cost = 0;
		String attribute = "";
		//String info = "";
		String attributeColor = "";
		
		// -- lastDamage + 5 minutes (5 * 60 * 1000 = 300000)
		// -- in seconds, timeRespecAfterDamage * 1000
			if (plugin.config.bDelayRespecAfterDamage == true && (hplayer.lastDamage + plugin.config.timeRespecAfterDamage) > System.currentTimeMillis()) {
				// -- get timeleft..
				long timeLeftLong = (((hplayer.lastDamage + 300000) - System.currentTimeMillis()));
				String timeLeftString = String.format("%d min, %d sec", 
					    TimeUnit.MILLISECONDS.toMinutes(timeLeftLong),
					    TimeUnit.MILLISECONDS.toSeconds(timeLeftLong) - 
					    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeLeftLong))
					);
				
				//double timeLeft = (double)(((hplayer.lastDamage + 300000) - System.currentTimeMillis())/1000)/60;
				// -- go JAVA for not having a round function with decimal points...
				//double newtimeLeft = Math.round(timeLeft*100.0)/100.0;
				//player.sendMessage(ChatColor.RED + "Unable to change attributes due to damage taken: " + ChatColor.GRAY + "(" + ChatColor.AQUA + timeLeftString + " remaining" + ChatColor.GRAY + ")");
				player.sendMessage(String.format(plugin.lang.unableChangeDamage, timeLeftString));
			}
			else
			{
				// -- no combat time, do the RESET!!!
				for (String s : hplayer.config.assignedAttributes) {
					// -- update permEffects..
					hplayer.removePermEffect(s);
				}
				
				hplayer.config.assignedAttributes.clear();
				//hplayer.config.pointsBase = 150;
				//hplayer.config.pointsCurrent = 150;
				//hplayer.config.slotsMax = 10;
				//hplayer.config.slotsSpent = 0;
				
				hplayer.resetPoints();
				
				player.sendMessage(plugin.lang.removedAddedFromSign);
				
				//plugin.log("For LOOP Adding");
				
				for (String cmdAttrib : newAttributes) {
					
					boolean bFound = false;
					for (AttributeBase a : plugin.configAttributes.types.register) {
						//plugin.debug(a.name);
						if (a.name.equalsIgnoreCase(cmdAttrib)) {
							//plugin.debug("Found " + cmdAttrib + " in types.register");
							cost = a.cost;
							attribute = a.name;
							//info = a.info;
							bFound = true;
							
							if (cost > 0) {
								attributeColor = ChatColor.GREEN + attribute + ChatColor.GRAY + "(" + ChatColor.WHITE + "+" + cost + ChatColor.GRAY + ")";
							}
							else
							{
								attributeColor = ChatColor.RED + attribute + ChatColor.GRAY + "(" + ChatColor.WHITE + "" + cost + ChatColor.GRAY + ")";
							}
							
							
							break;
						}
					}
					
					if (!bFound) {
						player.sendMessage(String.format(plugin.lang.attributeNotFound, cmdAttrib));
					}
					else {
						//plugin.log("Found: " + cmdAttrib);
						// -- new test..
						if (plugin.config.bUsePerms == true && !player.hasPermission("hexattributes.type." + attribute)) {
							player.sendMessage(String.format(plugin.lang.noPermissionForAttribute, attributeColor));
						}
						else {
							if (hplayer.config.assignedAttributes.contains(attribute)) {
								player.sendMessage(String.format(plugin.lang.alreadyHaveAttribute, attributeColor));
							}
							else {
								
								// -- cost system..
								if (hplayer.config.slotsSpent < hplayer.config.slotsMax) {
									// -- if our points left is more than the cost..
									if (hplayer.config.pointsCurrent >= cost) {
										
										//plugin.log("Assigning Attribute: " + cmdAttrib);
										
										// -- we have the points, and slots remaining.
										// -- so lets reduce num points, and apply attributes to player
										hplayer.config.slotsSpent += 1;
										hplayer.config.pointsCurrent -= cost;
										
										hplayer.config.assignedAttributes.add(attribute);
										hplayer.bNeedsSave = true;
										

										player.sendMessage(String.format(plugin.lang.addedAttribute, attributeColor));

										// -- update permEffects..
										hplayer.addPermEffects();
										
									}
									else {
										player.sendMessage(String.format(plugin.lang.notEnoughPoints, attributeColor));
									}
								}
								else {
									player.sendMessage(plugin.lang.noMoreSockets);
								}
							}
						}
					}
					
					
					
				}
				
				hplayer.bNeedsSave = true;
				player.sendMessage(plugin.lang.addedFromSign);
			}
	}

	@EventHandler
	private void InteractEvent(PlayerInteractEvent event) {
		
		// -- sign stuff
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		
		// -- World Check
		if (!HexAttributesAPI.bValidWorld(event.getClickedBlock().getWorld())) {
			return;
		}
		/*
		if (plugin.config.bUseWorldWhiteList) {
			if (plugin.config.worldWhitelist != null) {
				if (!plugin.config.worldWhitelist.contains(event.getClickedBlock().getWorld().getName())) {
					return false;
				}
			}
			else {
				plugin.log("Unable to use World White List: null");
			}
		}
		*/
		
		Block block = event.getClickedBlock();
		
		//plugin.log("Checking Block 1..");
		
		 if (CheckForSign(block)) {
		  Sign sign = (Sign) block.getState();
		  
		  
		  HPlayer hplayer = plugin.hPlayers.get(event.getPlayer().getUniqueId());
		  
		  // -- if the first line is [Attribute]
		  if (sign.getLine(0).equalsIgnoreCase("[attributes]")) {
			  
			  // -- build a list of Attributes that they would like to do...
			  List<String> newAttributes = new ArrayList<String>();
			  
			  //plugin.log("Adding Attributes from sign 1..");
			  
			  for (String line : sign.getLines()) {
				  
				  if (!line.equalsIgnoreCase("[attributes]") && !line.equalsIgnoreCase(null) && !line.equalsIgnoreCase("")) {
					  //plugin.log(line);
					  newAttributes.add(line);
				  }
				  
			  }
			  
			  // -- check blocks under this block
			  Location l = new Location(event.getPlayer().getWorld(), block.getLocation().getX(), (block.getLocation().getY() - 1), block.getLocation().getZ());
			  Block block2 = l.getBlock();
			  
			  //plugin.log("Checking Block 2..");
			  
			  
			  if (CheckForSign(block2)) {
				  
				  Sign sign2 = (Sign) block2.getState();
				  
				  //plugin.log("Adding Attributes from Sign 2");
				  
				  for (String line : sign2.getLines()) {
					  
					  if (!line.equalsIgnoreCase("[attributes]") && !line.equalsIgnoreCase(null) && !line.equalsIgnoreCase("")) {
						  //plugin.log(line);
						  newAttributes.add(line);
					  }
					  
				  }
				  
			  }
			  
			  // -- check blocks under this block
			  Location l2 = new Location(event.getPlayer().getWorld(), block2.getLocation().getX(), (block2.getLocation().getY() - 1), block2.getLocation().getZ());
			  Block block3 = l2.getBlock();
			  
			  //plugin.log("Checking Block 3..");
			  
			  if (CheckForSign(block3)) {
				  
				  Sign sign3 = (Sign) block3.getState();
				  
				  //plugin.log("Adding Attributes from Sign 3");
				  
				  for (String line : sign3.getLines()) {
					  
					  if (!line.equalsIgnoreCase("[attributes]") && !line.equalsIgnoreCase(null) && !line.equalsIgnoreCase("")) {
						  //plugin.log(line);
						  newAttributes.add(line);
					  }
					  
				  }
				  
			  }
			  
			  
			  // -- now that we have our list, remove attributes, add attributes
			  // -- check combat
			  // -- remove attributes
			  // -- add attributes..
			  addAttribute(hplayer, newAttributes);
			  
			  
			  
			  
		  }
		  
		  
		  
		  
		}
		
		
	}
	
}
