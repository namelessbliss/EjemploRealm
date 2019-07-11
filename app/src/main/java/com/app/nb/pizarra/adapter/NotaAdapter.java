package com.app.nb.pizarra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.nb.pizarra.R;
import com.app.nb.pizarra.model.Nota;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class NotaAdapter extends BaseAdapter {

    private Context context;
    private List<Nota> notas;
    private int layout;

    public NotaAdapter(Context context, List<Nota> notas, int layout) {
        this.context = context;
        this.notas = notas;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return notas.size();
    }

    @Override
    public Nota getItem(int posicion) {
        return notas.get(posicion);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int posicion, View converView, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (converView == null) {
            converView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.descripcion = converView.findViewById(R.id.tvDescripcionNota);
            viewHolder.fecha = converView.findViewById(R.id.tvNotaFecha);
            converView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) converView.getTag();
        }

        Nota nota = notas.get(posicion);
        viewHolder.descripcion.setText(nota.getDescripcion());

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = dateFormat.format(nota.getFechaCreacion());

        viewHolder.fecha.setText(fecha);

        return converView;
    }

    public class ViewHolder {
        TextView descripcion;
        TextView fecha;
    }
}
