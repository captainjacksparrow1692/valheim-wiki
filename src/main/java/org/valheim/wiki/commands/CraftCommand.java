package org.valheim.wiki.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.valheim.wiki.ItemRecipe;
import org.valheim.wiki.RecipeService;

public class CraftCommand implements  SlashCommand {
    private final RecipeService recipeService;

    public CraftCommand(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public String getName() {
        return "craft";
    }

    @Override
    public SlashCommandData getCommandData() {
        return Commands.slash(getName(), "craft")
                .addOption(OptionType.STRING, "item", "Item name", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String itemName = event.getOption("item").getAsString();
        ItemRecipe recipe = recipeService.findItem(itemName);

        if (recipe == null) {
            event.reply("❌ Item **" + itemName + "** not found!").queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("🔨 **Recipe for ").append(recipe.name()).append("**\n");
        sb.append("📍 Station: ").append(recipe.craftingStation().name())
                .append(" (Lvl: ").append(recipe.craftingStation().minLevel()).append(")\n\n");
        sb.append("📦 **Ingredients:**\n");

        for (ItemRecipe.Ingredient ing : recipe.recipe()) {
            sb.append("• ").append(ing.ingredientId()).append(": x").append(ing.amount()).append("\n");
        }

        event.reply(sb.toString()).queue();
    }
}
