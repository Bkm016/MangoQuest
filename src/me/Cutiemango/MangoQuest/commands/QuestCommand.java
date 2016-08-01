package me.Cutiemango.MangoQuest.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Cutiemango.MangoQuest.QuestGUIManager;
import me.Cutiemango.MangoQuest.QuestStorage;
import me.Cutiemango.MangoQuest.QuestUtil;
import me.Cutiemango.MangoQuest.data.QuestPlayerData;
import me.Cutiemango.MangoQuest.data.QuestProgress;
import me.Cutiemango.MangoQuest.editor.QuestEditorManager;
import me.Cutiemango.MangoQuest.model.Quest;

public class QuestCommand implements CommandExecutor{
	
	private List<String> confirm = new ArrayList<>();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player))
			return false;
		Player p = (Player) sender;
		if (cmd.getName().equals("mq")){
			if (args.length <= 1){
				if (args[0].equalsIgnoreCase("list")){
					QuestGUIManager.openJourney(p);
					return true;
				}
				sendHelp(p);
				return false;
			}
			if (QuestStorage.Quests.get(args[1]) == null){
				QuestUtil.error(p, "�A�ҭn�䪺���Ȥ��s�b�I");
				return false;
			}
			Quest quest = QuestStorage.Quests.get(args[1]);
			QuestPlayerData qd = QuestUtil.getData(p);
			switch(args[0]){
				case "view":
					if (qd.getProgress(quest) == null){
						QuestGUIManager.openGUI(p, new QuestProgress(quest, p));
						return true;
					}
					QuestGUIManager.openGUI(p, qd.getProgress(quest));
					return true;
				case "take":
					qd.takeQuest(quest);
					return true;
				case "quit":
					qd.quitQuest(quest);
					return true;
				case "editor":
					if (QuestEditorManager.isInEditorMode(p)){
						if (!confirm.contains(p.getName())){
							QuestUtil.error(p, "�ثe�o�{�z�w�g�����b�s�誺���ȡA�}�l�o�ӫ��w�����Ȩðh�X�ܡH");
							QuestUtil.error(p, "�Y&a&l�T�w&c�ЦA�׿�J�@���C");
							return false;
						}
					}
					QuestEditorManager.edit(p, quest);
					
				}
		
		}
		return false;
	}
	
	private void sendHelp(Player p){
		QuestUtil.info(p, "���O���U�G");
		QuestUtil.info(p, "/mq list - �d�ݥ��ȲM��");
		QuestUtil.info(p, "/mq view [���Ȥ����W��] - �d�ݥ��ȸ��");
		QuestUtil.info(p, "/mq take [���Ȥ����W��] - �������w����");
		QuestUtil.info(p, "/mq quit [���Ȥ����W��] - �����w����");
		QuestUtil.info(p, "/mq editor [���Ȥ����W��] - �s����w����(�i�J�s��Ҧ�)");
	}
	

}
