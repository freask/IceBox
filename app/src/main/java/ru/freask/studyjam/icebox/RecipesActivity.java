package ru.freask.studyjam.icebox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.sql.SQLException;

import ru.freask.studyjam.icebox.adapters.RecipesAdapter;
import ru.freask.studyjam.icebox.db.OrmHelper;
import ru.freask.studyjam.icebox.db.RecipeDao;
import ru.freask.studyjam.icebox.http.requests.GetRecipeSearchRequest;
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
        setActivityLayoutRes(R.layout.activity_recipes);
        super.onCreate(savedInstanceState);
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
                    Recipe recipeSearched = recipeDao.findRecipe(recipe.getLabel(), recipe.getUrl());
                    int recipe_id;
                    if (recipeSearched == null)
                    {
                        recipeDao.create(recipe);
                        recipe_id = recipe.getId();
                    } else {
                        recipe_id = recipeSearched.getId();
                    }
                    Intent i = new Intent(context, OneRecipeActivity.class);
                    i.putExtra("recipe_id", recipe_id);
                    context.startActivity(i);
                } catch (SQLException e) {

                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("query")!= null && !bundle.getString("query").equals(""))
            recipeSearchRequest.query = bundle.getString("query");
        else
            return;

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
