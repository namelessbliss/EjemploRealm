package com.app.nb.pizarra.model;

import com.app.nb.pizarra.app.MyApplication;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Nota extends RealmObject {

    @PrimaryKey
    private int id;
    @Required
    private String descripcion;
    @Required
    private Date fechaCreacion;

    public Nota(String descripcion) {
        //Incrementa ID
        this.id = MyApplication.notaID.incrementAndGet();
        this.descripcion = descripcion;
        this.fechaCreacion = new Date();
    }

    public Nota() {
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

}
