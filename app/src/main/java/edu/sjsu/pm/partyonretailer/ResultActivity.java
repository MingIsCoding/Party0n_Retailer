package edu.sjsu.pm.partyonretailer;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import edu.sjsu.pm.partyonretailer.config.AppData;
import edu.sjsu.pm.partyonretailer.entities.Transaction;

public class ResultActivity extends CloseableActivity {
    private final static int REQUEST_SCN = 10;
    private TextView mResultView;
    static String TAG = "ResultActivity";
    ImageView mResultIcon;
    TextView mResultTag;
    TextView mResultMsg;
    LinearLayout mMsgLayout;

    @Override
    int getToolBarID() {
        return 0;
    }

    @Override
    int getResourceID() {
        return R.layout.activity_result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_result);
        mResultView = (TextView)findViewById(R.id.result_text_view);
        mResultIcon = (ImageView)findViewById(R.id.result_icon);
        mResultTag = (TextView)findViewById(R.id.result_tag);
        mResultMsg = (TextView)findViewById(R.id.result_text_view);
        mMsgLayout = (LinearLayout)findViewById(R.id.msg_layout);
        Intent in = new Intent(ResultActivity.this,ScanActivity.class);
        startActivityForResult(in, REQUEST_SCN);
        Log.d(TAG,"on Create");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String str = data.getExtras().getString(ScanActivity.CODE_RESULT_STR);
        if(requestCode == REQUEST_SCN && resultCode == ScanActivity.SCAN_RESULT_SUCCESS){
            try {
                JSONObject result = new JSONObject(str);
                mResultView.append(result.getString("user_id")+"\n");
                mResultView.append(result.getString("balance")+"\n");
                mResultView.append(result.getString("expiresAt")+"\n");
                float balance = Float.parseFloat(result.getString("balance"));
                if(balance < AppData.getAmout()){
                    fail("Balance is not enough.");
                }
                Transaction transaction = new Transaction();
                transaction.setState(0);
                transaction.setRetailerID("TEST_RETAILER");
                transaction.setUserID(result.getString("user_id"));
                transaction.setLocation("21 4th E San Jose");
                transaction.setAmount(AppData.getAmout()+"");
                transaction.setItems(AppData.itemList);
                transaction.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        setSuccessMsg(null);
                    }
                });
            } catch (JSONException e) {
                fail("Invalid QR Code.");
                e.printStackTrace();
            }

        }else {
            fail(str);
        }

    }
    private void setSuccessMsg(String msg){
        mResultMsg.setText("Transaction has been successfully submitted." +
                " It will be finished soon.");
        mResultIcon.setBackground(getResources().getDrawable(R.drawable.ic_check_circle_white_24dp));
        mResultTag.setText("SUCCESS");
        mMsgLayout.setBackgroundColor(getResources().getColor(R.color.holo_green_light));
    }
    private void fail(String msg){
        mResultIcon.setBackground(getResources().getDrawable(R.drawable.ic_highlight_off_white_24dp));
        mResultTag.setText("FAILED");
        mResultMsg.setText(msg);
        mMsgLayout.setBackgroundColor(getResources().getColor(R.color.holo_red_light));
    }
    @Override
    protected void onResume() {
        super.onResume();

    }

    public void goBack(View view) {
        finish();
    }

    public void scan(View view) {
        Intent in = new Intent(ResultActivity.this,ScanActivity.class);
        startActivityForResult(in, REQUEST_SCN);
    }
}
