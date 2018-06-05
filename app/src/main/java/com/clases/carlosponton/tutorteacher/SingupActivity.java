package com.clases.carlosponton.tutorteacher;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class SingupActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 234;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtName;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Intent i;
    private Uri filePath;
    private ImageView profile;
    private StorageReference storageReference;
    private RadioButton rMan, rWoman;
    private Spinner spnCity;
    private CheckBox math, english, history, music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageReference = FirebaseStorage.getInstance().getReference();

        final ImageView view = (ImageView) findViewById(R.id.imgProfile);
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, getResources().getString(R.string.select_image)), PICK_IMAGE_REQUEST);
            }


        });

        progressDialog = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtName = (EditText) findViewById(R.id.txtName);
        profile = (ImageView) findViewById(R.id.imgProfile);
        rMan = (RadioButton) findViewById(R.id.man);
        rWoman = (RadioButton) findViewById(R.id.woman);
        math = (CheckBox) findViewById(R.id.checkMath);
        english = (CheckBox) findViewById(R.id.checkEnglish);
        music = (CheckBox) findViewById(R.id.checkMusic);
        history = (CheckBox) findViewById(R.id.checkHistory);
        spnCity = (Spinner) findViewById(R.id.spnCity);

        String opc[] = this.getResources().getStringArray(R.array.list_city);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opc);
        spnCity.setAdapter(adapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void Singup(final String uri, final ProgressDialog progressDialog) {
        final String email = txtEmail.getText().toString().trim();
        final String password = txtPassword.getText().toString().trim();
        final String name = txtName.getText().toString().trim();
        final String sex;
        final ArrayList<String> topics = new ArrayList<String>();
        final int city = spnCity.getSelectedItemPosition();

        if (math.isChecked()) {
            topics.add("math");
        }
        if (music.isChecked()) {
            topics.add("music");
        }
        if (english.isChecked()) {
            topics.add("english");
        }
        if (history.isChecked()) {
            topics.add("history");
        }

        if (topics.size() == 0) {
            Toast.makeText(this, getResources().getString(R.string.error_empty_email), Toast.LENGTH_SHORT).show();
            return;
        }
        if (!rMan.isChecked() && !rWoman.isChecked()) {
            Toast.makeText(this, getResources().getString(R.string.error_empty_email), Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (rMan.isChecked()) {
                sex = "man";
            } else {
                sex = "woman";
            }
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, getResources().getString(R.string.error_empty_email), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, getResources().getString(R.string.error_empty_password), Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, getResources().getString(R.string.error_empty_name), Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    String id = task.getResult().getUser().getUid();
                    Teacher teacher = new Teacher(id, email, name, sex, city, uri, topics);
                    teacher.save();
                    i = new Intent(SingupActivity.this, HomeActivity.class);
                    finish();
                    startActivity(i);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(SingupActivity.this, getResources().getString(R.string.error_registered), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void singup(View v) {
        uploadFile();
    }

    private void uploadFile() {
        progressDialog.setMessage(getResources().getString(R.string.registering_user));
        progressDialog.show();
        if (filePath == null) {
            Singup(null, progressDialog);
        } else {

            String nameFile = "images/" + Calendar.getInstance().getTime().toString() + ".jpg";
            StorageReference riversRef = storageReference.child(nameFile);

            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Singup(downloadUrl.toString(), progressDialog);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(SingupActivity.this, getResources().getString(R.string.fail_upload_profile), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}