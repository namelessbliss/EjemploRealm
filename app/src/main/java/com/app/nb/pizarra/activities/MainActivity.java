package com.app.nb.pizarra.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.nb.pizarra.R;
import com.app.nb.pizarra.adapter.PizarraAdapter;
import com.app.nb.pizarra.model.Pizarra;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements RealmChangeListener<RealmResults<Pizarra>>, AdapterView.OnItemClickListener {

    private Realm realm;
    private FloatingActionButton fab;
    private ListView listView;
    private PizarraAdapter adapter;

    private RealmResults<Pizarra> pizarras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realm = Realm.getDefaultInstance();
        //Obtener todas las pizarras
        pizarras = realm.where(Pizarra.class).findAll();
        pizarras.addChangeListener(this);
        /*pizarras.addChangeListener(new RealmChangeListener<RealmResults<Pizarra>>() {
            @Override
            public void onChange(RealmResults<Pizarra> pizarras) {

            }
        });*/

        adapter = new PizarraAdapter(this, pizarras, R.layout.list_view_item_pizarra);

        listView = findViewById(R.id.lvPizarra);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);

        fab = findViewById(R.id.fabAddPizarra);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarAlertaCrear("Añadir nueva pizarra", "Ingresa el nombre de la nueva pizarra");
            }
        });

        registerForContextMenu(listView);
        //Borrar todos los datos de la bd
        /*realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();*/
    }

    //CRUD

    private void crearNuevaPizarra(String nombrePizarra) {
        realm.beginTransaction();
        Pizarra pizarra = new Pizarra(nombrePizarra);
        realm.copyToRealm(pizarra);
        realm.commitTransaction();

       /* realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Pizarra pizarra = new Pizarra(nombrePizarra);
                realm.copyToRealm(pizarra);
            }
        });*/
    }

    private void borrarPizarra(Pizarra pizarra) {
        realm.beginTransaction();
        pizarra.deleteFromRealm();
        realm.commitTransaction();
    }

    private void editarPizarra(String nombre, Pizarra pizarra) {
        realm.beginTransaction();
        pizarra.setTitulo(nombre);
        realm.copyToRealmOrUpdate(pizarra);
        realm.commitTransaction();

    }

    private void borrarTodo() {
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }

    private void mostrarAlertaCrear(String titulo, String mensaje) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (titulo != null) builder.setTitle(titulo);
        if (mensaje != null) builder.setMessage(mensaje);

        //Inflar la vista de alert dialogo
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialogo_crear_pizarra, null);
        builder.setView(viewInflated);

        final EditText input = viewInflated.findViewById(R.id.etNuevaPizarra);

        builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombrePizarra = input.getText().toString().trim();
                if (nombrePizarra.length() > 0)
                    crearNuevaPizarra(nombrePizarra);
                else
                    Toast.makeText(getApplicationContext(), "Se requiere un nombre", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancelar", null);

        builder.create().show();
    }

    private void mostrarAlertaEditar(String titulo, String mensaje, final Pizarra pizarra) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (titulo != null) builder.setTitle(titulo);
        if (mensaje != null) builder.setMessage(mensaje);

        //Inflar la vista de alert dialogo
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialogo_crear_pizarra, null);
        builder.setView(viewInflated);

        final EditText input = viewInflated.findViewById(R.id.etNuevaPizarra);
        input.setText(pizarra.getTitulo());

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String nombrePizarra = input.getText().toString().trim();

                if (nombrePizarra.length() == 0)
                    Toast.makeText(getApplicationContext(), "Se requiere un nombre", Toast.LENGTH_SHORT).show();
                else if (nombrePizarra.equalsIgnoreCase(pizarra.getTitulo()))
                    Toast.makeText(getApplicationContext(), "El nombre es el mismo", Toast.LENGTH_SHORT).show();
                else
                    editarPizarra(nombrePizarra, pizarra);
            }
        }).setNegativeButton("Cancelar", null);

        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pizarra, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.borrarTodo:
                borrarTodo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(pizarras.get(info.position).getTitulo());
        getMenuInflater().inflate(R.menu.context_menu_pizarra, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.editPizarra:
                mostrarAlertaEditar("Editar Pizarra", "Cambie el nombre de la pizarra", pizarras.get(info.position));
                return true;
            case R.id.borrarPizarra:
                borrarPizarra(pizarras.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    /**
     * cada ves que se lance el evento el cual conyeva a un cambio en la lista, actualiza el adaptador
     *
     * @param pizarras
     */
    @Override
    public void onChange(RealmResults<Pizarra> pizarras) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long id) {

        Intent intent = new Intent(MainActivity.this, NotaActivity.class);
        intent.putExtra("id", pizarras.get(posicion).getId());
        startActivity(intent);

    }
}
