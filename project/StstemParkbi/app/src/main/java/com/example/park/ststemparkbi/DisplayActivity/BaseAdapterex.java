package com.example.park.ststemparkbi.DisplayActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.park.ststemparkbi.R;

import java.util.ArrayList;

/**
 * Created by Park on 2016-11-25.
 */

public class BaseAdapterex  extends BaseAdapter {
    Context             mContext        = null;
    ArrayList<List_Member>  mData           = null;
    LayoutInflater mLayoutInflater = null;

    public BaseAdapterex(Context context, ArrayList<List_Member> data )
    {
        mContext        = context;
        mData           = data;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public int getCount()
    {
        return mData.size();
    }

    public long getItemId( int position )
    {
        return position;
    }

    public List_Member getItem( int position )
    {
        return mData.get( position );
    }



    public View getView(int position, View convertView, ViewGroup parent )
    {
        View itemLayout = mLayoutInflater.inflate( R.layout.listview_item, null);

        TextView nameTv = (TextView) itemLayout.findViewById( R.id.name_text );
        TextView departmentTv = (TextView) itemLayout.findViewById( R.id.department_text );

        nameTv.setText( mData.get( position ).mNICK );
        departmentTv.setText( mData.get( position ).location );

        return itemLayout;
    }
}
