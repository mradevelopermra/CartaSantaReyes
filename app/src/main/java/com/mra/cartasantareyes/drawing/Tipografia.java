package com.mra.cartasantareyes.drawing;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

public class Tipografia {

    private AssetManager assetsManager;
    private Typeface tipoFuente;
    private String rutaFuente;
    public enum font_style  { Roboto,RobotoBold,QuicksandMedium,changa}

    public  Tipografia(AssetManager assetsManager){
        this.assetsManager = assetsManager;

    }
    public void setFuente(font_style fuente, TextView textView) {
        switch (fuente) {
            case Roboto:
                rutaFuente = "Tipografias/RobotoRegular.ttf";
                this.tipoFuente = Typeface.createFromAsset(assetsManager, rutaFuente);
                textView.setTypeface(tipoFuente);
                break;
            case RobotoBold:
                rutaFuente = "Tipografias/RobotoBold.ttf";
                this.tipoFuente = Typeface.createFromAsset(assetsManager, rutaFuente);
                textView.setTypeface(tipoFuente);
                break;
            case QuicksandMedium:
                rutaFuente = "Tipografias/QuicksandMedium.ttf";
                this.tipoFuente = Typeface.createFromAsset(assetsManager, rutaFuente);
                textView.setTypeface(tipoFuente);
                break;
            case changa:
                rutaFuente = "Tipografias/changa.ttf";
                this.tipoFuente = Typeface.createFromAsset(assetsManager, rutaFuente);
                textView.setTypeface(tipoFuente);
                break;

        }
    }
    public static Typeface tipoFuente(AssetManager assetManager){
        String ruta = "Tipografias/RobotoBold.ttf";
        return  Typeface.createFromAsset(assetManager,ruta);
    }
}
