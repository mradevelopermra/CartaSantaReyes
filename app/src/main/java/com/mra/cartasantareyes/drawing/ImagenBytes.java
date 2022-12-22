package com.mra.cartasantareyes.drawing;

import android.content.Context;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImagenBytes
{
    private static final float ALTO_IMAGEN  = 400;
    private static final float ANCHO_IMAGEN = 400;

    private static Bitmap imagenABitmap(ImageView imageView){
        try{
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            return bitmap;
        }catch (Exception e){
            e.printStackTrace();
            return  null;
        }
    }
    public static boolean existeFotoVehiculo(ImageView imageView){
        Bitmap bitmapImagen = imagenABitmap(imageView);
        if(bitmapImagen != null)
            return true;
        return false;
    }
    public  static byte[] convertirImagenArrayBytes(ImageView imageView){
         Bitmap bitmapImagen =  imagenABitmap(imageView);
         byte[] imagenByte = null;
         if(bitmapImagen != null)
         {
             bitmapImagen = redimensionarImagen(bitmapImagen);
             ByteArrayOutputStream array =  new ByteArrayOutputStream();
             bitmapImagen.compress(Bitmap.CompressFormat.JPEG,100,array);
             imagenByte = array.toByteArray();
         }
         return imagenByte;
    }
    public  static byte[] convertirViewArrayBytes(View view){
        Bitmap bitmapImagen =  view.getDrawingCache();
        byte[] imagenByte = null;
        if(bitmapImagen != null)
        {
            bitmapImagen = redimensionarImagen(bitmapImagen);
            ByteArrayOutputStream array =  new ByteArrayOutputStream();
            bitmapImagen.compress(Bitmap.CompressFormat.JPEG,100,array);
            imagenByte = array.toByteArray();
        }
        return imagenByte;
    }
    public static  byte[] convertirGestureArrayBytes(GestureOverlayView gestureOverlayView)
    {
        Bitmap bitmapGesture = gestureOverlayView.getGesture().toBitmap(200,200,5,Color.BLACK);
        ByteArrayOutputStream array =  new ByteArrayOutputStream();
        bitmapGesture.compress(Bitmap.CompressFormat.PNG,100,array);
        byte[] gestureByte = array.toByteArray();
        return gestureByte;
    }
    public static Bitmap ConvertirDrawableBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static byte[] convertirBitmapArrayBytes(Bitmap bitmap){
        ByteArrayOutputStream array =  new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,array);
        byte[] bitmapByte = array.toByteArray();
        return bitmapByte;
    }



    public static  String ImagenToString64(byte[] arrayFoto){
        String imagenString = Base64.encodeToString(arrayFoto,Base64.DEFAULT);
        return  imagenString;

    }
    public static Bitmap StringBase64ToBitmap(String imagen){
        try{
            byte[] decodificarString = Base64.decode(imagen,Base64.DEFAULT);
            BitmapFactory.Options opciones = new BitmapFactory.Options();
            opciones.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodificarString,0,decodificarString.length,opciones);
            return  bitmap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap redimensionarImagen(Bitmap bitmap){


        int ancho = bitmap.getWidth();
        int alto = bitmap.getHeight();
        if(ancho > ANCHO_IMAGEN || alto > ALTO_IMAGEN){

            float escalarAncho = ANCHO_IMAGEN/ancho;
            float escalarAlto  = ALTO_IMAGEN/alto;

            Matrix matrix = new Matrix();
            matrix.postScale(escalarAncho,escalarAlto);

            return  Bitmap.createBitmap(bitmap,0,0,ancho,alto,matrix,false);


        }else{
            return bitmap;
        }
    }
    public static Bitmap uriToBitmap(Uri uri, Context context)
    {
        try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
            return rotateImageUri(uri,context,bitmap);
            //return bitmap;
        }catch (Exception e){
            return  null;
        }
    }

    public static Bitmap uriToBitmapConsulta(Uri uri, Context context)
    {
        try{
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
            return rotateImageUriConsulta(uri,context,bitmap);
            //return bitmap;
        }catch (Exception e){
            return  null;
        }
    }

    public static  Bitmap rotateImageUriConsulta(Uri uri,Context context,Bitmap bitmap){

        try{

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                ExifInterface exif = new ExifInterface(context.getContentResolver().openInputStream(uri));
                int rotacion = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                Log.e("rotacion", rotacion+"");
                int rotacionAgrados = exifToDegreesConsulta(rotacion);

                Matrix matrix = new Matrix();

                if (rotacion != 0) {
                    matrix.preRotate(rotacionAgrados);
                    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                } else {
                    return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                }
            }else{
                return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            }


        }catch (IOException e){
            Log.e("error rotacion imagen",e.getMessage());
            return null;
        }

    }

    public static  Bitmap rotateImageUri(Uri uri,Context context,Bitmap bitmap){

        try{

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            {
                ExifInterface exif = new ExifInterface(context.getContentResolver().openInputStream(uri));
                int rotacion = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int rotacionAgrados = exifToDegrees(rotacion);

                Matrix matrix = new Matrix();

                if (rotacion != 0) {
                    matrix.preRotate(rotacionAgrados);
                    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                } else {
                    return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
                }
            }else{
                return MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            }


        }catch (IOException e){
            Log.e("error rotacion imagen",e.getMessage());
            return null;
        }

    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    private static int exifToDegreesConsulta(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    public static Bitmap dibujarTextoBitmap(Bitmap bitmap,Context context, String nombre, String regaloUno,
                                            String regaloDos, String regaloTres, String ayuda)
    {
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String text = sdf.format(Calendar.getInstance().getTime());
        Bitmap bitmapMutable = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        Canvas canvas = new Canvas(bitmapMutable);
        Paint paint = new Paint();
        paint.setTextSize(65);

        paint.setColor(Color.rgb(250, 97, 60));
        paint.setTypeface(Tipografia.tipoFuente(context.getAssets()));

        String portado = "muy bien";
        if(ayuda.equalsIgnoreCase("Si")){
            portado = "muy bien";
        }else{
            portado = "a veces no tan bien";
        }
        String carta = "Queridos reyes magos este año me he portado ";
        String carta2 = portado + ", asi que me encantaria:";

        Rect bounds = new Rect();
        paint.getTextBounds(carta, 0, carta.length(), bounds);

        int x =  20;
        int y =   80;
        //int y =  bitmapMutable.getHeight() - 20;

        canvas.drawText(carta, x , y  , paint);
        //Segunda parte carta
        paint.getTextBounds(carta, 0, carta.length(), bounds);

         x =  20;
         y =   160;
        paint.setColor(Color.rgb(250, 215, 60));
        //int y =  bitmapMutable.getHeight() - 20;

        canvas.drawText(carta2, x , y  , paint);
        //Regalo uno
        paint.getTextBounds(regaloUno, 0, regaloUno.length(), bounds);

        x =  20;
        y =   240;
        paint.setColor(Color.rgb(167, 250, 60));
        //int y =  bitmapMutable.getHeight() - 20;

        canvas.drawText(regaloUno, x , y  , paint);
        //Regalo dos
        paint.getTextBounds(regaloDos, 0, regaloDos.length(), bounds);

        x =  20;
        y =   320;
        paint.setColor(Color.rgb(60, 250, 187));
        //int y =  bitmapMutable.getHeight() - 20;

        canvas.drawText(regaloDos, x , y  , paint);
        //Regalo tres
        paint.getTextBounds(regaloTres, 0, regaloTres.length(), bounds);

        x =  20;
        y =   400;
        paint.setColor(Color.rgb(60, 201, 250));
        //int y =  bitmapMutable.getHeight() - 20;

        canvas.drawText(regaloTres, x , y  , paint);
        //Atentamente
        paint.getTextBounds(nombre, 0, nombre.length(), bounds);

        x =  20;
       // y =   400;
         y =  bitmapMutable.getHeight() - 80;
        paint.setColor(Color.rgb(123, 60, 250));

        canvas.drawText("Atentamente: " + nombre, x , y  , paint);

        return bitmapMutable;

    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public static Bitmap getBitmapFromViewDrawView(View view) {

        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

}
