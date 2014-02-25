/*******************************************************************************
 * Copyright (C) 2014 Artem Yankovskiy (artemyankovskiy@gmail.com).
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ru.neverdark.shufflelist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import ru.neverdark.shufflelist.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

public class ListAdapter extends ArrayAdapter<Item> {
    private class CheckBoxClickListener implements OnClickListener {
        private final int mPosition;

        public CheckBoxClickListener(final int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            Item item = getItem(mPosition);
            CheckBox cb = (CheckBox) v;
            boolean isChecked = cb.isChecked();
            item.setIsChecked(isChecked);
        }
    }

    private Context mContext;
    private DbAdapter mDbAdapter;
    private List<Item> mObjects;
    private int mResource;

    public ListAdapter(Context context) {
        this(context, R.layout.list_item, new ArrayList<Item>());
    }

    private ListAdapter(Context context, int resource, List<Item> list) {
        super(context, resource, list);
        mContext = context;
        mObjects = list;
        mResource = resource;
        mDbAdapter = new DbAdapter(mContext);
    }

    public void closeDb() {
        mDbAdapter.close();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CheckBox cb;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(mResource, parent, false);
            cb = (CheckBox) row.findViewById(R.id.checkBox);
            row.setTag(cb);
        } else {
            cb = (CheckBox) row.getTag();
        }

        cb.setText(mObjects.get(position).getText());
        cb.setChecked(mObjects.get(position).isChecked());
        cb.setOnClickListener(new CheckBoxClickListener(position));

        return row;
    }

    public void loadDataFromDb() {
        if (mObjects.size() > 0) {
            mObjects.clear();
        }

        mDbAdapter.loadItemFromDb(mObjects);
        notifyDataSetChanged();
    }

    public void openDb() {
        mDbAdapter.open();
    }

    public void removeSelected() {
        List<Item> copy = new ArrayList<Item>(mObjects);

        for (Item item : copy) {
            if (item.isChecked()) {
                remove(item);
            }
        }

        notifyDataSetChanged();
    }

    public void saveDataToDb() {
        for (int index = 0; index < mObjects.size(); index++) {
            mObjects.get(index).setPosition(index);
        }

        mDbAdapter.saveItemsToDb(mObjects);
    }

    public void shuffle() {
        long seed = System.nanoTime();
        Collections.shuffle(mObjects, new Random(seed));
        notifyDataSetChanged();
    }
}
