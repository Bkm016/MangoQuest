package me.Cutiemango.MangoQuest.questobjects;

import org.bukkit.inventory.ItemStack;

import me.Cutiemango.MangoQuest.QuestUtil;
import me.Cutiemango.MangoQuest.TextComponentFactory;
import net.citizensnpcs.api.npc.NPC;
import net.md_5.bungee.api.chat.TextComponent;

public class QuestObjectItemDeliver extends NumerableObject{
	
	public QuestObjectItemDeliver(NPC n, ItemStack is, int deliveramount) {
		npc = n;
		item = is;
		amount = deliveramount;
	}

	private NPC npc;
	private ItemStack item;

	public NPC getTargetNPC() {
		return npc;
	}

	public void setTargetNPC(NPC targetNPC) {
		npc = targetNPC;
	}

	public ItemStack getDeliverItem() {
		return item;
	}

	public void setDeliverItem(ItemStack deliverItem) {
		item = deliverItem;
	}

	@Override
	public TextComponent toTextComponent(boolean isFinished) {
		TextComponent text = new TextComponent();
		if (isFinished){
			text = new TextComponent(QuestUtil.translateColor("&8&m&o���� &8&m&o"));
			text.addExtra(TextComponentFactory.convertItemStacktoHoverEvent(isFinished, item));
			TextComponent suffix = new TextComponent(
					QuestUtil.translateColor(" &8&m&o" + amount + " &8&m&o�� �� "));
			text.addExtra(suffix);
		}
		else{
			text = new TextComponent(QuestUtil.translateColor("&0���� "));
			text.addExtra(TextComponentFactory.convertItemStacktoHoverEvent(isFinished, item));
			TextComponent suffix = new TextComponent(
					QuestUtil.translateColor(" &0&l" + amount + " &0�� �� "));
			text.addExtra(suffix);
		}
		text.addExtra(TextComponentFactory.convertLocationtoHoverEvent(npc.getName(), npc.getEntity().getLocation(), isFinished));
		return text;
	}

	@Override
	public String toPlainText() {
		if (item.getItemMeta().hasDisplayName())
			return QuestUtil.translateColor("&a���� " + item.getItemMeta().getDisplayName() + " &f" + item + " &a�� �� " + npc.getName());
		else
			return QuestUtil.translateColor("&a���� &f" + QuestUtil.translate(item.getType()) + " &f" + amount + " &a�� �� " + npc.getName());
	}
	
	

}
