package com.example.park.ststemparkbi.Chat;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.park.ststemparkbi.AppManager;
import com.example.park.ststemparkbi.R;

import java.util.ArrayList;

/**
 * Created by Park on 2016-12-02.
 */

public class Chat_Member_list  extends Activity{

    ArrayAdapter<String> adapter;
    ArrayList<String> listadapter;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                    WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.loginsart_chalist);

            listadapter = new ArrayList<String>();
            listadapter = AppManager.getInstance().getLocationMember_Nick();
            adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listadapter);
            ListView setplans_list = (ListView)findViewById(R.id.setplans_list);
            setplans_list.setAdapter(adapter);
        }
}