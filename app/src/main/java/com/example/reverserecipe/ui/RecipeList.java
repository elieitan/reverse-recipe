package com.example.reverserecipe.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.reverserecipe.Constants;
import com.example.reverserecipe.R;
import com.example.reverserecipe.models.Recipe;
import com.example.reverserecipe.util.OnRecipeSelectedListener;

import org.parceler.Parcels;

import java.util.ArrayList;

public class RecipeList extends AppCompatActivity implements OnRecipeSelectedListener {
    private Integer mPosition;
    ArrayList<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        if (savedInstanceState != null) {
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                mPosition = savedInstanceState.getInt(Constants.EXTRA_KEY_POSITION);
                mRecipes = Parcels.unwrap(savedInstanceState.getParcelable(Constants.EXTRA_KEY_RECIPES));

                if (mPosition != null && mRecipes != null) {
                    Intent intent = new Intent(this, RecipeDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_KEY_POSITION, mPosition);
                    intent.putExtra(Constants.EXTRA_KEY_RECIPES, Parcels.wrap(mRecipes));
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mPosition != null && mRecipes != null) {
            outState.putInt(Constants.EXTRA_KEY_POSITION, mPosition);
            outState.putParcelable(Constants.EXTRA_KEY_RECIPES, Parcels.wrap(mRecipes));
        }
    }

    @Override
    public void onRecipeSelected(Integer position, ArrayList<Recipe> recipes) {
        mPosition = position;
        mRecipes = recipes;
    }

}