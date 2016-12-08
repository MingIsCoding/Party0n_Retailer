package edu.sjsu.pm.partyonretailer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by Ming on 10/24/16.
 */

public abstract class CloseableActivity extends AppCompatActivity {
    abstract int getToolBarID();
    abstract int getResourceID();

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(getResourceID());
        initToolBar();
    }
    private void initToolBar(){
//        Log.d("CloseableActivity","initToolBar");
        Toolbar toolbar = (Toolbar) findViewById(getToolBarID());
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
