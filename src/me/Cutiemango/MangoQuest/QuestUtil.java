package me.Cutiemango.MangoQuest;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Cutiemango.MangoQuest.data.QuestPlayerData;
import me.Cutiemango.MangoQuest.model.Quest;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_10_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_10_R1.PacketPlayOutTitle.EnumTitleAction;

public class QuestUtil {
	
	public static String translateColor(String s){
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	public static ItemStack getItemStack(FileConfiguration config, String path) {
		Material m = Material.getMaterial(config.getString(path + ".���O"));
		int amount = config.getInt(path + ".�ƶq");
		ItemStack is = new ItemStack(m, amount);
		if (config.getString(path + ".�W��") != null) {
			String name = ChatColor.translateAlternateColorCodes('&', config.getString(path + ".�W��"));
			List<String> lore = new ArrayList<>();
			for (String s : config.getStringList(path + ".����")) {
				lore.add(ChatColor.translateAlternateColorCodes('&', s));
			}
			ItemMeta im = is.getItemMeta();
			im.setDisplayName(name);
			im.setLore(lore);
			is.setItemMeta(im);
		}
		return is;
	}
	
	public static void sendTitle(Player p, Integer fadeIn, Integer stay, Integer fadeOut, String title,
			String subtitle) {
		if (title != null) {
			title = ChatColor.translateAlternateColorCodes('&', title);
			PacketPlayOutTitle ppot = new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\":\"" + title + "\"}"), fadeIn, stay, fadeOut);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppot);
		}
		if (subtitle != null) {
			subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
			PacketPlayOutTitle ppot = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, ChatSerializer.a("{\"text\":\"" + subtitle + "\"}"), fadeIn, stay, fadeOut);
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(ppot);
		}
	}

	public enum QuestTitleEnum{
		ACCEPT, FINISH, QUIT;
	}
	
	public static void sendQuestTitle(Player target, Quest quest, QuestTitleEnum e) {
		switch(e){
			case ACCEPT:
				sendTitle(target, 1, 3, 1, "&b&l��������", quest.getQuestName());
				break;
			case FINISH:
				sendTitle(target, 1, 3, 1, "&6&l��������", quest.getQuestName());
				break;
			case QUIT:
				sendTitle(target, 1, 3, 1, "&c&l������", quest.getQuestName());
				break;
		}
	}
	
	public static QuestPlayerData getData(Player p){
		return QuestStorage.Players.get(p.getName());
	}
	
	public static Quest getQuest(String s){
		return QuestStorage.Quests.get(s);
	}
	
	public static void info(Player p, String s){
		p.sendMessage(QuestStorage.prefix + " " + translateColor(s));
		return;
	}
	
	public static void error(Player p, String s){
		p.sendMessage(translateColor("&cError> " + s));
		return;
	}
	
	public static void warnCmd(Class<?> clazz, String s){
		Bukkit.getLogger().warning("�ѪR " + clazz.getClass().getName() + ".class �ɵo�Ϳ��~�A���ˬd�]�w�ɡC");
		Bukkit.getLogger().warning("�Y�z�T�{�o�O��BUG�A�Ц^���}�o�̡C");
		Bukkit.getLogger().warning(s);
	}
	
	public static String convertTime(long l){
		String s = "";

		long days = l / 86400000;
		long hours = (l % 86400000) / 3600000;
		long minutes = ((l % 86400000) % 3600000) / 60000;
		long seconds = (((l % 86400000) % 3600000) % 60000) / 1000;
		
		if (days > 0)
			s += days + " ��,";
		if (hours > 0)
			s += hours + " �p��,";
		if (minutes > 0)
			s += minutes + " ����,";
		if (seconds > 0)
			s += seconds + " ��";
		return s;
	}
	
	public static String translate(Material m){
		if (!QuestStorage.TranslateMap.containsKey(m))
			return "����";
		else return QuestStorage.TranslateMap.get(m);
	}
	
	public static String translate(EntityType e){
		if (!QuestStorage.EntityTypeMap.containsKey(e))
			return "����";
		else return QuestStorage.EntityTypeMap.get(e);
	}
}
