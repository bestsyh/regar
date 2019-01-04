package com.example.a91214.findtheworld;

import java.io.Serializable;

/**
 * Created by 91214 on 2018/12/19.
 */

public class Person implements Serializable{
    private  String name;
    private  String number;
    private double latitude;
    private double longitude;
    public  void  setName(String name){
        this.name=name;
    }
    public  void setNumber(String number){
        this.number=number;
    }
    public  String getName(){
        return  this.name;
    }
public  String getNumber(){
        return  this.number;
}
public  void  setLatitude(double latitude){
    this.latitude=latitude;
}
public  void  setLongitude(double longitude){
    this.longitude=longitude;
}
public double getLatitude(){
    return  latitude;
}
public double getLongitude(){
    return  longitude;
}
}
