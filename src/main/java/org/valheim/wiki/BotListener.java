package org.valheim.wiki;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.valheim.wiki.commands.SlashCommand;

import java.util.HashMap;
import java.util.Map;

public class BotListener extends ListenerAdapter {
    private final Map<String, SlashCommand> commands = new HashMap<>();

    public void registerCommand(SlashCommand command) {
        commands.put(command.getName(), command);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        SlashCommand command = commands.get(event.getName());
        if (command != null) {
            try {
                command.execute(event);
            } catch (Exception e) {
                event.reply("⚠️ An error occurred while executing the command.").setEphemeral(true).queue();
                e.printStackTrace();
            }
        }
    }
}