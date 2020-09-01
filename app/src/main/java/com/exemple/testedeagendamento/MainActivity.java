package com.exemple.testedeagendamento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private CalendarView calendarioAgendamento;
    private int diaAtual;
    private int mesAtual;
    private int anoAtual;

    private TextView textDataAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarioAgendamento = findViewById(R.id.calendarioAgendamento);
        textDataAtual = findViewById(R.id.textDataAtual);

        obterDataAtual();
        configurarCalendario();
        calendarioAgendamento.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

                int mesAtualMaisUm = month+1;

                dataSelecionada(dayOfMonth, mesAtualMaisUm, year);
            }
        });
    }

    private void dataSelecionada(int dayOfMonth, int mesAtualMaisUm, int year) {

        Locale locale = new Locale("pt", "BR"); //Configura as datas para o BR.
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy", locale);

        Calendar data = Calendar.getInstance();
        try {
            data.setTime(simpleDateFormat.parse(dayOfMonth+"/"+mesAtualMaisUm+"/"+year));
            boolean disponivelAgendamento;
            if (mesAtualMaisUm != mesAtual){
                disponivelAgendamento = true;
            }else { disponivelAgendamento = disponibilidadeAgenda(data, dayOfMonth);}

            if (disponivelAgendamento){
                    Toast.makeText(getApplicationContext(), "AGENDAMENTO LIBERADO!", Toast.LENGTH_SHORT).show();
                    //PEGA A DATA SELECIONADA E CONVERTE PARA STRING
                    String dia = String.valueOf(dayOfMonth);
                    String mes = String.valueOf(mesAtualMaisUm);
                    String ano = String.valueOf(year);
                    //CRIA UM ARRAYLIST DE STRINGS
                    ArrayList<String> dataList= new ArrayList<String>();
                    //configura a lista com as datas coletadas
                    dataList.add(dia); //posição 0 do array
                    dataList.add(mes); //posição 1 do array
                    dataList.add(ano); //posição 2 do array

                    Intent horariosActivity = new Intent(getApplicationContext(), HorariosActivity.class);
                    //PASSA A LISTA PARA A TELAHORARIOS
                horariosActivity.putExtra("chaveData", dataList);
                startActivity(horariosActivity);

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private boolean disponibilidadeAgenda(Calendar data, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        int diaFinal = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        if (diaFinal == diaAtual){ //Se estiver no ultimo dia do mês
            Toast.makeText(getApplicationContext(), "Agendamento disponível à partir do dia 1º", Toast.LENGTH_SHORT).show();
            return false;
        } else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            Toast.makeText(getApplicationContext(), "Desculpe, não funcionamos aos Domingos", Toast.LENGTH_SHORT).show();
            return false;
        }else if (dayOfMonth <= diaAtual && diaFinal != diaAtual ){
            Toast.makeText(getApplicationContext(), "Agendamento disponível do dia "+ (diaAtual+1)+ " para frente", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true; // RETORNA TRUE se a data for disponível
        }

    }

    private void configurarCalendario() {
        Calendar dataMinima = Calendar.getInstance();
        Calendar dataMaxima = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();

        int diaInicio = 1;
        int diaFinal = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        dataMinima.set(anoAtual, mesAtual-1,diaInicio);
        if (diaAtual == diaFinal){ //se o dia atual for o ultimo dia do mês
            dataMaxima.set(anoAtual, mesAtual,15); //EXIBE o calendario do proximo mês, até o dia 15
        }else {
            dataMaxima.set(anoAtual, mesAtual - 1, diaFinal);
        }
        //Settar a data minima e maxima para agendamento
        calendarioAgendamento.setMinDate(dataMinima.getTimeInMillis());
        calendarioAgendamento.setMaxDate(dataMaxima.getTimeInMillis());
    }

    private void obterDataAtual() {
        long dataLong = calendarioAgendamento.getDate();
        Locale locale = new Locale("pt", "BR");
        SimpleDateFormat dia = new SimpleDateFormat("dd", locale);
        SimpleDateFormat mes = new SimpleDateFormat("MM", locale);
        SimpleDateFormat ano = new SimpleDateFormat("yyyy", locale);

        diaAtual = Integer.parseInt(dia.format(dataLong));
        mesAtual = Integer.parseInt(mes.format(dataLong));
        anoAtual = Integer.parseInt(ano.format(dataLong));

        textDataAtual.setText("Hoje é: "+diaAtual+"/"+mesAtual+"/"+anoAtual);

    }

}