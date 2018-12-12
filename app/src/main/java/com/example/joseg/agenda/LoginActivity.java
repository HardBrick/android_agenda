package com.example.joseg.agenda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    TextView tvLoginLogin;
    TextView tvNewUserLogin;
    EditText etUserLogin;
    EditText etPasswordLogin;
    Button btnAcceptLogin;
    Button btnCloseLogin;
    Switch swRecordarLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvNewUserLogin = (TextView) findViewById(R.id.tvNewUserLogin);
        etUserLogin = (EditText) findViewById(R.id.etUserLogin);
        etPasswordLogin = (EditText) findViewById(R.id.etPasswordLogin);
        btnAcceptLogin = (Button) findViewById(R.id.btnAcceptLogin);
        btnCloseLogin = (Button) findViewById(R.id.btnCloseLogin);
        swRecordarLogin = (Switch) findViewById(R.id.swRecordarLogin);

        SharedPreferences pref = getSharedPreferences("RecuerdaUsuario",MODE_PRIVATE);
        String user = pref.getString("Usuario", "");
        String pass = pref.getString("Password","");

        if(user.length()>0){
            swRecordarLogin.setChecked(true);
        }else{
            swRecordarLogin.setChecked(false);
        }

        etUserLogin.setText(user);
        etPasswordLogin.setText(pass);



        btnAcceptLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CODIGO QUE SE EJECUTARA CUANDO SE HAGA CLICK EN EL BOTON INGRESAR.
                String user = etUserLogin.getText().toString();
                String pass = etPasswordLogin.getText().toString();

                if(user.isEmpty()){
                    etUserLogin.setError(getResources().getString(R.string.userLoginIsEmpty));
                }
                if(pass.isEmpty()){
                    etPasswordLogin.setError(getResources().getString(R.string.passLoginIsEmpty));
                }

                if(!user.isEmpty() && !pass.isEmpty()){

                    if(user.equals("admin")){
                        if(pass.equals("admin")){
                            if(swRecordarLogin.isChecked()){
                                SharedPreferences.Editor pref = getSharedPreferences("RecuerdaUsuario",MODE_PRIVATE).edit();
                                pref.putString("Usuario", user);
                                pref.putString("Password", pass);
                                pref.commit();
                            }else{
                                SharedPreferences.Editor pref = getSharedPreferences("RecuerdaUsuario",MODE_PRIVATE).edit();
                                pref.putString("Usuario", "");
                                pref.putString("Password", "");
                                pref.commit();
                            }

                            Intent i = new Intent(LoginActivity.this , ListaActivity.class);
                            i.putExtra("user",user);
                            startActivity(i);
                        }else {
                            etPasswordLogin.setError(getResources().getString(R.string.passLoginIsWrong));
                        }
                    }else{
                        etUserLogin.setError(getResources().getString(R.string.userLoginIsWrong));
                    }
                }
            }
        });

        btnCloseLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CODIGO QUE SE EJECUTARA CUANDO SE HAGA CLICK EN EL BOTON CERRAR.
                finish();
            }
        });




    }
}