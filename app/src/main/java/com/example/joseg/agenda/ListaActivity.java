package com.example.joseg.agenda;

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
import android.widget.ListView;
import android.widget.Spinner;


import com.example.joseg.agenda.Modelo.Lista;
import com.example.joseg.agenda.Modelo.Tarea;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListaActivity extends AppCompatActivity {

    static ListView lvTareasListas;
    FloatingActionButton btnFloatAgregarTareaListas;
    static Spinner spnListasMain;

    public static List<Lista> listas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        lvTareasListas = (ListView) findViewById(R.id.lvTareasListas);
        btnFloatAgregarTareaListas = (FloatingActionButton) findViewById(R.id.btnFloatAgregarTareaListas);
        spnListasMain = (Spinner) findViewById(R.id.spnListasListas);

        listas.add(obtenerListaDesdePreferencias("Personal"));
        listas.add(obtenerListaDesdePreferencias("Trabajo"));
        ArrayAdapter<Lista> spinnerAdapter = new ArrayAdapter<Lista>(this, android.R.layout.simple_list_item_1, listas);
        spnListasMain.setAdapter(spinnerAdapter);
        spnListasMain.setSelection(1);


        spnListasMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("TAG_","Lista " + spnListasMain.getItemAtPosition(i).toString()+" seleccionada.");
                Lista lista = obtenerListaDesdePreferencias(spnListasMain.getSelectedItem().toString());
                ArrayAdapter<Tarea> adapter = new ArrayAdapter<Tarea>(getBaseContext(), android.R.layout.simple_list_item_1, lista.getTareas() ) ;
                lvTareasListas.setAdapter(adapter);
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
                return false;
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
        SharedPreferences pref = getSharedPreferences(nombrelista,MODE_PRIVATE);
        Lista lista = new Lista(nombrelista, new ArrayList<Tarea>());
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
        return lista;
    }
}
