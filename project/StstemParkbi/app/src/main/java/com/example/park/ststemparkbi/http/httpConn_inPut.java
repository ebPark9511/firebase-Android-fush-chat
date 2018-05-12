package com.example.park.ststemparkbi.http;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.park.ststemparkbi.AppManager;
import com.google.firebase.iid.FirebaseInstanceId;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Park on 2016-11-23.
 */

public class httpConn_inPut extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {
        //토큰값으로

        Log.e("두인백 ", params[1]);
        //이 동일한 토큰값이 있냐 체크 :
        if(tokenChk(params[0])) {
            //업데이트문
            Log.e("업데이트 ", "문");
            myTokenlocationUpdate(params[0]);
        }else{
            Log.e("인설트 ", "문");

            //인설트문
            myTokenlocationInsert(params[0], params[1]);
        }
        //있으면 -> 업데이트
        //없다면 -> 인설트



        return null;
    }

    private void myTokenlocationInsert(String param, String nick) {

        try {

            URL url = new URL(AppManager.getInstance().SERVER_ADDRESS + "input_myProfile.php?"
                    + "token=" + FirebaseInstanceId.getInstance().getToken()
                    + "&nick=" + URLEncoder.encode(nick, "UTF-8")
                    + "&location_X=" + AppManager.getInstance().getlocation().getLatitude()
                    + "&location_Y=" + AppManager.getInstance().getlocation().getLongitude());



            url.openStream(); //서버의 DB에 입력하기 위해 웹서버의 insert.php파일에 입력된 이름과 가격을 넘김


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void myTokenlocationUpdate(String param) {

        try {
            URL url = new URL(AppManager.getInstance().SERVER_ADDRESS + "location_Update.php?"
                    + "token=" + URLEncoder.encode(param, "UTF-8")
                    + "&location_X=" + AppManager.getInstance().getlocation().getLatitude()
                    + "&location_Y=" + AppManager.getInstance().getlocation().getLongitude());



            url.openStream(); //서버의 DB에 입력하기 위해 웹서버의 insert.php파일에 입력된 이름과 가격을 넘김


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean tokenChk(String token) {
        String chk = getXmlData("chk", "http://서버주소/token_check.php?token=" + token);

        Log.e("체크" , chk);
        if(chk.equals("User Found")){
            //업데이트문
            return true;
        }else{
            //인설트문
            return false;
        }
    }


    public String getXmlData(String str, String url) { //태그값 하나를 받아오기위한 String형 함수

        String rss = url;
        String ret = "";

        try { //XML 파싱을 위한 과정
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            URL server = new URL(rss);
            InputStream is = server.openStream();
            xpp.setInput(is, "UTF-8");

            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equals(str)) { //태그 이름이 str 인자값과 같은 경우
                        ret = xpp.nextText();//.nextText();
                    }
                }

                eventType = xpp.next();
            }
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }

        return ret;
    }
}
