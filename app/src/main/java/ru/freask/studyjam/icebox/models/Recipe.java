package ru.freask.studyjam.icebox.models;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Created by Alexander.Kashin01 on 30.04.2015.
 */
@DatabaseTable(tableName = "Recipe")
public class Recipe {
    @DatabaseField(generatedId = true)
    Integer id;
    @DatabaseField
    String label;
    @DatabaseField
    String image;
    @DatabaseField
    String url;



    @DatabaseField
    Float calories;
    @DatabaseField
    Long totalTime;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    String[] ingredientLines;

    public Recipe() {

    }

    public String getLabel() {
        return label;
    }

    public Integer getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }
    public Float getCalories() {
        return calories;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public String[] getIngredientLines() {
        return ingredientLines;
    }
}
