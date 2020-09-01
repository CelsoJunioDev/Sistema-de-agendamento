package com.exemple.testedeagendamento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.exemple.testedeagendamento.adapter.AdapterListView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HorariosActivity extends AppCompatActivity implements AdapterListView.ClickItemListView {
    private ListView listViewHorarios;
    private AdapterListView adapterListView;
    private List<String> horarios = new ArrayList<String>();
    private ArrayList<String> dataSelecionada = new ArrayList<String>();

    private FirebaseDatabase database;
    private DatabaseReference reference;

    private ChildEventListener childEventListenerHorariosAgendamento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        //RECEBE as informações do CalendarioFragment
        dataSelecionada = getIntent().getStringArrayListExtra("chaveData");
        //Toast.makeText(this, "Dia: "+ dataSelecionada.get(0)+"\nMês: "+ dataSelecionada.get(1)+"\nAno: "+ dataSelecionada.get(2), Toast.LENGTH_LONG).show();

        listViewHorarios = findViewById(R.id.listViewHorarios);
        adapterListView = new AdapterListView(this,horarios, this);
        listViewHorarios.setAdapter(adapterListView);
        carregarHorariosAgendamento();


    }
    private void buscarHorariosReservados() {
        //pega os dados que o usuario selecionou no calendario
        String dia = dataSelecionada.get(0);
        String mes = dataSelecionada.get(1);
        String ano = dataSelecionada.get(2);
        reference = database.getReference().child("Agendamento").child("HorariosReservados")
                .child(ano).child(mes).child(dia);

        if (childEventListenerHorariosAgendamento == null){
            childEventListenerHorariosAgendamento = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    String chave = snapshot.getKey(); //recupera a chave principal (nome da pasta) de cada nó da referencia
                    // pasta "8:00", pasta "9:00" estão no BD.
                    int index = horarios.indexOf(chave); //recupera a posição que o item está no BD
                    //horarios.get(0) = 08:00 horarios.get(1) = 09:00....
                    //"index": valor inicio, após a virgula: valor para trcar
                    horarios.set(index,chave + " - Reservado");// na posição "index", vai trocar pelo valor "08:00 - REVERVADO"

                    adapterListView.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) { //REMOVER A RESERVA AUTOMATICAMENTE

                    String chave = snapshot.getKey(); //recupera a chave principal (nome da pasta) de cada nó da referencia
                    // pasta "8:00", pasta "9:00" estão no BD.

                    int index = horarios.indexOf(chave + " - Reservado"); //procura na lista onde tem esse valor
                    horarios.set(index,chave);// na posição "index", vai trocar pelo valor "08:00" sem reservado

                    adapterListView.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            };
            reference.addChildEventListener(childEventListenerHorariosAgendamento);
        }
    }

    private void carregarHorariosAgendamento() {
        database =  FirebaseDatabase.getInstance();
        reference = database.getReference().child("Agendamento").child("HorariosAgendamento");
        reference.addListenerForSingleValueEvent(new ValueEventListener() { //SingleValueEvent não atualiza em tempo real
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    for (DataSnapshot snapshott: snapshot.getChildren()){
                        String horario = snapshott.getValue(String.class);
                        horarios.add(horario);

                    }
                    adapterListView.notifyDataSetChanged();
                    buscarHorariosReservados();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void clickItem(String horario, int position) {
        Toast.makeText(getApplicationContext(), horario, Toast.LENGTH_LONG).show();
    }
}