package ru.freask.studyjam.icebox;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ru.freask.studyjam.icebox.adapters.ProductAdapter;
import ru.freask.studyjam.icebox.db.OrmHelper;
import ru.freask.studyjam.icebox.db.ProductDao;
import ru.freask.studyjam.icebox.models.Product;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static ProductAdapter productListAdapter;
    private static ListView productListView;
    private static OrmHelper ormHelper;

    public static final String TAG = "TAG";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        Stetho.initialize(Stetho.newInitializerBuilder(this).enableDumpapp(Stetho.defaultDumperPluginsProvider(this)).enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this)).build());
        setContentView(R.layout.activity_main);
        navigationDrawerSetUp();
        initDB();
        productListView = (ListView) findViewById(R.id.listViewProducts);
        productListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        productListView.setMultiChoiceModeListener(new MultiChoiceImpl(productListView));
        productListAdapter = new ProductAdapter(this);
        productListView.setAdapter(productListAdapter);

        FloatingActionButton addButton = (FloatingActionButton) findViewById(R.id.add_but);
        addButton.setColorNormalResId(R.color.pink);
        addButton.setColorPressedResId(R.color.pink_pressed);
        //button.setIcon(R.drawable.ic_fab_star);
        addButton.setStrokeVisible(false);
        addButton.setOnClickListener(this);

        FloatingActionButton searchButton = (FloatingActionButton) findViewById(R.id.search_but);
        searchButton.setColorNormalResId(R.color.pink);
        searchButton.setColorPressedResId(R.color.pink_pressed);
        //button.setIcon(R.drawable.ic_fab_star);
        searchButton.setStrokeVisible(false);
        searchButton.setOnClickListener(this);

        fillProductList();
    }

    public static void fillProductList() {
        try {
            ProductDao productDao = (ProductDao) ormHelper.getDaoByClass(Product.class);
            productListAdapter.clear();
            for (Product product : productDao.getAllProducts()) {
                productListAdapter.add(product);
            }
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_but:
                openAddDialog();
                break;

            case R.id.search_but:
                List<String> selected = getSelectedItems();
                String text = implode(" ", selected);
                Intent i = new Intent(context, RecipesActivity.class);
                i.putExtra("query", text);
                context.startActivity(i);
                break;
        }
    }

    private void openAddDialog() {
        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.add_dialog, (ViewGroup) findViewById(R.id.add_dialog_linearlayout));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.product_add);
        builder.setView(layout);
        final EditText name = (EditText) layout.findViewById(R.id.add_text);
        final EditText count = (EditText) layout.findViewById(R.id.add_count);
        final EditText like_count = (EditText) layout.findViewById(R.id.add_like_count);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addProduct(name.getText().toString(), Integer.parseInt(count.getText().toString()), Integer.parseInt(like_count.getText().toString()));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void addProduct(String name, int count, int likeCount) {
        Log.v(TAG, "Adding " + name);

        Product product = new Product();
        product.name = name;
        product.count = count;
        product.like_count = likeCount;

        try {
            ProductDao productDao = (ProductDao) ormHelper.getDaoByClass(Product.class);
            productDao.create(product);
            fillProductList();
        } catch (SQLException e) {
            Log.e(TAG, e.getMessage());
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
            Log.e(MainActivity.TAG, e.getMessage());
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
}
