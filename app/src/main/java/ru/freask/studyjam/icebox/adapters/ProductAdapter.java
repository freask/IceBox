package ru.freask.studyjam.icebox.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.List;

import ru.freask.studyjam.icebox.MainActivity;
import ru.freask.studyjam.icebox.R;
import ru.freask.studyjam.icebox.RecipesActivity;
import ru.freask.studyjam.icebox.db.OrmHelper;
import ru.freask.studyjam.icebox.models.Product;

import static ru.freask.studyjam.icebox.R.color.*;


public class ProductAdapter extends ArrayAdapter<Product> {
    Context context;

    public ProductAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        this.context = context;
    }


    class ViewHolder{
        TextView name;
        TextView count;
        ImageButton minus;
        ImageButton plus;
        ImageButton galka;
        ImageButton del;

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

            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteDialog(product.getId());
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
            holder.del = (ImageButton) convertView.findViewById(R.id.del);
            holder.galka = (ImageButton) convertView.findViewById(R.id.item_image);
            holder.galka.setImageResource(!isItemChecked(position, parentView) ? R.drawable.galka_transp : R.drawable.galka);
            holder.galka.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectRow(v);
                }

                private void selectRow(View v) {
                    parentView.setItemChecked(position, !isItemChecked(position, parentView));
                    Log.v(MainActivity.TAG, "checked (" + !isItemChecked(position, parentView) + ") = "+ position);
                    ImageButton galka = (ImageButton) v.findViewById(R.id.item_image);
                    galka.setImageResource(!isItemChecked(position, parentView) ? R.drawable.galka_transp : R.drawable.galka);
                }
            });

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Product product = getItem(position);
        holder.populateItem(product);
        convertView.setBackgroundColor(context.getResources().getColor(product.count >= product.like_count ? green30 : pink30));
        return convertView;
    }

    private boolean isItemChecked(int pos, ListView parentView) {
        SparseBooleanArray sparseBooleanArray = parentView.getCheckedItemPositions();
        return sparseBooleanArray.get(pos);
    }

    private void deleteDialog(final long product_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.deleteAlert)
                .setMessage(R.string.ayousure)
                .setCancelable(false)
                .setPositiveButton(R.string.todelete,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.delProduct(product_id);
                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }


}
