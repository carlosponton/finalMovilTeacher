package com.clases.carlosponton.tutorteacher;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterReservation.OnReservationClickListener {

    private FirebaseAuth firebaseAuth;
    private Intent i;
    private DatabaseReference databaseReference;
    private Teacher teacher;
    private ImageView profile;
    private TextView txtName, txtEmail;
    private StorageReference storageReference;
    private AdapterReservation adapter;
    private RecyclerView lstStudent;
    private LinearLayoutManager llm;
    private String bd = "Reservation";
    private ArrayList<Reservation> reservations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        profile = (ImageView) headerView.findViewById(R.id.profileView);
        txtName = (TextView) headerView.findViewById(R.id.txtName);
        txtEmail = (TextView) headerView.findViewById(R.id.txtEmail);

        firebaseAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        String id = firebaseAuth.getCurrentUser().getUid();

        Datos.findById(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    teacher = dataSnapshot.getValue(Teacher.class);
                    txtName.setText(teacher.getName());
                    txtEmail.setText(teacher.getEmail());
                    if(teacher.getUri() != null){
                        storageReference.child(teacher.getUri()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
                                Picasso.get().load(uri).into(profile);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Listar los reservaciones
        lstStudent = findViewById(R.id.lstStudent);

        reservations = new ArrayList<>();

        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new AdapterReservation(reservations,this);

        lstStudent.setLayoutManager(llm);
        lstStudent.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(bd).orderByChild("status").equalTo(1d).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reservations.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Reservation r = snapshot.getValue(Reservation.class);
                        reservations.add(r);
                    }
                }
                adapter.notifyDataSetChanged();
                Datos.setTeachers(reservations);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();
        if (id == R.id.nav_report) {
        } else if (id == R.id.nav_logout) {
            firebaseAuth.signOut();
            i = new Intent(this,MainActivity.class);
            finish();
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onReservationClick(Reservation r) {
        Intent i = new Intent(HomeActivity.this,ReservationDetail.class);
        Bundle b = new Bundle();
        b.putString("id",r.getId());
        b.putString("idTeacher",r.getIdTeacher());
        b.putString("idStudent",r.getIdStudent());
        b.putString("comment",r.getComment());

        i.putExtra("datos",b);
        startActivity(i);
    }
}
