package me.Cutiemango.MangoQuest.editor;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Cutiemango.MangoQuest.QuestGUIManager;
import me.Cutiemango.MangoQuest.QuestUtil;
import me.Cutiemango.MangoQuest.TextComponentFactory;
import me.Cutiemango.MangoQuest.model.Quest;
import me.Cutiemango.MangoQuest.model.QuestTrigger;
import me.Cutiemango.MangoQuest.model.QuestTrigger.TriggerObject;
import me.Cutiemango.MangoQuest.model.QuestTrigger.TriggerType;
import me.Cutiemango.MangoQuest.model.RequirementType;
import net.md_5.bungee.api.chat.TextComponent;

public class QuestEditorManager {

	public static HashMap<String, Quest> isEditing = new HashMap<>();
	
	public static boolean isInEditorMode(Player p){
		return isEditing.containsKey(p.getName());
	}
	
	public static Quest getCurrentEditingQuest(Player p){
		if (!isInEditorMode(p))
			return null;
		else return isEditing.get(p.getName());
	}
	
	public static void edit(Player p, Quest q){
		isEditing.put(p.getName(), q);
		editQuest(p);
	}
	
	public static void exit(Player p){
		if (!isInEditorMode(p))
			return;
		else
			isEditing.remove(p.getName());
	}
	
	public static void editQuest(Player p){
		if (!QuestEditorManager.isInEditorMode(p)){
			QuestUtil.error(p, "�A���b�s��Ҧ����I");
			return;
		}
		Quest q = QuestEditorManager.getCurrentEditingQuest(p);
		TextComponent p1 = new TextComponent(QuestUtil.translateColor("&0&l�s����ȡG " + q.getQuestName()));
		TextComponent p2 = new TextComponent(QuestUtil.translateColor("&0���ȴ��n�G \n" + q.getQuestOutline()));
		p1.addExtra("\n");
		p1.addExtra(QuestUtil.translateColor("&0���Ȥ����X�G " + q.getInternalID()));
		p1.addExtra("\n");
		p1.addExtra(QuestUtil.translateColor("&0���ȦW�١G " + q.getQuestName()));
		p1.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit name"));
		p1.addExtra("\n");
		p1.addExtra(QuestUtil.translateColor("&0����NPC�G "));
		p1.addExtra(TextComponentFactory.convertLocationtoHoverEvent(q.getQuestNPC().getName(), q.getQuestNPC().getEntity().getLocation(), false));
		p1.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit npc"));
		p1.addExtra("\n");
		p1.addExtra(QuestUtil.translateColor("&0���ȻݨD�G "));
		p1.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit req"));
		p1.addExtra("\n");
		p1.addExtra(QuestUtil.translateColor("&0���Ȩƥ�G "));
		p1.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit evt"));
		p1.addExtra("\n");
		p1.addExtra(QuestUtil.translateColor("&0�O�_�i���ư���G " + Boolean.toString(q.isRedoable())));
		p1.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit redo"));
		p1.addExtra("\n");
		if (q.isRedoable()){
			p1.addExtra(QuestUtil.translateColor("&0���_����CD�ɶ��G \n" + QuestUtil.convertTime(q.getRedoDelay())));
			p1.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit redodelay"));
			p1.addExtra("\n");
		}
		
		
		p2.addExtra("\n");
		p2.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit outline"));
		QuestGUIManager.openBook(p, p1, p2);
	}
	
