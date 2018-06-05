package com.clases.carlosponton.tutorteacher;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Datos {

    private static String db = "Teacher";
    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public static DatabaseReference findById(String id){
        return databaseReference.child("Teacher").child(id);
    }

    public static String getId(){
        return databaseReference.push().getKey();
    }

    public static void edit(Teacher teacher){
        databaseReference.child(db).child(teacher.getId()).setValue(teacher);
    }

    public static void save(Teacher teacher){
        databaseReference.child(db).child(teacher.getId()).setValue(teacher);
    }
    public static void delete(Teacher teacher){
        databaseReference.child(db).child(teacher.getId()).removeValue();
    }
}
