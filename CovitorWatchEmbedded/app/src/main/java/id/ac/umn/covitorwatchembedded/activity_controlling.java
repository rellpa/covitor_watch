package id.ac.umn.covitorwatchembedded;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class activity_controlling extends AppCompatActivity {
    private static final String TAG = "CovitorWatch";
    private int mMaxChars = 50000;
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
    private ReadInput mReadThread = null;
    private boolean mIsUserInitiatedDisconnect = false;
    private boolean mIsBluetoothConnected = false;
    private BluetoothDevice mDevice;
    public static final String DEVICE_EXTRA = "id.ac.umn.covitorwatchembedded.SOCKET";
    public static final String DEVICE_UUID = "id.ac.umn.covitorwatchembedded.uuid";
    public static final String BUFFER_SIZE = "id.ac.umn.covitorwatchembedded.buffersize";
    final static String heartratecommand="20";
    final static String oxygensaturationcommand="21";
    final static String temperaturecommand="22";
    private ProgressDialog progressDialog;
    private Button heartrate,oxygensaturation, temperature, changedevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_controlling);
        ActivityHelper.initialize(this);
        heartrate=(Button)findViewById(R.id.heartrate);
        oxygensaturation=(Button)findViewById(R.id.oxygensaturation);
        temperature=(Button)findViewById(R.id.temperature);
        changedevice=(Button)findViewById(R.id.changedevice);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        mDevice = b.getParcelable(MainActivity.DEVICE_EXTRA);
        mDeviceUUID = UUID.fromString(b.getString(MainActivity.DEVICE_UUID));
        mMaxChars = b.getInt(MainActivity.BUFFER_SIZE);
        Log.d(TAG, "Ready");
        temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mBTSocket.getOutputStream().write(temperaturecommand.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent activity_monitoring_screen = new Intent(activity_controlling.this, activity_monitoring_screen.class);
                activity_monitoring_screen.putExtra(DEVICE_EXTRA, mDevice);
                activity_monitoring_screen.putExtra(DEVICE_UUID, mDeviceUUID.toString());
                activity_monitoring_screen.putExtra(BUFFER_SIZE, mMaxChars);
                startActivity(activity_monitoring_screen);
            }});
        heartrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mBTSocket.getOutputStream().write(heartratecommand.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent activity_monitoring_heart = new Intent(activity_controlling.this, activity_monitoring_heart.class);
                activity_monitoring_heart.putExtra(DEVICE_EXTRA, mDevice);
                activity_monitoring_heart.putExtra(DEVICE_UUID, mDeviceUUID.toString());
                activity_monitoring_heart.putExtra(BUFFER_SIZE, mMaxChars);
                startActivity(activity_monitoring_heart);
            }});
        oxygensaturation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mBTSocket.getOutputStream().write(oxygensaturationcommand.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent activity_monitoring_oxymeter = new Intent(activity_controlling.this, activity_monitoring_oxymeter.class);
                activity_monitoring_oxymeter.putExtra(DEVICE_EXTRA, mDevice);
                activity_monitoring_oxymeter.putExtra(DEVICE_UUID, mDeviceUUID.toString());
                activity_monitoring_oxymeter.putExtra(BUFFER_SIZE, mMaxChars);
                startActivity(activity_monitoring_oxymeter);
            }});
        changedevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }});
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
                inputStream = mBTSocket.getInputStream();
                while (!bStop) {
                    byte[] buffer = new byte[256];
                    if (inputStream.available() > 0) {
                        inputStream.read(buffer);
                        int i = 0;
                        for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
                        }
                        final String strInput = new String(buffer, 0, i);
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
            new DisConnectBT().execute();
        }
        Log.d(TAG, "Paused");
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mBTSocket == null || !mIsBluetoothConnected) {
            new ConnectBT().execute();
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
            progressDialog = ProgressDialog.show(activity_controlling.this, "Hold on", "Connecting");
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
                mConnectSuccessful = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (!mConnectSuccessful) {
                Toast.makeText(getApplicationContext(), "Could not connect to device.Please turn on your Hardware", Toast.LENGTH_LONG).show();
                finish();
            } else {
                msg("Connected to device");
                mIsBluetoothConnected = true;
                mReadThread = new ReadInput(); // Kick off input reader
            }
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}