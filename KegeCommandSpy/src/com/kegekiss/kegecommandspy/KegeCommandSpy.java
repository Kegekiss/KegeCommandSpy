package com.kegekiss.kegecommandspy;

import java.util.HashSet;
import java.util.Set;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class KegeCommandSpy extends JavaPlugin implements Listener {
	
	Set<OfflinePlayer> commandSpies = new HashSet<OfflinePlayer>();

	@Override
	public void onEnable() {
		// On enregistre l'event
		Bukkit.getPluginManager().registerEvents(this, this);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		// La commande n'a pas été annulé à ce stade là.
		for (OfflinePlayer spy : commandSpies) {
			if (spy.isOnline()) {
				// Si l'espion est en ligne, on envoie le message
				spy.getPlayer().sendMessage(ChatColor.RED + "[KegeCommandSpy] " + 
				event.getPlayer().getDisplayName() + ChatColor.RED + " : " + event.getMessage());
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("commandspy")) {
			if (sender instanceof Player) {
				// un Player est FORCEMENT un OfflinePlayer
				OfflinePlayer spy = (OfflinePlayer)sender;
				if (args.length == 0) {
					if (commandSpies.contains(spy)) {
						commandSpies.remove(spy);
						sender.sendMessage(ChatColor.GOLD 
								+ "Mode espion des commandes" + 
								ChatColor.RED + " désactivé" + ChatColor.GOLD + ".");
					} else {
						commandSpies.add(spy);
						sender.sendMessage(ChatColor.GOLD 
								+ "Mode espion des commandes" + 
								ChatColor.GREEN + " activé" + ChatColor.GOLD + ".");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("toggle") && args.length >= 2) {
					if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true")) {
						commandSpies.add(spy);
						sender.sendMessage(ChatColor.GOLD 
								+ "Mode espion des commandes" + 
								ChatColor.GREEN + " activé" + ChatColor.GOLD + ".");
						return true;
					} else if (args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("false")) {
						commandSpies.remove(spy);
						sender.sendMessage(ChatColor.GOLD 
								+ "Mode espion des commandes" + 
								ChatColor.RED + " désactivé" + ChatColor.GOLD + ".");
						return true;
					}
				}
			}
		}
		return false;
	}

}
