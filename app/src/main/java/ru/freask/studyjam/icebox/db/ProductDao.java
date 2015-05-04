package ru.freask.studyjam.icebox.db;


import android.accounts.Account;

import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;

import ru.freask.studyjam.icebox.models.Product;

public class ProductDao extends CommonDao<Product, Long> {
    public ProductDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Product.class);
    }

    public List<Product> getAllProducts() throws SQLException{
        return this.queryForAll();
    }

    public List<Product> getNeededProducts() throws SQLException{
        QueryBuilder<Product, Long> orderQb = this.queryBuilder();
        orderQb.where().lt(Product.NAME_FIELD_COUNT, Product.NAME_FIELD_LIKECOUNT);
        return orderQb.query();
    }
}