package org.valheim.wiki;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record ItemRecipe(
        @JsonProperty("schema_version") int schemaVersion,
        String id,
        String name,
        String type,
        String biome,
        List<String> aliases,
        @JsonProperty("crafting_station") CraftingStation craftingStation,
        List<Ingredient> recipe
) {
    public record CraftingStation(
            String name,
            @JsonProperty("min_level") int minLevel
    ) {}

    public record Ingredient(
            @JsonProperty("ingredient_id") String ingredientId,
            int amount
    ) {}
}