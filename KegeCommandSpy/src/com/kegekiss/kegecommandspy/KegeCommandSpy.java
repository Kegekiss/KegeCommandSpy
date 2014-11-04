package com.kegekiss.kegecommandspy;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
	
	Set<UUID> commandSpies = new HashSet<UUID>();

	@Override
	public void onEnable() {
		// On enregistre l'event
		Bukkit.getPluginManager().registerEvents(this, this);
		// On lit la liste
		getDataFolder().mkdir();
		loadData();
	}
	
	@Override
	public void onDisable() {
		// On enregistre la liste
		saveData();
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
		// La commande n'a pas été annulé à ce stade là.
		for (UUID uuid : commandSpies) {
			OfflinePlayer spy = Bukkit.getOfflinePlayer(uuid);
			if (spy.isOnline()) {
				// Si l'espion est en ligne, on envoie le message
				spy.getPlayer().sendMessage(ChatColor.RED + "[KegeCommandSpy] " + 
				event.getPlayer().getDisplayName() + ChatColor.RED + " : " + ChatColor.GOLD + event.getMessage());
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("commandspy")) {
			if (sender instanceof Player) {
				Player spy = (Player)sender;
				UUID uuid = spy.getUniqueId();
				if (args.length == 0) {
					if (commandSpies.contains(uuid)) {
						commandSpies.remove(uuid);
						spy.sendMessage(ChatColor.GOLD 
								+ "Mode espion des commandes" + 
								ChatColor.RED + " désactivé" + ChatColor.GOLD + ".");
					} else {
						commandSpies.add(uuid);
						spy.sendMessage(ChatColor.GOLD 
								+ "Mode espion des commandes" + 
								ChatColor.GREEN + " activé" + ChatColor.GOLD + ".");
					}
					return true;
				} else if (args[0].equalsIgnoreCase("toggle") && args.length >= 2) {
					if (args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true")) {
						commandSpies.add(uuid);
						spy.sendMessage(ChatColor.GOLD 
								+ "Mode espion des commandes" + 
								ChatColor.GREEN + " activé" + ChatColor.GOLD + ".");
						return true;
					} else if (args[1].equalsIgnoreCase("off") || args[1].equalsIgnoreCase("false")) {
						commandSpies.remove(uuid);
						spy.sendMessage(ChatColor.GOLD 
								+ "Mode espion des commandes" + 
								ChatColor.RED + " désactivé" + ChatColor.GOLD + ".");
						return true;
					}
				}
			}
		}
		return false;
	}

	private void loadData() {
		try (Scanner scanner = new Scanner(new File(getDataFolder(), "data.txt"))) {
			while (scanner.hasNext()) {
				// Pour chaque ligne...
				String line = scanner.nextLine();
				if (!line.equals("")) {
					// On sépare le UUID du pseudo
					String[] lineData = line.split(":");
					// On rajoute le UUID
					commandSpies.add(UUID.fromString(lineData[0]));
				}
			}
			scanner.close();
		} catch (Exception e) {
			this.getLogger().severe("Lecture des données impossible !");
			e.printStackTrace();
		}
	}
	
	private void saveData() {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(getDataFolder(), "data.txt")))) {
			for (UUID uuid : commandSpies) {
				// On sauvegarde l'UUID et le pseudo du joueur
				writer.append(uuid.toString() + ":" + Bukkit.getOfflinePlayer(uuid).getName());
				writer.newLine();
			}
			writer.close();
		} catch (Exception e) {
			this.getLogger().severe("Ecriture des données impossible !");
			e.printStackTrace();		
		}		
	}	
}
