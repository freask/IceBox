package ru.freask.studyjam.icebox.http.requests;

import android.util.Log;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ru.freask.studyjam.icebox.http.ClientService;
import ru.freask.studyjam.icebox.http.RecipeService;
import ru.freask.studyjam.icebox.models.RecipeList;
import ru.freask.studyjam.icebox.models.RecipeSearch;

/**
 * Created by Alexander.Kashin01 on 30.04.2015.
 */
public class GetRecipeSearchRequest extends RetrofitSpiceRequest<RecipeSearch, RecipeService> {
    String query;

    public GetRecipeSearchRequest(String query) {
        super(RecipeSearch.class, RecipeService.class);
        this.query = query;
    }

    @Override
    public RecipeSearch loadDataFromNetwork() throws Exception {
        try {
            Log.d(GetRecipeSearchRequest.class.getCanonicalName(), "Calling web service");
            RecipeSearch recipeSearch = getService().recipeSearch(ClientService.API_APP_ID, ClientService.API_APP_KEY, query);
            return recipeSearch;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IllegalAccessError("The fuck happen!");
        }
    }
}
