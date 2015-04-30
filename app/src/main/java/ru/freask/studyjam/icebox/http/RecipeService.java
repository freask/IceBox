package ru.freask.studyjam.icebox.http;

import retrofit.http.GET;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import ru.freask.studyjam.icebox.models.RecipeList;
import ru.freask.studyjam.icebox.models.RecipeSearch;

/**
 * Created by Alexander.Kashin01 on 30.04.2015.
 */
public interface RecipeService {
    @GET("/search")
    RecipeSearch recipeSearch(@Query("app_id") String app_id, @Query("app_key") String app_key, @Query("q") String query);
}