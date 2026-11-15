package org.valheim.wiki.dto;

import org.jetbrains.annotations.Nullable;
import org.valheim.wiki.enums.ItemCategory;

import java.util.Map;

public record ItemDefinitionDto(
        String key,
        Map<String, String> name,
        ItemCategory category,
        String biome,
        RecipeDTO recipeDTO,
        @Nullable
        FoodStat foodStats
) {
    public boolean isCraftable() {
        return recipeDTO != null && recipeDTO.ingredients() != null && !recipeDTO.ingredients().isEmpty();
    }

    public boolean isEdible() {
        return foodStats != null;
    }
}
