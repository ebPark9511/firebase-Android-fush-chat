package com.example.park.ststemparkbi;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Park on 2016-11-20.
 */

public class DBHelper extends SQLiteOpenHelper {
    ArrayList<String> nick;
    ArrayList<String> msg;
    ArrayList<Integer> Yni;
    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */

        db.execSQL("CREATE TABLE SystemMSG (nick varchar(10), msg varchar(100), Yni int(10));");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersionz) {

    }


    public void insert(String nick, String msg, int Yni) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO SystemMSG VALUES('" + nick + "','" + msg + "','"+ Yni + "');");

        db.close();
    }



    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM SystemMSG", null);
        cursor.moveToFirst();
        nick = new ArrayList<String>();
        msg = new ArrayList<String>();
        Yni = new ArrayList<Integer>();


        while (cursor.moveToNext()) {
            nick.add(cursor.getString(0));
            msg.add(cursor.getString(1));
            Yni.add(cursor.getInt(2));

            Log.e("asdf     ", cursor.getString(0) + "    "   +  cursor.getString(1) +"    "    +  cursor.getString(2));
        }

        AppManager.getInstance().setMessage(nick, msg, Yni);

        return result;
    }
}