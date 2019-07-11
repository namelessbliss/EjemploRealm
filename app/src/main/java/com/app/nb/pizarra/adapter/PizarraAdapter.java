package com.app.nb.pizarra.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.nb.pizarra.R;
import com.app.nb.pizarra.model.Pizarra;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class PizarraAdapter extends BaseAdapter {

    private Context context;
    private List<Pizarra> pizarras;
    private int layout;

    public PizarraAdapter(Context context, List<Pizarra> pizarras, int layout) {
        this.context = context;
        this.pizarras = pizarras;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return pizarras.size();
    }

    @Override
    public Pizarra getItem(int posicion) {
        return pizarras.get(posicion);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout, null);
            viewHolder = new ViewHolder();
            viewHolder.titulo = (TextView) convertView.findViewById(R.id.tvTituloPizarra);
            viewHolder.notas = (TextView) convertView.findViewById(R.id.tvNotasPizarra);
            viewHolder.fechaCreacion = (TextView) convertView.findViewById(R.id.tvFecha);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Pizarra pizarra = pizarras.get(posicion);
        viewHolder.titulo.setText(pizarra.getTitulo());

        int numeroNotas = pizarra.getNotas().size();
        String textoNotas = (numeroNotas == 1) ? numeroNotas + " Nota" : numeroNotas + " Notas";

        viewHolder.notas.setText(textoNotas);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = dateFormat.format(pizarra.getFechaCreacion());
        viewHolder.fechaCreacion.setText(fecha);

        return convertView;
    }

    public class ViewHolder {
        TextView titulo;
        TextView notas;
        TextView fechaCreacion;
    }
}
