package me.Cutiemango.MangoQuest.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.Cutiemango.MangoQuest.Main;
import me.Cutiemango.MangoQuest.QuestConfigLoad;
import me.Cutiemango.MangoQuest.QuestStorage;
import me.Cutiemango.MangoQuest.QuestUtil;
import me.Cutiemango.MangoQuest.QuestUtil.QuestTitleEnum;
import me.Cutiemango.MangoQuest.model.Quest;
import me.Cutiemango.MangoQuest.model.QuestRequirement;
import me.Cutiemango.MangoQuest.model.QuestTrigger;
import me.Cutiemango.MangoQuest.model.QuestTrigger.TriggerType;
import me.Cutiemango.MangoQuest.questobjects.QuestObjectBreakBlock;
import me.Cutiemango.MangoQuest.questobjects.QuestObjectItemDeliver;
import me.Cutiemango.MangoQuest.questobjects.QuestObjectKillMob;
import me.Cutiemango.MangoQuest.questobjects.QuestObjectTalkToNPC;
import me.Cutiemango.MangoQuest.questobjects.SimpleQuestObject;
import net.citizensnpcs.api.npc.NPC;

public class QuestPlayerData {

	public QuestPlayerData(Player p) {
		this.p = p;
	}
	
	public QuestPlayerData(Player p, FileConfiguration c){
		this.p = p;
		c.set("���a���." + p.getUniqueId() + ".���aID", p.getName());
		if (c.getConfigurationSection("���a���." + p.getUniqueId() + ".���ȶi��") != null){
			for (String index : c.getConfigurationSection("���a���." + p.getUniqueId() + ".���ȶi��").getKeys(false)){
				if (QuestStorage.Quests.get(index) == null){
					QuestUtil.error(p, "�z�����a��Ʀ����s�b�θg�Q���������ȡA�w�g�򥢸�ơI"
							+ "�򥢪����Ȥ����X�G " + index + "�A�Y�zı�o�o�����ӵo�͡A�Ц^���޲z���C");
					continue;
				}
				Quest q = QuestStorage.Quests.get(index);
				int t = 0;
				int s = c.getInt("���a���." + p.getUniqueId() + ".���ȶi��." + index + ".QuestStage");
				List<QuestObjectProgress> qplist = new ArrayList<>();
				for (SimpleQuestObject ob : q.getStage(s).getObjects()){
					QuestObjectProgress qp = new QuestObjectProgress(ob, c.getInt("���a���." + p.getUniqueId() + ".���ȶi��." + index + ".QuestObjectProgress." + t));
					qp.checkIfFinished();
					qplist.add(qp);
					t++;
				}
				CurrentQuest.add(new QuestProgress(q, p, s, qplist));
			}
		}
		
		if (c.isConfigurationSection("���a���." + p.getUniqueId() + ".�w����������")){
			for (String s : c.getConfigurationSection("���a���." + p.getUniqueId() + ".�w����������").getKeys(false)){
				if (QuestStorage.Quests.get(s) == null){
					QuestUtil.error(p, "�z�����a��Ʀ����s�b�θg�Q���������ȡA�w�g�򥢸�ơI"
							+ "�򥢪����Ȥ����X�G " + s + "�A�Y�zı�o�o�����ӵo�͡A�Ц^���޲z���C");
					continue;
				}
				QuestFinishData qd = new QuestFinishData(QuestStorage.Quests.get(s) 
						,c.getInt("���a���." + p.getUniqueId() + ".�w����������." + s + ".FinishedTimes")
						,c.getLong("���a���." + p.getUniqueId() + ".�w����������." + s + ".LastFinishTime"));
				FinishedQuest.add(qd);
			}
		}
		QuestUtil.info(p, "&a���a���ȸ��Ū�������I");
	}

	private Player p;
	private List<QuestProgress> CurrentQuest = new ArrayList<>();
	private List<QuestFinishData> FinishedQuest = new ArrayList<>();

