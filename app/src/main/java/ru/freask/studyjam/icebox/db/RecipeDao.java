package ru.freask.studyjam.icebox.db;


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
}