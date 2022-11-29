package com.example.can301_2;

import static androidx.navigation.ui.NavigationUI.onNavDestinationSelected;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.can301_2.api.ShopInfoApi;
import com.example.can301_2.databinding.ActivityMainBinding;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.can301_2.domain.ShopInfo;
import com.example.can301_2.domain.ShopType;
import com.example.can301_2.utils.RequestUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity{

    private final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private BottomNavigationView navView;

    public MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //避免切换加载地图黑屏
        //getWindow().setFormat(PixelFormat.TRANSLUCENT);
        super.onCreate(savedInstanceState);
        // Check permission
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
        else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 2);
        }
        else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, 3);
        }
        else {
            initSDK(true);
        }


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_map, R.id.navigation_user)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
        navView.setOnItemSelectedListener(item -> {
            Log.d(TAG, String.valueOf(item));
            // Note that here can only go back one step in navigation.
            navController.navigateUp();
//            onNavDestinationSelected(item, navController);

            NavOptions.Builder builder = new NavOptions.Builder()
                    .setLaunchSingleTop(false)
                    .setEnterAnim(R.anim.in_from_right)
                    .setExitAnim(R.anim.out_to_left)
                    .setPopEnterAnim(R.anim.in_from_right)
                    .setPopExitAnim(R.anim.out_to_left);
            NavigationUIHelper navigationUIHelper = new NavigationUIHelper();
            return navigationUIHelper.onNavDestinationSelected(item, navController, builder);
        });

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            ShopInfoApi shopInfoService = RequestUtils.getService(ShopInfoApi.class);
            List<ShopType> shopTypeList = shopInfoService.getAllShopType().getData();
            for(ShopType shopType : shopTypeList) {
                try {
                    shopType.setShopTypeMarkerIconBitmap(
                            Glide.with(this)
                                    .asBitmap()
                                    .load(RequestUtils.baseStaticUrl + shopType.getShopTypeMarkerIcon())
                                    .submit()
                                    .get()
                    );
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Map<Long, ShopType> shopTypeMap = shopTypeList.stream().collect(Collectors.toMap(ShopType::getShopTypeId, item -> item));
            handler.post(() -> {
                mainViewModel.setShopTypeMap(shopTypeMap);
            });
        });
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    private void initSDK(boolean status) {
        SDKInitializer.setAgreePrivacy(getApplicationContext(), status);
        try {
            SDKInitializer.initialize(getApplicationContext());
            SDKInitializer.setCoordType(CoordType.BD09LL);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.nav_host_fragment_activity_main).navigateUp();
    }
    
    public int getBarHeight() {
        return navView.getMeasuredHeight();
    }

    // Reference: https://stackoverflow.com/questions/53976785/android-navigation-component-pop-to-transition-issue
    public static class NavigationUIHelper {
        public boolean onNavDestinationSelected(@NonNull MenuItem item,
                                                @NonNull NavController navController,
                                                @NonNull NavOptions.Builder builder) {
            if ((item.getOrder() & Menu.CATEGORY_SECONDARY) == 0) {
                NavDestination destination = findStartDestination(navController.getGraph());
                builder.setPopUpTo(destination.getId(), false);
            }
            NavOptions options = builder.build();
            try {
                navController.navigate(item.getItemId(), null, options);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        // Need to copy this private method as well
        private NavDestination findStartDestination(@NonNull NavGraph graph) {
            NavDestination startDestination = graph;
            while (startDestination instanceof NavGraph) {
                NavGraph parent = (NavGraph) startDestination;
                startDestination = parent.findNode(parent.getStartDestination());
            }
            return startDestination;
        }
    }
}
