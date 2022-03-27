package me.aa07.mook.commands;

import me.aa07.botcore.AABotCore;
import me.aa07.botcore.AACommand;
import me.aa07.mook.DataManager;
import me.aa07.mook.config.sections.Discord;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

public class RefreshCommand extends AACommand {
    private Discord config;
    private DataManager dm;

    public RefreshCommand(AABotCore bot, Discord config, DataManager dm) {
        super("refresh", "[Admin Only] Refreshes the current fortune pool.", bot);
        this.config = config;
        this.dm = dm;
    }

    @Override
    public void execute(SlashCommandInteraction event) {
        boolean result = dm.refresh();
        EmbedBuilder embed;
        if (result) {
            embed = getBot().getEmbed().setTitle("Mook").setDescription("Successfully refreshed fortunes").addField("New count:", String.valueOf(dm.getFortuneCount()), false);
        } else {
            embed = getBot().getEmbed().setTitle("Mook").setDescription("Failed to refresh fortunes. Previous ones restored. Yell at AA.");
        }

        event.createImmediateResponder().addEmbed(embed).respond();
    }

    @Override
    public boolean canExecute(SlashCommandInteraction event) {
        if (config.adminIds.contains(event.getUser().getId())) {
            return true;
        }

        return false;
    }
}
