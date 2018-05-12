package com.example.park.ststemparkbi.DisplayActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.park.ststemparkbi.AppManager;
import com.example.park.ststemparkbi.Chat.Chat_Activity;
import com.example.park.ststemparkbi.R;
import com.example.park.ststemparkbi.http.httpConn_delete;
import com.example.park.ststemparkbi.http.httpConn_getLocationMember;
import com.example.park.ststemparkbi.http.httpConn_inPut;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ssomai.android.scalablelayout.ScalableLayout;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.value;

public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapLongClickListener, OnMapReadyCallback, View.OnClickListener , AdapterView.OnItemClickListener{


    ImageView open_btn;
    TextView open_view_tv,open_tv;
    private GoogleMap mMap;
    ScalableLayout open_view;
    Animation translateBottomAnim; //왼쪽오른쪽 왔다갓다하는것을 넣기위한 애니메이션 변수
    Animation translateTopAnim;

    ListView listview;
    String[] dataKeyList = {"title", "locationXnY"};
    int[] viewIdList = {R.id.name_text, R.id.department_text};
    ArrayList<List_Member> mData = null;
    ImageView mylocation;
    TextView mylocation_tv;
    int locationnow ;
    TextView nick_tv;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    BitmapDescriptor bitmapDescriptor;
    HashMap<String, String> mapData;
    SimpleAdapter adapter;
    BaseAdapterex                       mAdapter = null;
    ImageView chat_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        translateBottomAnim = AnimationUtils.loadAnimation(this, R.anim.translate_bottom);
        translateTopAnim = AnimationUtils.loadAnimation(this, R.anim.translate_top);
        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();
        chat_img = (ImageView)findViewById(R.id.chat_img);
        bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(
                BitmapDescriptorFactory.HUE_AZURE);

        nick_tv=(TextView)findViewById(R.id.nick_tv);
        nick_tv.setText(setting.getString("NICK",""));
        mylocation = (ImageView) findViewById(R.id.mylocation);
        mylocation.setOnClickListener(this);
        listview = (ListView) findViewById(R.id.listview1);
        mylocation_tv = (TextView
                ) findViewById(R.id.mylocation_tv);
        mylocation_tv.setText("나의위치");
        locationnow = 0;
         mapData = new HashMap<String, String>();



        open_btn = (ImageView) findViewById(R.id.open_btn);
        open_view = (ScalableLayout) findViewById(R.id.open_view);
        open_view_tv = (TextView) findViewById(R.id.open_view_tv);
        open_tv = (TextView)findViewById(R.id.open_tv);
        open_view_tv.setText("오백원 : " + AppManager.getInstance().getLocationMember_location_Y().size());
        open_tv.setText("오백원 : " + AppManager.getInstance().getLocationMember_location_Y().size());


        open_view_tv.setOnClickListener(this);
        open_btn.setOnClickListener(this);
        mylocation.setOnClickListener(this);
        listview.setOnItemClickListener(this);
        chat_img.setOnClickListener(this);

        mData = new ArrayList<List_Member>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume"," 호출");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy"," 호출");
        //위치갱신stop하기
        locationnow =0;
        mylocation_tv.setText("onDestroy");
        startLocationService();


        httpConn_delete task = new httpConn_delete();
        task.execute(FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_btn:
                //애니메이션을 이용한 카드뷰
                open_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        open_view.setVisibility(View.VISIBLE);
                        open_view.startAnimation(translateTopAnim);
                        Handler mHandler = new Handler();
                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                open_btn.setVisibility(View.INVISIBLE);
                            }
                        }, 200);

                    }


                });

                break;

            case R.id.open_view_tv:
                open_view.startAnimation(translateBottomAnim);
                open_view.setVisibility(View.INVISIBLE);
                Handler mHandler = new Handler();
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        open_btn.setVisibility(View.VISIBLE);
                    }
                }, 350);
                break;

            case R.id.mylocation:
                if (mylocation_tv.getText().equals("나의위치")) {
                    if (chkGpsService()) {
                        //트루이면 내위치를 받아오자
                        locationnow = 1;
                        startLocationService();
                        mylocation_tv.setText("ON");

                    }
                } else {
                    //위치갱신stop하기
                    locationnow =0;
                    mylocation_tv.setText("나의위치");
                    startLocationService();
                }

                break;

            case R.id.chat_img:

                //위도경도가 null이라면 다이얼로그
                if(locationNullCeck()){
                    startActivity(new Intent(MapsActivity.this, Chat_Activity.class));
                    overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_left);
                }
                break;
        }

    }

    private boolean locationNullCeck() {
        if(AppManager.getInstance().getlocation() == null){
            //널이면 다이얼로그,
            AlertDialog.Builder aDialog = new AlertDialog.Builder(MapsActivity.this);
            aDialog.setMessage("내 위치정보가 필요해요T.T \n 내 위치를 눌러주세요.\n 누르셨다면 조금만 기다려주세요!"); // 보여지는 글 내용 세팅
            aDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() { // 확인 버튼 클릭시, 여기서 선택한 값을 메인 Activity 로 넘길 수 있다.
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }
            );
            AlertDialog ad = aDialog.create();  // 위 내용의 팝업창 생성

            ad.show();  //팝업창(액티비티)을 보여준다.
            return false;

        }else {
            //널이아니면
            return true;
        }
    }

    LocationManager manager;
    Location lastLocation;
    private void startLocationService() {
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        long minTime = 1000;
        float minDistance = 0;

        try {
            if (locationnow == 1) {
                lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                //최근에 알려진 위치 얻어오기
                Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastLocation != null) {
                    Double latitude = lastLocation.getLatitude();
                    Double longitude = lastLocation.getLongitude();

                }

                //주기적으로 GPS 정보 받도록 요청
                manager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        minTime,
                        minDistance,
                        locationListener);

                manager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        minTime,
                        minDistance,
                        locationListener);
            } else {
                manager.removeUpdates(locationListener);

            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    LatLng friend;
    List_Member man;
    public void displayMarker(double latit, double longit){
        mMap.clear();
        mData.clear();

        open_view_tv.setText("오백원 : " + AppManager.getInstance().getLocationMember_location_Y().size());
        open_tv.setText("오백원 : " + AppManager.getInstance().getLocationMember_location_Y().size());
        LatLng mylocation = new LatLng(latit, longit);
        mMap.addMarker(new MarkerOptions().position(mylocation).title(setting.getString("NICK", "")));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mylocation));

        for(int i = 0 ; i < AppManager.getInstance().getLocationMember_Nick().size() ; i++){


            Log.e("display가져온 데이터  ++" , AppManager.getInstance().getLocationMember_Nick().get(i));
            friend = new LatLng( AppManager.getInstance().getLocationMember_location_X().get(i), AppManager.getInstance().getLocationMember_location_Y().get(i));
            mMap.addMarker(new MarkerOptions()
                    .position(friend)
                    .icon(bitmapDescriptor)
                    .title(AppManager.getInstance().getLocationMember_Nick().get(i)));

            man = new List_Member();
            man.mNICK = AppManager.getInstance().getLocationMember_Nick().get(i);
            man.location = AppManager.getInstance().getLocationMember_location_X().get(i) + ", "
                        + AppManager.getInstance().getLocationMember_location_Y().get(i);

            mData.add( man );
            mAdapter = new BaseAdapterex( this, mData );
        }





        listview.setAdapter( mAdapter );



    //    mapData.put(dataKeyList[0], AppManager.getInstance().getLocationMember_Nick().get(0));
    //    mapData.put(dataKeyList[1], AppManager.getInstance().getLocationMember_location_X().get(0) + ", "
    //            + AppManager.getInstance().getLocationMember_location_Y().get(0));
     //   mData.add(mapData);



    }


    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();


            //여기에서 웹디비에 토큰과 위도 경도를 넣어줘야함

            FirebaseMessaging.getInstance().subscribeToTopic("news");
            String token = FirebaseInstanceId.getInstance().getToken();


            AppManager.getInstance().setlocation(location);

            httpConn_inPut task = new httpConn_inPut();
            task.execute(token, setting.getString("NICK", ""));
            //내주변에 있는 사람들의 위치를 받아온뒤
            //동시에 상대방의 위치도 그려준다.

            httpConn_getLocationMember task3 = new httpConn_getLocationMember();
            task3.execute(token);

            editor.putString("location_X", String.valueOf(latitude));
            editor.putString("location_Y", String.valueOf(longitude));
            editor.commit();


            displayMarker(latitude , longitude);
            //나의 위치 마커

            if((Chat_Activity) Chat_Activity.Chat_Activity != null){
                ((Chat_Activity) Chat_Activity.Chat_Activity).restartLocation();
            }

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    private boolean chkGpsService() {
        String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

            // GPS OFF 일때 Dialog 표시
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle("위치 서비스 설정");
            gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을 모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?");
            gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).create().show();
            return false;

        } else {
            return true;
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);



        double location_X = Double.parseDouble(setting.getString("location_X", "37.5834809"));
        double location_Y = Double.parseDouble(setting.getString("location_Y", "126.9243783"));

        LatLng mjclocation = new LatLng(location_X,location_Y);//명지전문대 위치
        mMap.addMarker(new MarkerOptions().position(mjclocation).title(setting.getString("NICK", "")));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mjclocation, 15));
    }


    @Override
    public void onMapLongClick(LatLng point) {
        // 현재 위도와 경도에서 화면 포인트를 알려준다

        mMap.moveCamera(CameraUpdateFactory.newLatLng(point ));


    }
    LatLng itemPoint;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        itemPoint = new LatLng(AppManager.getInstance().getLocationMember_location_X().get(position) ,
                AppManager.getInstance().getLocationMember_location_Y().get(position) );
        mMap.moveCamera(CameraUpdateFactory.newLatLng(itemPoint));

    }
}
