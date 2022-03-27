package me.aa07.mook.commands;

import me.aa07.botcore.AABotCore;
import me.aa07.botcore.AACommand;
import me.aa07.mook.DataManager;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

public class HelpCommand extends AACommand {
    private DataManager dm;

    public HelpCommand(AABotCore bot, DataManager dm) {
        super("help", "Help for Mook", bot);
        this.dm = dm;
    }

    @Override
    public void execute(SlashCommandInteraction event) {
        EmbedBuilder embed = getBot().getEmbed().setTitle("Mook").setDescription("Fortune picker for Haven")
            .addField("/help", "Shows this menu", false)
            .addField("/mook", String.format("Posts 1 of %s Mook images", dm.getFortuneCount()), false)
            .addField("/refresh", "[Admin Only] Refresh fortunes", false);

        event.createImmediateResponder().addEmbed(embed).respond();
    }
}
