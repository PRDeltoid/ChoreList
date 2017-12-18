package com.taylor.chorelist;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

public class ChoreItemActivity extends AppCompatActivity {
    private EditText chore_name_text,
                    chore_interval_text;
    private int chore_id;
    private boolean chore_has_id = false;
    private ChoreItem chore_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chore_item);

        chore_name_text = (EditText) findViewById(R.id.chore_name_text);
        chore_interval_text = (EditText) findViewById(R.id.chore_interval_text);

        Toolbar chore_item_toolbar = (Toolbar) findViewById(R.id.chore_item_toolbar);
        try {
            setSupportActionBar(chore_item_toolbar);
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        //Chore ID Flag is true if the chore has an ID
        //Otherwise, false
        set_chore_id_flag();

        if(chore_has_id) {
            chore_item = ChoreDatabaseHelper.pull_chore_item(getApplicationContext(), chore_id);
            populate_ui();
        } else {
            chore_item = new ChoreItem();
        }
    }

    void set_chore_id_flag() {
        chore_id = getIntent().getIntExtra("EXTRA_CHORE_ID", -1);
        if(chore_id != -1) {
            chore_has_id = true; //set entry_has_id flag
        }
    }

    private void populate_ui() {
        String name = chore_item.get_name();
        String interval = String.valueOf(chore_item.get_interval());

        chore_name_text.setText(name);
        chore_interval_text.setText(interval);
    }

    public static Intent get_start_intent(Context context, int chore_id) {
        Intent intent = new Intent(context, ChoreItemActivity.class);
        intent.putExtra("EXTRA_CHORE_ID", chore_id);

        return intent;
    }

}
