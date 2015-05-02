package ru.freask.studyjam.icebox.db;


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
}