package me.Cutiemango.MangoQuest;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public class QuestReward {

	private double REWARD_MONEY;
	private List<ItemStack> REWARD_ITEMS = new ArrayList<>();

	public QuestReward(ItemStack is) {
		this.REWARD_ITEMS.add(is);
	}

	public QuestReward(double amount) {
		this.REWARD_MONEY = amount;
	}

	public void add(ItemStack is) {
		this.REWARD_ITEMS.add(is);
	}

	public void add(double money) {
		this.REWARD_MONEY += money;
	}

	public void remove(ItemStack is) {
		if (REWARD_ITEMS.contains(is))
			this.REWARD_ITEMS.remove(is);
		else
			return;
	}

	public void remove(double money) {
		if (this.REWARD_MONEY < money)
			this.REWARD_MONEY = 0;
		else
			this.REWARD_MONEY -= money;
	}

	public boolean hasItem() {
		return !(this.REWARD_ITEMS.isEmpty());
	}

	public boolean hasMoney() {
		return !(this.REWARD_MONEY == 0.0D);
	}

	public List<ItemStack> getItems() {
		return this.REWARD_ITEMS;
	}

	public double getMoney() {
		return this.REWARD_MONEY;
	}

	public void giveRewardTo(Player p) {
		if (this.hasItem()) {
			for (ItemStack is : this.REWARD_ITEMS) {
				if (p.getInventory().firstEmpty() == -1) {
					p.sendMessage(QuestStorage.prefix + ChatColor.RED + "�I�]���~�L�h�A�A�����ȼ��y "
							+ is.getItemMeta().getDisplayName() + ChatColor.RED + " �����a���I");
					p.getWorld().dropItem(p.getLocation(), is);
					return;
				} else {
					p.getInventory().addItem(is);
					if (is.getItemMeta().hasDisplayName())
						QuestUtil.info(p, "&e&l�A�o��F���ȼ��y " + is.getItemMeta().getDisplayName() + " &f" + is.getAmount() + " &e&l��");
					else
						QuestUtil.info(p, "&e&l�A�o��F���ȼ��y " + QuestUtil.translateItemStackToChinese(is) + " &f" + is.getAmount() + " &e&l��");
				}
			}
		}

		if (this.hasMoney()) {
			Main.economy.depositPlayer(p, this.REWARD_MONEY);
			QuestUtil.info(p, "&e&l�A�o��F���ȼ��y  &f" + REWARD_MONEY + " &e&l��");
		}
	}
}
