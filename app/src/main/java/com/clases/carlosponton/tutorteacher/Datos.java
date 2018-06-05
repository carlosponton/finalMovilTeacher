package com.clases.carlosponton.tutorteacher;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Datos {

    private static String db = "Teacher";
    private static String db2 = "Reservation";
    private static String db3 = "Student";
    private static ArrayList<Reservation> reservations  = new ArrayList();
    private static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    public static DatabaseReference findById(String id){
        return databaseReference.child(db).child(id);
    }

    public static DatabaseReference reservationFindById(String id){
        return databaseReference.child(db2).child(id);
    }

    public static DatabaseReference studentFindById(String id){
        return databaseReference.child("Student").child(id);
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

    public static void editReservation(Reservation reservation){
        databaseReference.child(db2).child(reservation.getId()).setValue(reservation);
    }

    public static void saveReservation(Reservation reservation){
        databaseReference.child(db2).child(reservation.getId()).setValue(reservation);
    }
    public static void deleteReservation(Reservation reservation){
        databaseReference.child(db2).child(reservation.getId()).removeValue();
    }

    public static void setTeachers(ArrayList<Reservation> reservations) {
        Datos.reservations = reservations;
    }
}
