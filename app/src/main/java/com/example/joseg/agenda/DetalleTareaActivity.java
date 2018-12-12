package com.example.joseg.agenda;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.joseg.agenda.Modelo.Lista;
import com.example.joseg.agenda.Modelo.Tarea;

import java.util.Calendar;

import static com.example.joseg.agenda.ListaActivity.listas;
import static com.example.joseg.agenda.ListaActivity.spnListasMain;

public class DetalleTareaActivity extends AppCompatActivity {

    Spinner spnListasTareaDetalle;
    EditText etTituloTareaDetalle;
    EditText etDescripcionTareaDetalle;
    EditText etFechaTareaDetalle;
    Button btnGuardarTareaDetalle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tarea);

         spnListasTareaDetalle = (Spinner) findViewById(R.id.spnListasTareaDetalle);
         etTituloTareaDetalle = (EditText) findViewById(R.id.etTituloTareaDetalle);
         etDescripcionTareaDetalle = (EditText) findViewById(R.id.etDescripcionTareaDetalle);
         etFechaTareaDetalle = (EditText) findViewById(R.id.etFechaTareaDetalle);
         btnGuardarTareaDetalle = (Button) findViewById(R.id.btnGuardarTareaDetalle);

        ArrayAdapter<Lista> spinnerAdapter = new ArrayAdapter<Lista>(this, android.R.layout.simple_list_item_1, listas);
        spnListasTareaDetalle.setAdapter(spinnerAdapter);

        Tarea t = (Tarea)getIntent().getSerializableExtra("tarea");
        if(t!=null){
            etTituloTareaDetalle.setText(t.getTitulo());
            etTituloTareaDetalle.setEnabled(false);
            etDescripcionTareaDetalle.setText(t.getDescripcion());
            etFechaTareaDetalle.setText(t.getFecha());
            spnListasTareaDetalle.setSelection(spnListasMain.getSelectedItemPosition());
        }else{
            etTituloTareaDetalle.setEnabled(true);
            spnListasTareaDetalle.setSelection(spnListasMain.getSelectedItemPosition());
        }


         etFechaTareaDetalle.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 int diaDatePicker=0;
                 int mesDatePicker=0;
                 int anoDatePicker=0;
                 if(etFechaTareaDetalle.getText().toString().isEmpty()){
                     diaDatePicker = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
                     mesDatePicker = Calendar.getInstance().get(Calendar.MONTH);
                     anoDatePicker = Calendar.getInstance().get(Calendar.YEAR);

                 }else{
                     String fechasel = etFechaTareaDetalle.getText().toString();
                     diaDatePicker = Integer.parseInt(fechasel.substring(0,2));
                     mesDatePicker = (Integer.parseInt(fechasel.substring(3,5)))-1;
                     anoDatePicker = Integer.parseInt(fechasel.substring(6,10));
                 }

                 DatePickerDialog datePickerDialog = new DatePickerDialog(DetalleTareaActivity.this, new DatePickerDialog.OnDateSetListener() {
                     public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                         etFechaTareaDetalle.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                     }

                 }, anoDatePicker, mesDatePicker, diaDatePicker);

                 datePickerDialog.show();
             }
         });

         btnGuardarTareaDetalle.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 final String titulo = etTituloTareaDetalle.getText().toString();
                 final String descripcion = etDescripcionTareaDetalle.getText().toString();
                 final String fecha = etFechaTareaDetalle.getText().toString();
                 String lista = spnListasTareaDetalle.getSelectedItem().toString();

                 if(titulo.isEmpty()){
                     etTituloTareaDetalle.setError(getResources().getString(R.string.errorTituloTareaEmpty));
                 }
                 if(descripcion.isEmpty()){
                     etDescripcionTareaDetalle.setError(getResources().getString(R.string.errorDescripcionTareaEmpty));
                 }
                 if(fecha.isEmpty()){
                     etFechaTareaDetalle.setError(getResources().getString(R.string.errorFechaTareaEmpty));
                 }

                 if(!titulo.isEmpty() && !descripcion.isEmpty() && !fecha.isEmpty()){
                     final SharedPreferences pref = getSharedPreferences(lista,MODE_PRIVATE);
                     String existe = pref.getString(titulo,null);
                     if(existe != null){
                         AlertDialog.Builder builder = new AlertDialog.Builder(DetalleTareaActivity.this);
                         builder.setMessage(R.string.msgDialogSobreEscribirTarea)
                                 .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                     public void onClick(DialogInterface dialog, int id) {
                                         // FIRE ZE MISSILES!
                                         SharedPreferences.Editor editPref = pref.edit();
                                         editPref.putString(titulo,fecha+descripcion);
                                         editPref.commit();
                                         finish();
                                     }
                                 })
                                 .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                     public void onClick(DialogInterface dialog, int id) {
                                         // User cancelled the dialog
                                     }
                                 });
                         builder.create().show();
                     }else{
                         SharedPreferences.Editor editPref = pref.edit();
                         editPref.putString(titulo,fecha+descripcion);
                         editPref.commit();
                         finish();
                     }

                 }
             }
         });

    }
}
