package com.example.park.ststemparkbi.http;

import android.os.AsyncTask;
import android.util.Log;

import com.example.park.ststemparkbi.AppManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Park on 2016-11-23.
 */

public class httpConn_delete extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {
        if(tokenChk(params[0])) {
            //토큰값이 있다면 지워주자,
            deleteToken(params[0]);
        }
        return null;
    }

    private void deleteToken(String param) {
            Log.e("토큰값 지우기","이");
        try {
            URL url = new URL(AppManager.getInstance().SERVER_ADDRESS + "deleteToken.php?"
                    + "token=" + URLEncoder.encode(param, "UTF-8"));


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
        String chk = getXmlData("chk", "HOST_NAME_HERE/token_check.php?token=" + token);

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