	public static void editQuestTrigger(Player p){
		if (!QuestEditorManager.isInEditorMode(p)){
			QuestUtil.error(p, "�A���b�s��Ҧ����I");
			return;
		}
		Quest q = QuestEditorManager.getCurrentEditingQuest(p);
		TextComponent p1 = new TextComponent(QuestUtil.translateColor("&0&l�s����Ȩƥ�G " + q.getQuestName()));
		p1.addExtra("\n");
		int index = 0;
		for (QuestTrigger qt : q.getTriggers()){
			p1.addExtra("- " + index + ".");
			if (qt.getType().equals(TriggerType.TRIGGER_STAGE_START)
					|| qt.getType().equals(TriggerType.TRIGGER_STAGE_FINISH)) {
				p1.addExtra(TextComponentFactory.registerHoverStringEvent(qt.getTriggerObject().toCustomString(),
						"Ĳ�o�ɾ��G " + qt.getType().toCustomString(qt.getCount()) + "\nĲ�o���󤺮e�G "
								+ qt.getObject().toString()));
				p1.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit evt " + index + " "
						+ qt.getType().toString() + " " + qt.getCount() + " " + qt.getTriggerObject().toString()));
			} else {
				p1.addExtra(TextComponentFactory.registerHoverStringEvent(qt.getTriggerObject().toCustomString(),
						"Ĳ�o�ɾ��G " + qt.getType().toCustomString() + "\nĲ�o���󤺮e�G " + qt.getObject().toString()));
				p1.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit evt " + index + " "
						+ qt.getType().toString() + " " + qt.getTriggerObject().toString()));
			}
			p1.addExtra(TextComponentFactory.registerClickCommandEvent("&7[����]", "/mqe remove evt " + index));
			p1.addExtra("\n");
			index++;
		}
		p1.addExtra(TextComponentFactory.registerClickCommandEvent("&0&l[�s�W]", "/mqe addnew evt"));
		QuestGUIManager.openBook(p, p1);
	}
	
	@SuppressWarnings("unchecked")
	public static void editQuestRequirement(Player p){
		if (!QuestEditorManager.isInEditorMode(p)){
			QuestUtil.error(p, "�A���b�s��Ҧ����I");
			return;
		}
		Quest q = QuestEditorManager.getCurrentEditingQuest(p);
		TextComponent p1 = new TextComponent(QuestUtil.translateColor("&0&l�s����ȻݨD�G " + q.getQuestName()));
		TextComponent p2 = new TextComponent(QuestUtil.translateColor(""));
		p1.addExtra("\n");
		for (RequirementType t : RequirementType.values()){
			switch(t){
			case ITEM:
				p1.addExtra("���~�ݨD�G");
				p1.addExtra("\n");
				int i = 0;
				for (ItemStack item : (List<ItemStack>)q.getRequirements().get(t)){
					p1.addExtra("- ");
					p1.addExtra(TextComponentFactory.convertItemStacktoHoverEvent(false, item));
					p1.addExtra(QuestUtil.translateColor(" &0&l" + item.getAmount() + "&0 ��"));
					p1.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit req ITEM " + i));
					p1.addExtra(TextComponentFactory.registerClickCommandEvent("&7[����]", "/mqe remove req ITEM " + i));
					p1.addExtra("\n");
					i++;
				}
				p1.addExtra(TextComponentFactory.registerClickCommandEvent("&0&l[�s�W]", "/mqe addnew req ITEM " + i));
				p1.addExtra("\n");
				break;
			case LEVEL:
				p1.addExtra("���ŻݨD�G " + q.getRequirements().get(t).toString() + " ");
				p1.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit req LEVEL"));
				p1.addExtra("\n");
				break;
			case MONEY:
				p1.addExtra("�����ݨD�G " + q.getRequirements().get(t).toString() + " ");
				p1.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit req MONEY"));
				p1.addExtra("\n");
				break;
				
			case NBTTAG:
				p2.addExtra("NBT���һݨD�G");
				p2.addExtra("\n");
				i = 0;
				for (String s : (List<String>)q.getRequirements().get(t)){
					p2.addExtra("- ");
					p2.addExtra(s);
					p2.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit req NBTTAG " + i));
					p2.addExtra(TextComponentFactory.registerClickCommandEvent("&7[����]", "/mqe remove req NBTTAG " + i));
					p2.addExtra("\n");
					i++;
				}
				p2.addExtra(TextComponentFactory.registerClickCommandEvent("&0&l[�s�W]", "/mqe addnew req NBTTAG " + i));
				p2.addExtra("\n");
				break;
			case QUEST:
				p2.addExtra("���ȻݨD�G");
				p2.addExtra("\n");
				i = 0;
				for (String s : (List<String>)q.getRequirements().get(t)){
					p2.addExtra("- ");
					p2.addExtra(s);
					p2.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit req QUEST " + i));
					p2.addExtra(TextComponentFactory.registerClickCommandEvent("&7[����]", "/mqe remove req QUEST " + i));
					p2.addExtra("\n");
					i++;
				}
				p2.addExtra(TextComponentFactory.registerClickCommandEvent("&0&l[�s�W]", "/mqe addnew req QUEST " + i));
				p2.addExtra("\n");
				break;
			case SCOREBOARD:
				p2.addExtra("�O���O�ݨD�G");
				p2.addExtra("\n");
				i = 0;
				for (String s : (List<String>)q.getRequirements().get(t)){
					p2.addExtra("- ");
					p2.addExtra(s);
					p2.addExtra(TextComponentFactory.registerClickCommandEvent("&7[�s��]", "/mqe edit req SCOREBOARD " + i));
					p2.addExtra(TextComponentFactory.registerClickCommandEvent("&7[����]", "/mqe remove req SCOREBOARD " + i));
					p2.addExtra("\n");
					i++;
				}
				p2.addExtra(TextComponentFactory.registerClickCommandEvent("&0&l[�s�W]", "/mqe addnew req SCOREBOARD " + i));
				p2.addExtra("\n");
				break;
			}
		}
		
		QuestGUIManager.openBook(p, p1, p2);
	}
	
	public static void selectTriggerType(Player p){
		TextComponent p1 = new TextComponent(QuestUtil.translateColor("&0&l���Ĳ�o�ƥ�ɾ��G \n"));
		for (TriggerType t : TriggerType.values()){
			p1.addExtra(TextComponentFactory.registerClickCommandEvent("- [" + t.toCustomString() + "]", "/mqe addnew evt " + t.toString()));
			p1.addExtra("\n");
		}
		QuestGUIManager.openBook(p, p1);
	}
	
	public static void selectStage(Player p, TriggerType t){
		TextComponent p1 = new TextComponent(QuestUtil.translateColor("&0&l���Ĳ�o�ƥ󶥬q�G \n"));
		Quest q = QuestEditorManager.getCurrentEditingQuest(p);
		for (int s = 1; s <= q.getStages().size(); s++){
			p1.addExtra(TextComponentFactory.registerClickCommandEvent("- [���q" + s + "]", "/mqe addnew evt " + t.toString() + " " + s));
			p1.addExtra("\n");
		}
		QuestGUIManager.openBook(p, p1);
	}
	
	public static void selectTriggerObject(Player p, TriggerType t, int s){
		TextComponent p1 = new TextComponent(QuestUtil.translateColor("&0&l���Ĳ�o����G \n"));
		for (TriggerObject o : TriggerObject.values()){
			if (t.equals(TriggerType.TRIGGER_STAGE_START) || t.equals(TriggerType.TRIGGER_STAGE_FINISH)){
				p1.addExtra(TextComponentFactory.registerClickCommandEvent("- [" + o.toCustomString() + "]", "/mqe addnew evt " + t.toString() + " " + s + " " + o.toString()));
				p1.addExtra("\n");
				continue;
			}
			p1.addExtra(TextComponentFactory.registerClickCommandEvent("- [" + o.toCustomString() + "]", "/mqe addnew evt " + t.toString() + " " + o.toString()));
			p1.addExtra("\n");
		}
		QuestGUIManager.openBook(p, p1);
	}
	
	
}
