package me.aa07.mook;

import com.moandjiezana.toml.Toml;
import java.io.File;
import me.aa07.mook.config.ConfigHolder;

public class Main  {
    public static void main(String[] args) {
        MookBot bot;

        try {
            File configfile = new File("config.toml");
            ConfigHolder configuration = new Toml().read(configfile).to(ConfigHolder.class);
            bot = new MookBot(configuration);
        } catch (Exception e) {
            System.out.println("Failed to load bot config");
            e.printStackTrace();
            return;
        }

        bot.mookLaunch();
    }
}
