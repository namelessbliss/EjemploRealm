package com.app.nb.pizarra.activities;

import android.content.DialogInterface;
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
import com.app.nb.pizarra.adapter.NotaAdapter;
import com.app.nb.pizarra.model.Nota;
import com.app.nb.pizarra.model.Pizarra;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;

public class NotaActivity extends AppCompatActivity implements RealmChangeListener<Pizarra> {

    private ListView listView;
    private FloatingActionButton fab;

    private NotaAdapter adapter;
    private RealmList<Nota> notas;
    private Realm realm;

    private int pizarraId;
    private Pizarra pizarra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);

        realm = Realm.getDefaultInstance();

        if (getIntent().getExtras() != null)
            pizarraId = getIntent().getExtras().getInt("id");

        pizarra = realm.where(Pizarra.class).equalTo("id", pizarraId).findFirst();
        pizarra.addChangeListener(this);

        notas = pizarra.getNotas();

        this.setTitle(pizarra.getTitulo());

        fab = findViewById(R.id.fabAddNota);
        listView = findViewById(R.id.lvNota);
        adapter = new NotaAdapter(this, notas, R.layout.list_view_item_nota);

        listView.setAdapter(adapter);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mostrarAlerta("Añadir nueva nota", "Ingresa nota para la pizarra" + pizarra.getTitulo());
            }
        });

        registerForContextMenu(listView);
    }

    //CRUD

    private void crearNuevaNota(String nombreNota) {
        realm.beginTransaction();
        Nota nota = new Nota(nombreNota);
        realm.copyToRealm(nota);
        pizarra.getNotas().add(nota);
        realm.commitTransaction();

    }

    private void borrarNota(Nota nota) {
        realm.beginTransaction();
        nota.deleteFromRealm();
        realm.commitTransaction();
    }

    private void editarNota(String descripcion, Nota nota) {
        realm.beginTransaction();
        nota.setDescripcion(descripcion);
        realm.copyToRealmOrUpdate(nota);
        realm.commitTransaction();

    }

    private void borrarTodo() {
        realm.beginTransaction();
        pizarra.getNotas().deleteAllFromRealm();
        realm.commitTransaction();
    }

    private void mostrarAlerta(String titulo, String mensaje) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (titulo != null) builder.setTitle(titulo);
        if (mensaje != null) builder.setMessage(mensaje);

        //Inflar la vista de alert dialogo
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialogo_crear_nota, null);
        builder.setView(viewInflated);

        final EditText input = viewInflated.findViewById(R.id.etNuevaNota);

        builder.setPositiveButton("Añadir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String nombreNota = input.getText().toString().trim();
                if (nombreNota.length() > 0)
                    crearNuevaNota(nombreNota);
                else
                    Toast.makeText(getApplicationContext(), "La nota no puede estar en blanco", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("Cancelar", null);

        builder.create().show();
    }

    private void mostrarAlertaEditar(String titulo, String mensaje, final Nota nota) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if (titulo != null) builder.setTitle(titulo);
        if (mensaje != null) builder.setMessage(mensaje);

        //Inflar la vista de alert dialogo
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialogo_crear_nota, null);
        builder.setView(viewInflated);

        final EditText input = viewInflated.findViewById(R.id.etNuevaNota);
        input.setText(nota.getDescripcion());

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String newDescripcion = input.getText().toString().trim();

                if (newDescripcion.length() == 0)
                    Toast.makeText(getApplicationContext(), "Se requiere un nombre", Toast.LENGTH_SHORT).show();
                else if (newDescripcion.equalsIgnoreCase(nota.getDescripcion()))
                    Toast.makeText(getApplicationContext(), "El nombre es el mismo", Toast.LENGTH_SHORT).show();
                else
                    editarNota(newDescripcion, nota);
            }
        }).setNegativeButton("Cancelar", null);

        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nota, menu);
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
        getMenuInflater().inflate(R.menu.context_menu_nota, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.editNota:
                mostrarAlertaEditar("Editar Nota", "Cambie la descripcion", notas.get(info.position));
                return true;
            case R.id.borrarNota:
                borrarNota(notas.get(info.position));
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    public void onChange(Pizarra pizarra) {
        adapter.notifyDataSetChanged();
    }
}
