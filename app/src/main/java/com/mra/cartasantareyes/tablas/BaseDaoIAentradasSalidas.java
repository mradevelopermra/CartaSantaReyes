package com.mra.cartasantareyes.tablas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mra.cartasantareyes.constantes.Constantes;
import com.mra.cartasantareyes.model.Carta_Model;

import java.util.ArrayList;
import java.util.List;

import static com.mra.cartasantareyes.constantes.Constantes.TABLA_CARTA;
import static com.mra.cartasantareyes.constantes.Constantes.TABLA_CARTA_SANTA;


public class BaseDaoIAentradasSalidas extends SQLiteOpenHelper {

	private static BaseDaoIAentradasSalidas instance;
	private static final int   DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "CartaSanta";



	public BaseDaoIAentradasSalidas(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		String creaTablaCartaSanta = "CREATE TABLE CartaSanta(" +
				"idCarta INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
				"edad INTEGER NOT NULL, " +
				"sePorto INTEGER NOT NULL, " +
				"nombreFirma blob, " +
				"nombreNino VARCHAR(250) , " +
				"opcionUno VARCHAR(250) , " +
				"opcionDos VARCHAR(250) , " +
				"opcionTres VARCHAR(250) , " +
				"fechaCreacion VARCHAR(250)) ";
		db.execSQL(creaTablaCartaSanta);


		String creaTablaUsuarios = "CREATE TABLE Usuario(" +
				"idUsuario INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
				"nombreUsuario INTEGER NOT NULL, " +
				"password TEXT NOT NULL, " +
				"usuarioCreacion VARCHAR(50) , " +
				"usuarioModificacion VARCHAR(50) , " +
				"usuarioEliminacion VARCHAR(50) , " +
				"fechaCreacion VARCHAR(50) , " +
				"fechaModificacion VARCHAR(50) , " +
				"fechaEliminacion VARCHAR(50) ) ";
		db.execSQL(creaTablaUsuarios);


	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

        db.execSQL("drop table if exists creaTablaCartaSanta");
        onCreate(db);

		db.execSQL("drop table if exists Usuario");
		onCreate(db);

	}


	public void truncateTable(String tableName){
		getWritableDatabase().execSQL(
				"DELETE FROM " + tableName
		);

	}

	public void resetAutoincrement(String tableName) {
		getWritableDatabase().execSQL(
				"delete from sqlite_sequence where name='" + tableName + "'"
		);
		Log.d("DATABASE", tableName + " autoincrement reset");
	}



	public boolean insertaCarta(Carta_Model dto){

		boolean estado1=true;
		int resultado1;
		ContentValues datos= new ContentValues();

		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransactionNonExclusive();

		try{
			//datos.put(Constantes.idCarta, dto.getIdCarta());
			datos.put(Constantes.edad, dto.getEdad());
			datos.put(Constantes.nombreFirma, dto.getNombreFirma());
			datos.put(Constantes.nombreNino, dto.getNombreNino());
			datos.put(Constantes.sePorto, dto.getSePorto());

			datos.put(Constantes.opcionUno, dto.getOpcionUno());
			datos.put(Constantes.opcionDos, dto.getOpcionDos());
			datos.put(Constantes.opcionTres, dto.getOpcionTres());
			datos.put(Constantes.fechaCreacion, dto.getFechaCreacion());

			resultado1=(int)this.getWritableDatabase().insert(TABLA_CARTA_SANTA, "edad, sePorto, " +
					"nombreFirma, nombreNino, opcionUno, " +
					"opcionDos, opcionTres, fechaCreacion", datos);

			if(resultado1>0)estado1=true;
			else estado1=false;

		}catch(Exception e){
			estado1=false;
		}

		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		return estado1;
	}

	public boolean actualizaFirmaEntradaSalida(Carta_Model datos2){
		String[] argumento ={String.valueOf(datos2.getIdCarta())};
		ContentValues datos=new ContentValues();
		int resultado;

		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransactionNonExclusive();

		datos.put(Constantes.edad, datos2.getEdad());
		datos.put(Constantes.nombreFirma, datos2.getNombreFirma());
		datos.put(Constantes.nombreNino, datos2.getNombreNino());
		datos.put(Constantes.sePorto, datos2.getSePorto());

		datos.put(Constantes.opcionUno, datos2.getOpcionUno());
		datos.put(Constantes.opcionDos, datos2.getOpcionDos());
		datos.put(Constantes.opcionTres, datos2.getOpcionTres());
		datos.put(Constantes.fechaCreacion, datos2.getFechaCreacion());

		resultado=this.getWritableDatabase().update(TABLA_CARTA_SANTA, datos, "idCarta=?", argumento);

		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();

		if (resultado>0)return true;
		else return false;

	}

	public List consultaListaFirmas(int idCarta){
		String query = "SELECT * FROM " + TABLA_CARTA + " WHERE "
				+ Constantes.camposCarta.idCarta + " = " + idCarta;
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = null;
		c= db.rawQuery(query, null);

		List regresamos1= new ArrayList();

		int IidCarta,
				Iedad, Iseporto,
				InombreFirma, InombreNino, IopcionUno, IopcionDos, IopcionTres, IfechaCreacion;;

		IidCarta = c.getColumnIndex(Constantes.camposCarta.idCarta);
		Iedad = c.getColumnIndex(Constantes.camposCarta.edad);
		Iseporto = c.getColumnIndex(Constantes.camposCarta.sePorto);
		InombreFirma = c.getColumnIndex(Constantes.camposCarta.nombreFirma);
		InombreNino= c.getColumnIndex(Constantes.camposCarta.nombreNino);
		IopcionUno= c.getColumnIndex(Constantes.camposCarta.opcionUno);
		IopcionDos = c.getColumnIndex(Constantes.camposCarta.opcionDos);
		IopcionTres = c.getColumnIndex(Constantes.camposCarta.opcionTres);
		IfechaCreacion = c.getColumnIndex(Constantes.camposCarta.fechaCreacion);



		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

			Carta_Model dto=new Carta_Model();

			dto.setIdCarta(c.getInt(IidCarta));
			dto.setEdad(c.getInt(Iedad));
			dto.setSePorto(c.getInt(Iseporto));
			dto.setNombreFirma(c.getBlob(InombreFirma));
			dto.setNombreNino(c.getString(InombreNino));
			dto.setOpcionUno(c.getString(IopcionUno));
			dto.setOpcionDos(c.getString(IopcionDos));
			dto.setOpcionTres(c.getString(IopcionTres));
			dto.setFechaCreacion(c.getString(IfechaCreacion));

			regresamos1.add(dto);
		}

		c.close();
		db.close();

		return regresamos1;
	}



}
