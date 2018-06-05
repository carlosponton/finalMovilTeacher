package com.clases.carlosponton.tutorteacher;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterReservation extends RecyclerView.Adapter<AdapterReservation.ReservationViewHolder> {
    private ArrayList<Reservation> reservations;
    private OnReservationClickListener clickListener;
    private StorageReference storageReference;

    public AdapterReservation(ArrayList<Reservation> reservations, OnReservationClickListener clickListener){
        this.reservations=reservations;
        this.clickListener = clickListener;
    }

    @Override
    public ReservationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_reservation,parent,false);
        return new ReservationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ReservationViewHolder holder, int position) {
        Resources res = holder.itemView.getContext().getResources();
        final Reservation r = reservations.get(position);
        final String[] city = res.getStringArray(R.array.list_city);
        storageReference = FirebaseStorage.getInstance().getReference();
        Datos.studentFindById(r.getIdStudent()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.name.setText(dataSnapshot.toString());
                if(dataSnapshot.exists()) {
                    Student s = dataSnapshot.getValue(Student.class);
                    holder.name.setText(s.getName());
                    holder.city.setText(city[s.getCity()]);
                    holder.email.setText(s.getEmail());
                    if(s.getUri()!=null){
                        storageReference.child(s.getUri()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'
                                Picasso.get().load(uri).into(holder.pic);
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

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.onReservationClick(r);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    public static class ReservationViewHolder extends RecyclerView.ViewHolder{
        private ImageView pic;
        private TextView name;
        private TextView email;
        private TextView city;
        private View v;

        public ReservationViewHolder(View itemView){
            super(itemView);
            v = itemView;
            pic = v.findViewById(R.id.profileView);
            name = v.findViewById(R.id.lblName);
            email = v.findViewById(R.id.lblEmail);
            city = v.findViewById(R.id.lblCity);
        }

    }

    public interface OnReservationClickListener{
        void onReservationClick(Reservation r);
    }

}

