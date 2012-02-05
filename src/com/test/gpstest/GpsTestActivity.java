package com.test.gpstest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GpsTestActivity extends Activity {
	private boolean registra;
	private boolean gps;
	private TextView edt_lat;
	private TextView edt_lon;
	private TextView edt_quo;
	private TextView edt_acc;
	private TextView edt_tempo;
	
	private Button btn1; 
	private int acquisizioni;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        edt_lat = (TextView)findViewById(R.id.edit_lat);
        edt_lon = (TextView)findViewById(R.id.edit_lon);
        edt_quo = (TextView)findViewById(R.id.edit_quota);
        edt_acc = (TextView)findViewById(R.id.edit_acc);
        edt_tempo = (TextView)findViewById(R.id.edit_tempo);
        btn1 = (Button)findViewById(R.id.button_start);
        btn1.setEnabled(false);
        registra = false;
        btn1.setOnClickListener(new Button.OnClickListener(){
        	public void onClick(View v){ 
        							change(); 
        							}
		private void change() {
			if (registra == false)
			{
				btn1.setText("Recording.... Click to stop");
				registra = true;
				acquisizioni = 0;
			}
			else
			{
				btn1.setText("Start Recording");
				registra = false;
			}
		}});

        
        /* Use the LocationManager class to obtain GPS locations */
        LocationManager mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new MyLocationListener();
        mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
    }
    public class MyLocationListener implements LocationListener
    {
    private long tempo;
	private String stringa_tempo;
	private Date expiry;


	@Override
    public void onLocationChanged(Location loc)
    {
    double latitudine = loc.getLatitude();
    double longitudine = loc.getLongitude();
    double quota = loc.getAltitude();
    double accuracy = loc.getAccuracy();

    tempo = loc.getTime();
    expiry = new Date(tempo);     
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
    stringa_tempo = formatter.format(expiry);

    
	edt_lat.setText(Double.toString(latitudine));
	edt_lon.setText(Double.toString(longitudine));
	edt_quo.setText(Double.toString(quota));
	edt_acc.setText(Double.toString(accuracy));
	edt_tempo.setText(stringa_tempo);

    btn1.setEnabled(true);

    // scrive i dati sul file
	if (registra == true)
	{
    try {
        File root = Environment.getExternalStorageDirectory();
        if (root.canWrite()){
            File gpxfile = new File(root, "dati_gps.txt");
            FileWriter gpxwriter = new FileWriter(gpxfile,true);
            BufferedWriter out = new BufferedWriter(gpxwriter);
            out.write(stringa_tempo+";"+loc.getTime()+";"+loc.getLatitude()+";"+loc.getLongitude()+";"+loc.getAltitude()+";"+loc.getAccuracy()+"\n");
            out.close();
            acquisizioni = acquisizioni + 1;
            btn1.setText("Acq : "+acquisizioni+".Click to stop");
            
        }
    } catch (IOException e) {
    }
    
	}

    }


    @Override
    public void onProviderDisabled(String provider)
    {
    gps = false;
    btn1.setEnabled(false);
    Toast.makeText( getApplicationContext(),"Gps Disabled",Toast.LENGTH_SHORT ).show();
    }


    @Override
    public void onProviderEnabled(String provider)
    {
    gps = true;
    btn1.setEnabled(true);
    Toast.makeText( getApplicationContext(),"Gps Enabled",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    }/* End of Class MyLocationListener */
    
}