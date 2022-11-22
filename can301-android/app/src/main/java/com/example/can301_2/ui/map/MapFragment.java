package com.example.can301_2.ui.map;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.can301_2.R;
import com.example.can301_2.api.ShopInfoApi;
import com.example.can301_2.domain.ShopInfo;
import com.example.can301_2.ui.home.HomeViewModel;
import com.example.can301_2.utils.RequestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MapFragment extends Fragment {

    private View view;
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationClient locClient = null;
    //防止每次定位都重新设置中心点
    private boolean isFirstLoc = true;
    //注册LocationListener监听器
    MyLocationListener myLocationListener = new MyLocationListener();
    //经纬度
    private double lat;
    private double lon;

    private ImageView myLocationImage;//定位图标
    private final String TAG = "BaiduFragment";

    MapViewModel mapViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_map, container, false);
        }
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mapView = view.findViewById(R.id.baiduMapView);
        myLocationImage = view.findViewById(R.id.my_location);
        try {
            initMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.onCreate(getContext(), savedInstanceState);
        addShopInfoOverlay();
        Log.e(TAG, "onCreateView: ");
        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myLocationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locClient.requestLocation();
                LatLng ll =new LatLng(lat,lon);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                baiduMap.animateMapStatus(u);
                baiduMap.setMapStatus(u);
                MapStatusUpdate u1 = MapStatusUpdateFactory.zoomTo(18f);        //缩放
                baiduMap.animateMapStatus(u1);
            }
        });
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || mapView == null){
                return;
            }
            lat = location.getLatitude();
            lon = location.getLongitude();

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
            baiduMap.setMyLocationConfiguration(configuration);
            // baiduMap.animateMapStatus(update);
            baiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                // MapStatus.Builder builder = new MapStatus.Builder();
                //builder.target(latLng).zoom(18.0f);
                // baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                baiduMap.setMyLocationData(locData);
            }
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
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        locClient.setLocOption(option);
        locClient.registerLocationListener(myLocationListener);
        locClient.start();
        Log.e(TAG, "initMap: ");
    }

    public void addShopInfoOverlay() {
        List<ShopInfo> shopInfoList = new ArrayList<>();
        baiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);

        MyAsyncTasks myAsyncTasks = new MyAsyncTasks();
        try {
            shopInfoList = myAsyncTasks.execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "addShopInfoOverlay: " + shopInfoList.size());

        for(ShopInfo item : shopInfoList){
            Log.d(TAG, "addShopInfoOverlay: " + item.getShopLatitude());
            latLng = new LatLng(item.getShopLatitude(), item.getShopLongitude());
            overlayOptions = new MarkerOptions().position(latLng).icon(bitmap);
            Log.e("MapFragment", "add Marker: " + item.getShopId());
            Bundle bundle = new Bundle();
            bundle.putLong("shop_id", item.getShopId());
            Marker marker = (Marker) baiduMap.addOverlay(overlayOptions);
            marker.setExtraInfo(bundle);
        }
        initListener();
    }

    private void initListener() {
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle = marker.getExtraInfo();
                Log.e("MapFragment", "onClick: " + bundle.getString("shop_id"));
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_detail3, bundle);
                return true;
            }
        });
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
        mapView.setVisibility(View.VISIBLE);
        mapView = view.findViewById(R.id.baiduMapView);
        isFirstLoc = true;
        mapView.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.setVisibility(View.VISIBLE);
        //在暂定时销毁定位，防止切换到其他Fragment再切回来时出现黑屏现象
        locClient.unRegisterLocationListener(myLocationListener);
        locClient.stop();
        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
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

    public class MyAsyncTasks extends AsyncTask<Void, Void, List<ShopInfo>> {

        @Override
        protected List<ShopInfo> doInBackground(Void... params) {
            ShopInfoApi shopInfoService = RequestUtils.getService(ShopInfoApi.class);
            return shopInfoService.getAllShopInfo().getData();
        }

        @Override
        protected void onPostExecute(List<ShopInfo> shopInfo) {
            mapViewModel.setShopInfo(shopInfo);
        }
    }
}