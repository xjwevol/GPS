package android.GPSJiweiXu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
//import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GPSJiweiXuActivity extends Activity implements OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button b1 = (Button) findViewById(R.id.button1);
		 b1.setOnClickListener(this);
    }

	public void onClick(View v) {
		openGPSSettings();
		getLocation();
		
	}
	
	//Check the GPS function
	private void openGPSSettings() {
        LocationManager alm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        
        if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "GPS function is OK", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Please Open GPS！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
        startActivityForResult(intent,0); 

    }
		
	
	 private void getLocation()
	    {
	        // Manage location server
	        LocationManager locationManager;	     
	        String serviceName = Context.LOCATION_SERVICE;
	        locationManager = (LocationManager) this.getSystemService(serviceName);
	        LocationListener locationListener = null;
	        	        
	        // Criteria setting
	        Criteria criteria = new Criteria();
	        criteria.setAccuracy(Criteria.ACCURACY_FINE); 
	        criteria.setAltitudeRequired(false);
	        criteria.setBearingRequired(false);
	        criteria.setCostAllowed(true);
	        criteria.setPowerRequirement(Criteria.POWER_LOW);
	        
	        
	        String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
	        Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
	        updateToNewLocation(location);
	        // Set Listener   设置监听器，自动更新的最小时间为间隔N秒(1秒为1*1000，这样写主要为了方便)或最小位移变化超过N米
	        locationManager.requestLocationUpdates(provider, 100 * 1000, 500, locationListener);
	    }
	 
	 private void updateToNewLocation(Location location) {

	        TextView tv1;
	        tv1 = (TextView) this.findViewById(R.id.tv1);
	        if (location != null) {
	            double  latitude = location.getLatitude();
	            double  longitude= location.getLongitude();
	            tv1.setText(latitude+ "  " + longitude);
	        } else {
	            tv1.setText("Fail to get location infomation");
	        }

	    }
	 
}
	



