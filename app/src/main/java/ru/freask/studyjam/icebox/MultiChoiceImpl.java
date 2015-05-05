package ru.freask.studyjam.icebox;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FreaskHOME on 02.05.2015.
 */
public class MultiChoiceImpl implements ListView.MultiChoiceModeListener {
    private ListView listView;

    public MultiChoiceImpl(ListView listView) {
        this.listView = listView;
    }

    @Override
    //Метод вызывается при любом изменении состояния выделения рядов
    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
        Log.d(MainActivity.TAG, "onItemCheckedStateChanged");
        int selectedCount = listView.getCheckedItemCount();
        //Добавим количество выделенных рядов в Context Action Bar
        setSubtitle(actionMode, selectedCount);
    }

    @Override
    //Здесь надуваем CAB из xml
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        Log.d(MainActivity.TAG, "onCreateActionMode");
        /*MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);*/
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        Log.d(MainActivity.TAG, "onPrepareActionMode");
        return false;
    }

    @Override
    //Вызывается при клике на любой Item из СAB
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        String text = "Action - " + menuItem.getTitle() + " ; Selected items: " + MainActivity.getSelectedItems();
        Toast.makeText(listView.getContext(), text, Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        Log.d(MainActivity.TAG, "onDestroyActionMode");
    }

    private void setSubtitle(ActionMode mode, int selectedCount) {
        switch (selectedCount) {
            case 0:
                mode.setSubtitle(null);
                break;
            default:
                mode.setTitle(String.valueOf(selectedCount));
                break;
        }
    }


}