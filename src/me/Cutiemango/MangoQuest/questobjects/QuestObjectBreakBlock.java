package me.Cutiemango.MangoQuest.questobjects;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import me.Cutiemango.MangoQuest.QuestUtil;
import net.md_5.bungee.api.chat.TextComponent;

public class QuestObjectBreakBlock extends SimpleQuestObject{
	
	public QuestObjectBreakBlock(Material m, int i){
		if (!m.isBlock()){
			Bukkit.getLogger().warning("ERROR: argument of QuestObjectBreakBlock does not have a matched Material Object.");
			return;
		}
		block = m;
		amount = i;
	}
	
	private Material block;
	private int amount;

	@Override
	public TextComponent toTextComponent(boolean isFinished) {
		TextComponent text = new TextComponent();
		if (isFinished)
			text = new TextComponent(QuestUtil.translateColor("&8&m&o���� " + amount + " &8&m&o�� "));
		else
			text = new TextComponent(QuestUtil.translateColor("&0���� " + amount + " &0�� "));
		text.addExtra(QuestUtil.translate(block));
		return text;
	}

	@Override
	public String toPlainText() {
		return QuestUtil.translateColor("&a���� " + amount + " &a�� " + QuestUtil.translate(block));
	}
	
	public Material getType(){
		return block;
	}
	
	public int getAmount(){
		return amount;
	}
}
