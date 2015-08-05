package com.wtowto7207.firstcode.mymap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

public class MainActivity extends Activity {

    //基本参数
    private BaiduMap mBaiduMap;
    private MapView mapView;

    //定位需要
    private LocationClient mClient;
    private boolean isFirstIn = true;
    private double lantitud, longitud;
    private Context context;

    //方向传感器指向
    private BitmapDescriptor mIconLocation;
    private MyOrientationListener myOrientationListener;
    private float mCurrentX;

    //模式切换
    private MyLocationConfiguration.LocationMode mLocationMode;

    //覆盖物相关
    private BitmapDescriptor mMarker;
    private RelativeLayout mMarkerly;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        this.context = this;

        initView();

        initLocation();

        initDirection();

        initOverlay();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle bundle = marker.getExtraInfo();
                Info info = (Info) bundle.getSerializable("info");

                ImageView img = (ImageView) mMarkerly.findViewById(R.id.id_info_img);
                TextView name = (TextView) mMarkerly.findViewById(R.id.id_info_name);
                TextView distance = (TextView) mMarkerly.findViewById(R.id.id_info_distance);
                TextView zan = (TextView) mMarkerly.findViewById(R.id.id_info_zan);

                img.setImageResource(info.getImgId());
                name.setText(info.getName());
                distance.setText(info.getDistance());
                zan.setText(info.getZan() + "");

                InfoWindow infoWindow;
                TextView tv = new TextView(context);
                tv.setText(info.getName());
                tv.setTextColor(Color.parseColor("#ffffff"));
                tv.setBackgroundResource(R.drawable.location_tips);
                tv.setPadding(40,20,40,0);



                final LatLng latLng = marker.getPosition();
                final int offY = -100;

                infoWindow = new InfoWindow(tv,latLng,offY);

                mBaiduMap.showInfoWindow(infoWindow);

                mMarkerly.setVisibility(View.VISIBLE);
                return true;
            }
        });

        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMarkerly.setVisibility(View.GONE);
                mBaiduMap.hideInfoWindow();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }



    private void initOverlay() {
        mMarker = new BitmapDescriptorFactory().fromResource(R.drawable.maker);
        mMarkerly = (RelativeLayout) findViewById(R.id.id_marker_ly);
    }

    private void initDirection() {
        mIconLocation = BitmapDescriptorFactory.fromResource(R.drawable.ic_diraction);

        myOrientationListener = new MyOrientationListener(context);
        myOrientationListener.setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
            @Override
            public void onOrientationChanged(float x) {
                mCurrentX = x;
            }
        });


    }

    private void initLocation() {
        mClient = new LocationClient(this);
        MyLocationListener myLocationListener = new MyLocationListener();
        mClient.registerLocationListener(myLocationListener);

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        option.setScanSpan(1000);
        option.setOpenGps(true);

        mClient.setLocOption(option);

        mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;

    }

    private void initView() {
        mapView = (MapView) findViewById(R.id.map_view);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setBaiduHeatMapEnabled(false);
        mBaiduMap.setTrafficEnabled(false);

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
        mBaiduMap.animateMapStatus(msu);

    }


    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        if (!mClient.isStarted())
            mClient.start();
        //开启方向传感器
        myOrientationListener.start();


    }

    @Override
    protected void onStop() {
        super.onStop();
        mBaiduMap.setMyLocationEnabled(false);
        mClient.stop();
        //停止方向传感器
        myOrientationListener.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.map_common:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.map_satellite:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.map_traffic:
                if (!mBaiduMap.isTrafficEnabled()) {
                    mBaiduMap.setTrafficEnabled(true);
                    item.setTitle("实时交通地图on");
                } else {
                    mBaiduMap.setTrafficEnabled(false);
                    item.setTitle("实时交通地图off");
                }
                break;
            case R.id.map_heat:
                if (!mBaiduMap.isBaiduHeatMapEnabled()) {
                    mBaiduMap.setBaiduHeatMapEnabled(true);
                    item.setTitle("城市热力图on");
                } else {
                    mBaiduMap.setBaiduHeatMapEnabled(false);
                    item.setTitle("城市热力图off");
                }
                break;
            case R.id.map_location:
                LatLng latLng = new LatLng(lantitud, longitud);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                break;
            case R.id.map_mode_normal:
                mLocationMode = MyLocationConfiguration.LocationMode.NORMAL;
                break;
            case R.id.map_mode_follow:
                mLocationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                break;
            case R.id.map_mode_compass:
                mLocationMode = MyLocationConfiguration.LocationMode.COMPASS;
                break;
            case R.id.map_overlay:
                addOverlays(Info.infos);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //添加覆盖物
    private void addOverlays(List<Info> infos) {
        mBaiduMap.clear();
        LatLng latLng = null;
        Marker marker = null;
        OverlayOptions options;
        for (Info info : infos) {
            //经纬度
            latLng = new LatLng(info.getmLatitude(), info.getmLongitude());
            //图标
            options = new MarkerOptions().position(latLng).icon(mMarker).zIndex(5);
            marker = (Marker) mBaiduMap.addOverlay(options);
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }

        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);
    }


    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(location.getRadius())//
                    .direction(mCurrentX)//
                    .latitude(location.getLatitude())//
                    .longitude(location.getLongitude())//
                    .build();

            mBaiduMap.setMyLocationData(data);

            //设置指向图标
            MyLocationConfiguration config = new MyLocationConfiguration(mLocationMode, true, mIconLocation);
            mBaiduMap.setMyLocationConfigeration(config);

            lantitud = location.getLatitude();
            longitud = location.getLongitude();
            if (isFirstIn) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                isFirstIn = false;


            }


        }
    }
}