	public void save() {
		QuestConfigLoad.pconfig.set("���a���." + p.getUniqueId() + ".���aID", p.getName());
		for (QuestFinishData q : FinishedQuest) {
			String id = q.getQuest().getInternalID();
			QuestConfigLoad.pconfig.set("���a���." + p.getUniqueId() + ".�w����������." + id + ".FinishedTimes", q.getFinishedTimes());
			QuestConfigLoad.pconfig.set("���a���." + p.getUniqueId() + ".�w����������." + id + ".LastFinishTime", q.getLastFinish());
		}
		
		QuestConfigLoad.pconfig.set("���a���." + p.getUniqueId() + ".���ȶi��", "");
		
		if (!CurrentQuest.isEmpty()){
			for (QuestProgress qp : CurrentQuest) {
				qp.save(QuestConfigLoad.pconfig);
			}
		}
		try {
			QuestConfigLoad.pconfig.save(new File(Main.instance.getDataFolder(), "players.yml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Player getPlayer() {
		return p;
	}

	public boolean hasFinished(Quest q) {
		for (QuestFinishData qd : FinishedQuest){
			if (qd.getQuest().getInternalID().equals(q.getInternalID()))
				return true;
		}
		return false;
	}
	
	public QuestProgress getProgress(Quest q){
		for (QuestProgress qp : CurrentQuest){
			if (q.getInternalID().equals(qp.getQuest().getInternalID())) {
				return qp;
			}
		}
		return null;
	}

	public List<QuestProgress> getProgresses() {
		return CurrentQuest;
	}
	
	public void takeQuest(Quest q){
		if (CurrentQuest.size() + 1 > 4){
			QuestUtil.info(p, "&c�A�����ȦC��w���A����A�������ȤF�C");
			return;
		}
		for (QuestProgress qp : CurrentQuest){
			if (q.getInternalID().equals(qp.getQuest().getInternalID())) {
				QuestUtil.info(p, "&c�A�����ȦC��w�������ȡA����A�������ȤF�C");
				return;
			}
		}
		if (q.hasRequirement()){
			for (QuestRequirement r : q.getRequirements()){
				if (!r.meetRequirementWith(p)){
					QuestUtil.info(p, q.getFailMessage());
					return;
				}
			}
		}
		if (hasFinished(q)){
			long d = getDelay(getFinishData(q).getLastFinish(), q.getRedoDelay());
			if (d > 0){
				QuestUtil.info(p, "&c�A�����A���� " + QuestUtil.convertTime(d) + " �~��A�ױ����o�ӥ��ȡC");
				return;
			}
		}
		if (q.hasTrigger()){
			for (QuestTrigger t : q.getTriggers()){
				if (t.getType().equals(TriggerType.TRIGGER_ON_TAKE)){
					t.trigger(p);
					continue;
				}
				else if (t.getType().equals(TriggerType.TRIGGER_STAGE_START) && t.getCount() == 1){
					t.trigger(p);
					continue;
				}
			}
		}
		CurrentQuest.add(new QuestProgress(q, p));
		QuestUtil.sendQuestTitle(p, q, QuestTitleEnum.ACCEPT);
	}
	
	public void quitQuest(Quest q){
		for (QuestTrigger t : q.getTriggers()){
			if (t.getType().equals(TriggerType.TRIGGER_ON_QUIT)){
				t.trigger(p);
				continue;
			}
		}
		removeProgress(q);
		QuestUtil.sendQuestTitle(p, q, QuestTitleEnum.QUIT);
	}
	
	public void breakBlock(Material m){
		for (QuestProgress qp : CurrentQuest){
			for (QuestObjectProgress qop : qp.getCurrentObjects()){
				if (qop.isFinished())
					continue;
				if (qop.getObject() instanceof QuestObjectBreakBlock){
					QuestObjectBreakBlock o = (QuestObjectBreakBlock)qop.getObject();
					if (o.getType().equals(m)){
						qop.setProgress(qop.getProgress() + 1);
						qop.checkIfFinished();
						if (qop.getProgress() == o.getAmount())
							QuestUtil.info(p, o.toPlainText() + " &a(�w����)");
						else
							QuestUtil.info(p, o.toPlainText() + " &6�i�סG (" + qop.getProgress() + "/" + o.getAmount() + ")");
						qp.checkIfnextStage();
						return;
					}
				}
			}
		}
	}
	
	public void talkToNPC(NPC npc){
		for (QuestProgress qp : CurrentQuest){
			for (QuestObjectProgress qop : qp.getCurrentObjects()){
				if (qop.isFinished())
					continue;
				if (qop.getObject() instanceof QuestObjectTalkToNPC){
					if (((QuestObjectTalkToNPC)qop.getObject()).getTargetNPC().equals(npc)){
						QuestObjectTalkToNPC o = (QuestObjectTalkToNPC)qop.getObject();
						qop.finish();
						qop.setProgress(1);
						QuestUtil.info(p, o.toPlainText() + " &a(�w����)");
						qp.checkIfnextStage();
						return;
					}
				}
			}
		}
	}
	
	public void deliverItem(NPC npc){
		for (QuestProgress qp : CurrentQuest){
			for (QuestObjectProgress qop : qp.getCurrentObjects()){
				if (qop.isFinished())
					continue;
				if (qop.getObject() instanceof QuestObjectItemDeliver){
					QuestObjectItemDeliver o = (QuestObjectItemDeliver)qop.getObject();
					if (o.getTargetNPC().equals(npc) && o.getDeliverItem().isSimilar(p.getInventory().getItemInMainHand())){
						if (p.getInventory().getItemInMainHand().getAmount() > (o.getDeliverAmount() - qop.getProgress())){
							p.getInventory().getItemInMainHand().setAmount(
									p.getInventory().getItemInMainHand().getAmount() - (o.getDeliverAmount() - qop.getProgress()));
							qop.setProgress(o.getDeliverAmount());
							qop.checkIfFinished();
							QuestUtil.info(p, o.toPlainText() + " &a(�w����)");
							qp.checkIfnextStage();
							return;
						}
						else if (p.getInventory().getItemInMainHand().getAmount() == (o.getDeliverAmount() - qop.getProgress())){
							p.getInventory().setItemInMainHand(null);
							qop.setProgress(o.getDeliverAmount());
							qop.checkIfFinished();
							QuestUtil.info(p, o.toPlainText() + " &a(�w����)");
							qp.checkIfnextStage();
							return;
						}
						else{
							qop.checkIfFinished();
							qop.setProgress(qop.getProgress() + p.getInventory().getItemInMainHand().getAmount());
							p.getInventory().setItemInMainHand(null);
							QuestUtil.info(p, o.toPlainText() + " &6�i�סG (" + qop.getProgress() + "/" + o.getDeliverAmount() + ")");
							qp.checkIfnextStage();
							return;
						}
					}
				}
			}
		}
	}
	
	public void killEntity(Entity e){
		for (QuestProgress qp : CurrentQuest){
			for (QuestObjectProgress qop : qp.getCurrentObjects()){
				if (qop.isFinished())
					continue;
				if (qop.getObject() instanceof QuestObjectKillMob){
					QuestObjectKillMob o = (QuestObjectKillMob)qop.getObject();
					if (o.hasCustomName()){
						if (e.getCustomName() == null || !e.getCustomName().equals(o.getCustomName()) || !e.getType().equals(o.getType()))
							return;
						else{
							qop.setProgress(qop.getProgress() + 1);
							qop.checkIfFinished();
							qp.checkIfnextStage();
							if (qop.getProgress() == o.getAmount())
								QuestUtil.info(p, o.toPlainText() + " &a(�w����)");
							else
								QuestUtil.info(p, o.toPlainText() + " &6�i�סG (" + qop.getProgress() + "/" + o.getAmount() + ")");
							return;
						}
					}
					else{
						if (e.getType().equals(o.getType())){
							qop.setProgress(qop.getProgress() + 1);
							qop.checkIfFinished();
							qp.checkIfnextStage();
							if (qop.getProgress() == o.getAmount())
								QuestUtil.info(p, o.toPlainText() + " &a(�w����)");
							else
								QuestUtil.info(p, o.toPlainText() + " &6�i�סG (" + qop.getProgress() + "/" + o.getAmount() + ")");
							return;
						}
					}
				}
			}
		}
	}

	public void removeProgress(Quest q) {
		for (QuestProgress qp : CurrentQuest){
			if (q.getInternalID().equals(qp.getQuest().getInternalID())) {
				CurrentQuest.remove(qp);
				break;
			}
		}
	}
	
	public QuestFinishData getFinishData(Quest q){
		if (!hasFinished(q))
			return null;
		for (QuestFinishData qd : FinishedQuest){
			if (qd.getQuest().getInternalID().equals(q.getInternalID())){
				return qd;
			}
		}
		return null;
	}

	public void addFinishedQuest(Quest q) {
		if (hasFinished(q)){
			getFinishData(q).finish();
			return;
		}
		FinishedQuest.add(new QuestFinishData(q, 1, System.currentTimeMillis()));
		return;
	}
	
	public static boolean hasConfigData(Player p){
		return !(QuestConfigLoad.pconfig.getString("���a���." + p.getUniqueId() + ".���aID") == null);
	}
	
	private long getDelay(long last, long quest){
		return quest - (System.currentTimeMillis() - last);
	}
}
