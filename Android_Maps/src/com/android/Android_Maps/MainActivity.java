package com.android.Android_Maps;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MainActivity extends MapActivity {
	
	  private static final int MIN_TIME_BETWEEN_GPS_UPDATES = 0;
	  private static final int MIN_DIST_BETWEEN_GPS_UPDATES = 0;

	  private ServerConnection conn = new ServerConnection("http://ase-group3.appspot.com/gpsupdate");
	  private LocationManager lm;
	  private LocationListener ll;
	  private boolean have_data;
	  
      private MapController mapController;
      private GeoPoint mDefPoint;
    
    private MapView mapView;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapView = (MapView) findViewById(R.id.map_view);       
        mapView.setBuiltInZoomControls(true);
        
        //List<Overlay> mapOverlays = mapView.getOverlays();
        //Drawable drawable = this.getResources().getDrawable(R.drawable.ic_notification_overlay);
        //MapItemizedOverlay itemizedOverlay = new MapItemizedOverlay(drawable, this);
        
        have_data = false;        
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        ll = new CustomLocationListener();

        registerForUpdates();        
    }
    
    private void registerForUpdates() {
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
          return;
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
            MIN_TIME_BETWEEN_GPS_UPDATES, 
            MIN_DIST_BETWEEN_GPS_UPDATES, 
            ll);
      }
    
    private void stopUpdates() {
        if (lm != null && ll != null)
          lm.removeUpdates(ll);
          have_data = false;
      }
    
    
    public void addGPSLayer()  
    {  
        List<Overlay> mapOverlays = mapView.getOverlays();  
        Drawable drawable = this.getResources().getDrawable(R.drawable.ic_notification_overlay);  
        MapItemizedOverlay itemizedoverlay = new MapItemizedOverlay(drawable,this);  
         
        OverlayItem GPSoverlayitem = new OverlayItem(mDefPoint, "Hello!", "I'm here!");  
        itemizedoverlay.addOverlay(GPSoverlayitem);  
        
        mapOverlays.clear();   //清除先前的图层，不然会叠加很多位置信息，当然如果想记录行程路线的话就不需要clear了  
        //addSiteLayer();           //重新加入自定义的基站图层  
        mapOverlays.add(itemizedoverlay);   
    }
    
    
    
    private class CustomLocationListener implements LocationListener {

		public void onLocationChanged(Location arg0) {
		    
			if (arg0 != null) {
				have_data = true;
		        mDefPoint = new GeoPoint((int)(arg0.getLatitude()* 1E6),(int)(arg0.getLongitude()* 1E6));
		        mapView = (MapView) findViewById(R.id.map_view);
		        mapView.setBuiltInZoomControls(true);// 可以被用户进行缩放
                mapController = mapView.getController();// 地图大小控制
		        mapController.setZoom(6);		    
			    mapController.animateTo(mDefPoint);    //跳转至GPS位置  
                mapController.setCenter(mDefPoint);                     
            addGPSLayer();
			}
		}

		public void onProviderDisabled(String provider) {
			stopUpdates();
			
		}

		public void onProviderEnabled(String provider) {
			registerForUpdates();
			
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
}