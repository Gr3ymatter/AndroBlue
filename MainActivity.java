package com.example.androblue;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {

	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice mmDevice;
	private OutputStream mmOutputStream;
	private SeekBar leftSeekBar;
	private SeekBar rightSeekBar;
	private int servoSpeed;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        leftSeekBar = (SeekBar) findViewById(R.id.leftSeekBar);
        rightSeekBar = (SeekBar) findViewById(R.id.rightSeekBar);
        
        leftSeekBar.setOnSeekBarChangeListener(servoSeekBarListener);
        rightSeekBar.setOnSeekBarChangeListener(servoSeekBarListener);
        
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume(); 
		BluetoothSocket mmBluetoothSocket;
        
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBt = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBt, 0);
        }
        
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        
        if(pairedDevices.size() > 0){
        	
        	for(BluetoothDevice device : pairedDevices){
        		Log.d("Devices: ", "" +device.getName());
        		if(device.getName().equals("HC-05")){
        			Log.d("Found Device: ", "Yes");
        			mmDevice = device;
        			break;
        		}
        	}
        }
	
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        try {
			mmBluetoothSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
	        mmBluetoothSocket.connect();
	        mmOutputStream = mmBluetoothSocket.getOutputStream();
	    	
        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Error: ", "Connection Error");
		}        	
	
	}

	
	private OnSeekBarChangeListener servoSeekBarListener = new OnSeekBarChangeListener(){
		
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
			
			
			servoSpeed = seekBar.getProgress();
		
			Log.d("SeekBar: ", "" +servoSpeed);
			
			if(seekBar.getId() == leftSeekBar.getId()){
				
				try {
					mmOutputStream.write('l');
					mmOutputStream.write(servoSpeed);
				//	mmOutputStream.write(servoSpeed);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("Error: ", "Write Error");

				}
			
			}
			
			if(seekBar.getId() == rightSeekBar.getId()){
				
				try {
					mmOutputStream.write('r');
					mmOutputStream.write((char)servoSpeed);
					//mmOutputStream.write(servoSpeed);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.d("Error: ", "Write Error");

				}
			
			}
						
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub			
			seekBar.setProgress(50);
			Log.d("Error: ", "Inside onStopTracking");

				
				
			
			
		}
		
	};
    
}
