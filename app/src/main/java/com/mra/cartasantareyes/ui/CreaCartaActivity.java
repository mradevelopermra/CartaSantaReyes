package com.mra.cartasantareyes.ui;

import static com.mra.cartasantareyes.drawing.ImagenBytes.getBitmapFromView;
import static com.mra.cartasantareyes.drawing.ImagenBytes.getBitmapFromViewDrawView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mra.cartasantareyes.R;
import com.mra.cartasantareyes.drawing.DrawView;
import com.mra.cartasantareyes.drawing.DrawerView;
import com.mra.cartasantareyes.drawing.ImagenBytes;
import com.mra.cartasantareyes.model.Carta_Model;
import com.mra.cartasantareyes.tablas.BaseDaoIAentradasSalidas;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreaCartaActivity extends AppCompatActivity {

    private ProgressBar progressBarLogin;
    TextInputEditText nombre, opcionUno, opcionDos, opcionTres;
    String name, regaloUno, regaloDos, regaloTres;
    LinearLayout formRegistro;
    Button btnGuardar, btnLimpiar, btnColores, btnGuardaDibujo, btnDibujar;
    GestureOverlayView gesturesFirma;
    Context context;
    private DrawerView myDrawView;
    private final static String TAG = "Main";
    List<String>  listaPermisos = new ArrayList<>();
    private RadioButton si, no ;
    private ImageView img_flecha;
    String ayuda = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_carta);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = this;
        init();
        actionControls();
        pedirPermisos();
        //isStoragePermissionGranted();

    }

    public void pedirPermisos() {

        //Lista de permisos que necesitan ser aceptados.
        String[] PERMISSIONS = {Manifest.permission.INTERNET,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};

        //Se revisa si ya se han aceptado todos los permisos.
        if (!hasPermissions(PERMISSIONS)) {
            //En caso de que no se hayan obtenido todos los permisos, se crea una lista con los
            //permisos faltantes.
            PERMISSIONS = new String[listaPermisos.size()];

            for (int i = 0; i < listaPermisos.size(); i++) {
                PERMISSIONS[i] = listaPermisos.get(i);
            }

            //Se pide al usuario que acepte los permisos requeridos.
            ActivityCompat.requestPermissions(this,
                    PERMISSIONS,
                    100);
        } else { //Si ya hay permisos, entonces se intenta iniciar sesión.

        }
    }
    /** MÉTODO QUE REVISA QUÉ PERMISOS FALTAN POR ACEPTAR **/
    public boolean hasPermissions(String... permissions) {

        listaPermisos.clear(); //Lista que guarda los permisos que no se han aceptado.
        boolean permisosObtenidos = true; //Variable que indica si ya se han aceptado todos los permisos.

        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(getBaseContext(), permission) != PackageManager.PERMISSION_GRANTED) {

                    listaPermisos.add(permission); //Guarda el permiso que no ha sido aceptado aún.
                    permisosObtenidos = false; //Indica que existe al menos un permiso no aceptado.
                }
            }
        }

        return permisosObtenidos;
    }

    private void init(){
        progressBarLogin = findViewById(R.id.progressBarLogin);
        nombre = findViewById(R.id.nombre);
        opcionUno = findViewById(R.id.opcionUno);
        opcionDos = findViewById(R.id.opcionDos);
        opcionTres = findViewById(R.id.opcionTres);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnLimpiar = findViewById(R.id.btnLimpiar);
        btnColores = findViewById(R.id.btnColores);
        formRegistro = findViewById(R.id.formRegistro);
        gesturesFirma = findViewById(R.id.gesturesFirma);
        btnDibujar = findViewById(R.id.btnDibujar);
        si=(RadioButton)findViewById(R.id.si);
        no=(RadioButton)findViewById(R.id.no);
        img_flecha = findViewById(R.id.img_flecha);

    }

    private void actionControls(){
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nombre.getText().toString();
                regaloUno = opcionUno.getText().toString();
                regaloDos = opcionDos.getText().toString();
                regaloTres  = opcionTres.getText().toString();
                if(name.equalsIgnoreCase("")) {
                    nombre.setError("Antes de guardar la carta debes agregar un nombre");
                    Toast.makeText(context, "Antes de guardar la carta debes agregar un nombre", Toast.LENGTH_SHORT).show();
                }else if(regaloUno.equalsIgnoreCase("")){
                    opcionUno.setError("Antes de guardar la carta debes agregar una opción de regalo uno");
                    Toast.makeText(context, "Antes de guardar la carta debes agregar una opción de regalo uno", Toast.LENGTH_SHORT).show();
                }else if(regaloDos.equalsIgnoreCase("")){
                    opcionDos.setError("Antes de guardar la carta debes agregar una opción de regalo dos");
                    Toast.makeText(context, "Antes de guardar la carta debes agregar una opción de regalo dos", Toast.LENGTH_SHORT).show();
                }else if(regaloTres.equalsIgnoreCase("")){
                    opcionTres.setError("Antes de guardar la carta debes agregar una opción de regalo tres");
                    Toast.makeText(context, "Antes de guardar la carta debes agregar una opción de regalo tres", Toast.LENGTH_SHORT).show();
                }else if(ayuda.equalsIgnoreCase("")){
                    opcionTres.setError("Antes de guardar la carta debes elegir como te portaste");
                    Toast.makeText(context, "Antes de guardar la carta debes elegir como te portaste", Toast.LENGTH_SHORT).show();
                }else if(myDrawView == null){
                    Toast.makeText(context, "Antes de guardar la carta debes agregar un dibujo", Toast.LENGTH_SHORT).show();
                    ventanaAgregaColores(context);
                }else{
                    guardaCArtaSanta(myDrawView);

                }

            }
        });

        btnDibujar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nombre.getText().toString();
                regaloUno = opcionUno.getText().toString();
                regaloDos = opcionDos.getText().toString();
                regaloTres  = opcionTres.getText().toString();
                if(name.equalsIgnoreCase("")) {
                    nombre.setError("Antes de guardar la carta debes agregar un nombre");
                    Toast.makeText(context, "Antes de guardar la carta debes agregar un nombre", Toast.LENGTH_SHORT).show();
                }else if(regaloUno.equalsIgnoreCase("")){
                    opcionUno.setError("Antes de guardar la carta debes agregar una opción de regalo uno");
                    Toast.makeText(context, "Antes de guardar la carta debes agregar una opción de regalo uno", Toast.LENGTH_SHORT).show();
                }else if(regaloDos.equalsIgnoreCase("")){
                    opcionDos.setError("Antes de guardar la carta debes agregar una opción de regalo dos");
                    Toast.makeText(context, "Antes de guardar la carta debes agregar una opción de regalo dos", Toast.LENGTH_SHORT).show();
                }else if(regaloTres.equalsIgnoreCase("")){
                    opcionTres.setError("Antes de guardar la carta debes agregar una opción de regalo tres");
                    Toast.makeText(context, "Antes de guardar la carta debes agregar una opción de regalo tres", Toast.LENGTH_SHORT).show();
                }else if(ayuda.equalsIgnoreCase("")){
                    opcionTres.setError("Antes de guardar la carta debes elegir como te portaste");
                    Toast.makeText(context, "Antes de guardar la carta debes elegir como te portaste", Toast.LENGTH_SHORT).show();
                }else{
                    ventanaAgregaColores(context);
                }

            }
        });

        si.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    ayuda = "Si";
                    no.setChecked(false);
                }
            }
        });

        no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    ayuda = "No";
                    si.setChecked(false);
                }
            }
        });

        img_flecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent linsertar=new Intent(CreaCartaActivity.this, MenuActivity.class);
                startActivity(linsertar);
            }
        });

    }

    private void changeLoginFormVisibility(boolean showForm ) {
        progressBarLogin.setVisibility(showForm ? View.GONE : View.VISIBLE);
    }

    private android.app.AlertDialog unidadAgregaPopup;
    private ImageView imagenCerrar;
    private Button btnColorRojo, btnColorAmarillo, btnColorMorado,
            btnColorAzul, btnColorNaranja, btnColorVerde,
            btnColorCafe, btnColorGris, btnColorNegro,
            btnColorAzulClaro, btnColorPurple, btnColorRosa,
            btnColorVerdeAgua, btnColorAmarilloCanario, btnColorGrisPerla,
            btnColorRojoFuego, btnColorRosaMexicano, btnColorMoradoEncendido;

    public void ventanaAgregaColores(Context cont) {
        try {

            LayoutInflater inflater = (LayoutInflater) cont
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.dialog_colores,
                    null);

            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(cont);

            alertDialogBuilder.setView(layout);

            myDrawView = layout.findViewById(R.id.draw);

            imagenCerrar = layout.findViewById(R.id.imagenCerrar);

            btnColorRojo = layout.findViewById(R.id.btnColorRojo);
            btnColorAmarillo = layout.findViewById(R.id.btnColorAmarillo);
            btnColorMorado = layout.findViewById(R.id.btnColorMorado);

            btnColorAzul = layout.findViewById(R.id.btnColorAzul);
            btnColorNaranja = layout.findViewById(R.id.btnColorNaranja);
            btnColorVerde = layout.findViewById(R.id.btnColorVerde);

            btnColorCafe = layout.findViewById(R.id.btnColorCafe);
            btnColorGris = layout.findViewById(R.id.btnColorGris);
            btnColorNegro = layout.findViewById(R.id.btnColorNegro);

            btnColorAzulClaro = layout.findViewById(R.id.btnColorAzulClaro);
            btnColorPurple = layout.findViewById(R.id.btnColorPurple);
            btnColorRosa = layout.findViewById(R.id.btnColorRosa);

            btnColorVerdeAgua = layout.findViewById(R.id.btnColorVerdeAgua);
            btnColorAmarilloCanario = layout.findViewById(R.id.btnColorAmarilloCanario);
            btnColorGrisPerla = layout.findViewById(R.id.btnColorGrisPerla);

            btnColorRojoFuego = layout.findViewById(R.id.btnColorRojoFuego);
            btnColorRosaMexicano = layout.findViewById(R.id.btnColorRosaMexicano);
            btnColorMoradoEncendido = layout.findViewById(R.id.btnColorMoradoEncendido);

            btnLimpiar = layout.findViewById(R.id.btnLimpiar);
            btnColores = layout.findViewById(R.id.btnColores);
            btnGuardaDibujo = layout.findViewById(R.id.btnGuardaDibujo);

            imagenCerrar.setOnClickListener(cerrarUnidadAgrega);
           // btnColorRojo.setOnClickListener(cambiarColorRojo);

            btnLimpiar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //gesturesFirma.cancelClearAnimation();
                    //gesturesFirma.clear(true);
                    myDrawView.clear();
                }
            });
            btnColores.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            btnGuardaDibujo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*View view = myDrawView;

                    Bitmap icon = getBitmapFromViewDrawView(view);
                    ImagenBytes.dibujarTextoBitmap(icon, context, name, regaloUno, regaloDos, regaloTres, ayuda);
                    myDrawView.setBackground(new BitmapDrawable(ImagenBytes.dibujarTextoBitmap(icon, context, name, regaloUno, regaloDos, regaloTres,ayuda)));*/

                    Toast.makeText(context, "Se ha guardado el dibujo correctamente", Toast.LENGTH_SHORT).show();
                    if(unidadAgregaPopup.isShowing())
                        unidadAgregaPopup.dismiss();
                }
            });

             btnColorRojo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("rojo");
                }
            });

            btnColorAmarillo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("amarillo");
                }
            });

            btnColorMorado.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("morado");
                }
            });

            btnColorAzul.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("azul");
                }
            });

            btnColorNaranja.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("naranja");
                }
            });

            btnColorVerde.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("verde");
                }
            });

            btnColorCafe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("cafe");
                }
            });

            btnColorGris.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("gris");
                }
            });

            btnColorNegro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("negro");
                }
            });

            btnColorAzulClaro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("azulClaro");
                }
            });

            btnColorPurple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("purple");
                }
            });

            btnColorRosa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("rosa");
                }
            });

            btnColorVerdeAgua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("verdeAgua");
                }
            });

            btnColorAmarilloCanario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("amarilloCanario");
                }
            });

            btnColorGrisPerla.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("grisPerla");
                }
            });

            btnColorRojoFuego.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("rojoFuego");
                }
            });

            btnColorRosaMexicano.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("rosaMexicano");
                }
            });

            btnColorMoradoEncendido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambiarColorGesto("moradoEncendido");
                }
            });


            unidadAgregaPopup = alertDialogBuilder.show();
            unidadAgregaPopup.setCanceledOnTouchOutside(false);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public View.OnClickListener cerrarUnidadAgrega = new View.OnClickListener() {
        public void onClick(View v) {

            if(unidadAgregaPopup.isShowing())
                unidadAgregaPopup.dismiss();

        }
    };

    public View.OnClickListener cambiarColorRojo = new View.OnClickListener() {
        public void onClick(View v) {

            Log.e("pasa por", "cambiarColorRojo");
            myDrawView.setPathColor(R.color.rojo);
            /*gesturesFirma.setUncertainGestureColor(getResources().getColor(R.color.rojo));
            gesturesFirma.setGestureColor(Color.BLUE);
            if(unidadAgregaPopup.isShowing())
                unidadAgregaPopup.dismiss();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                gesturesFirma.setGestureColor(getColor(R.color.rojo));
                btnColorRojo.setCompoundDrawables(getDrawable(R.drawable.ic_baseline_check_24), null,null,null);
            }*/

        }
    };

    private void cambiarColorGesto(String colorChoosen){
        Log.e("pasa por", "cambiarColorGesto");
        Log.e("pasa por", "cambiarColorRojo");
        int color = ContextCompat.getColor(context, R.color.black);
        if(colorChoosen.equalsIgnoreCase("rojo")){
            color = ContextCompat.getColor(context, R.color.rojo);
        }else if(colorChoosen.equalsIgnoreCase("amarillo")){
            color = ContextCompat.getColor(context, R.color.amarillo);
        }else if(colorChoosen.equalsIgnoreCase("morado")){
            color = ContextCompat.getColor(context, R.color.morado);
        }else if(colorChoosen.equalsIgnoreCase("azul")){
            color = ContextCompat.getColor(context, R.color.azul);
        }else if(colorChoosen.equalsIgnoreCase("naranja")){
            color = ContextCompat.getColor(context, R.color.naranja);
        }else if(colorChoosen.equalsIgnoreCase("verde")){
            color = ContextCompat.getColor(context, R.color.verde);
        }else if(colorChoosen.equalsIgnoreCase("cafe")){
            color = ContextCompat.getColor(context, R.color.cafe);
        }else if(colorChoosen.equalsIgnoreCase("gris")){
            color = ContextCompat.getColor(context, R.color.gris);
        }else if(colorChoosen.equalsIgnoreCase("negro")){
            color = ContextCompat.getColor(context, R.color.black);
        }else if(colorChoosen.equalsIgnoreCase("azulClaro")){
            color = ContextCompat.getColor(context, R.color.azulclaro);
        }else if(colorChoosen.equalsIgnoreCase("purple")){
            color = ContextCompat.getColor(context, R.color.purple);
        }else if(colorChoosen.equalsIgnoreCase("rosa")){
            color = ContextCompat.getColor(context, R.color.rosa);
        }else if(colorChoosen.equalsIgnoreCase("verdeAgua")){
            color = ContextCompat.getColor(context, R.color.verdeagua);
        }else if(colorChoosen.equalsIgnoreCase("amarilloCanario")){
            color = ContextCompat.getColor(context, R.color.amarillocanario);
        }else if(colorChoosen.equalsIgnoreCase("grisPerla")){
            color = ContextCompat.getColor(context, R.color.grisperla);
        }else if(colorChoosen.equalsIgnoreCase("rojoFuego")){
            color = ContextCompat.getColor(context, R.color.rojofuego);
        }else if(colorChoosen.equalsIgnoreCase("rosaMexicano")){
            color = ContextCompat.getColor(context, R.color.rojomexicano);
        }else if(colorChoosen.equalsIgnoreCase("moradoEncendido")){
            color = ContextCompat.getColor(context, R.color.moradoencendido);
        }

        myDrawView.setPathColor(color);

        /*if(unidadAgregaPopup.isShowing())
            unidadAgregaPopup.dismiss();*/

    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    private void comprtirCarta(View view){

        Bitmap icon2 = getBitmapFromView(view);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon2.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");


        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String direccion = "file:///sdcard/temporary_file.jpg";
        Uri almacen = Uri.parse(direccion);
        String direccionFinal = almacen+"";
        ArrayList<Uri> tmpList = new ArrayList<>();
        tmpList.add(Uri.parse(direccion));

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Uri uri = Uri.fromFile(f);

        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        /*share.putExtra(Intent.EXTRA_SUBJECT,
                "Sharing File...");
        share.putExtra(Intent.EXTRA_TEXT, "Sharing File...");*/
        share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(Intent.createChooser(share, "Share_Image"));
    }


    private void guardaCArtaSanta(View view){

        Bitmap icon = getBitmapFromViewDrawView(view);
        ImagenBytes.dibujarTextoBitmap(icon, context, name, regaloUno, regaloDos, regaloTres, ayuda);
        myDrawView.setBackground(new BitmapDrawable(ImagenBytes.dibujarTextoBitmap(icon, context, name, regaloUno, regaloDos, regaloTres,ayuda)));


        BaseDaoIAentradasSalidas con = new BaseDaoIAentradasSalidas(this);
        String fechaIngreso = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault()).format(new Date());
        Carta_Model carta = new Carta_Model();
        //Se anexa al momento de guardarlo
        carta.setEdad(0);
        if(ayuda.equalsIgnoreCase("si")){
            carta.setSePorto(1);
        }else{
            carta.setSePorto(2);
        }
        carta.setFechaCreacion(fechaIngreso);
        carta.setNombreNino(nombre.getText().toString());
        carta.setOpcionUno(regaloUno);
        carta.setOpcionDos(regaloDos);
        carta.setOpcionTres(regaloTres);
        carta.setNombreFirma(ImagenBytes.convertirViewArrayBytes(view));

        List firmasUltimaEntradaSalida = con.consultaListaFirmas(1);

        if(firmasUltimaEntradaSalida.size()<=0){
            if(con.insertaCarta(carta)){
                Toast.makeText(this, "Se ha guardado el dibujo correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No se ha guardado el dibujo correctamente", Toast.LENGTH_SHORT).show();
            }
        }else{
            carta.setIdCarta(1);
            if(con.actualizaFirmaEntradaSalida(carta)){
                Toast.makeText(this, "Se ha actualizado el dibujo correctamente", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No se ha actualizado el dibujo correctamente", Toast.LENGTH_SHORT).show();
            }
        }

        comprtirCarta(myDrawView);
    }

}