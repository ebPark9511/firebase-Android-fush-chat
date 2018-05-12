package com.example.park.ststemparkbi.Chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.park.ststemparkbi.AppManager;
import com.example.park.ststemparkbi.DBHelper;
import com.example.park.ststemparkbi.R;
import com.example.park.ststemparkbi.http.httpConn_delete;
import com.example.park.ststemparkbi.http.httpConn_msgput;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;

/**
 * Created by Park on 2016-11-25.
 */

public class Chat_Activity extends Activity implements View.OnClickListener {
    ListView m_ListView;
    public static Context Chat_Activity ;
    ImageView peofle_fre;
    custom_chat m_Adapter;
    EditText editText;
    TextView chatid,chatlocation;

    ArrayList<String> nick;
    ArrayList<String> msg;
    ArrayList<Integer> Yni;
    SharedPreferences setting;
    SharedPreferences.Editor editor;

    DBHelper dbHelper;

    @Override
    protected void onDestroy(){
        super.onDestroy();
        AppManager.getInstance().setChat_Actvity(false);
    }

    //엑티비티가 더이상 가려져서 보이지 않을때
    @Override
    protected void onStop() {
        super.onStop();
        AppManager.getInstance().setChat_Actvity(false);
        Log.e("d", "onStop() ");

    }

    //다른 엑티비티가 호출될때
    @Override
    protected void onPause() {
        super.onPause();
        AppManager.getInstance().setChat_Actvity(false);
        Log.e("d", "onPause() ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginstart_chat);
        peofle_fre=(ImageView)findViewById(R.id.peofle_fre);
        AppManager.getInstance().setChat_Actvity(true);

        dbHelper = new DBHelper(getApplicationContext(), "SystemParkbi.db", null, 1);
        Chat_Activity = this;
        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();
        m_Adapter = new custom_chat();
        editText = (EditText) findViewById(R.id.editText1);
        chatid = (TextView) findViewById(R.id.chatid);
        chatid.setText(setting.getString("NICK", ""));
        chatlocation = (TextView)findViewById(R.id.chatlocation);
        if(AppManager.getInstance().getlocation() != null) {
            chatlocation.setText(AppManager.getInstance().getlocation().getLatitude() + ", " + AppManager.getInstance().getlocation().getLongitude());
        }
        m_ListView = (ListView) findViewById(R.id.listView1);
        m_ListView.setAdapter(m_Adapter);


        dbHelper.getResult();
        int i = 0;
        if (AppManager.getInstance().getMessage().size() >= 0) {
            msg = AppManager.getInstance().getMessage();

            while(AppManager.getInstance().getMessage().size() > i) {
                m_Adapter.add(AppManager.getInstance().getMessage().get(i), AppManager.getInstance().getnickMessage().get(i), AppManager.getInstance().getmsgyniArray().get(i));
                i++;

            }
        }

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER){
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.button1).setOnClickListener(new Button.OnClickListener() {
                                                          @Override
                                                          public void onClick(View v) {
                                                              String inputValue = editText.getText().toString();
                                                              if (inputValue.equals("")) {
                                                                  Toast.makeText(Chat_Activity.this, "메세지를를 입력해주세요.", Toast.LENGTH_SHORT).show();
                                                              } else {
                                                                  //
                                                                  //   public void putMsg(Double location_X, Double location_Y, String name, String msg, String mytoken) {

                                                                  httpConn_msgput task = new httpConn_msgput();
                                                                  task.execute(String.valueOf(AppManager.getInstance().getlocation().getLatitude()), String.valueOf(AppManager.getInstance().getlocation().getLongitude()),
                                                                             setting.getString("NICK",""), inputValue, FirebaseInstanceId.getInstance().getToken());
                                                                  String msg = "";


                                                                  int j = 0;
                                                                  for(int i = 0; i< inputValue.length() ; i++){
                                                                      msg += String.valueOf(inputValue.charAt(i));
                                                                      j++;
                                                                      if(j == 15) {
                                                                          msg += "\n";
                                                                          j=0;
                                                                      }
                                                                  }

                                                                  refresh(msg,"", 1);
                                                                  editText.setText("");
                                                              }
                                                          }
                                                      }
        );

        peofle_fre.setOnClickListener(this);

    }

    public void restartLocation(){
        if(AppManager.getInstance().getlocation() != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            chatlocation.setText(AppManager.getInstance().getlocation().getLatitude() + ", " + AppManager.getInstance().getlocation().getLongitude());
                        }
                    });
                }
            }).start();
        }
    }

    public void AdapterreSet() {

        dbHelper.getResult();
        if (AppManager.getInstance().getMessage().size() >= 0) {
            nick = AppManager.getInstance().getnickMessage();
            msg = AppManager.getInstance().getMessage();
            Yni = AppManager.getInstance().getmsgyniArray();
        }

        if(msg.size() > 0 ) {
            m_Adapter.add(msg.get(msg.size() - 1) , nick.get(nick.size() - 1), Yni.get(Yni.size() - 1));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable(){
                    @Override
                    public void run() {
                        m_Adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    private void refresh(String inputValue, String nick,int _str) {
        dbHelper.insert(setting.getString("NICK", ""), inputValue, 1);
        m_Adapter.add(inputValue, nick ,_str);
        m_Adapter.notifyDataSetChanged();
    }

    public void timerefresh(){
        //여기들어왔을때부터

    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_slide_cancel_left, R.anim.anim_slide_cancel_right);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.peofle_fre :
                //누르면 현재 방에 참여하고 있는
                //사람들의 리스트
                startActivity(new Intent(this, Chat_Member_list.class));

                break;
        }
    }
}
