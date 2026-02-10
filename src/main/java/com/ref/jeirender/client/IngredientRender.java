package com.ref.jeirender.client;

import mezz.jei.api.ingredients.ITypedIngredient;

public record IngredientRender<T>(ITypedIngredient<T> ingredient, int x, int y) {}
