package com.example.park.ststemparkbi;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Park on 2016-11-21.
 */
public class AppManager{


    public static final String SERVER_ADDRESS = "HOST_NAME_HERE/SystemParkbi/" ;
    private static AppManager ourInstance = new AppManager();

    public static AppManager getInstance() {
        return ourInstance;
    }

    private AppManager() {


    }


    Activity act;
    public void setActivity(Activity act){
        this.act = act;
    }
    public Activity getActivity(){
        return act;
    }


    Location location = null;
    public void setlocation(Location location) {
        this.location = location;
    }


    public Location getlocation() {
        return location;
    }

    String token ;
    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }



    ArrayList<String> nick = new ArrayList<String>();
    ArrayList<Double> location_X = new ArrayList<Double>();
    ArrayList<Double> location_Y = new ArrayList<Double>();

    public void setLocationMember(ArrayList<String> nick, ArrayList<Double> location_x, ArrayList<Double> location_y) {
        this.nick = nick;
        this.location_X = location_x;
        this.location_Y = location_y;

    }

    public ArrayList<String> getLocationMember_Nick() {
        return this.nick;
    }
    public ArrayList<Double> getLocationMember_location_X() {
        return this.location_X;
    }
    public ArrayList<Double> getLocationMember_location_Y() {
        return this.location_Y;
    }



    ArrayList<String> msgnickArray = new ArrayList<String>();
    ArrayList<String> msgArray = new ArrayList<String>();
    ArrayList<Integer> msgyniArray = new ArrayList<Integer>();

    public void setMessage(ArrayList<String> nick, ArrayList<String> msg, ArrayList<Integer> yni) {
        this.msgnickArray = nick;
        this.msgArray = msg;
        this.msgyniArray = yni;

    }

    public ArrayList<String> getnickMessage() {
        return msgnickArray;
    }
    public ArrayList<String> getMessage() {
        return msgArray;
    }
    public ArrayList<Integer> getmsgyniArray() {
        return msgyniArray;
    }

    boolean chat_Activity = false;
    public void setChat_Actvity(boolean chat_Actvity) {
        this.chat_Activity = chat_Actvity;
    }

    public boolean getChat_Actvity() {
        return chat_Activity;
    }
}