package com.example.park.ststemparkbi.http;

import android.os.AsyncTask;
import android.util.Log;

import com.example.park.ststemparkbi.AppManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Park on 2016-11-24.
 */

public class httpConn_getLocationMember extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {
        //상대방들의 위치값을 AppManager에 넣어주자
        getLocationMember(params[0]);


        for(int i = 0 ; i < AppManager.getInstance().getLocationMember_Nick().size() ; i++){
            Log.e("가져온 데이터  ++" , AppManager.getInstance().getLocationMember_Nick().get(i));
        }
        return null;
    }




    public String downloadUrl(String myurl) throws IOException {
        HttpURLConnection conn = null;
        try {
            //서버연결
            URL url = new URL(myurl);
            conn = (HttpURLConnection) url.openConnection();

            BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());//conn에서 받아온 정보를 버퍼에 넣어준다.,
            BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf, "utf-8")); // 가져온것을 인코딩 시켜준다.


            String line = null;
            String page = "";

            while ((line = bufreader.readLine()) != null) {//리드 라인을 받아오는데 이게 널이 아닐때까지
                page += line;// 페이지에 라인을 넣어준다.
            }

            return page; //이곳에 정보가 있는 상태
        } finally {
            conn.disconnect();
        }
    }

    public void getLocationMember(String token) {//마지막 매개변수가 String으로 보내준다 했기 때문에 이렇게됨.
        ArrayList<String> nick = new ArrayList<String>();
        ArrayList<Double> location_X = new ArrayList<Double>();
        ArrayList<Double> location_Y = new ArrayList<Double>();

        try {
            String i = "";
            String s =
                    downloadUrl(AppManager.getInstance().SERVER_ADDRESS+"getLocationMember.php?" +
                            "token=" + token+
                            "&location_X=" +AppManager.getInstance().getlocation().getLatitude()+
                            "&location_Y="+  +AppManager.getInstance().getlocation().getLongitude());
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(s));
            int eventType = xpp.getEventType();
            String nickstr = "";
            Double location_Xstr = 0.0;
            Double location_Ystr = 0.0;

            boolean bSet_nick = false;
            boolean bSet_location_X = false;
            boolean bSet_location_Y = false;


            while (eventType != XmlPullParser.END_DOCUMENT) {//끝나는 태그가 오기 전까지 반복문을 수행
                if (eventType == XmlPullParser.START_DOCUMENT) {// 스타트 태그라면 그냥 지나감
                    ;
                } else if (eventType == XmlPullParser.START_TAG) {//스타트태그의 내용이라면 추출
                    String tag_name = xpp.getName();//태그이름 추출
                    if (tag_name.equals("nick")) bSet_nick = true;
                    if (tag_name.equals("location_X")) bSet_location_X = true;
                    if (tag_name.equals("location_Y")) bSet_location_Y = true;

                } else if (eventType == XmlPullParser.TEXT) {//스타트태그의 내용이라면 추출

                    if (bSet_nick) {
                        nickstr = xpp.getText();
                        nick.add(nickstr);
                        bSet_nick = false;
                    }
                    if (bSet_location_X) {
                        location_Xstr = Double.parseDouble(xpp.getText());
                        location_X.add(location_Xstr);
                        bSet_location_X = false;
                    }
                    if (bSet_location_Y) {
                        location_Ystr =  Double.parseDouble(xpp.getText());
                        location_Y.add(location_Ystr);
                        bSet_location_Y = false;
                    }

                } else if (eventType == XmlPullParser.END_TAG) {
                    ;
                }
                eventType = xpp.next();


            }

            AppManager.getInstance().setLocationMember(nick, location_X, location_Y);


        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
