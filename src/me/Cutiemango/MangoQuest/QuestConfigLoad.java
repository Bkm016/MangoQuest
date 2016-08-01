package me.Cutiemango.MangoQuest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import me.Cutiemango.MangoQuest.model.Quest;
import me.Cutiemango.MangoQuest.model.QuestReward;
import me.Cutiemango.MangoQuest.model.QuestStage;
import me.Cutiemango.MangoQuest.model.QuestTrigger;
import me.Cutiemango.MangoQuest.model.QuestTrigger.TriggerObject;
import me.Cutiemango.MangoQuest.model.QuestTrigger.TriggerType;
import me.Cutiemango.MangoQuest.model.RequirementType;
import me.Cutiemango.MangoQuest.questobjects.QuestObjectBreakBlock;
import me.Cutiemango.MangoQuest.questobjects.QuestObjectItemConsume;
import me.Cutiemango.MangoQuest.questobjects.QuestObjectItemDeliver;
import me.Cutiemango.MangoQuest.questobjects.QuestObjectKillMob;
import me.Cutiemango.MangoQuest.questobjects.QuestObjectReachLocation;
import me.Cutiemango.MangoQuest.questobjects.QuestObjectTalkToNPC;
import me.Cutiemango.MangoQuest.questobjects.SimpleQuestObject;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;

public class QuestConfigLoad {
	
	public static FileConfiguration pconfig;
	private FileConfiguration qconfig;
	private FileConfiguration tconfig;
	private Main plugin;
	
	public QuestConfigLoad(Main pl){
		plugin = pl;
		init();
		loadTranslation();
	}
	
	private void loadTranslation() {
		for (String s : tconfig.getConfigurationSection("Material").getKeys(false)){
			Material m = Material.getMaterial(s);
			QuestStorage.TranslateMap.put(m, tconfig.getString("Material." + m.toString()));
		}
		for (String e : tconfig.getConfigurationSection("EntityType").getKeys(false)){
			EntityType t = EntityType.valueOf(e);
			QuestStorage.EntityTypeMap.put(t, tconfig.getString("EntityType." + e.toString()));
		}
		Bukkit.getLogger().log(Level.INFO, "[MangoQuest] ½Ķ�ɮ�Ū�������I");
	}

