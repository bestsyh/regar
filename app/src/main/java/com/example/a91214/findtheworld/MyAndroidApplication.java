package com.example.a91214.findtheworld;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

/**
 * Created by 91214 on 2018/11/5.
 */

public class MyAndroidApplication extends Application{
    @Override
    public  void  onCreate(){
        super.onCreate();
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

}
