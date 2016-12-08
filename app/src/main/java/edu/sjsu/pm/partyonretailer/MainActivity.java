package edu.sjsu.pm.partyonretailer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import edu.sjsu.pm.partyonretailer.config.AppData;
import edu.sjsu.pm.partyonretailer.entities.Transaction;

public class MainActivity extends AppCompatActivity {
    EditText mItemNameField, mItemPriceField;
    TextView mListView, mTotalView;
    Button mItemAddBtn, mScanBtn;
    List<Merchandise> mItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mItemNameField = (EditText)findViewById(R.id.item_name_field);
        mItemPriceField = (EditText)findViewById(R.id.item_price_field);
        mTotalView = (TextView)findViewById(R.id.total_view);
        mItemAddBtn = (Button) findViewById(R.id.item_add_btn);
        mScanBtn = (Button)findViewById(R.id.scan_btn);
        mListView = (TextView)findViewById(R.id.list_field);
        mItemPriceField.setInputType(InputType.TYPE_CLASS_NUMBER);
        mItemPriceField.addTextChangedListener(new MoneyTextWatcher(mItemPriceField));
        mItemList = new ArrayList<>();
        if(!AppData.isParseAdapterInitiated){
            Log.v("MainActivity", "on Creating..................................");
            Parse.enableLocalDatastore(this);
            Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                    .applicationId(AppData.backendServerAppID)
                    //.clientKey("test")
                    .server(AppData.backendServerURL).build());
            AppData.isParseAdapterInitiated = true;
            ParseObject.registerSubclass(Transaction.class);
        }
    }

    public void addItem(View view) {
        if(mItemNameField.getText() == null || mItemNameField.getText().equals("")){
            Toast.makeText(MainActivity.this,
                    "Please add the item's name.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(mItemPriceField.getText() == null || mItemPriceField.getText().equals("")){
            Toast.makeText(MainActivity.this,
                    "Please add the item's price.", Toast.LENGTH_SHORT).show();
            return;
        }
        mItemList.add(new Merchandise(mItemNameField.getText().toString(),
                Float.parseFloat(mItemPriceField.getText().toString()
                        .substring(mItemPriceField.getText().toString().indexOf("$")+1))));
        updateList();
    }
    private float sumUp(){
        float total = 0;
        for(Merchandise m : mItemList){
            total += m.getPrice();
        }
        return total;
    }
    private void updateList(){
        mListView.setText("");
        for(Merchandise m :mItemList){
            mListView.append(m.getName()+"\t\t\t\t\t$"+m.getPrice()+"\n");
        }
        mTotalView.setText("Total: "+sumUp());
    }

    public void submitAndScan(View view) {
        AppData.itemList = mItemList;
        Intent i = new Intent(MainActivity.this,ResultActivity.class);
        startActivity(i);
    }
}
