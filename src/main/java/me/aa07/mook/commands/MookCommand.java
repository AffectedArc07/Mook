package me.aa07.mook.commands;

import me.aa07.botcore.AABotCore;
import me.aa07.botcore.slashcommand.AASlashCommand;
import me.aa07.mook.DataManager;
import me.aa07.mook.DataManager.Fortune;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

public class MookCommand extends AASlashCommand {
    private DataManager dm;

    public MookCommand(AABotCore bot, DataManager dm) {
        super("mook", "Pick a random Mook fortune", bot);
        this.dm = dm;
    }

    @Override
    public void execute(SlashCommandInteraction event) {
        Fortune f = dm.pickFortune();
        EmbedBuilder embed = getBot().getEmbed().setTitle("Mook").setDescription(f.fortuneText);
        embed.setImage(f.imageUrl);
        event.createImmediateResponder().addEmbed(embed).respond();
    }

}
