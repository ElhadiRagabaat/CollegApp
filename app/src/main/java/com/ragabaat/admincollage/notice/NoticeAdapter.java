package com.ragabaat.admincollage.notice;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewAdapter> {


    @NonNull
    @Override
    public NoticeViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewAdapter holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class NoticeViewAdapter extends RecyclerView.ViewHolder {


        public NoticeViewAdapter(@NonNull View itemView) {
            super(itemView);


        }
    }
}
