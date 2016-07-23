package org.hexcraft.hexattributes;

import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.hexcraft.HexAttributes;
import org.hexcraft.chest.*;
import org.hexcraft.hexattributes.api.AttributeBase;
import org.hexcraft.hexattributes.api.HexAttributesAPI;

public class Commands implements CommandExecutor {
	
	private final HexAttributes plugin;
	
	public Commands(HexAttributes plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			
			Player player = (Player) sender;
			
			// -- Fix for null worldWhitelist
			if (!HexAttributesAPI.bValidWorld(player.getWorld())) {
				return false;
			}
			/*
			if (plugin.config.bUseWorldWhiteList) {
				if (plugin.config.worldWhitelist != null) {
					if (!plugin.config.worldWhitelist.contains(player.getWorld().getName())) {
						return false;
					}
				}
				else {
					plugin.log("Unable to use World White List: null");
				}
			}
			*/
			
			
			HPlayer hplayer = plugin.hPlayers.get(player.getUniqueId());
			
			//plugin.debug("Entering Command");
			
			
			if (label.equals("attribute")) {
				if (args.length == 2)
				{
					//plugin.debug("Command Attribute: " + args[1]);
					
					String cmdAttrib = args[1];
					
					int cost = 0;
					// -- lets build this to have COLOR attribute(+cost)
					String attributeColor = "";
					String attribute = "";
					String info = "";

					// -- GUI stuff
					if (args[0].equalsIgnoreCase("gui")) {
						
						if (cmdAttrib.equalsIgnoreCase(ChatColor.WHITE + "deficiency")) {
							
							if (hplayer.defGui == null) {
								hplayer.defGui = new DeficiencyGUI(player, plugin, hplayer);
							}
							else
							{
								hplayer.defGui.openGUI(hplayer);
							}
						}
						if (cmdAttrib.equalsIgnoreCase(ChatColor.WHITE + "food")) {
							if (hplayer.fooGui == null) {
								hplayer.fooGui = new FoodGUI(player, plugin, hplayer);
							}
							else
							{
								hplayer.fooGui.openGUI(hplayer);
							}
						}
						if (cmdAttrib.equalsIgnoreCase(ChatColor.WHITE + "immunity")) {
							if (hplayer.immGui == null) {
								hplayer.immGui = new ImmunityGUI(player, plugin, hplayer);
							}
							else
							{
								hplayer.immGui.openGUI(hplayer);
							}
						}
						if (cmdAttrib.equalsIgnoreCase(ChatColor.WHITE + "mastery")) {
							if (hplayer.masGui == null) {
								hplayer.masGui = new MasteryGUI(player, plugin, hplayer);
							}
							else
							{
								hplayer.masGui.openGUI(hplayer);
							}
						}
						if (cmdAttrib.equalsIgnoreCase(ChatColor.WHITE + "passive")) {
							if (hplayer.pasGui == null) {
								hplayer.pasGui = new PassiveGUI(player, plugin, hplayer);
							}
							else
							{
								hplayer.pasGui.openGUI(hplayer);
							}
						}
						if (cmdAttrib.equalsIgnoreCase(ChatColor.WHITE + "permeffect")) {
							if (hplayer.perGui == null) {
								hplayer.perGui = new PermEffectGUI(player, plugin, hplayer);
							}
							else
							{
								hplayer.perGui.openGUI(hplayer);
							}
						}
						if (cmdAttrib.equalsIgnoreCase(ChatColor.WHITE + "reflect")) {
							if (hplayer.refGui == null) {
								hplayer.refGui = new ReflectGUI(player, plugin, hplayer);
							}
							else
							{
								hplayer.refGui.openGUI(hplayer);
							}
						}
						if (cmdAttrib.equalsIgnoreCase(ChatColor.WHITE + "strike")) {
							if (hplayer.strGui == null) {
								hplayer.strGui = new StrikeGUI(player, plugin, hplayer);
							}
							else
							{
								hplayer.strGui.openGUI(hplayer);
							}
						}
						if (cmdAttrib.equalsIgnoreCase(ChatColor.WHITE + "toughness")) {
							if (hplayer.touGui == null) {
								hplayer.touGui = new ToughnessGUI(player, plugin, hplayer);
							}
							else
							{
								hplayer.touGui.openGUI(hplayer);
							}
						}
						if (cmdAttrib.equalsIgnoreCase(ChatColor.WHITE + "truce")) {
							if (hplayer.truGui == null) {
								hplayer.truGui = new TruceGUI(player, plugin, hplayer);
							}
							else
							{
								hplayer.truGui.openGUI(hplayer);
							}
						}
						if (cmdAttrib.equalsIgnoreCase(ChatColor.WHITE + "weakness")) {
							if (hplayer.weaGui == null) {
								hplayer.weaGui = new WeaknessGUI(player, plugin, hplayer);
							}
							else
							{
								hplayer.weaGui.openGUI(hplayer);
							}
						}
						
						return false;
					}
					
					//plugin.debug("For loop AttributeBase:");
					// -- remove all
					if ((args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("r")) && cmdAttrib.equalsIgnoreCase("all")) {
						
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
						
						hplayer.bNeedsSave = true;
						player.sendMessage(plugin.lang.removedAllAttributes);
						return false;
					}
					
					boolean bFound = false;
					for (AttributeBase a : plugin.configAttributes.types.register) {
						//plugin.debug(a.name);
						if (a.name.equalsIgnoreCase(cmdAttrib)) {
							//plugin.debug("Found " + cmdAttrib + " in types.register");
							cost = a.cost;
							attribute = a.name;
							info = a.info;
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
						return false;
					}
					
					// -- attribute info
					if ((args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i"))) {
						
						player.sendMessage(String.format(plugin.lang.attributeInfo, attributeColor, info));

						return false;
					}
					
					// -- lastDamage + 5 minutes (5 * 60 * 1000 = 300000)
					if ((hplayer.lastDamage + plugin.config.timeRespecAfterDamage) > System.currentTimeMillis()) {
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
						player.sendMessage(String.format(plugin.lang.unableChangeDamage, timeLeftString));
						return false;
					}
					
					
					
					if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
						
						
						// -- check if they want perms or not
						if (plugin.config.bUsePerms ==  true) {
							if (!player.hasPermission("hexattributes.type." + attribute)) {
								player.sendMessage(String.format(plugin.lang.noPermissionForAttribute, attributeColor));
								return false;
							}
						}
						
						
						if (hplayer.config.assignedAttributes.contains(attribute)) {
							player.sendMessage(String.format(plugin.lang.alreadyHaveAttribute, attributeColor));
						}
						else {
							
							
							// -- cost system..
							// -- perm based slots??
							if (hplayer.config.slotsSpent < hplayer.config.slotsMax) {
								// -- if our points left is more than the cost..
								if (hplayer.config.pointsCurrent >= cost) {
									
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
					else if (args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("r")) {
						
						
						
						if (hplayer.config.assignedAttributes.contains(attribute)) {
							
							// -- make sure player can't go negative..
							// -- if 10 > ABS(-20)
							// -- allow
							// -- else free up points first
							if (cost < 0)	
							{
								if (hplayer.config.pointsCurrent >= Math.abs(cost)) {
									hplayer.config.slotsSpent -= 1;
									hplayer.config.pointsCurrent += cost;
									
									player.sendMessage(String.format(plugin.lang.removedAttribute, attributeColor));
									hplayer.config.assignedAttributes.remove(attribute);
									hplayer.bNeedsSave = true;
									
									// -- update permEffects..
									hplayer.removePermEffect(attribute);
								}
								else
								{
									player.sendMessage(String.format(plugin.lang.helpRemovalNonNegative, attributeColor));
								}
							}
							else
							{
								
								hplayer.config.slotsSpent -= 1;
								hplayer.config.pointsCurrent += cost;
								
								player.sendMessage(String.format(plugin.lang.removedAttribute, attributeColor));
								hplayer.config.assignedAttributes.remove(attribute);
								hplayer.bNeedsSave = true;
								
								hplayer.removePermEffect(attribute);
								
							}
							
							
						}
						else {
							player.sendMessage(String.format(plugin.lang.helpUnassigned, attributeColor));
						}
					}
				}
				else if (args.length == 1)
				{
					// -- test GUI stuff
					if (args[0].equalsIgnoreCase("gui")) {
						
						// -- test GUI stuff
						if (args[0].equalsIgnoreCase("gui")) {
							
							// -- open the local hplayer GUI..
							if (hplayer.attributeOverview == null) {
								hplayer.attributeOverview = new Overview(player, plugin, hplayer);
							}
							else
							{
								hplayer.attributeOverview.openGUI(hplayer);
							}
							return false;
						}
						return false;
					}
					
					
					// -- list stuff..
					String list = "";
					
					for (AttributeBase b : plugin.configAttributes.types.register) {
						if (hplayer.config.assignedAttributes.contains(b.name)) {
							
							if (b.cost > 0) {
								list += ChatColor.GREEN + b.name + ChatColor.GRAY + "(" + ChatColor.WHITE + "+" + b.cost + ChatColor.GRAY + "), ";
							}
							else {
								list += ChatColor.RED + b.name + ChatColor.GRAY + "(" + ChatColor.WHITE + "" + b.cost + ChatColor.GRAY + "), ";
							}
						}
					}
					
					// -- fix because i like to keep it clean..
					// -- trim of LAST ","
					if (list.length() > 2) {
						list = list.substring(0, list.length()-2);
					}
					

					/*
					for (String att : hplayer.config.assignedAttributes) {
						
					}
					*/
					player.sendMessage(plugin.lang.yourAttributes);
					
					
					player.sendMessage(String.format(plugin.lang.sockets, Math.abs(hplayer.config.slotsSpent), hplayer.config.slotsMax));
					player.sendMessage(String.format(plugin.lang.shards, hplayer.config.pointsCurrent, hplayer.config.pointsBase));
					player.sendMessage(list);
				}
				else
				{
					// -- help info..
					player.sendMessage(plugin.lang.attributeCommands);
					player.sendMessage(plugin.lang.cmdGui);
					player.sendMessage(plugin.lang.cmdAdd);
					player.sendMessage(plugin.lang.cmdRemove);
					player.sendMessage(plugin.lang.cmdList);
					player.sendMessage(plugin.lang.cmdInfo);
				}
			}
			
		}
		
		return false;
	}
}