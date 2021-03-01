package com.example.simpleenterrecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private ArrayList<Item> mData = null ;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView num, time, phoneNum, city ;

        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            num = itemView.findViewById(R.id.num);
            time = itemView.findViewById(R.id.time);
            phoneNum = itemView.findViewById(R.id.phoneNum);
            city = itemView.findViewById(R.id.city);
        }

        public void setItem(Item item) {
            num.setText(item.getNum());
            time.setText(item.getTime());
            phoneNum.setText(item.getPhoneNum());
            city.setText(item.getCity());
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    Adapter(ArrayList<Item> list) {
        mData = list ;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.admin_recycler, parent, false);

        return new ViewHolder(itemView);
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mData.get(position);
        holder.setItem(item);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
