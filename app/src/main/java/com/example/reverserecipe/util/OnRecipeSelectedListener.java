package com.example.reverserecipe.util;

import com.example.reverserecipe.models.Recipe;
import java.util.ArrayList;

public interface OnRecipeSelectedListener {
    public void onRecipeSelected(Integer position, ArrayList<Recipe> recipes);

}
