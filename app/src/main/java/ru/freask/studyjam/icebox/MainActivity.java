package ru.freask.studyjam.icebox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.freask.studyjam.icebox.adapters.ProductAdapter;
import ru.freask.studyjam.icebox.db.OrmHelper;
import ru.freask.studyjam.icebox.db.ProductDao;
import ru.freask.studyjam.icebox.dialogs.AddDialogFragment;
import ru.freask.studyjam.icebox.models.Product;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static ProductAdapter productListAdapter;
    private static ListView productListView;
    private static OrmHelper ormHelper;

    public static final String TAG = "TAG";
    static Context context;
    private static FloatingActionButton searchButton, addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setActivityLayoutRes(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        context = this;
        initDB();
        productListView = (ListView) findViewById(R.id.listViewProducts);
        productListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        productListView.setMultiChoiceModeListener(new MultiChoiceImpl(productListView));
        productListAdapter = new ProductAdapter(this, getSupportFragmentManager());

        addButton = (FloatingActionButton) findViewById(R.id.add_but);
        addButton.setStrokeVisible(false);
        addButton.setOnClickListener(this);

        searchButton = (FloatingActionButton) findViewById(R.id.search_but);
        searchButton.setStrokeVisible(false);
        searchButton.setOnClickListener(this);

        fillProductList();
        updateSearchBut();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        updateSearchBut();
    }

    public static void fillProductList() {
        try {
            ProductDao productDao = (ProductDao) ormHelper.getDaoByClass(Product.class);
            productListAdapter.clear();
            for (Product product : productDao.getAllProducts()) {
                productListAdapter.add(product);
            }
            if (productListView.getCount() == 0)
                searchButton.setVisibility(View.GONE);
            else
                searchButton.setVisibility(View.VISIBLE);
        } catch (SQLException e) {

        }
        productListView.setAdapter(productListAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_but:
                new AddDialogFragment().show(getSupportFragmentManager(), "add");
                break;

            case R.id.search_but:
                List<String> selected = getSelectedItems();
                String text = implode(" ", selected);
                Intent i = new Intent(context, RecipesActivity.class);
                Toast.makeText(context, "Searching: " + text, Toast.LENGTH_SHORT).show();
                i.putExtra("query", text);
                context.startActivity(i);
                break;
        }
    }

    public void addProduct(String name, int count, int likeCount) {
        Product product = new Product();
        product.name = name;
        product.count = count;
        product.like_count = likeCount;

        try {
            ProductDao productDao = (ProductDao) ormHelper.getDaoByClass(Product.class);
            productDao.create(product);
            fillProductList();
        } catch (SQLException e) {

        }

    }

    public static void delProduct(long id) {
        try {
            ProductDao productDao = (ProductDao) ormHelper.getDaoByClass(Product.class);
            productDao.deleteById(id);
            fillProductList();
        } catch (SQLException e) {

        }
    }

    public static void updateProduct(Product product) {
        try {
            if (product.count < 0)
                return;
            ProductDao productDao = (ProductDao) ormHelper.getDaoByClass(Product.class);
            productDao.update(product);
            MainActivity.fillProductList();
        } catch (SQLException e) {

        }
    }

    private void initDB() {
        ormHelper = OpenHelperManager.getHelper(context, OrmHelper.class);
    }

    public static List<String> getSelectedItems() {
        List<String> selectedItems = new ArrayList<>();

        SparseBooleanArray sparseBooleanArray = productListView.getCheckedItemPositions();
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            if (sparseBooleanArray.valueAt(i)) {
                Product selectedItem = (Product) productListView.getItemAtPosition(sparseBooleanArray.keyAt(i));
                selectedItems.add(selectedItem.name);
            }
        }
        return selectedItems;
    }

    public static String implode(String glue, List<String> strArray)
    {
        String ret = "";
        for(int i=0;i<strArray.size();i++)
        {
            ret += (i == strArray.size() - 1) ? strArray.get(i) : strArray.get(i) + glue;
        }
        return ret;
    }

    public static void updateSearchBut() {
        searchButton.setVisibility(getSelectedItems().size() > 0 ? View.VISIBLE : View.GONE);
    }
}

/* TODO
*  - обфускация
*  - Названия с кавычкой. Ошибка при нажатии на рецепт в списке
* */


