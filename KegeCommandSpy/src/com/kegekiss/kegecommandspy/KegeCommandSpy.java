package com.kegekiss.kegecommandspy;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class KegeCommandSpy extends JavaPlugin implements Listener {
	

	@Override
	public void onEnable() {
		// On enregistre l'event
		Bukkit.getPluginManager().registerEvents(this, this);
	}

}
