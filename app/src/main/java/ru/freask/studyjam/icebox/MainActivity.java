package ru.freask.studyjam.icebox;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import ru.freask.studyjam.icebox.http.requests.GetRecipeSearchRequest;
import ru.freask.studyjam.icebox.models.Recipe;
import ru.freask.studyjam.icebox.models.RecipeList;
import ru.freask.studyjam.icebox.models.RecipeSearch;

public class MainActivity extends BaseActivity {

    private ArrayAdapter<Recipe> recipeListAdapter;
    GetRecipeSearchRequest recipeSearchRequest;
    private ListView recipeListView;
    String recipeQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRequests();

        recipeListView = (ListView) findViewById(R.id.listViewRecipes);
        recipeListAdapter = new ArrayAdapter<Recipe>(this, android.R.layout.simple_list_item_1);
        recipeListView.setAdapter(recipeListAdapter);

        recipeQuery = "meat";
        int version = 2;
        getSpiceManager().execute(recipeSearchRequest, "recipeList_" + version + "_" + recipeQuery, DurationInMillis.ONE_WEEK, new RecipeListRequestListener());

    }

    private void initRequests() {
        recipeSearchRequest = new GetRecipeSearchRequest(recipeQuery);
    }

    public final class RecipeListRequestListener implements
            RequestListener<RecipeSearch> {

        @Override
        public void onRequestFailure(SpiceException spcExcptn) {
            System.out.println("-------------------------------------");
            spcExcptn.getCause().printStackTrace();
            System.out.println("-------------------------------------");
        }

        @Override
        public void onRequestSuccess(RecipeSearch recipeSearch) {
            RecipeList recipeList = recipeSearch.getRecipeList();
            //String msg = "Recipes Obtained: " + recipes.size();
            //Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
            if (recipeListView != null) {
                fillProductList(recipeList);
            }
        }

        private void fillProductList(RecipeList recipes) {
            recipeListAdapter.clear();
            for (Recipe recipe : recipes) {
                recipeListAdapter.add(recipe);
            }
        }
    }

}
