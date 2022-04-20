package com.diettracker.core.location;
import android.location.Location;

public interface GPSCallback {
    void onGPSUpdate(Location location);
}

