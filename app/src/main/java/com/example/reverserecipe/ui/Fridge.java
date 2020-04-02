package com.example.reverserecipe.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.reverserecipe.Constants;
import com.example.reverserecipe.R;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Fridge extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @BindView(R.id.instructionView) TextView mInstructionView;
    @BindView(R.id.ingredient) EditText mIngredient;
    @BindView(R.id.searchButton) Button mSearchRecipesButton;
    @BindView(R.id.savedButton) Button mSavedRecipesButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fridge);
        ButterKnife.bind(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();
        mSearchRecipesButton.setOnClickListener(this);
        mSavedRecipesButton.setOnClickListener(this);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Fridge.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    public void onClick(View v) {
        if (v == mSearchRecipesButton) {
            String ingredient = mIngredient.getText().toString();
            if(!(ingredient).equals("")) {
                addToSharedPreferences(ingredient);
            }
            Intent intent = new Intent(Fridge.this, RecipeList.class);
            intent.putExtra("ingredient", ingredient);
            startActivity(intent);
        }
        if (v == mSavedRecipesButton) {
            Intent intent = new Intent(Fridge.this, SavedRecipesListActivity.class);
            startActivity(intent);
        }
    }

    private void addToSharedPreferences(String ingredient) {
        mEditor.putString(Constants.PREFERENCES_INGREDIENT_KEY, ingredient).apply();
    }
}
