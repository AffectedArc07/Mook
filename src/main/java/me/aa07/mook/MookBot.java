
package me.aa07.mook;

import java.util.ArrayList;
import me.aa07.botcore.AABotCore;
import me.aa07.botcore.messagecommand.AAMessageCommand;
import me.aa07.botcore.slashcommand.AASlashCommand;
import me.aa07.mook.commands.HelpCommand;
import me.aa07.mook.commands.MookCommand;
import me.aa07.mook.commands.RefreshCommand;
import me.aa07.mook.config.ConfigHolder;
import org.javacord.api.listener.GloballyAttachableListener;

public class MookBot extends AABotCore {
    private ConfigHolder config;
    private DataManager dataManager;

    public MookBot(ConfigHolder config) {
        super(config.discord.token, "Mook v6.0 | Contact affected for support", "#e67e22");
        this.config = config;

        dataManager = new DataManager(config.data, getLogger());
        if (dataManager.refresh()) {
            getLogger().info(String.format("Loaded %s fortunes", dataManager.getFortuneCount()));
        } else {
            getLogger().fatal("Failed to load fortunes. This is a fatal error.");
            System.exit(1);
        }
    }

    public void mookLaunch() {
        launch();
    }

    @Override
    public ArrayList<AASlashCommand> getSlashCommands() {
        ArrayList<AASlashCommand> commands = new ArrayList<AASlashCommand>();
        commands.add(new HelpCommand(this, dataManager));
        commands.add(new MookCommand(this, dataManager));
        commands.add(new RefreshCommand(this, config.discord, dataManager));
        return commands;
    }

    @Override
    public ArrayList<GloballyAttachableListener> getListeners() {
        ArrayList<GloballyAttachableListener> listeners = new ArrayList<GloballyAttachableListener>();
        return listeners;
    }

    @Override
    public ArrayList<AAMessageCommand> getMessageCommands() {
        return new ArrayList<AAMessageCommand>();
    }
}
