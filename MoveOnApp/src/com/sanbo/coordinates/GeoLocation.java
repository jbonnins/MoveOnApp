/*
 * Copyright 2013 Juan José Bonnín Sansó (jbonnins@uoc.edu)
 *
 * This file is part of MoveOnApp.
 *
 *    MoveOnApp is free software: you can redistribute it and/or modify
 *    it under the terms of the Affero GNU General Public License version 3
 *    as published by the Free Software Foundation.
 *
 *    MoveOnApp is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    Affero GNU General Public License for more details
 *    (https://www.gnu.org/licenses/agpl-3.0.html).
 *    
 *    All the creations are under sanbo package, which is my short surname. 
 */
package com.sanbo.coordinates;

import com.sanbo.moveonapp.R;
import com.sanbo.utils.Config;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class GeoLocation
{
	
  @SuppressWarnings("unused")
private static final int HUNDREDFIFTY_METERS = 150;
  @SuppressWarnings("unused")
private static final int SIXTY_SECONDS = 60000;
  private static final int TWO_MINUTES = 120000;
  private static Context mContext;
  private final LocationListener listener = new LocationListener()
  {
    public void onLocationChanged(Location paramAnonymousLocation)
    {
      GeoLocation.this.updateUI(paramAnonymousLocation);
    }

    public void onProviderDisabled(String paramAnonymousString)
    {
    }

    public void onProviderEnabled(String paramAnonymousString)
    {
    }

    public void onStatusChanged(String paramAnonymousString, int paramAnonymousInt, Bundle paramAnonymousBundle)
    {
    }
  };
  
  private Handler mHandlerCallback;
  private LocationManager mLocationManager;
  private int msgSetValue;

  public GeoLocation(Context paramContext, Handler paramHandler, int paramInt)
  {
    mContext = paramContext;
    this.mHandlerCallback = paramHandler;
    this.msgSetValue = paramInt;
  }

  
  /** Determines whether one Location reading is better than the current Location fix.
   * Code taken from
   * http://developer.android.com/guide/topics/location/obtaining-user-location.html
   *
   * @param newLocation  The new Location that you want to evaluate
   * @param currentBestLocation  The current Location fix, to which you want to compare the new
   *        one
   * @return The better Location object based on recency and accuracy.
   */
  private Location getBetterLocation(Location newLocation, Location currentBestLocation) {
     if (currentBestLocation == null) {
         // A new location is always better than no location
         return newLocation;
     }

     // Check whether the new location fix is newer or older
     long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
     boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
     boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
     boolean isNewer = timeDelta > 0;

     // If it's been more than two minutes since the current location, use the new location
     // because the user has likely moved.
     if (isSignificantlyNewer) {
         return newLocation;
     // If the new location is more than two minutes older, it must be worse
     } else if (isSignificantlyOlder) {
         return currentBestLocation;
     }

     // Check whether the new location fix is more or less accurate
     int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation.getAccuracy());
     boolean isLessAccurate = accuracyDelta > 0;
     boolean isMoreAccurate = accuracyDelta < 0;
     boolean isSignificantlyLessAccurate = accuracyDelta > 200;

     // Check if the old and new location are from the same provider
     boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
             currentBestLocation.getProvider());

     // Determine location quality using a combination of timeliness and accuracy
     if (isMoreAccurate) {
         return newLocation;
     } else if (isNewer && !isLessAccurate) {
         return newLocation;
     } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
         return newLocation;
     }
     return currentBestLocation;
 }


  private void get_UserLocation()
  {
    this.mLocationManager.removeUpdates(this.listener);
    Location localLocation1 = requestUpdatesFromProvider("gps", R.string.error_location_GPS);
    Location localLocation2 = requestUpdatesFromProvider("network", R.string.error_location_NETWORK);
    if ((localLocation1 != null) && (localLocation2 != null))
      updateUI(getBetterLocation(localLocation1, localLocation2));
    while (true)
    {
      this.mLocationManager.removeUpdates(this.listener);
      if (localLocation1 != null)
        updateUI(localLocation1);
      else if (localLocation2 != null)
        updateUI(localLocation2);
      else
        updateUI(null);
    }
  }

  private boolean isSameProvider(String mString1, String mString2)
  {
    if (mString1 == null)
      return mString2 == null;
    return mString1.equals(mString2);
  }

  private Location requestUpdatesFromProvider(String mString, int mInt)
  {
    if (this.mLocationManager.isProviderEnabled(mString))
    {
      this.mLocationManager.requestLocationUpdates(mString, 100L, 60000.0F, this.listener);
      return this.mLocationManager.getLastKnownLocation(mString);
    }
    if (Config.DEBUG) Toast.makeText(mContext, mInt, Toast.LENGTH_SHORT).show();
    return null;
  }

  private void updateUI(Location mLocation)
  {
    if (mLocation != null)
    {
      Message.obtain(this.mHandlerCallback, this.msgSetValue, mLocation.getLatitude() + "," + mLocation.getLongitude()).sendToTarget();
      return;
    }
    Message.obtain(this.mHandlerCallback, this.msgSetValue, "").sendToTarget();
  }

  public void startLocation()
  {
    this.mLocationManager = ((LocationManager)mContext.getSystemService("location"));
    get_UserLocation();
  }
}
