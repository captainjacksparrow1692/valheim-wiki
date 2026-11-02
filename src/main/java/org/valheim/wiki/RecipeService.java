package org.valheim.wiki;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RecipeService {
    private final Map<String, ItemRecipe> storage = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RecipeService() {
        loadData();
    }

    private void loadData() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("data/frostner.json")) {
            if (inputStream != null) {
                ItemRecipe recipe = objectMapper.readValue(inputStream, ItemRecipe.class);
                recipe = migrateIfNeeded(recipe);

                storage.put(recipe.id().toLowerCase(), recipe);
                if (recipe.aliases() != null) {
                    for (String alias : recipe.aliases()) {
                        storage.put(alias.toLowerCase(), recipe);
                    }
                }
                System.out.println("Загружен предмет: " + recipe.name() + " (Схема v" + recipe.schemaVersion() + ")");
            }
        } catch (Exception e) {
            System.err.println("Ошибка загрузки рецептов: " + e.getMessage());
        }
    }

    private ItemRecipe migrateIfNeeded(ItemRecipe recipe) {
        if (recipe.schemaVersion() < 1) {
            // Логика миграции старой схемы при необходимости
        }
        return recipe;
    }

    public ItemRecipe findItem(String query) {
        if (query == null) return null;
        return storage.get(query.toLowerCase().trim());
    }
}