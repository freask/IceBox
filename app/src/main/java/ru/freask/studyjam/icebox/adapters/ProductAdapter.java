package ru.freask.studyjam.icebox.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import ru.freask.studyjam.icebox.MainActivity;
import ru.freask.studyjam.icebox.R;
import ru.freask.studyjam.icebox.db.OrmHelper;
import ru.freask.studyjam.icebox.models.Product;

import static ru.freask.studyjam.icebox.R.color.*;


public class ProductAdapter extends ArrayAdapter<Product> {
    Context context;
    private static OrmHelper ormHelper;

    public ProductAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);

        ormHelper = OpenHelperManager.getHelper(context, OrmHelper.class);
        this.context = context;
    }


    static class ViewHolder{
        TextView name;
        TextView count;
        ImageButton minus;
        ImageButton plus;
        ImageButton galka;

        void populateItem(final Product product) {
            name.setText(product.name);
            count.setText(Integer.toString(product.count));

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product.count -= 1;
                    MainActivity.updateProduct(product);
                }
            });

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    product.count += 1;
                    MainActivity.updateProduct(product);
                }
            });
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final ListView parentView = (ListView) parent;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.product_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            holder.minus = (ImageButton) convertView.findViewById(R.id.minus);
            holder.plus = (ImageButton) convertView.findViewById(R.id.plus);
            holder.galka = (ImageButton) convertView.findViewById(R.id.item_image);
            holder.galka.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectRow(v);
                }

                private void selectRow(View v) {
                    parentView.setItemChecked(position, !isItemChecked(position));
                    ImageButton galka = (ImageButton) v.findViewById(R.id.item_image);
                    galka.setImageResource(!isItemChecked(position) ? R.drawable.galka_transp : R.drawable.galka);
                }

                private boolean isItemChecked(int pos) {
                    SparseBooleanArray sparseBooleanArray = parentView.getCheckedItemPositions();
                    return sparseBooleanArray.get(pos);
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Product product = getItem(position);
        holder.populateItem(product);
        convertView.setBackgroundColor(context.getResources().getColor(product.count >= product.like_count ? green : red));
        return convertView;
    }
}
