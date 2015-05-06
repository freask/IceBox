package ru.freask.studyjam.icebox;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.stetho.Stetho;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.sql.SQLException;
import java.util.List;

import ru.freask.studyjam.icebox.adapters.RecipesAdapter;
import ru.freask.studyjam.icebox.db.OrmHelper;
import ru.freask.studyjam.icebox.db.ProductDao;
import ru.freask.studyjam.icebox.db.RecipeDao;
import ru.freask.studyjam.icebox.http.requests.GetRecipeSearchRequest;
import ru.freask.studyjam.icebox.models.Product;
import ru.freask.studyjam.icebox.models.Recipe;
import ru.freask.studyjam.icebox.models.RecipeList;
import ru.freask.studyjam.icebox.models.RecipeObj;
import ru.freask.studyjam.icebox.models.RecipeSearch;

public class RecipesActivity extends BaseActivity {

    private RecipesAdapter recipeListAdapter;
    GetRecipeSearchRequest recipeSearchRequest;
    private ListView recipeListView;
    private static OrmHelper ormHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Stetho.initialize(Stetho.newInitializerBuilder(this).enableDumpapp(Stetho.defaultDumperPluginsProvider(this)).enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
        setContentView(R.layout.activity_recipes);
        navigationDrawerSetUp();
        initRequests();
        ormHelper = OpenHelperManager.getHelper(context, OrmHelper.class);

        recipeListView = (ListView) findViewById(R.id.listViewRecipes);
        recipeListAdapter = new RecipesAdapter(this);
        recipeListView.setAdapter(recipeListAdapter);
        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe recipe = (Recipe) recipeListView.getItemAtPosition(position);
                try {
                    RecipeDao recipeDao = (RecipeDao) ormHelper.getDaoByClass(Recipe.class);
                    recipeDao.create(recipe);
                    Intent i = new Intent(context, OneRecipeActivity.class);
                    i.putExtra("recipe_id", recipe.getId());
                    context.startActivity(i);
                } catch (SQLException e) {
                    Log.e(MainActivity.TAG, e.getMessage());
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("query")!= null)
            recipeSearchRequest.query = bundle.getString("query");
        else
        {
            return;
        }
        Log.v(MainActivity.TAG, "QUERY = " + recipeSearchRequest.query);
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
            if (recipeListView != null) {
                fillRecipeList(recipeList);
            }
        }

        private void fillRecipeList(RecipeList recipe_objs) {
            recipeListAdapter.clear();
            for (RecipeObj recipe_obj : recipe_objs) {
                recipeListAdapter.add(recipe_obj.getRecipe());
            }
        }
    }

}
