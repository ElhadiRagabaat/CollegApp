package com.ragabaat.admincollage.faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ragabaat.admincollage.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateFaculty extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView csDepartment,ChemistryDepartment,phisicDepartment,MechanicalDepartment;
    private LinearLayout csNoData,mechNoData,physicsNoData,ChemistryNoData;
    private List<TeacherData> list1,list2,list3,list4;
    private TeacherAdapter adapter;


    private DatabaseReference reference,dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_faculty);
        setTitle("");

        ChemistryDepartment = findViewById(R.id.ChemistryDepartment);
        ChemistryNoData = findViewById(R.id.ChemistryNoData);
        phisicDepartment = findViewById(R.id.phisicDepartment);
        physicsNoData = findViewById(R.id.physicsNoData);
        MechanicalDepartment = findViewById(R.id.MechanicalDepartment);
        mechNoData = findViewById(R.id.mechNoData);
        csDepartment = findViewById(R.id.csDepartment);
        csNoData = findViewById(R.id.csNoData);

        fab = findViewById(R.id.fab);


        reference = FirebaseDatabase.getInstance().getReference().child("teacher");

        csDepartment();
        MechanicalDepartment();
        phisicDepartment();
        ChemistryDepartment();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(UpdateFaculty.this,AddTeachers.class));
            }
        });
    }

    private void csDepartment() {

        dbRef = reference.child("Computer Science");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list1 = new ArrayList<>();
                if (!dataSnapshot.exists()){

                    csNoData.setVisibility(View.VISIBLE);
                    csDepartment.setVisibility(View.GONE);

                }else {

                    csNoData.setVisibility(View.GONE);
                    csDepartment.setVisibility(View.VISIBLE);

                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list1,UpdateFaculty.this,"Computer Science");
                    csDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateFaculty.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }


    private void MechanicalDepartment() {

        dbRef = reference.child("Mechanical");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list2 = new ArrayList<>();
                if (!dataSnapshot.exists()){

                    mechNoData.setVisibility(View.VISIBLE);
                    MechanicalDepartment.setVisibility(View.GONE);

                }else {

                 mechNoData.setVisibility(View.GONE);
                    MechanicalDepartment.setVisibility(View.VISIBLE);

                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    MechanicalDepartment.setHasFixedSize(true);
                    MechanicalDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list2,UpdateFaculty.this,"Mechanical");
                    MechanicalDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateFaculty.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }



    private void phisicDepartment() {

        dbRef = reference.child("Physics");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list3 = new ArrayList<>();
                if (!dataSnapshot.exists()){

                    physicsNoData.setVisibility(View.VISIBLE);
                    phisicDepartment.setVisibility(View.GONE);

                }else {

                    physicsNoData.setVisibility(View.GONE);
                    phisicDepartment.setVisibility(View.VISIBLE);

                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    phisicDepartment.setHasFixedSize(true);
                    phisicDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list3,UpdateFaculty.this,"Physics");
                    phisicDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateFaculty.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    private void ChemistryDepartment() {

        dbRef = reference.child("Chemistry");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){

                    ChemistryNoData.setVisibility(View.VISIBLE);
                   ChemistryDepartment.setVisibility(View.GONE);

                }else {

                    ChemistryNoData.setVisibility(View.GONE);
                    ChemistryDepartment.setVisibility(View.VISIBLE);

                    for (DataSnapshot snapshot:dataSnapshot.getChildren()){

                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    ChemistryDepartment.setHasFixedSize(true);
                    ChemistryDepartment.setLayoutManager(new LinearLayoutManager(UpdateFaculty.this));
                    adapter = new TeacherAdapter(list4,UpdateFaculty.this,"Chemistry");
                    ChemistryDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(UpdateFaculty.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}