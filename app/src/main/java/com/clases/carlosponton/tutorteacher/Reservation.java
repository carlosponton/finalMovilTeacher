package com.clases.carlosponton.tutorteacher;

public class Reservation {
    private String id;
    private String idStudent;
    private String idTeacher;
    private String comment;
    private int status;
    public Reservation(String id, String idStudent, String idTeacher, String comment){
        this.id = id;
        this.idStudent = idStudent;
        this.idTeacher = idTeacher;
        this.comment = comment;
        this.status = 1;
    }

    public Reservation(){

    }

    public Reservation(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(String idStudent) {
        this.idStudent = idStudent;
    }

    public String getIdTeacher() {
        return idTeacher;
    }

    public void setIdTeacher(String idTeacher) {
        this.idTeacher = idTeacher;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void save(){

        Datos.saveReservation(this);
    }

    public void edit(){
        Datos.editReservation(this);
    }

    public void delete(){
        Datos.deleteReservation(this);
    }
}
