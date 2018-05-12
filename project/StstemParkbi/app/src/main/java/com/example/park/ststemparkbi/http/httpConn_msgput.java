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
 * Created by Park on 2016-11-30.
 */

public class httpConn_msgput extends AsyncTask<String, Integer, String> {
    @Override
    protected String doInBackground(String... params) {
        Log.e("asdf", "d " + params[0] + "  " + params[1]);
        //토큰값으로
        putMsg(Double.parseDouble(params[0]), Double.parseDouble(params[1]),
                params[2], params[3], params[4]);


        return null;
    }

    public void putMsg(Double location_X, Double location_Y, String name, String msg, String mytoken) {
        try {
            URL url = new URL(AppManager.getInstance().SERVER_ADDRESS + "send_msg.php?"
                    + "location_X=" + location_X
                    + "&location_Y=" + location_Y
                    + "&nick=" + URLEncoder.encode(name, "UTF-8")
                    + "&msg=" + URLEncoder.encode(msg, "UTF-8")
                    + "&token=" + URLEncoder.encode(mytoken, "UTF-8")//->내토큰은 제외
            );

            url.openStream(); //서버의 DB에 입력하기 위해 웹서버의 insert.php파일에 입력된 이름과 가격을 넘김


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
