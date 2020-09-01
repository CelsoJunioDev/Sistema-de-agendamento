package com.exemple.testedeagendamento.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;


import com.exemple.testedeagendamento.R;

import java.util.List;

public class AdapterListView extends BaseAdapter {
    private Activity activity;
    private List<String> horarios;
    private ClickItemListView clickItemListView;

    public AdapterListView(Activity activity, List<String> horarios, ClickItemListView clickItemListView) {
        this.activity = activity;
        this.horarios = horarios;
        this.clickItemListView = clickItemListView;
    }

    @Override
    public int getCount() {
        return horarios.size();
    }

    @Override
    public Object getItem(int position) {
        return horarios.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = activity.getLayoutInflater().inflate(R.layout.lista_horarios, parent, false);

        TextView textListHorarios = view.findViewById(R.id.textListHorarios);
        CardView cardViewListaHorarios = view.findViewById(R.id.cardViewListaHorarios);

        textListHorarios.setText(horarios.get(position));

        cardViewListaHorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickItemListView.clickItem(horarios.get(position), position);

            }
        });
        return view;
    }
    public interface ClickItemListView{
        void clickItem(String horario, int position);
    }
}
