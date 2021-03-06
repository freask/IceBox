package ru.freask.studyjam.icebox;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.picasso.Picasso;
import java.sql.SQLException;
import ru.freask.studyjam.icebox.db.OrmHelper;
import ru.freask.studyjam.icebox.db.RecipeDao;
import ru.freask.studyjam.icebox.models.Recipe;

/**
 * Created by Alexander.Kashin01 on 06.05.2015.
 */
public class OneRecipeActivity extends BaseActivity implements View.OnClickListener {
    Button openLink, shareBut;
    TextView label, time, calories;
    ImageView image;
    Recipe recipe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setActivityLayoutRes(R.layout.activity_one_recipe);
        super.onCreate(savedInstanceState);

        OrmHelper ormHelper = OpenHelperManager.getHelper(context, OrmHelper.class);

        Bundle bundle = getIntent().getExtras();
        Integer recipe_id = bundle.getInt("recipe_id", -1);

        if(recipe_id == -1)
         return;

        try {
            RecipeDao recipeDao = (RecipeDao) ormHelper.getDaoByClass(Recipe.class);
            recipe = recipeDao.queryForId(Long.parseLong(recipe_id.toString()));
        } catch (SQLException e) {
            return;
        }

        image = (ImageView) findViewById(R.id.recipe_image);
        label = (TextView) findViewById(R.id.recipe_label);
        time = (TextView) findViewById(R.id.recipe_totalTime);
        calories = (TextView) findViewById(R.id.recipe_calories);
        openLink = (Button) findViewById(R.id.open_url);
        shareBut = (Button) findViewById(R.id.share);
        openLink.setOnClickListener(this);
        shareBut.setOnClickListener(this);

        Picasso.with(context).load(recipe.getImage()).into(image);

        label.setText(recipe.getLabel());
        time.setText("Total time: " + Math.round(recipe.getTotalTime()) + " min");
        calories.setText("Calories: " + Math.round(recipe.getCalories()) + " kkal");

        LinearLayout ingridients = (LinearLayout) findViewById(R.id.ingridients);
        String[] ingridValues = recipe.getIngredientLines();
        for (String ingridient : ingridValues) {
            TextView txt = new TextView(this);
            txt.setText("- " + ingridient);
            ingridients.addView(txt);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open_url:
                Uri address = Uri.parse(recipe.getUrl());
                Intent openlink = new Intent(Intent.ACTION_VIEW, address);
                startActivity(openlink);
                break;
            case R.id.share:
                Intent It = new Intent();
                It.setAction(Intent.ACTION_SEND);
                It.setType("text/plain");
                It.putExtra(android.content.Intent.EXTRA_TEXT, this.getString(R.string.share_text) + ' ' + recipe.getLabel() + ": " + recipe.getUrl());
                startActivity(Intent.createChooser(It, this.getString(R.string.share)));
                break;
        }
    }
}
