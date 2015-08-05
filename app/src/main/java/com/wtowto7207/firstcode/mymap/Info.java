package com.wtowto7207.firstcode.mymap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/5.
 */
public class Info implements Serializable {


    private static final long serialVersionUID = -4525278913679491045L;
    private double latitude, longitude;
    private int imgId;
    private String name;
    private String distance;
    private int zan;
    private int zanId;
    private boolean isFirstClick;

    public static List<Info> infos = new ArrayList<Info>();

    static {
        //infos.add();
        infos.add(new Info(34.242652, 108.971171, R.drawable.a01, "英伦贵族小旅馆",
                "距离209米", R.drawable.map_zan, 1456,true));
        infos.add(new Info(34.242952, 108.972171, R.drawable.a02, "沙井国际洗浴会所",
                "距离897米", R.drawable.map_zan, 456,true));
        infos.add(new Info(34.242852, 108.973171, R.drawable.a03, "五环服装城",
                "距离249米", R.drawable.map_zan, 1456,true));
        infos.add(new Info(34.242152, 108.971971, R.drawable.a04, "老米家泡馍小炒",
                "距离679米", R.drawable.map_zan, 1456,true));
    }

    public Info(double latitude, double longitude, int imgId, String name,
                String distance, int zanId, int counter,boolean isFirstClick) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgId = imgId;
        this.name = name;
        this.distance = distance;
        this.zanId = zanId;
        this.zan = counter;
        this.isFirstClick = isFirstClick;
    }


    public double getmLatitude() {
        return latitude;
    }

    public void setmLatitude(double mLatitude) {
        this.latitude = mLatitude;
    }

    public double getmLongitude() {
        return longitude;
    }

    public void setmLongitude(double mLongitude) {
        this.longitude = mLongitude;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getZanId(){ return zanId;}

    public void setZanId(int zanId){ this.zanId = zanId;}

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public boolean getFirstClick(){
        return isFirstClick;
    }

    public void setFirstClick(boolean isFirstClick){
        this.isFirstClick = isFirstClick;
    }
}



