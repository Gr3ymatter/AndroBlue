package com.example.androblue;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class BluetoothDevicesActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mmDevice;
    private OutputStream mOutputStream;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_devices);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



    }

    @Override
    protected void onResume() {
        super.onResume();
        BluetoothSocket mBluetoothSocket;

        if(mBluetoothAdapter == null)
        {
            TextView noAdapterView = new TextView(this);
            noAdapterView.setText("No Bluetooth Adapeter Found");
        }
        else
        {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 0);
            }
        }


        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0){

            for(BluetoothDevice device : pairedDevices){
                Log.d("Devices: ", "" + device.getName());
                if(device.getName().equals("HC-05")){
                    Log.d("Found Device: ", "Yes");
                    mmDevice = device;
                    break;
                }
            }
        }

        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        try {
            mBluetoothSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            mBluetoothSocket.connect();
            mOutputStream = mBluetoothSocket.getOutputStream();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.d("Error: ", "Connection Error");
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth_devices, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
