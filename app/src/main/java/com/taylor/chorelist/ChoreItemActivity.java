package com.taylor.chorelist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class ChoreItemActivity extends AppCompatActivity {
    private EditText chore_name_text,
                    chore_interval_text;
    private long chore_id;
    private boolean chore_has_id = false;
    private ChoreItem chore_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chore_item);

        chore_name_text = (EditText) findViewById(R.id.chore_name_text);
        chore_interval_text = (EditText) findViewById(R.id.chore_interval_text);

        Toolbar chore_item_toolbar = (Toolbar) findViewById(R.id.chore_item_toolbar);
        setSupportActionBar(chore_item_toolbar);

        //Chore ID Flag is true if the chore has an ID
        //Otherwise, false
        set_chore_id_flag();

        if (chore_has_id) {
            chore_item = ChoreDatabaseHelper.pull_chore_item(getApplicationContext(), chore_id);
            populate_ui();
        } else {
            chore_item = new ChoreItem();
        }

        TextWatcher refresh_on_change_watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //if textfield is not empty, update the object with new values after change
                if (!(s.toString().isEmpty())) {
                    update_entry_object();
                }
            }
        };
        chore_name_text.addTextChangedListener(refresh_on_change_watcher);
        chore_interval_text.addTextChangedListener(refresh_on_change_watcher);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chore_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case(R.id.action_save):
                if(chore_has_id) {
                    ChoreDatabaseHelper.update_entry(getApplicationContext(), chore_id, chore_item);
                } else {
                    ChoreDatabaseHelper.new_entry(getApplicationContext(), chore_item);
                }
                setResult(RESULT_OK, null);
                finish();
                return true;
            case(R.id.action_delete):
                //Delete the chore
                ChoreDatabaseHelper.delete_chore_item(getApplicationContext(), chore_id);
                setResult(RESULT_OK, null);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void set_chore_id_flag() {
        chore_id = getIntent().getLongExtra("EXTRA_CHORE_ID", -1);
        if(chore_id != -1) {
            chore_has_id = true; //set entry_has_id flag
        }
    }

    private void update_entry_object() {
        String chore_name;
        int chore_interval;

        try {
            chore_interval = Integer.parseInt(chore_interval_text.getText().toString());
        } catch(Exception e) {
            chore_interval = 0;
        }
        chore_name = chore_name_text.getText().toString();

        //Update the local chore item with a new copy of the chore item
        chore_item = new ChoreItem(chore_name, chore_interval);
    }

    private void populate_ui() {
        String name = chore_item.get_name();
        String interval = String.valueOf(chore_item.get_interval());

        chore_name_text.setText(name);
        chore_interval_text.setText(interval);
    }

    public static Intent get_start_intent(Context context, long chore_id) {
        Intent intent = new Intent(context, ChoreItemActivity.class);
        intent.putExtra("EXTRA_CHORE_ID", chore_id);

        return intent;
    }

}
