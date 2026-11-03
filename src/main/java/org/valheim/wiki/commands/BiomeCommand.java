package org.valheim.wiki.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.valheim.wiki.RecipeService;

public class BiomeCommand implements SlashCommand{

    private final RecipeService recipeService;
    public BiomeCommand(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public String getName() {
        return "Biome";
    }

    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash(getName(), "Biome Command")
                .addOption(OptionType.STRING, "Biome", "Biome Name", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

    }
}
