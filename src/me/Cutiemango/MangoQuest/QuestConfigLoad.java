package me.Cutiemango.MangoQuest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import me.Cutiemango.MangoQuest.questobjects.QuestObjectItemDeliver;
import me.Cutiemango.MangoQuest.questobjects.QuestObjectTalkToNPC;
import me.Cutiemango.MangoQuest.questobjects.SimpleQuestObject;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class QuestConfigLoad {
	
	public static FileConfiguration pconfig;
	private FileConfiguration qconfig;
	private Main plugin;
	
	public QuestConfigLoad(Main pl){
		plugin = pl;
		init();
		new BukkitRunnable(){
			@Override
			public void run() {
				loadQuests();
			}
		}.runTaskLater(plugin, 5L);
	}
	
	private void init(){
		File file = new File(plugin.getDataFolder(), "players.yml");
		
		if (!file.exists()){
			plugin.saveResource("players.yml", true);
			Bukkit.getLogger().log(Level.SEVERE, "[MangoQuest] �䤣��players.yml�A�إ߷s�ɮסI");
		}
		pconfig = YamlConfiguration.loadConfiguration(file);
		
		file = new File(this.plugin.getDataFolder(), "quests.yml");
		if (!file.exists()){
			plugin.saveResource("quests.yml", true);
			Bukkit.getLogger().log(Level.SEVERE, "[MangoQuest] �䤣��quests.yml�A�إ߷s�ɮסI");
		}
		
		qconfig = YamlConfiguration.loadConfiguration(file);
	}
	
	public void loadQuests(){
		if (qconfig.getConfigurationSection("���ȦC��") == null)
			return;
		for (String internal : qconfig.getConfigurationSection("���ȦC��").getKeys(false)) {
			String questname = qconfig.getString("���ȦC��." + internal + ".���ȦW��");
			String questoutline = qconfig.getString("���ȦC��." + internal + ".���ȴ��n");
			List<QuestStage> stages = new ArrayList<>();
			for (String stagecount : qconfig.getConfigurationSection("���ȦC��." + internal + ".���Ȥ��e").getKeys(false)) {
				List<SimpleQuestObject> objs = new ArrayList<>();
				int scount = Integer.parseInt(stagecount);
				for (String objcount : qconfig.getConfigurationSection("���ȦC��." + internal + ".���Ȥ��e." + scount).getKeys(false)) {
					int ocount = Integer.parseInt(objcount);
					String s = qconfig.getString("���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".���Ⱥ���");
					SimpleQuestObject obj = null;
					switch (s) {
					case "ITEM_DELIVER":
						obj = new QuestObjectItemDeliver(CitizensAPI.getNPCRegistry()
								.getById(qconfig.getInt("���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".�ؼ�NPC")),
						QuestUtil.getItemStack(qconfig, "���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".���~"),
						qconfig.getInt("���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".���~.�ƶq"));
						break;
					case "TALK_TO_NPC":
						obj = new QuestObjectTalkToNPC(CitizensAPI.getNPCRegistry()
								.getById(qconfig.getInt("���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".�ؼ�NPC")));
						break;
					default:
						break;
					}
					objs.add(obj);
				}
				QuestStage qs = new QuestStage(null, null, objs);
				stages.add(qs);
			}
			QuestReward reward = new QuestReward(QuestUtil.getItemStack(qconfig, "���ȦC��." + internal + ".���ȼ��y.���~.1"));
			for (String temp : qconfig.getConfigurationSection("���ȦC��." + internal + ".���ȼ��y.���~").getKeys(false)) {
				int count = Integer.parseInt(temp);
				if (count == 1)
					continue;
				reward.add(QuestUtil.getItemStack(qconfig, "���ȦC��." + internal + ".���ȼ��y.���~." + count));
			}
			
			if (plugin.citizens != null && qconfig.contains("���ȦC��." + internal + ".����NPC")){
				if (CitizensAPI.getNPCRegistry().getById(0) != null){
					NPC npc = CitizensAPI.getNPCRegistry().getById(0);
					Quest quest = new Quest(internal, questname, questoutline, reward, stages, npc);
					QuestStorage.Quests.put(internal, quest);
					Bukkit.getLogger().log(Level.INFO, "���� " + questname + " �w�gŪ�����\�I");
				}else{
					Bukkit.getLogger().log(Level.SEVERE, "���� " + questname + " ��NPC ID �L�kŪ���I");
					Bukkit.getLogger().log(Level.SEVERE, "���� " + questname + " �w�g���LŪ���C");
					continue;
				}
			}else{
				Bukkit.getLogger().log(Level.SEVERE, qconfig.getInt("���ȦC��." + internal + ".����NPC") + "");
				Bukkit.getLogger().log(Level.SEVERE, "���� " + questname + " ��NPC ID �����T�I�Э��s�T�{�I");
				Bukkit.getLogger().log(Level.SEVERE, "���� " + questname + " �w�g���LŪ���C");
				continue;
			}
		}
	}

}
