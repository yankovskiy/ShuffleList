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

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import ru.neverdark.shufflelist.R;

public class MainActivity extends Activity {
    private class AddButtonClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            String text = mEditText.getText().toString().trim();
            if (text.length() > 0) {
                mAdapter.insert(new Item(text), 0);
                mEditText.setText("");
            }
        }
    }

    private ListAdapter mAdapter;
    private ImageView mButton;
    private EditText mEditText;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list);
        mButton = (ImageView) findViewById(R.id.addButton);
        mEditText = (EditText) findViewById(R.id.text);

        mButton.setOnClickListener(new AddButtonClickListener());
        mButton.setOnTouchListener(new ImageOnTouchListener());
        mAdapter = new ListAdapter(this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_shuffle:
            mAdapter.shuffle();
            return true;
        case R.id.action_trash:
            mAdapter.removeSelected();
            return true;
        case R.id.action_clear:
            mAdapter.clear();
            return true;
        case R.id.action_rate:
            showRate();
            return true;
        case R.id.action_feedback:
            showFeedback();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.openDb();
        mAdapter.loadDataFromDb();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.saveDataToDb();
        mAdapter.closeDb();
    }

    private void showFeedback() {
        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.setType("plain/text");
        mailIntent.putExtra(Intent.EXTRA_EMAIL,
                new String[] { getString(R.string.author_email) });
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        startActivity(Intent.createChooser(mailIntent,
                getString(R.string.chooseEmailApp)));
    }

    private void showRate() {
        String url = "market://details?id=".concat(getPackageName());
        Intent marketIntent = new Intent(Intent.ACTION_VIEW);
        marketIntent.setData(Uri.parse(url));
        startActivity(marketIntent);
    }

}
