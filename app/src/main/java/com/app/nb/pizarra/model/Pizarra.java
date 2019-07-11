package com.app.nb.pizarra.model;

import com.app.nb.pizarra.app.MyApplication;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Pizarra extends RealmObject {

    //Representacion de campos de la bd

    @PrimaryKey
    private int id;
    @Required
    private String titulo;
    @Required
    private Date fechaCreacion;

    // cada pizarra puede tener 0 o + notas
    private RealmList<Nota> notas;

    public Pizarra(String titulo) {
        //Incrementa el id
        this.id = MyApplication.pizarraID.incrementAndGet();
        this.titulo = titulo;
        this.fechaCreacion = new Date();
        this.notas = new RealmList<Nota>();
    }

    public Pizarra() {
    }

    public int getId() {
        return id;
    }


    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public RealmList<Nota> getNotas() {
        return notas;
    }

}
