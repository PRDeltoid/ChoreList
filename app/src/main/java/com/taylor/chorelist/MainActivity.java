package com.taylor.chorelist;

import android.content.Intent;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listview;
    private ArrayList<ChoreItem> chore_items;
    private ArrayList<Integer> selected_entry_positions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Populate the chore entries list
        chore_items = ChoreDatabaseHelper.pull_chore_items(getApplicationContext());

        //Create an adapter for our data
        ListViewAdapter adapter = new ListViewAdapter();

        //Create our "selected" list
        selected_entry_positions = new ArrayList<>();

        //Initialize our listview object
        listview = findViewById(R.id.chore_entry_listview);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        //Multi-Select listener
        listview.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (checked) {
                    selected_entry_positions.add(position);
                    ((TransitionDrawable) listview.getChildAt(position).getBackground()).startTransition(250);
                } else {
                    selected_entry_positions.remove(position);
                    ((TransitionDrawable) listview.getChildAt(position).getBackground()).reverseTransition(250);
                }
                mode.invalidate();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate a menu resource providing context menu items
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_multiselect, menu);
                return true;
            }

            // Called each time the action mode is shown. Always called after onCreateActionMode, but
            // may be called multiple times if the mode is invalidated.
            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false; // Return false if nothing is done
            }

            // Called when the user selects a contextual menu item
            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                BaseAdapter adapter = (BaseAdapter) listview.getAdapter();
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        mode.finish();
                    default:
                        return false;
                }
            }

            // Called when the user exits the action mode
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                //refresh the log
                BaseAdapter adapter = (BaseAdapter) listview.getAdapter();
                adapter.notifyDataSetChanged();
                reset_checked_items();
            }
        });
        listview.setLongClickable(true);

        listview.setAdapter(adapter);

        //Create item click listener event
        //Click open a single entry view intent and passes the ID of the entry
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long chore_id) {
                Intent chore_item_intent = ChoreItemActivity.get_start_intent(getApplicationContext(), chore_id);
                startActivityForResult(chore_item_intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK) {
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case(R.id.action_new):
                Intent new_item_intent = new Intent(getApplicationContext(), ChoreItemActivity.class);
                startActivityForResult(new_item_intent, 1);
                return true;
            case(R.id.action_settings):
                //Add settings menu intent here
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void reset_checked_items() {
        for(int i=0; i < listview.getCount(); i++) {
            //Get our transitional background object from the current list item's view
            TransitionDrawable transition = (TransitionDrawable) listview.getChildAt(i).getBackground();
            //Start the transition back to unchecked
            transition.startTransition(250);
            //set our current transition state as our "default" to prevent re-transitioning in the multi-select listener
            transition.resetTransition();
        }
        selected_entry_positions.clear();
    }

    //Adapter for turning an ArrayList of ChoreItems into a ListView
    private class ListViewAdapter extends BaseAdapter {
        //Code related to composing our list view

        @Override
        public int getCount() {
            return chore_items.size();
        }

        //Gets list item data
        @Override
        public ChoreItem getItem(int position) {
            return chore_items.get(position);
        }

        //Get list item id
        @Override
        public long getItemId(int position) {
            return chore_items.get(position).get_index();
            //return position;
        }

        //Creates the the individual list item view
        @Override
        public View getView(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_single_line, container, false);
            }

            //Get current chore item from chore_items arraylist
            ChoreItem chore_item = getItem(position);
            String chore_name = chore_item.get_name();
            String interval = String.valueOf(chore_item.get_interval());

            ((TextView) convertView.findViewById(R.id.chore_name))
                    .setText(chore_name);
            ((TextView) convertView.findViewById(R.id.interval))
                    .setText(interval);

            return convertView;
        }
    }
}