	private void init(){
		File file = new File(plugin.getDataFolder(), "players.yml");
		
		if (!file.exists()){
			plugin.saveResource("players.yml", true);
			Bukkit.getLogger().log(Level.SEVERE, "[MangoQuest] �䤣��players.yml�A�إ߷s�ɮסI");
		}
		pconfig = YamlConfiguration.loadConfiguration(file);
		
		file = new File(plugin.getDataFolder(), "translations.yml");
		if (!file.exists()){
			plugin.saveResource("translations.yml", true);
			Bukkit.getLogger().log(Level.SEVERE, "[MangoQuest] �䤣��translations.yml�A�إ߷s�ɮסI");
		}
		
		tconfig = YamlConfiguration.loadConfiguration(file);
		
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
					case "KILL_MOB":
						String name = null;
						if (qconfig.getString("���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".�Ǫ��W��") != null)
							name = qconfig.getString("���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".�Ǫ��W��");
						obj = new QuestObjectKillMob(
								EntityType.valueOf(qconfig.getString("���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".�Ǫ�����")),
								qconfig.getInt("���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".�ƶq"), name);
						break;
					case "BREAK_BLOCK":
						obj = new QuestObjectBreakBlock(Material.getMaterial(
								qconfig.getString("���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".���")),
								qconfig.getInt("���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".�ƶq"));
						break;
					case "CONSUME_ITEM":
						obj = new QuestObjectItemConsume(QuestUtil.getItemStack(qconfig, "���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".���~"),
								qconfig.getInt("���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".���~.�ƶq"));
						break;
					case "REACH_LOCATION":
						String[] splited = qconfig.getString("���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".�a�I").split(":");
						Location loc = new Location(
								Bukkit.getWorld(splited[0]),
								Double.parseDouble(splited[1]),
								Double.parseDouble(splited[2]),
								Double.parseDouble(splited[3]));
						obj = new QuestObjectReachLocation(loc,
								qconfig.getInt("���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".�d��"),
								qconfig.getString("���ȦC��." + internal + ".���Ȥ��e." + scount + "." + ocount + ".�W��"));
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
					if (qconfig.getString("���ȦC��." + internal + ".���ŦX���ȻݨD�T��") != null)
						quest.setFailMessage(qconfig.getString("���ȦC��." + internal + ".���ŦX���ȻݨD�T��"));
					
					//Requirements
					if (qconfig.isConfigurationSection("���ȦC��." + internal + ".���ȻݨD")){
						if (qconfig.getInt("���ȦC��." + internal + ".���ȻݨD.Level") != 0)
							quest.getRequirements().put(RequirementType.LEVEL, qconfig.getInt(qconfig.getString("���ȦC��." + internal + ".���ȻݨD.Level")));
						if (qconfig.getStringList("���ȦC��." + internal + ".���ȻݨD.Quest") != null){
							quest.getRequirements().put(RequirementType.QUEST, qconfig.getStringList("���ȦC��." + internal + ".���ȻݨD.Quest"));
						}
						if (qconfig.isConfigurationSection("���ȦC��." + internal + ".���ȻݨD.Item")){
							List<ItemStack> l = new ArrayList<>();
							for (String i : qconfig.getConfigurationSection("���ȦC��." + internal + ".���ȻݨD.Item").getKeys(false)) {
								l.add(QuestUtil.getItemStack(qconfig, "���ȦC��." + internal + ".���ȻݨD.Item." + i));
							}
							quest.getRequirements().put(RequirementType.ITEM, l);
						}
						if (qconfig.getStringList("���ȦC��." + internal + ".���ȻݨD.Scoreboard") != null){
							quest.getRequirements().put(RequirementType.SCOREBOARD, qconfig.getStringList("���ȦC��." + internal + ".���ȻݨD.Scoreboard"));
						}
						if (qconfig.getStringList("���ȦC��." + internal + ".���ȻݨD.NBTTag") != null){
							quest.getRequirements().put(RequirementType.NBTTAG, qconfig.getStringList("���ȦC��." + internal + ".���ȻݨD.NBTTag"));
						}
					}
					
					//Triggers
					if (qconfig.getStringList("���ȦC��." + internal + ".����Ĳ�o�ƥ�") != null){
						List<QuestTrigger> list = new ArrayList<>();
						for (String tri : qconfig.getStringList("���ȦC��." + internal + ".����Ĳ�o�ƥ�")){
							String[] Stri = tri.split(" ");
							QuestTrigger trigger = null;
							TriggerType type = TriggerType.valueOf(Stri[0]);
							TriggerObject obj;
							switch(type){
							case TRIGGER_STAGE_START:
							case TRIGGER_STAGE_FINISH:
								obj = TriggerObject.valueOf(Stri[2]);
								String s = Stri[3];
								if (obj.equals(TriggerObject.COMMAND)){
									if (Stri.length > 4){
										for (int k = 4; k < Stri.length; k++){
											s += " " + Stri[k];
										}
									}
								}
								trigger = new QuestTrigger(type, obj, Integer.parseInt(Stri[1]), s);
								break;
							default:
								obj = TriggerObject.valueOf(Stri[1]);
								String t = Stri[2];
								if (obj.equals(TriggerObject.COMMAND)){
									if (Stri.length > 3){
										for (int k = 3; k < Stri.length; k++){
											t += " " + Stri[k];
										}
									}
								}
								trigger = new QuestTrigger(type, obj, t);
								break;
							}
							list.add(trigger);
						}
						quest.setTriggers(list);
					}
					if (qconfig.getBoolean("���ȦC��." + internal + ".�i���ư���")){
						quest.setRedoable(true);
						quest.setRedoDelay(qconfig.getInt("���ȦC��." + internal + ".���ư���ɶ�") * 1000);
					}
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
