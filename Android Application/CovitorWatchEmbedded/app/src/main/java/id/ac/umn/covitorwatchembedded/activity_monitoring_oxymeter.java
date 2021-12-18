package id.ac.umn.covitorwatchembedded;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class activity_monitoring_oxymeter extends AppCompatActivity {
    private static final String TAG = "CovitorWatch";
    final static String saturation="25";
    private int mMaxChars = 50000;
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
    private activity_monitoring_oxymeter.ReadInput mReadThread = null;
    private boolean mIsUserInitiatedDisconnect = false;
    private TextView mTxtReceive, value, analisis;
    private ScrollView scrollView;
    private CheckBox chkScroll;
    private CheckBox chkReceiveText;
    private boolean mIsBluetoothConnected = false;
    private BluetoothDevice mDevice;
    private ProgressDialog progressDialog;
    public String body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_monitoring_oxymeter);
        ActivityHelper.initialize(this);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        mDevice = b.getParcelable(activity_controlling.DEVICE_EXTRA);
        mDeviceUUID = UUID.fromString(b.getString(activity_controlling.DEVICE_UUID));
        mMaxChars = b.getInt(activity_controlling.BUFFER_SIZE);
        Log.d(TAG, "Ready");
        mTxtReceive = (TextView) findViewById(R.id.txtReceive);
        mTxtReceive.setVisibility(View.GONE);
        value = (TextView) findViewById(R.id.value);
        analisis = (TextView) findViewById(R.id.analisis);
        chkScroll = (CheckBox) findViewById(R.id.chkScroll);
        chkScroll.setVisibility(View.GONE);
        chkReceiveText = (CheckBox) findViewById(R.id.chkReceiveText);
        chkReceiveText.setVisibility(View.GONE);
        scrollView = (ScrollView) findViewById(R.id.viewScroll);
        scrollView.setVisibility(View.GONE);
        mTxtReceive.setMovementMethod(new ScrollingMovementMethod());
    }

    private class ReadInput implements Runnable {
        private boolean bStop = false;
        private Thread t;
        public ReadInput() {
            t = new Thread(this, "Input Thread");
            t.start();
        }
        public boolean isRunning() {
            return t.isAlive();
        }

        @Override
        public void run() {
            InputStream inputStream;
            try {
                Thread.sleep(2000);
                try {
                    mBTSocket.getOutputStream().write(saturation.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                inputStream = mBTSocket.getInputStream();
                while (!bStop) {
                    byte[] buffer = new byte[256];
                    if (inputStream.available() > 0) {
                        inputStream.read(buffer);
                        int i = 0;
                        for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
                        }
                        final String strInput = new String(buffer, 0, i);
                        if (chkReceiveText.isChecked()) {
                            mTxtReceive.post(new Runnable() {
                                @Override
                                public void run() {
                                    for (int i=0; i<strInput.length(); i++) {
                                        if (strInput.charAt(i) == '\n' || strInput.charAt(i) == '\t' || strInput.charAt(i) == ' ') {
                                            body = strInput.substring(0, i) + "" + strInput.substring(i+1, strInput.length());
                                            body = body.replaceAll("\\D+","");
                                        }
                                    }
                                    value.setText(body+" %");
                                    Integer suhu = Integer.valueOf(body);
                                    if(suhu < 95){
                                        analisis.setText("You’re Not Healthy");
                                    }if(suhu >= 95){
                                        analisis.setText("You’re Healthy");
                                    }
                                    try {
                                        mBTSocket.getOutputStream().write(saturation.getBytes());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    mTxtReceive.append(strInput);
                                    int txtLength = mTxtReceive.getEditableText().length();
                                    if(txtLength > mMaxChars){
                                        mTxtReceive.getEditableText().delete(0, txtLength - mMaxChars);
                                    }
                                    if (chkScroll.isChecked()) {
                                        scrollView.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                scrollView.fullScroll(View.FOCUS_DOWN);
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    }
                    Thread.sleep(500);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        public void stop() {
            bStop = true;
        }
    }

    private class DisConnectBT extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (mReadThread != null) {
                mReadThread.stop();
                while (mReadThread.isRunning())
                    ;
                mReadThread = null;
            }
            try {
                mBTSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mIsBluetoothConnected = false;
            if (mIsUserInitiatedDisconnect) {
                finish();
            }
        }
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        if (mBTSocket != null && mIsBluetoothConnected) {
            new activity_monitoring_oxymeter.DisConnectBT().execute();
        }
        Log.d(TAG, "Paused");
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mBTSocket == null || !mIsBluetoothConnected) {
            new activity_monitoring_oxymeter.ConnectBT().execute();
        }
        Log.d(TAG, "Resumed");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopped");
        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean mConnectSuccessful = true;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(activity_monitoring_oxymeter.this, "Hold on", "Connecting");
        }

        @Override
        protected Void doInBackground(Void... devices) {
            try {
                if (mBTSocket == null || !mIsBluetoothConnected) {
                    mBTSocket = mDevice.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBTSocket.connect();
                }
            } catch (IOException e) {
                e.printStackTrace();
                mConnectSuccessful = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!mConnectSuccessful) {
                Toast.makeText(getApplicationContext(), "Could not connect to device. Is it a Serial device? Also check if the UUID is correct in the settings", Toast.LENGTH_LONG).show();
                finish();
            } else {
                msg("Connected to device");
                mIsBluetoothConnected = true;
                mReadThread = new ReadInput();
            }
            progressDialog.dismiss();
        }
    }
}