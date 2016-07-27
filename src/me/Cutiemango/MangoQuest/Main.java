package me.Cutiemango.MangoQuest;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.Cutiemango.MangoQuest.data.QuestPlayerData;
import me.Cutiemango.MangoQuest.listeners.PlayerListener;
import me.Cutiemango.MangoQuest.listeners.QuestListener;
import net.citizensnpcs.api.CitizensPlugin;
import net.milkbowl.vault.Vault;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin{
	
	public static Main instance;
	public static Economy economy = null;
	public CitizensPlugin citizens;
	public Vault vault = null;
	
	private QuestConfigLoad cfg;
	
	@Override
	public void onEnable(){
		instance = this;
		
		linkOtherPlugins();
		
		getCommand("mq").setExecutor(new QuestCommand());
		getServer().getPluginManager().registerEvents(new QuestListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		cfg = new QuestConfigLoad(this);
		
		
		new BukkitRunnable(){
			@Override
			public void run() {
				cfg.loadQuests();
				for (Player p : Bukkit.getOnlinePlayers()){
					QuestPlayerData qd = new QuestPlayerData(p);
					if (QuestPlayerData.hasConfigData(p))
						qd = new QuestPlayerData(p, QuestConfigLoad.pconfig);
					QuestStorage.Players.put(p.getName(), qd);
				}
			}
		}.runTaskLater(this, 5L);
		
		getLogger().info("�w�g�}�ҡI");
	}
	
	@Override
	public void onDisable(){
		getLogger().info("�w�g�����I");
		for (Player p : Bukkit.getOnlinePlayers()){
			QuestUtil.getData(p).save();
			QuestUtil.info(p, "&b���a����x�s��...");
		}
	}
	
	private void linkOtherPlugins() {
		try {
			if (getServer().getPluginManager().getPlugin("Citizens") != null) {
				citizens = (CitizensPlugin) getServer().getPluginManager().getPlugin("Citizens");
			}
			getLogger().log(Level.INFO, "Citizens����w�g�s�����\�C");
		} catch (Exception e) {
			getLogger().log(Level.WARNING, "�S���ŦX������Citizens����CNPC�\��N���|�ҰʡC");

		}

		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
				.getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
			getLogger().log(Level.INFO, "Vault����w�g�s�����\�C");
		}
	}

}
