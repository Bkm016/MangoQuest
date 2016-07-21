package me.Cutiemango.MangoQuest;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.Cutiemango.MangoQuest.QuestUtil.QuestTitleEnum;
import me.Cutiemango.MangoQuest.questobjects.SimpleQuestObject;

public class QuestProgress {

	public QuestProgress(Quest quest, Player owner) {
		this.quest = quest;
		this.owner = owner;
		CurrentStage = 0;
		objlist = new ArrayList<>();
		for (SimpleQuestObject o : quest.getStage(CurrentStage).getObjects()){
			objlist.add(new QuestObjectProgress(o, 0));
		}
	}
	
	public QuestProgress(Quest q, Player p, int s, List<QuestObjectProgress> o){
		quest = q;
		owner = p;
		CurrentStage = s;
		objlist = o;
	}

	private Quest quest;
	private Player owner;
	private int CurrentStage;
	private List<QuestObjectProgress> objlist;

	public void finish() {
		QuestPlayerData pd = QuestUtil.getData(owner);
		pd.addFinishedQuest(quest);
		pd.removeProgress(quest);
		quest.getQuestReward().giveRewardTo(owner);
		QuestUtil.sendQuestTitle(owner, quest, QuestTitleEnum.FINISH);
		QuestUtil.info(owner, "&b&l���� &f" + quest.getQuestName() + " &b&l�����I");
	}
	
	public void save(FileConfiguration c){
		c.set("���a���." + owner.getUniqueId() + ".���ȶi��." + quest.getInternalID() + ".QuestStage", CurrentStage);
		int t = 0;
		for (QuestObjectProgress qop : objlist){
			c.set("���a���." + owner.getUniqueId() + ".���ȶi��." + quest.getInternalID() + ".QuestObjectProgress." + t, qop.getProgress());
			t++;
		}
	}

//	public void save() {
//		SimpleQuestObject obj = this.quest.getStages().get(CurrentStage).getObjects().get(CurrentObject);
//		Main.config.pconfig.set(
//				"���a���." + this.getOwner().getUniqueId() + ".���ȶi��." + this.quest.getInternalID() + ".QuestStage",
//				this.CurrentStage);
//		Main.config.pconfig.set(
//				"���a���." + this.getOwner().getUniqueId() + ".���ȶi��." + this.quest.getInternalID() + ".QuestObject",
//				obj.toString());
//		if (obj instanceof QuestObjectItemDeliver) {
//			Main.config.pconfig.set(
//					"���a���." + this.getOwner().getUniqueId() + ".���ȶi��." + this.quest.getInternalID() + ".Amount",
//					((QuestObjectItemDeliver) obj).getDeliveredAmount());
//		}
//		if (obj instanceof QuestObjectKillMob) {
//			Main.config.pconfig.set(
//					"���a���." + this.getOwner().getUniqueId() + ".���ȶi��." + this.quest.getInternalID() + ".Amount",
//					((QuestObjectKillMob) obj).getKilledAmount());
//		}
//	}
	
	public void checkIfnextStage(){
		for (QuestObjectProgress o : objlist){
			if (!o.isFinished())
				return;
		}
		nextStage();
	}
	
	public void nextStage(){
		if (CurrentStage + 1 < quest.getStages().size()){
			CurrentStage++;
			owner.sendMessage(ChatColor.translateAlternateColorCodes('&',
					QuestStorage.prefix + " &d&l���� &f" + quest.getQuestName() + " &d&l�w�����i�סG (" + CurrentStage + "/" + quest.getStages().size() + ")"));
			objlist = new ArrayList<>();
			for (SimpleQuestObject o : quest.getStage(CurrentStage).getObjects()){
				objlist.add(new QuestObjectProgress(o, 0));
			}
		}
		else if (CurrentStage + 1 >= quest.getStages().size()) {
			finish();
		}
	}
	

	public List<QuestObjectProgress> getCurrentObjects() {
		return objlist;
	}
	
	public int getCurrentStage(){
		return CurrentStage;
	}

	public Quest getQuest() {
		return this.quest;
	}

	public Player getOwner() {
		return this.owner;
	}
}
