package com.clases.carlosponton.tutorteacher;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReservationDetail extends AppCompatActivity {

    private TextView txtName;
    private TextView txtEmail;
    private TextView txtCity;
    private TextView txtSex;
    private TextView txtComment;
    private Button confirm, cancel;
    private ImageView imgPic;
    private String idTeacher, name, email, sex, pic, comment;
    private ArrayList<String> topics;
    private int city;
    private Intent i;
    private Bundle bundle;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private String id;
    private String idStudent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_detail);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);
        txtCity = findViewById(R.id.txtCity);
        txtComment = findViewById(R.id.txtComment);
        txtSex = findViewById(R.id.txtSex);
        imgPic = findViewById(R.id.imgPic);
        final String[] arrayCity = getResources().getStringArray(R.array.list_city);

        storageReference = FirebaseStorage.getInstance().getReference();

        i = getIntent();

        topics = new ArrayList<String>();

        bundle = i.getBundleExtra("datos");
        id = bundle.getString("id");
        idTeacher = bundle.getString("idTeacher");
        idStudent = bundle.getString("idStudent");
        comment = bundle.getString("comment");


        Datos.studentFindById(idStudent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Student student = dataSnapshot.getValue(Student.class);
                    name = student.getName();
                    email = student.getEmail();
                    if(student.getSex().equals("man")){
                        sex = getResources().getString(R.string.sexMan);
                    }else{
                        sex = getResources().getString(R.string.SexWoman);
                    }
                    city = student.getCity();
                    txtName.setText(name);
                    txtCity.setText(arrayCity[city]);
                    txtEmail.setText(email);
                    txtSex.setText(sex);
                    if(student.getUri() != null){

                        storageReference.child(student.getUri()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
                                Picasso.get().load(uri).into(imgPic);
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


        txtComment.setText(comment);
    }

    public void eliminar(View v){
        String positivo, negativo;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.rechazar));
        builder.setMessage(getResources().getString(R.string.pregunta_eliminacion));
        positivo = getResources().getString(R.string.positivo);
        negativo = getResources().getString(R.string.negativo);

        builder.setPositiveButton(positivo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Reservation r = new Reservation(id, idStudent, idTeacher, comment,3);
                r.edit();
                onBackPressed();
            }
        });

        builder.setNegativeButton(negativo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void onBackPressed() {
        finish();
        Intent i = new Intent(ReservationDetail.this,HomeActivity.class);
        startActivity(i);
    }
}
