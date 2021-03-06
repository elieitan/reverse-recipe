package com.example.reverserecipe.services;

import android.util.Log;

import com.example.reverserecipe.Constants;
import com.example.reverserecipe.models.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecipeService {
    public static void findRecipes(String ingredient, Callback callback) {

        OkHttpClient client = new OkHttpClient.Builder().build();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.API_URL).newBuilder();
        urlBuilder.addQueryParameter(Constants.API_INGREDENT_QUERY_PARAMETER, ingredient);
        String url = urlBuilder.build().toString();
        Log.d("RecipeService", url);

        Request request= new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }

    public ArrayList<Recipe> processResults(Response response) {
        ArrayList<Recipe> recipes = new ArrayList<>();

        try {
            String jsonData = response.body().string();
            if (response.isSuccessful()) {
                JSONObject recipePuppyJSON = new JSONObject(jsonData);
                JSONArray recipeArrayJSON = recipePuppyJSON.getJSONArray("results");
                for (int i = 0; i < recipeArrayJSON.length(); i++) {
                    JSONObject recipeJSON = recipeArrayJSON.getJSONObject(i);
                    String title = recipeJSON.optString("title");
                    String link = recipeJSON.optString("href");
                    String imageUrl = recipeJSON.optString("thumbnail");
                    String ingredient = recipeJSON.optString("ingredients");

                    if (imageUrl.equals("")) {
                        imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/1024px-No_image_available.svg.png";
                    }
                    Recipe recipe = new Recipe(title, link, imageUrl, ingredient);
                    recipes.add(recipe);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }
}
