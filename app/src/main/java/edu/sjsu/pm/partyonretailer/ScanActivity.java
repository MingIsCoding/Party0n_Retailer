package edu.sjsu.pm.partyonretailer;

import android.content.Intent;
import android.graphics.PointF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;

import org.json.JSONException;
import org.json.JSONObject;

public class ScanActivity extends CloseableActivity implements QRCodeReaderView.OnQRCodeReadListener {
    public final static String CODE_SUCCESS = "success";
    public final static String CODE_RESULT_STR = "result_string";
    private boolean isValid = false;
    public final static int SCAN_RESULT_SUCCESS = 102;
    //private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;

    @Override
    int getToolBarID() {
        return R.id.scan_toolbar;
    }

    @Override
    int getResourceID() {
        return R.layout.activity_scan;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_scan);
        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        //resultTextView = (TextView)findViewById(R.id.scan_result_view);
        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        // Use this function to change the autofocus interval (default is 5 secs)
        qrCodeReaderView.setAutofocusInterval(2000L);

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true);

        // Use this function to set front camera preview
        qrCodeReaderView.setFrontCamera();

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
    }
    // Called when a QR is decoded
    // "text" : the text encoded in QR
    // "points" : points where QR control points are placed in View
    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        //resultTextView.setText(text);
        System.out.println(text);
        try {
            JSONObject result = new JSONObject(text);
            if(result.getString("app_id").equals("partyon") &&
            result.getString("intent").equals("payment") && !isValid){
                isValid = true;
                dealResult(true,text);
            }else {
                dealResult(false,text);
            };
        } catch (JSONException e) {
            e.printStackTrace();
            dealResult(false,text);
        }

    }
    private void dealResult(boolean success, String json){
        Bundle b = new Bundle();
        b.putBoolean(CODE_SUCCESS,success);
        b.putString(CODE_RESULT_STR,json);
        Intent i = new Intent();
        i.putExtras(b);
        setResult(SCAN_RESULT_SUCCESS,i);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }
}
