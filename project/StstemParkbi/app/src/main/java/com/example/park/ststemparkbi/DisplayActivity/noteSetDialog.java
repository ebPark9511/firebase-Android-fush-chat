package com.example.park.ststemparkbi.DisplayActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.park.ststemparkbi.AppManager;
import com.example.park.ststemparkbi.R;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Park on 2016-11-18.
 */

public class noteSetDialog extends Activity implements View.OnClickListener {
    TextView location_tv;
    EditText put_title, put_not;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notesetdialog_activity);






        location_tv =(TextView)findViewById(R.id.lcoation_tv);



        put_title=(EditText)findViewById(R.id.put_title);
        put_not = (EditText)findViewById(R.id.put_not);


        findViewById(R.id.dialog_cancel).setOnClickListener(this);
        findViewById(R.id.dialog_put).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_put:
                //웹디비에 넣는다.
                if(put_title.getText().toString().equals("") || put_not.getText().toString().equals("") ) {
                    Toast.makeText(getApplicationContext(), "데이터를 모두 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "데이터넣기", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.dialog_cancel:
                finish();
                break;
        }
    }
}
