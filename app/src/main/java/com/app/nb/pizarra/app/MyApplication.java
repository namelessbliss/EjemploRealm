package com.app.nb.pizarra.app;

import android.app.Application;

import com.app.nb.pizarra.model.Nota;
import com.app.nb.pizarra.model.Pizarra;

import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

public class MyApplication extends Application {

    public static AtomicInteger pizarraID = new AtomicInteger();
    public static AtomicInteger notaID = new AtomicInteger();

    @Override
    public void onCreate() {

        super.onCreate();

        setUpRealmConfig();

        Realm realm = Realm.getDefaultInstance();
        pizarraID = getIdByTable(realm, Pizarra.class);
        notaID = getIdByTable(realm, Nota.class);
        realm.close();
    }


    /**
     * Configuracion para Realm
     */
    private void setUpRealmConfig() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(configuration);
    }


    /**
     * Cualquier clase generica que extienda del RealmObject, devolvera un AtomicInteger,
     * pasamos la bd Realm y pasamos la clase generica
     *
     * @param realm
     * @param anyclass
     * @param <T>
     * @return
     */
    private <T extends RealmObject> AtomicInteger getIdByTable(Realm realm, Class<T> anyclass) {
        RealmResults<T> results = realm.where(anyclass).findAll();
        return (results.size() > 0) ? new AtomicInteger(results.max("id").intValue()) : new AtomicInteger();
    }
}
