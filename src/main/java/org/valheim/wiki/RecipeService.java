package org.valheim.wiki;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeService {
    private final Map<String, ItemRecipe> storage = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RecipeService() {
        loadData();
    }

    private void loadData() {
        File rootDataDir = new File("data"); // Корень папки data в проекте

        if (!rootDataDir.exists() || !rootDataDir.isDirectory()) {
            System.err.println("❌ Главная папка 'data' не найдена в корне проекта!");
            return;
        }

        scanDirectory(rootDataDir);

        System.out.println("📊 Всего загружено предметов в память: " + storage.size());
    }

    private void scanDirectory(File dir) {
        File[] files = dir.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                scanDirectory(file);
            } else if (file.getName().toLowerCase().endsWith(".json")) {
                parseJsonFile(file);
            }
        }
    }

    private void parseJsonFile(File file) {
        try (FileInputStream inputStream = new FileInputStream(file)) {
            com.fasterxml.jackson.databind.JsonNode rootNode = objectMapper.readTree(inputStream);

            if (rootNode.isArray()) {
                List<ItemRecipe> recipes = objectMapper.readValue(rootNode.traverse(), new TypeReference<List<ItemRecipe>>() {});
                for (ItemRecipe recipe : recipes) {
                    registerRecipe(recipe);
                }
                System.out.println("✅ Загружен список из файла: " + file.getPath() + " (предметов: " + recipes.size() + ")");
            } else if (rootNode.isObject()) {
                ItemRecipe recipe = objectMapper.treeToValue(rootNode, ItemRecipe.class);
                registerRecipe(recipe);
                System.out.println("✅ Загружен одиночный предмет: " + file.getPath() + " (" + recipe.name() + ")");
            }
        } catch (Exception e) {
            System.err.println("❌ Ошибка чтения файла " + file.getPath() + ": " + e.getMessage());
        }
    }

    private void registerRecipe(ItemRecipe recipe) {
        if (recipe.id() != null) {
            storage.put(recipe.id().toLowerCase(), recipe);
        }
        if (recipe.name() != null) {
            storage.put(recipe.name().toLowerCase(), recipe);
        }
        if (recipe.aliases() != null) {
            for (String alias : recipe.aliases()) {
                if (alias != null) {
                    storage.put(alias.toLowerCase().trim(), recipe);
                }
            }
        }
    }

    public ItemRecipe findItem(String query) {
        if (query == null) return null;
        return storage.get(query.toLowerCase().trim());
    }
}