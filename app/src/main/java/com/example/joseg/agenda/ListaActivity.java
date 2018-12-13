package com.example.joseg.agenda;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.joseg.agenda.Modelo.Lista;
import com.example.joseg.agenda.Modelo.Tarea;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ListaActivity extends AppCompatActivity {

    static ListView lvTareasListas;
    FloatingActionButton btnFloatAgregarTareaListas;
    static Spinner spnListasMain;
    EditText etFechaDesde;
    EditText etFechaHasta;
    Button btnBuscarPorFecha;
    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/mm/yyyy");

    public static List<Lista> listas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        lvTareasListas = (ListView) findViewById(R.id.lvTareasListas);
        btnFloatAgregarTareaListas = (FloatingActionButton) findViewById(R.id.btnFloatAgregarTareaListas);
        spnListasMain = (Spinner) findViewById(R.id.spnListasListas);
        etFechaDesde = (EditText) findViewById(R.id.etFechaDesde);
        etFechaHasta = (EditText) findViewById(R.id.etFechaHasta);
        btnBuscarPorFecha = (Button) findViewById(R.id.btnBuscarPorFecha);

        if(listas.size()<3){
            listas.add(obtenerListaDesdePreferencias("Personal"));
            listas.add(obtenerListaDesdePreferencias("Trabajo"));
            listas.add(new Lista("Todo", new ArrayList<Tarea>()));
            ArrayAdapter<Lista> spinnerAdapter = new ArrayAdapter<Lista>(this, android.R.layout.simple_list_item_1, listas);
            spnListasMain.setAdapter(spinnerAdapter);
            spnListasMain.setSelection(1);
        }

        spnListasMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /*Log.d("TAG_","Lista " + spnListasMain.getItemAtPosition(i).toString()+" seleccionada.");
                Lista lista = obtenerListaDesdePreferencias(spnListasMain.getSelectedItem().toString());
                ArrayAdapter<Tarea> adapter = new ArrayAdapter<Tarea>(getBaseContext(), android.R.layout.simple_list_item_1, lista.getTareas() ) ;
                lvTareasListas.setAdapter(adapter);*/
                btnBuscarPorFecha.callOnClick();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnFloatAgregarTareaListas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNuevaTarea = new Intent (v.getContext(), DetalleTareaActivity.class);
                startActivity(intentNuevaTarea);
            }
        });

        lvTareasListas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getBaseContext(),DetalleTareaActivity.class);
                i.putExtra("tarea", ((Tarea)(parent.getItemAtPosition(position))));
                startActivity(i);
            }



        });

        lvTareasListas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final Tarea t = (Tarea)lvTareasListas.getItemAtPosition(i);

                AlertDialog.Builder builder = new AlertDialog.Builder(ListaActivity.this);
                builder.setMessage(R.string.msgDialogEliminarTarea)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // FIRE ZE MISSILES!

                                String stringPref = spnListasMain.getSelectedItem().toString();
                                SharedPreferences pref = getSharedPreferences(stringPref,MODE_PRIVATE);
                                SharedPreferences.Editor editPref = pref.edit();
                                editPref.remove(t.getTitulo());
                                editPref.commit();
                                Lista lista = obtenerListaDesdePreferencias(spnListasMain.getSelectedItem().toString());
                                ArrayAdapter<Tarea> adapter = new ArrayAdapter<Tarea>(getBaseContext(), android.R.layout.simple_list_item_1, lista.getTareas() ) ;
                                lvTareasListas.setAdapter(adapter);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                builder.create().show();


                // Toast.makeText(view.getContext(),getResources().getString(R.string.TareaEliminada)+" "+tareaeliminada , Toast.LENGTH_LONG ).show();
                return true;
            }
        });

        etFechaDesde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int diaDatePicker=0;
                int mesDatePicker=0;
                int anoDatePicker=0;
                if(etFechaDesde.getText().toString().isEmpty()){
                    diaDatePicker = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    mesDatePicker = Calendar.getInstance().get(Calendar.MONTH);
                    anoDatePicker = Calendar.getInstance().get(Calendar.YEAR);
                }else{
                    String fechasel = etFechaDesde.getText().toString();
                    diaDatePicker = Integer.parseInt(fechasel.substring(0,2));
                    mesDatePicker = (Integer.parseInt(fechasel.substring(3,5)))-1;
                    anoDatePicker = Integer.parseInt(fechasel.substring(6,10));
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(ListaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String diaDelMes = dayOfMonth+"";
                        if((dayOfMonth+"").length()==1){
                            diaDelMes = "0"+dayOfMonth;
                        }
                        String fechaHasta = etFechaHasta.getText().toString();
                        String fechaDesde = diaDelMes+"/"+monthOfYear+"/"+year;

                        if(!fechaHasta.isEmpty()){
                            int diaDatePicker = Integer.parseInt(fechaHasta.substring(0,2));
                            int mesDatePicker = (Integer.parseInt(fechaHasta.substring(3,5)))-1;
                            int anoDatePicker = Integer.parseInt(fechaHasta.substring(6,10));
                            fechaHasta = diaDatePicker+"/"+mesDatePicker+"/"+anoDatePicker;
                            try{
                                Date dateDesde = formatoFecha.parse(fechaDesde);
                                Date dateHasta = formatoFecha.parse(fechaHasta);
                                if(dateDesde.compareTo(dateHasta)>0){
                                    Toast.makeText(ListaActivity.this,getResources().getString(R.string.msgFechaDesdeMayorQueFechaHasta),Toast.LENGTH_LONG).show();
                                }else{
                                    etFechaDesde.setText(diaDelMes+"/"+(monthOfYear+1)+"/"+year);
                                }
                            }catch (Exception e){
                            }
                        }else{
                            etFechaDesde.setText(diaDelMes+"/"+(monthOfYear+1)+"/"+year);
                        }
                    }
                }, anoDatePicker, mesDatePicker, diaDatePicker);
                datePickerDialog.show();
            }
        });

        etFechaHasta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int diaDatePicker=0;
                int mesDatePicker=0;
                int anoDatePicker=0;
                if(etFechaHasta.getText().toString().isEmpty()){
                    diaDatePicker = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                    mesDatePicker = Calendar.getInstance().get(Calendar.MONTH);
                    anoDatePicker = Calendar.getInstance().get(Calendar.YEAR);
                }else{
                    String fechasel = etFechaHasta.getText().toString();
                    diaDatePicker = Integer.parseInt(fechasel.substring(0,2));
                    mesDatePicker = (Integer.parseInt(fechasel.substring(3,5)))-1;
                    anoDatePicker = Integer.parseInt(fechasel.substring(6,10));
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(ListaActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String diaDelMes = dayOfMonth+"";
                        if((dayOfMonth+"").length()==1){
                            diaDelMes = "0"+dayOfMonth;
                        }

                        String fechaDesde = etFechaDesde.getText().toString();
                        String fechaHasta = diaDelMes+"/"+monthOfYear+"/"+year;

                        if(!fechaDesde.isEmpty()){
                            int diaDatePicker = Integer.parseInt(fechaDesde.substring(0,2));
                            int mesDatePicker = (Integer.parseInt(fechaDesde.substring(3,5)))-1;
                            int anoDatePicker = Integer.parseInt(fechaDesde.substring(6,10));
                            fechaDesde = diaDatePicker+"/"+mesDatePicker+"/"+anoDatePicker;
                            try{
                                Date dateDesde = formatoFecha.parse(fechaDesde);
                                Date dateHasta = formatoFecha.parse(fechaHasta);
                                if(dateHasta.compareTo(dateDesde)<0){
                                    Toast.makeText(ListaActivity.this,getResources().getString(R.string.msgFechaHastaMenorQueFechaDesde),Toast.LENGTH_LONG).show();
                                }else{
                                    etFechaHasta.setText(diaDelMes+"/"+(monthOfYear+1)+"/"+year);
                                }
                            }catch (Exception e){
                            }
                        }else{
                            etFechaHasta.setText(diaDelMes+"/"+(monthOfYear+1)+"/"+year);
                        }
                    }
                }, anoDatePicker, mesDatePicker, diaDatePicker);
                datePickerDialog.show();
            }
        });

        btnBuscarPorFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fechaDesde = etFechaDesde.getText().toString();
                String fechaHasta = etFechaHasta.getText().toString();
                /*if(fechaDesde.isEmpty()){
                    etFechaDesde.setError("");
                }
                if(fechaHasta.isEmpty()){
                    etFechaHasta.setError("");
                }*/

                if(!fechaDesde.isEmpty() && !fechaHasta.isEmpty()){

                    int diaDatePicker = Integer.parseInt(fechaDesde.substring(0,2));
                    int mesDatePicker = (Integer.parseInt(fechaDesde.substring(3,5)))-1;
                    int anoDatePicker = Integer.parseInt(fechaDesde.substring(6,10));
                    fechaDesde = diaDatePicker+"/"+mesDatePicker+"/"+anoDatePicker;

                    diaDatePicker = Integer.parseInt(fechaHasta.substring(0,2));
                    mesDatePicker = (Integer.parseInt(fechaHasta.substring(3,5)))-1;
                    anoDatePicker = Integer.parseInt(fechaHasta.substring(6,10));
                    fechaHasta = diaDatePicker+"/"+mesDatePicker+"/"+anoDatePicker;

                    try{
                        Date dateDesde = formatoFecha.parse(fechaDesde);
                        Date dateHasta = formatoFecha.parse(fechaHasta);
                        String lista = spnListasMain.getSelectedItem().toString();
                        ArrayAdapter<Tarea> adapter = new ArrayAdapter<Tarea>(getBaseContext(), android.R.layout.simple_list_item_1, obtenerListaPorFechas(lista,dateDesde,dateHasta) ) ;
                        lvTareasListas.setAdapter(adapter);
                    }catch (Exception e){

                    }


                }else if (!fechaDesde.isEmpty() && fechaHasta.isEmpty()){

                    int diaDatePicker = Integer.parseInt(fechaDesde.substring(0,2));
                    int mesDatePicker = (Integer.parseInt(fechaDesde.substring(3,5)))-1;
                    int anoDatePicker = Integer.parseInt(fechaDesde.substring(6,10));
                    fechaDesde = diaDatePicker+"/"+mesDatePicker+"/"+anoDatePicker;

                    try{
                        Date dateDesde = formatoFecha.parse(fechaDesde);
                        String lista = spnListasMain.getSelectedItem().toString();
                        ArrayAdapter<Tarea> adapter = new ArrayAdapter<Tarea>(getBaseContext(), android.R.layout.simple_list_item_1,obtenerListaDesdeFecha(lista,dateDesde)) ;
                        lvTareasListas.setAdapter(adapter);
                    }catch (Exception e){
                    }
                }else if(fechaDesde.isEmpty() && !fechaHasta.isEmpty()){
                    int diaDatePicker = Integer.parseInt(fechaDesde.substring(0,2));
                    int mesDatePicker = (Integer.parseInt(fechaDesde.substring(3,5)))-1;
                    int anoDatePicker = Integer.parseInt(fechaDesde.substring(6,10));
                    fechaHasta = diaDatePicker+"/"+mesDatePicker+"/"+anoDatePicker;

                    try{
                        Date dateHasta = formatoFecha.parse(fechaHasta);
                        String lista = spnListasMain.getSelectedItem().toString();
                        ArrayAdapter<Tarea> adapter = new ArrayAdapter<Tarea>(getBaseContext(), android.R.layout.simple_list_item_1,obtenerListaHastaFecha(lista,dateHasta)) ;
                        lvTareasListas.setAdapter(adapter);
                    }catch (Exception e){
                    }
                }else if(fechaDesde.isEmpty() && fechaHasta.isEmpty()){
                    String lista = spnListasMain.getSelectedItem().toString();
                    ArrayAdapter<Tarea> adapter = new ArrayAdapter<Tarea>(getBaseContext(), android.R.layout.simple_list_item_1,obtenerListaDesdePreferencias(lista).getTareas()) ;
                    lvTareasListas.setAdapter(adapter);
                }


            }
        });

    }



    @Override
    protected void onResume() {
        super.onResume();

        Lista lista = obtenerListaDesdePreferencias(spnListasMain.getSelectedItem().toString());
        ArrayAdapter<Tarea> adapter = new ArrayAdapter<Tarea>(getBaseContext(), android.R.layout.simple_list_item_1, lista.getTareas() ) ;
        lvTareasListas.setAdapter(adapter);
    }

    public Lista obtenerListaDesdePreferencias(String nombrelista){
        Lista lista = new Lista(nombrelista, new ArrayList<Tarea>());
        if(nombrelista.equals("Todo")){
            SharedPreferences pref = getSharedPreferences("Trabajo",MODE_PRIVATE);
            if(!(pref.getAll().isEmpty())){
                for (Map.Entry<String,?> entry : pref.getAll().entrySet()) {
                    Log.d("TAG_","clave=" + entry.getKey() + ", valor=" + entry.getValue());
                    String titulo = entry.getKey();
                    String desc = entry.getValue().toString().substring(10,entry.getValue().toString().length());
                    String fecha = entry.getValue().toString().substring(0,10);

                    Log.d("TAG_fecha",fecha + "-" + desc);

                    Tarea t = new Tarea(titulo,desc,fecha,"Trabajo");
                    lista.getTareas().add(t);

                }
            }
            SharedPreferences pref2 = getSharedPreferences("Personal",MODE_PRIVATE);
            if(!(pref2.getAll().isEmpty())){
                for (Map.Entry<String,?> entry : pref2.getAll().entrySet()) {
                    Log.d("TAG_","clave=" + entry.getKey() + ", valor=" + entry.getValue());
                    String titulo = entry.getKey();
                    String desc = entry.getValue().toString().substring(10,entry.getValue().toString().length());
                    String fecha = entry.getValue().toString().substring(0,10);

                    Log.d("TAG_fecha",fecha + "-" + desc);

                    Tarea t = new Tarea(titulo,desc,fecha,"Personal");
                    lista.getTareas().add(t);

                }
            }
        }else{
            SharedPreferences pref = getSharedPreferences(nombrelista,MODE_PRIVATE);
            if(!(pref.getAll().isEmpty())){
                for (Map.Entry<String,?> entry : pref.getAll().entrySet()) {
                    Log.d("TAG_","clave=" + entry.getKey() + ", valor=" + entry.getValue());
                    String titulo = entry.getKey();
                    String desc = entry.getValue().toString().substring(10,entry.getValue().toString().length());
                    String fecha = entry.getValue().toString().substring(0,10);

                    Log.d("TAG_fecha",fecha + "-" + desc);

                    Tarea t = new Tarea(titulo,desc,fecha,nombrelista);
                    lista.getTareas().add(t);

                }
            }
        }
        return lista;
    }

    public List<Tarea> obtenerListaPorFechas(String lista, Date desde, Date hasta){

        List<Tarea> tareas = new ArrayList<>();
        for (Tarea tarea : obtenerListaDesdePreferencias(lista).getTareas()
             ) {
            String fecha = tarea.getFecha();
            int diaDatePicker = Integer.parseInt(fecha.substring(0,2));
            int mesDatePicker = (Integer.parseInt(fecha.substring(3,5)))-1;
            int anoDatePicker = Integer.parseInt(fecha.substring(6,10));
            fecha = diaDatePicker+"/"+mesDatePicker+"/"+anoDatePicker;
            try{
                Date dateTarea = formatoFecha.parse(fecha);
                if(dateTarea.compareTo(desde)>=0 && dateTarea.compareTo(hasta)<=0 ){
                    tareas.add(tarea);
                }
            }catch (Exception e){

            }
        }
        return tareas;
    }

    public List<Tarea> obtenerListaDesdeFecha(String lista, Date desde){

        List<Tarea> tareas = new ArrayList<>();
        for (Tarea tarea : obtenerListaDesdePreferencias(lista).getTareas()
                ) {
            String fecha = tarea.getFecha();
            int diaDatePicker = Integer.parseInt(fecha.substring(0,2));
            int mesDatePicker = (Integer.parseInt(fecha.substring(3,5)))-1;
            int anoDatePicker = Integer.parseInt(fecha.substring(6,10));
            fecha = diaDatePicker+"/"+mesDatePicker+"/"+anoDatePicker;
            try{
                Date dateTarea = formatoFecha.parse(fecha);
                if(dateTarea.compareTo(desde)>=0){
                    tareas.add(tarea);
                }
            }catch (Exception e){

            }
        }
        return tareas;
    }

    public List<Tarea> obtenerListaHastaFecha(String lista, Date hasta){

        List<Tarea> tareas = new ArrayList<>();
        for (Tarea tarea : obtenerListaDesdePreferencias(lista).getTareas()
                ) {
            String fecha = tarea.getFecha();
            int diaDatePicker = Integer.parseInt(fecha.substring(0,2));
            int mesDatePicker = (Integer.parseInt(fecha.substring(3,5)))-1;
            int anoDatePicker = Integer.parseInt(fecha.substring(6,10));
            fecha = diaDatePicker+"/"+mesDatePicker+"/"+anoDatePicker;
            try{
                Date dateTarea = formatoFecha.parse(fecha);
                if(dateTarea.compareTo(hasta)<=0 ){
                    tareas.add(tarea);
                }
            }catch (Exception e){

            }
        }
        return tareas;
    }
}
