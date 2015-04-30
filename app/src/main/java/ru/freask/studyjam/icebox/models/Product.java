package ru.freask.studyjam.icebox.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

/**
 * Created by Alexander.Kashin01 on 30.04.2015.
 */
@DatabaseTable(tableName = "Product")
public class Product {

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField
    public String name;
    @DatabaseField
    public int count;

    @SuppressWarnings("serial")
    public static class List extends ArrayList<Product> {
    }
}
