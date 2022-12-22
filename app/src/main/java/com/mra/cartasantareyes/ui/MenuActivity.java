package com.mra.cartasantareyes.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mra.cartasantareyes.R;
import com.mra.cartasantareyes.tablas.BaseDaoIAentradasSalidas;

import java.util.List;

public class MenuActivity extends AppCompatActivity {

    private CardView cardViewNuevo, cardViewConsultaReporte, cardViewSincronizacion, buttonExportar;
    BaseDaoIAentradasSalidas con = new BaseDaoIAentradasSalidas(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        init();
        actionControls();
    }

    private void init(){

        cardViewNuevo  = findViewById(R.id.buttonNuevaActa);
        cardViewConsultaReporte  = findViewById(R.id.buttonListaPagares);
        cardViewSincronizacion  = findViewById(R.id.buttonSincronizacion);
        buttonExportar = findViewById(R.id.buttonExportar);

    }

    private void actionControls(){

        cardViewNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                horoscopo();
            }
        });


        cardViewConsultaReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List firmasUltimaEntradaSalida = con.consultaListaFirmas(1);

                if(firmasUltimaEntradaSalida.size()>0){
                    lecturaCArtas();
                }else{
                    Toast.makeText(MenuActivity.this, "No tienes alguna carta registrada", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cardViewSincronizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                integrantes();


            }
        });

        buttonExportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }

    private void horoscopo(){

        Bundle bundle = new Bundle();
        startActivity(new Intent(MenuActivity.this, CreaCartaActivity.class).putExtras(bundle));
    }

    private void lecturaCArtas(){

        Bundle bundle = new Bundle();

         startActivity(new Intent(MenuActivity.this, ActualizaCarta.class).putExtras(bundle));
    }

    private void integrantes(){

        Bundle bundle = new Bundle();

        //startActivity(new Intent(MenuActivity.this, IntegrantesRegalosActivity.class).putExtras(bundle));
    }

    private void login(){

        Bundle bundle = new Bundle();
        //startActivity(new Intent(MenuActivity.this, LoginActivity.class).putExtras(bundle));
    }
}