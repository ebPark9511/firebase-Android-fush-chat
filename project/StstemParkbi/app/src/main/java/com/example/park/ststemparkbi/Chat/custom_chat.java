package com.example.park.ststemparkbi.Chat;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.park.ststemparkbi.R;
import com.google.firebase.database.ThrowOnExtraProperties;

import java.util.ArrayList;

/**
 * Created by Park on 2016-09-17.
 */
public class custom_chat extends BaseAdapter {
    public class ListContents{
        String msg;
        int type;
        String nick;
        ListContents(String _msg,String nick,int _type)
        {
            this.msg = _msg;
            this.type = _type;
            this.nick = nick;
        }
    }

    private ArrayList<ListContents> m_List;
    public custom_chat() {
        m_List = new ArrayList<ListContents>();
    }
    // 외부에서 아이템 추가 요청 시 사용
    public void add(String _msg,String nick,int _type) {

        m_List.add(new ListContents(_msg, nick ,_type));
    }

    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        m_List.remove(_position);
    }
    @Override
    public int getCount() {
        return m_List.size();
    }

    @Override
    public Object getItem(int position) {
        return m_List.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        TextView text1    = null;
        TextView text    = null;
        CustomHolder    holder  = null;
        LinearLayout layout  = null;
        View            viewRight = null;
        View            viewLeft = null;

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.loginstart_chatitem, parent, false);

            layout    = (LinearLayout) convertView.findViewById(R.id.layout);
            text    = (TextView) convertView.findViewById(R.id.text);
            text1 =(TextView)convertView.findViewById(R.id.text1);
            viewRight    = (View) convertView.findViewById(R.id.imageViewright);
            viewLeft    = (View) convertView.findViewById(R.id.imageViewleft);


            // 홀더 생성 및 Tag로 등록
            holder = new CustomHolder();
            holder.m_TextView   = text;
            holder.layout = layout;
            holder.m_TextView2 =text1;
            holder.viewRight = viewRight;
            holder.viewLeft = viewLeft;
            convertView.setTag(holder);
        }
        else {
            holder  = (CustomHolder) convertView.getTag();
            text    = holder.m_TextView;
            text1    = holder.m_TextView2;
            layout  = holder.layout;
            viewRight = holder.viewRight;
            viewLeft = holder.viewLeft;
        }


        //12자마다 다음줄로 넘어가게

        // Text 등록
        text.setText(m_List.get(position).msg);
        text1.setText(m_List.get(position).nick);
        if( m_List.get(position).type == 0 ) {

            text.setBackgroundResource(R.drawable.inbox);
            layout.setGravity(Gravity.LEFT);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.GONE);
            text1.setVisibility(View.VISIBLE);


        }else if(m_List.get(position).type == 1){
            text.setBackgroundResource(R.drawable.outbox);
            text1.setVisibility(View.GONE);
            layout.setGravity(Gravity.RIGHT);
            viewRight.setVisibility(View.GONE);
            viewLeft.setVisibility(View.GONE);

        }else if(m_List.get(position).type == 2){
            text.setBackgroundResource(R.drawable.restart);
            //~명이 참여하고 있습니다.
            layout.setGravity(Gravity.CENTER);
            viewRight.setVisibility(View.INVISIBLE);
            viewLeft.setVisibility(View.INVISIBLE);
            text1.setVisibility(View.GONE);
        }



        return convertView;
    }

    private class CustomHolder {
        TextView    m_TextView;
        TextView    m_TextView2;
        LinearLayout    layout;
        View viewRight;
        View viewLeft;
    }
}
