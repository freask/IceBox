package ru.freask.studyjam.icebox.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.picasso.Picasso;

import ru.freask.studyjam.icebox.MainActivity;
import ru.freask.studyjam.icebox.R;
import ru.freask.studyjam.icebox.db.OrmHelper;
import ru.freask.studyjam.icebox.models.Product;
import ru.freask.studyjam.icebox.models.Recipe;

public class RecipesAdapter extends ArrayAdapter<Recipe> {
    Context context;

    public RecipesAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        this.context = context;
    }


    static class ViewHolder{
        TextView name;
        ImageView image;

        void populateItem(Context context, final Recipe recipe) {
            name.setText(recipe.getLabel());
            Picasso.with(context).load(recipe.getImage()).into(image);
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.recipe_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.image = (ImageView) convertView.findViewById(R.id.item_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Recipe recipe = getItem(position);
        holder.populateItem(context, recipe);
        return convertView;
    }
}
