package com.example.can301_2;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;


public class BaiduMapFragment extends Fragment {

    private View view;
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationClient locClient = null;
    private final String TAG = "BaiduFragment";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_baidu_map, container, false);
        }

        mapView = view.findViewById(R.id.baiduMapView);
        try {
            initMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.onCreate(getContext(), savedInstanceState);
        Log.e(TAG, "onCreateView: ");
        return view;
    }
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mapView == null){
                return;
            }
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
            baiduMap.setMyLocationConfiguration(configuration);
            baiduMap.animateMapStatus(update);
            baiduMap.setMyLocationData(locData);
        }
    }

    private void initMap() throws Exception {
        LocationClient.setAgreePrivacy(true);
        locClient = new LocationClient(getActivity().getApplicationContext());
        locClient.registerLocationListener(new MyLocationListener());
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd0911");
        option.setScanSpan(1000);
        locClient.setLocOption(option);
        MyLocationListener myLocationListener = new MyLocationListener();
        locClient.registerLocationListener(myLocationListener);
        locClient.start();
        Log.e(TAG, "initMap: ");
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.e(TAG, "onAttach: ");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView = view.findViewById(R.id.baiduMapView);
        mapView.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        Log.e(TAG, "onPause: ");
    }

    @Override
    public void onDestroy() {
        locClient.stop();
        baiduMap.setMyLocationEnabled(false);
        super.onDestroy();
        mapView.onDestroy();
        Log.e(TAG, "onDestroy: ");
    }
}