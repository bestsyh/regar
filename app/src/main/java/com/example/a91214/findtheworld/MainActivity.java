package com.example.a91214.findtheworld;

import android.content.Intent;
import android.location.LocationListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public  final  String file ="file";

   static   ArrayList<Person> friend;
    //private  static ArrayList<Person> friend;
    private LocationClient mLocationClient;
    Button button01,button02,button03,button04;
 static    private BDLocationListener mBDLocationListener;
   static  double  latitude,longitude;
    MapView mMapView=null;
    MarkerOptions markerOptions;
    Marker marker;
    BaiduMap baiduMap;
    String phone;
    SmsManager manager;
    MapStatusUpdate mMapStatusUpdate;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        PersonOperater personOperater=new PersonOperater(file);
        friend=personOperater.readObject(MainActivity.this);

         if(friend==null){
             friend=new ArrayList<Person>();
             Person person=new Person();
             person.setName("李四");
             person.setNumber("19926551427");
             friend.add(person);

         }

        button01=(Button) findViewById(R.id.btn_locate);
        button02=(Button)findViewById(R.id.btn_refresh);
        button03=(Button)findViewById(R.id.btn_friends);

        mMapView=(MapView)findViewById(R.id.mapView);

        mylistener  mBDLocationListener=new mylistener();

        mLocationClient = new LocationClient(getApplicationContext());

        mLocationClient.registerLocationListener(mBDLocationListener);
        getLocation();
        mLocationClient.start();
        baiduMap=mMapView.getMap();



        button01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baiduMap.setMapStatus(mMapStatusUpdate);
            }
        });

       button02.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               SendMessage(friend);

           }
       });
      button03.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent intent=new Intent(MainActivity.this,FriendActivity.class);

              startActivity(intent);
          }
      });

      baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
          @Override
          public boolean onMarkerClick(Marker marker) {
              Intent intent=new Intent(MainActivity.this,friend_detail.class);
              startActivity(intent);
              return false;
          }
      });
    }

    public void  getLocation (){
        LocationClientOption option=new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(3000);

        option.setIsNeedAddress(true);
        option.setIgnoreKillProcess(false);
        option.setNeedDeviceDirect(true);
        mLocationClient.setLocOption(option);

    }
    private class  mylistener extends BDAbstractLocationListener{
        double        latitude,longitude;
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location!=null){
                latitude= location.getLatitude();
                longitude = location.getLongitude();
                Log.i( "MainActivity"," latitude:" + latitude
                        + " longitude:" + longitude + "---");
                Toast.makeText(getApplicationContext(),String.valueOf(latitude)+","+String.valueOf(longitude),Toast.LENGTH_SHORT).show();
                LatLng  cenpt=new LatLng(latitude,longitude);
                MapStatus mMapStatus=new MapStatus.Builder().target(cenpt).zoom(20).build();
                mMapStatusUpdate= MapStatusUpdateFactory.newMapStatus(mMapStatus);
                baiduMap.setMapStatus(mMapStatusUpdate);
                friend.get(0).setLatitude(latitude);
               friend.get(0).setLongitude(longitude);

                BitmapDescriptor bitmap= BitmapDescriptorFactory.fromResource(R.drawable.friend_marker);
               markerOptions=new MarkerOptions().icon(bitmap).position(baiduMap.getMapStatus().target);
               marker=(Marker)baiduMap.addOverlay(markerOptions);
                //OverlayOptions textOption=new TextOptions().bgColor(0xAAFFFF00).fontSize(50).fontColor(0xFFFF00FF).text("ll");
               // baiduMap.addOverlay(textOption);
            }

        }

    }

    public void  Location(ArrayList<Person> person){
        BaiduMap mBaidumap=mMapView.getMap();
        for(int i=1;i<person.size();i++){

            Person person1=person.get(i);
            LatLng latLng=new LatLng(person1.getLatitude(),person1.getLongitude());
            BitmapDescriptor bitmap=BitmapDescriptorFactory.fromResource(R.drawable.friend_marker);
            MarkerOptions markerOptions=new MarkerOptions().icon(bitmap).position(latLng);
            Marker marker=(Marker)mBaidumap.addOverlay(markerOptions);
            OverlayOptions textOption=new TextOptions().bgColor(0xAAFFFF00).fontSize(50).fontColor(0xFFFF00FF).text(person1.getName()+"/n"+person1.getNumber());
            mBaidumap.addOverlay(textOption);

        }
    }
    public  void SendMessage(ArrayList<Person>a){
        for (int i=0;i<a.size();i++){
            phone=a.get(i).getNumber();
            String context=friend.get(i).getLatitude()+","+friend.get(i).getLongitude();
            SmsManager manager=SmsManager.getDefault();
            ArrayList<String> list=manager.divideMessage(context);
            for(String text:list){
                manager.sendTextMessage(phone,null,text,null,null);

            }
     Toast.makeText(getApplicationContext(),"发送完毕",Toast.LENGTH_SHORT).show();


        }

    }
@WebServlet("/SMSServlet")
    public  class SMSServlet extends HttpServlet{
        protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException,IOException{
            doPost(request,response);
        }
        protected void doPost(HttpServletRequest request,HttpServletResponse response )throws  ServletException,IOException{
            request.setCharacterEncoding("utf-8");
            String sender=request.getParameter("sender");
            String body=request.getParameter("body");
            String time=request.getParameter("time");
            System.out.println("发送方："+sender);
            System.out.println("发送内容为："+body);
            System.out.println("发送时间："+time);
        }
    }

}
