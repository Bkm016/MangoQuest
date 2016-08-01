package me.Cutiemango.MangoQuest.questobjects;

import org.bukkit.inventory.ItemStack;

import me.Cutiemango.MangoQuest.QuestUtil;
import me.Cutiemango.MangoQuest.TextComponentFactory;
import net.md_5.bungee.api.chat.TextComponent;

public class QuestObjectItemConsume extends NumerableObject{
	
	public QuestObjectItemConsume(ItemStack is, int i){
//		if (!is.getType().equals(Material.POTION) || !is.getType().isEdible()){
//			Bukkit.getLogger().warning("ERROR: argument of QuestObjectConsume does not have a matched Material Object.");
//			return;
//		}
		item = is;
		amount = i;
	}
	
	private ItemStack item;

	@Override
	public TextComponent toTextComponent(boolean isFinished) {
		TextComponent text = new TextComponent();
		if (isFinished){
			text = new TextComponent(QuestUtil.translateColor("&8&m&o�ϥ� "));
			text.addExtra(QuestUtil.translateColor(amount + " &8&m&o�� "));
			text.addExtra(TextComponentFactory.convertItemStacktoHoverEvent(true, item));
			return text;
		}
		else{
			text = new TextComponent(QuestUtil.translateColor("&0�ϥ� "));
			text.addExtra(QuestUtil.translateColor(amount + " &0�� "));
			text.addExtra(TextComponentFactory.convertItemStacktoHoverEvent(false, item));
			return text;
		}
	}

	@Override
	public String toPlainText() {
		if (item.getItemMeta().hasDisplayName())
			return QuestUtil.translateColor("&a���� " + amount + " �� " + item.getItemMeta().getDisplayName());
		else
			return QuestUtil.translateColor("&a���� " + amount + " �� " + QuestUtil.translate(item.getType()));
	}
	
	public ItemStack getItem(){
		return item;
	}

}
