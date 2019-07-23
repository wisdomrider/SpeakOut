package com.org.speakout.service

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.org.speakout.MainActivity

class GetLocationClass : GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.location.LocationListener {

    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLocationRequest: LocationRequest? = null
    lateinit var myLocationInterface: MyLocationInterface;

    fun start(home: MainActivity, myInterface: MyLocationInterface ) {
        this.myLocationInterface = myInterface

        mGoogleApiClient = GoogleApiClient.Builder(home)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build()
        if (!mGoogleApiClient!!.isConnected)
            mGoogleApiClient?.connect()
    }

    override fun onConnected(bundle: Bundle?) {
        startLocationUpdate()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        initLocationRequest()
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)

    }

    private fun initLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest!!.interval = 48000
        mLocationRequest!!.fastestInterval = 42000
        mLocationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onConnectionSuspended(i: Int) {}

    override fun onConnectionFailed(connectionResult: ConnectionResult) {

    }


    override fun onLocationChanged(location: Location?) {
        if (location != null) {
            myLocationInterface.getLocation(location.latitude, location.longitude)
            mGoogleApiClient?.disconnect()
        }
    }
}
