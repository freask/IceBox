package ru.freask.studyjam.icebox.db;


import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

import ru.freask.studyjam.icebox.models.Product;
import ru.freask.studyjam.icebox.models.Recipe;

public class RecipeDao extends CommonDao<Recipe, Long> {
    public RecipeDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Recipe.class);
    }

    public Recipe findRecipe(String label, String url)  throws SQLException{
        QueryBuilder<Recipe, Long> queryBuilder = queryBuilder();
        queryBuilder.where().eq("label", label).and().eq("url", url);
        PreparedQuery<Recipe> preparedQuery = queryBuilder.prepare();
        return queryForFirst(preparedQuery);
    }
}