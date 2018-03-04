package me.Cutiemango.MangoQuest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;

import lombok.Getter;
import me.Cutiemango.MangoQuest.manager.QuestChatManager;
import me.skymc.taboolib.string.language2.Language2;
import me.skymc.taboolib.string.language2.Language2Value;

public class I18n
{

//	private static ResourceBundle bundle;
	
	@Getter
	private static Language2 language;

	public static void init(Locale local)
	{
		// 载入语言文件
		language = new Language2(Main.getInstance());
		
//		try
//		{
//			Main.getInstance().saveResource("messages_" + ConfigSettings.LOCALE_USING.toString() + ".properties", true);
//			Main.getInstance().saveResource("lang/original_" + ConfigSettings.LOCALE_USING.toString() + ".yml", 
//					!new File("lang/original_" + ConfigSettings.LOCALE_USING.toString() + ".yml").exists());
//			bundle = ResourceBundle.getBundle("messages", local);
//		}
//		catch (MissingResourceException e)
//		{
//			bundle = ResourceBundle.getBundle("messages", local, new FileResClassLoader(I18n.class.getClassLoader(), Main.getInstance()));
//		}
	}

	public static String locMsg(String path)
	{
		return language.get(path.replace(".", "_")).asString();
//		String format = bundle.getString(path);
//		format = QuestChatManager.translateColor(format);
//		if (format == null)
//			return path;
//		else
//			return format;
	}

	public static String locMsg(String path, String... args)
	{
		// 获取语言
		Language2Value value = language.get(path.replace(".", "_"));
		// 替换变量
		for (int arg = 0; arg < args.length; arg++) {
			value.addPlaceholder("[%" + arg + "]", args[arg]);
		}
		// 返回文本
		return value.asString();
		
//		String format = bundle.getString(path);
//		if (format == null)
//			return path;
//		format = QuestChatManager.translateColor(format);
//		if (format.contains("%"))
//		{
//			try
//			{
//				for (int arg = 0; arg < args.length; arg++)
//				{
//					format = format.replace("[%" + arg + "]", args[arg]);
//				}
//				return format;
//			}
//			catch (Exception e)
//			{
//				QuestChatManager.logCmd(Level.WARNING, "An error occured whilst localizing " + path + " .");
//				e.printStackTrace();
//			}
//		}
//		return format;
	}

	private static class FileResClassLoader extends ClassLoader
	{
		private final File dataFolder;

		FileResClassLoader(final ClassLoader classLoader, final Main plugin)
		{
			super(classLoader);
			this.dataFolder = plugin.getDataFolder();
		}

		@Override
		public URL getResource(final String string)
		{
			final File file = new File(dataFolder, string);
			if (file.exists())
			{
				try
				{
					return file.toURI().toURL();
				}
				catch (MalformedURLException ex)
				{
				}
			}
			return null;
		}

		@Override
		public InputStream getResourceAsStream(final String string)
		{
			final File file = new File(dataFolder, string);
			if (file.exists())
			{
				try
				{
					return new FileInputStream(file);
				}
				catch (FileNotFoundException ex)
				{
				}
			}
			return null;
		}
	}

}
