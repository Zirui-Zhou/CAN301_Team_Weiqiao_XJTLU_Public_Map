package com.example.can301_2.ui.map;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.bumptech.glide.Glide;
import com.example.can301_2.MainActivity;
import com.example.can301_2.MainViewModel;
import com.example.can301_2.R;
import com.example.can301_2.adapter.CategoryItemAdapter;
import com.example.can301_2.api.ItemInfoApi;
import com.example.can301_2.api.ShopInfoApi;
import com.example.can301_2.databinding.FragmentHomeBinding;
import com.example.can301_2.databinding.FragmentMapBinding;
import com.example.can301_2.domain.ItemInfo;
import com.example.can301_2.domain.ShopInfo;
import com.example.can301_2.domain.ShopType;
import com.example.can301_2.utils.RequestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MapFragment extends Fragment implements CategoryItemAdapter.EventListener{
    private FragmentMapBinding binding;
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
    private RecyclerView recyclerViewMapShopType;
    CategoryItemAdapter mapShopTypeAdapter;
    private final String TAG = "BaiduFragment";
    private List<ShopInfo> shopInfoList;
    private PopupWindow popupWindow;

    MapViewModel mapViewModel;
    MainViewModel mainViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_map, container, false);
        }

        binding = FragmentMapBinding.inflate(inflater, container, false);

        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        mainViewModel.getShopTypeMap().observe(getViewLifecycleOwner(), shopTypeMap -> {
            mapShopTypeAdapter.setShopTypeList(new ArrayList<>(shopTypeMap.values()));
            mapShopTypeAdapter.notifyDataSetChanged();
        });

        mapView = view.findViewById(R.id.baiduMapView);
        myLocationImage = view.findViewById(R.id.my_location);
        try {
            initMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mapView.onCreate(getContext(), savedInstanceState);

        recyclerViewMapShopType = view.findViewById(R.id.map_shop_type_list);
        mapShopTypeAdapter = new CategoryItemAdapter(this);
        recyclerViewMapShopType.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewMapShopType.setAdapter(mapShopTypeAdapter);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            ShopInfoApi shopInfoService = RequestUtils.getService(ShopInfoApi.class);
            List<ShopInfo> shopInfoList = shopInfoService.getAllShopInfo().getData();

            handler.post(() -> {
                mapViewModel.setShopInfo(shopInfoList);
                addShopInfoOverlay(-1);
            });

        });

        return view;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myLocationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locClient.requestLocation();
                LatLng ll = new LatLng(lat, lon);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                baiduMap.animateMapStatus(u);
                baiduMap.setMapStatus(u);
                MapStatusUpdate u1 = MapStatusUpdateFactory.zoomTo(18f);        //缩放
                baiduMap.animateMapStatus(u1);
            }
        });
    }

    @Override
    public void updateShopInfoListByType(ShopType shopType) {
        addShopInfoOverlay(shopType == null ? -1 : shopType.getShopTypeId());
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
                LatLng ll = new LatLng(lat,lon);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                baiduMap.animateMapStatus(u);
                baiduMap.setMapStatus(u);
                MapStatusUpdate u1 = MapStatusUpdateFactory.zoomTo(18f);        //缩放
                baiduMap.animateMapStatus(u1);
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
        locClient = new LocationClient(requireContext());
        locClient.registerLocationListener(new MyLocationListener());
        baiduMap = mapView.getMap();
        baiduMap.getUiSettings().setRotateGesturesEnabled(false); //force not rotate
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

        locClient.requestLocation();

    }

    public void addShopInfoOverlay(long typeId) {
        shopInfoList = mapViewModel.getShopInfo().getValue();
        baiduMap.clear();
        LatLng latLng;
        OverlayOptions overlayOptions;

        Log.d(TAG, "addShopInfoOverlay: " + shopInfoList.size());

        for(int position = 0; position < shopInfoList.size(); position++){
            ShopInfo item = shopInfoList.get(position);
            if(typeId != -1 && item.getShopTypeId() != typeId) {
                continue;
            }

            latLng = new LatLng(item.getShopLatitude(), item.getShopLongitude());

            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromBitmap(
                    mainViewModel.getShopTypeMap()
                            .getValue()
                            .get((long) item.getShopTypeId())
                            .getShopTypeMarkerIconBitmap()
            );
            overlayOptions = new MarkerOptions().position(latLng).icon(bitmap);
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            Marker marker = (Marker) baiduMap.addOverlay(overlayOptions);
            marker.setExtraInfo(bundle);
        }
        initListener();
    }

    private void initListener() {
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                Bundle bundle = marker.getExtraInfo();
                Log.e(TAG, "onMarkerClick: " + bundle.getInt("position"));
                initPopupWindow(bundle.getInt("position"));
                return true;
            }
        });
    }

    private void initPopupWindow(int position) {
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.recycleitem_shop_card, null, false);

        popupWindow = new PopupWindow(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setOutsideTouchable(true);

        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1,
                Animation.RELATIVE_TO_PARENT, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(400);

        TextView textViewShopName = itemView.findViewById(R.id.shopName);
        TextView textViewShopSales = itemView.findViewById(R.id.shopSales);
        RatingBar ratingBarShopRating = itemView.findViewById(R.id.shopRating);
        ImageView imageViewShopCoverImage = itemView.findViewById(R.id.shopCoverImage);
        TextView textViewShopType = itemView.findViewById(R.id.recycleitem_shop_card_shop_type);
        CardView cardView = itemView.findViewById(R.id.shop_card);

        ShopInfo shopInfo = shopInfoList.get(position);

        LatLng ll = new LatLng(shopInfo.getShopLatitude(), shopInfo.getShopLongitude());
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
        baiduMap.animateMapStatus(u);
        baiduMap.setMapStatus(u);
        MapStatusUpdate u1 = MapStatusUpdateFactory.zoomTo(25f);        //缩放
        baiduMap.animateMapStatus(u1);

        textViewShopName.setText(shopInfo.getShopName());
        textViewShopSales.setText(itemView.getContext().getString(R.string.shop_sales, shopInfo.getShopSales()));
        if (!mainViewModel.getShopTypeMap().getValue().isEmpty()) {
            textViewShopType.setText(mainViewModel.getShopTypeMap().getValue().get(shopInfo.getShopTypeId()).getShopTypeName());
        }
        ratingBarShopRating.setRating(shopInfo.getShopRating().floatValue());
        Glide.with(itemView).load(RequestUtils.baseStaticUrl + shopInfo.getShopCoverImage()).into(imageViewShopCoverImage);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Bundle bundle = new Bundle();
                bundle.putLong("shop_id", shopInfo.getShopId());
                Log.e(TAG, "onClick: " + shopInfo.getShopId());
                Navigation.findNavController(view).navigate(R.id.action_navigation_home_to_navigation_detail3, bundle);
            }
        });

        MainActivity mainActivity = (MainActivity) getActivity();
        Log.e(TAG, "initPopupWindow: " + mainActivity.getBarHeight());
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, mainActivity.getBarHeight() + getNavigationBarHeight(getContext()));
        
        itemView.startAnimation(animation);
        Log.e(TAG, "initPopupWindow: animation");
    }
    
    private int getNavigationBarHeight(Context context) {
        int result = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        Log.e("TAG", "getNavigationBarHeight: " + result);
        return result;
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

}
