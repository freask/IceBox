package ru.freask.studyjam.icebox;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.stetho.Stetho;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import ru.freask.studyjam.icebox.http.requests.GetRecipeSearchRequest;
import ru.freask.studyjam.icebox.models.RecipeList;
import ru.freask.studyjam.icebox.models.RecipeObj;
import ru.freask.studyjam.icebox.models.RecipeSearch;

public class RecipesActivity extends BaseActivity {

    private ArrayAdapter<String> recipeListAdapter;
    GetRecipeSearchRequest recipeSearchRequest;
    private ListView recipeListView;
    String recipeQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initialize(Stetho.newInitializerBuilder(this).enableDumpapp(Stetho.defaultDumperPluginsProvider(this)).enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
        setContentView(R.layout.activity_recipes);
        navigationDrawerSetUp();
        initRequests();

        recipeListView = (ListView) findViewById(R.id.listViewRecipes);
        recipeListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        recipeListView.setAdapter(recipeListAdapter);

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("query")!= null)
            recipeSearchRequest.query = bundle.getString("query");
        else
        {
            return;
        }
        Log.v(MainActivity.TAG, recipeSearchRequest.query);
        int version = 2;
        getSpiceManager().execute(recipeSearchRequest, "recipeList_" + version + "_" + recipeSearchRequest.query, DurationInMillis.ONE_WEEK, new RecipeListRequestListener());

    }

    private void initRequests() {
        recipeSearchRequest = new GetRecipeSearchRequest();
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

        private void fillProductList(RecipeList recipe_objs) {
            recipeListAdapter.clear();
            for (RecipeObj recipe_obj : recipe_objs) {
                recipeListAdapter.add(recipe_obj.getRecipe().getLabel());
            }
        }
    }

}
