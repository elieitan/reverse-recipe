package com.example.reverserecipe.adapters;


import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.reverserecipe.Constants;
import com.example.reverserecipe.R;
import com.example.reverserecipe.models.Recipe;
import com.example.reverserecipe.ui.RecipeDetailActivity;
import com.example.reverserecipe.ui.RecipeDetailFragment;
import com.example.reverserecipe.util.OnRecipeSelectedListener;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeViewHolder> {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    private ArrayList<Recipe> mRecipes = new ArrayList<>();
    private Context mContext;
    private OnRecipeSelectedListener mOnRecipeSelectedListener;

    public RecipeListAdapter(Context context, ArrayList<Recipe> recipes, OnRecipeSelectedListener recipeSelectedListener) {
        mContext = context;
        mRecipes = recipes;
        mOnRecipeSelectedListener = recipeSelectedListener;
    }

    @Override
    public RecipeListAdapter.RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_list_item, parent, false);
        RecipeViewHolder viewHolder = new RecipeViewHolder(view, mRecipes, mOnRecipeSelectedListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeListAdapter.RecipeViewHolder holder, int position) {
        holder.bindRecipe(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.recipeImageView) ImageView mRecipeImageView;
        @BindView(R.id.recipeTitleTextView) TextView mRecipeTitleTextView;
        @BindView(R.id.recipeIngredientsTextView) TextView mRecipeIngredientsTextView;

        private Context mContext;
        private int mOrientation;
        private ArrayList<Recipe> mRecipes = new ArrayList<>();
        private OnRecipeSelectedListener mRecipeSelectedListener;

        public RecipeViewHolder(View itemView, ArrayList<Recipe> recipes, OnRecipeSelectedListener recipeSelectedListener) {
           super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            mOrientation = itemView.getResources().getConfiguration().orientation;
            mRecipes = recipes;
            mRecipeSelectedListener = recipeSelectedListener;

            if(mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                createDetailFragment(0);
            }
            itemView.setOnClickListener(this);
        }

        public void bindRecipe(Recipe recipe) {
            Picasso.get().setLoggingEnabled(true);
            Picasso.get().load(recipe.getImageUrl()).into(mRecipeImageView);

            mRecipeTitleTextView.setText(recipe.getTitle());
            mRecipeIngredientsTextView.setText(recipe.getIngredients());
        }

        private void createDetailFragment(int position) {
            RecipeDetailFragment detailFragment = RecipeDetailFragment.newInstance(mRecipes, position);
            FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.recipeDetailContainer, detailFragment);
            ft.commit();
        }


        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition();
            mOnRecipeSelectedListener.onRecipeSelected(itemPosition, mRecipes);

            if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                createDetailFragment(itemPosition);
            } else {
                Intent intent = new Intent(mContext, RecipeDetailActivity.class);
                intent.putExtra(Constants.EXTRA_KEY_POSITION, itemPosition);
                intent.putExtra(Constants.EXTRA_KEY_RECIPES, Parcels.wrap(mRecipes));
                mContext.startActivity(intent);
            }
        }
    }
}
