package org.valheim.wiki.dto;

import java.util.List;

public record RecipeDTO(
        String station,
        Integer stationLvl,
        List<IngredientDTO> ingredients
) {
}
