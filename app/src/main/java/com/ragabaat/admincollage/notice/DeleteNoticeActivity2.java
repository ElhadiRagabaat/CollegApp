package com.ragabaat.admincollage.notice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ragabaat.admincollage.R;

import java.util.ArrayList;
import java.util.List;

public class DeleteNoticeActivity2 extends AppCompatActivity {

    private RecyclerView deleteNoticeRecycler;
    private ProgressBar progressBar;
    private ArrayList<NoticeData> list;
    private NoticeAdapter adapter;
    LinearLayoutManager layoutManager;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_notice2);


        progressBar = findViewById(R.id.progressBar);

        reference = FirebaseDatabase.getInstance().getReference().child("Notice");

        deleteNoticeRecycler = findViewById(R.id.deleteNoticeRecycler);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        deleteNoticeRecycler.setLayoutManager(layoutManager);
        deleteNoticeRecycler.setHasFixedSize(true);


        /////func to get the notice
        getNotice();

    }


    private void getNotice() {


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                list = new ArrayList<>();

                for (DataSnapshot snapshot : datasnapshot.getChildren()){

                    NoticeData data = snapshot.getValue(NoticeData.class);
                    list.add(data);

                }
                adapter = new NoticeAdapter(DeleteNoticeActivity2.this,list);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                deleteNoticeRecycler.setAdapter(adapter);


            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(DeleteNoticeActivity2.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}