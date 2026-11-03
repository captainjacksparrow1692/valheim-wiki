package org.valheim.wiki.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.valheim.wiki.ItemRecipe;
import org.valheim.wiki.RecipeService;

public class FoodCommand implements SlashCommand{

    private final RecipeService recipeService;
    public FoodCommand(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public String getName() {
        return "food";
    }

    @Override
    public SlashCommandData getCommandData(){
        return Commands.slash(getName(), "Food Command")
                .addOption(OptionType.STRING, "Food", "Food Name", true);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event){
        String foodName = event.getOption("Food").getAsString();
        ItemRecipe recipe = recipeService.findItem(foodName);

        if (recipe == null) {
            event.reply("Food **" + foodName + "** not found!").queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("**Recipe for ").append(recipe.name()).append("**\n");
        sb.append("Station: ").append(recipe.craftingStation().name())
                .append(" (Lvl: ").append(recipe.craftingStation().minLevel()).append(")\n\n");
        sb.append("**Ingredients:**\n");

        for (ItemRecipe.Ingredient ing : recipe.recipe()) {
            sb.append("• ").append(ing.ingredientId()).append(": x").append(ing.amount()).append("\n");
        }

        event.reply(sb.toString()).queue();
    }
}
