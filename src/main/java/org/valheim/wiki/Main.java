package org.valheim.wiki;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.valheim.wiki.commands.CraftCommand;
import org.valheim.wiki.commands.FoodCommand;
import org.valheim.wiki.commands.SlashCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String token;
        try {
            token = Files.readString(Path.of("token.txt")).trim();
        } catch (IOException e) {
            System.err.println("Could not find the token.txt file with the bot token!");
            return;
        }

        RecipeService recipeService = new RecipeService();

        // Создаем изолированные команды
        SlashCommand craftCommand = new CraftCommand(recipeService);
        SlashCommand foodCommand = new FoodCommand(recipeService);

        // Настраиваем слушатель и регистрируем команды
        BotListener listener = new BotListener();
        listener.registerCommand(craftCommand);
        listener.registerCommand(foodCommand);

        try {
            JDA jda = JDABuilder.createDefault(token)
                    .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                    .addEventListeners(listener)
                    .build()
                    .awaitReady();

            // Автоматически собираем данные для регистрации из классов команд
            List<net.dv8tion.jda.api.interactions.commands.build.SlashCommandData> commandDataList = List.of(
                    craftCommand.getCommandData(),
                    foodCommand.getCommandData()
            );

            jda.updateCommands().addCommands(commandDataList).queue();

            System.out.println("Бот запущен и модульные команды зарегистрированы!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}