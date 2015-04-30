package ru.freask.studyjam.icebox.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Alexander.Kashin01 on 30.04.2015.
 */
@DatabaseTable(tableName = "Recipe")
public class Recipe {
    @DatabaseField(generatedId = true)
    Integer id;
    @DatabaseField
    String uri;
    @DatabaseField
    String label;
    @DatabaseField
    String image;
    @DatabaseField
    String source;
    @DatabaseField
    String url;
    @DatabaseField
    int yield;
    @DatabaseField
    String summary;
    @DatabaseField
    Float calories;

    public Recipe() {}
}
