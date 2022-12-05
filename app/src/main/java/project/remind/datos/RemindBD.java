package project.remind.datos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.widget.Toast;

public class RemindBD{
    SQLiteDatabase datos;
    Context context; // Quitar

    public RemindBD(Context context){
        RemindBDHelper instancia = new RemindBDHelper(context);
        datos = instancia.getWritableDatabase();
        this.context = context;
    }

    public Cursor seleccionarTContrasenas(){
        String seleccionar = "Select p_clave from Persona where p_contrasena != ''";
        return this.datos.rawQuery(seleccionar, null);
    }

    public Cursor seleccionarTContrasenas(String datos[]){
        String seleccionar
               = "Select p_clave from Persona where p_contrasena == '"+datos[0]+"'";
        return this.datos.rawQuery(seleccionar, null);
    }

    public Cursor seleccionarTCatorias(){
        String seleccionar = "Select * from Categoria";
        return this.datos.rawQuery(seleccionar, null);
    }

    public Cursor seleccionarCategoria(){
        String seleccionar = "Select max(c_clave) from Categoria";
        return this.datos.rawQuery(seleccionar, null);
    }

    public Cursor seleccionarTActividades(String datos[]){
        String seleccionar = "Select * from Actividad where c_clave = "+datos[0];
        return this.datos.rawQuery(seleccionar, null);
    }

    public Cursor seleccionarActividad(){
        String seleccionar = "Select max(a_clave) from Actividad";
        return this.datos.rawQuery(seleccionar, null);
    }

    public Cursor seleccionarTEtiquetas(String datos[]){
        String seleccionar = "Select * from Etiqueta where a_clave = "+datos[0];
        return this.datos.rawQuery(seleccionar, null);
    }

    public Cursor seleccionarEtiqueta(){
        String seleccionar = "Select max(e_clave) from Etiqueta";
        return this.datos.rawQuery(seleccionar, null);
    }

    // id, nombre
    public void insertarCategoria(String datos[]){
        String insertar = "Insert into Categoria (c_nombre) values ('"+datos[1]+"')";
        this.datos.execSQL(insertar);
    }
    // id, nombre, categoria_id
    public void insertarActividad(String datos[]){
        String insertar
                = "Insert into Actividad (a_nombre, c_clave) values ('"+datos[1]+"', "+datos[2]+")";
        this.datos.execSQL(insertar);
    }

    // id, nombre, actividad_id
    public void insertarEtiqueta(String datos[]){
        String insertar
                = "Insert into Etiqueta (e_nombre, a_clave) values ('"+datos[1]+"', "+datos[3]+")";
        this.datos.execSQL(insertar);
    }

    // id, nombre
    public void modificarContrasena(String datos[]){
        String modificar
                = "Update Persona set p_contrasena = '"+datos[0]+"'";
        this.datos.execSQL(modificar);
    }

    // id, nombre
    public void modificarCategoria(String datos[]){
        String modificar
                = "Update Categoria set c_nombre = '"+datos[1]+"' where c_clave = "+datos[0];
        this.datos.execSQL(modificar);
    }
    // id, nombre
    public void modificarActividad(String datos[]){
        String modificar
                = "Update Actividad set a_nombre = '"+datos[1]+"' where a_clave = "+datos[0];
        this.datos.execSQL(modificar);
    }

    // id, nombre
    public void modificarEtiqueta(String datos[]){
        String modificar
                = "Update Etiqueta set e_nombre = '"+datos[1]+"'," +
                "  e_enlace = '"+datos[2]+"' where e_clave = "+datos[0];
        this.datos.execSQL(modificar);
    }

    // id
    public void eliminarCategoria(String datos[]){
        String eliminar = "Delete from Categoria where c_clave = "+datos[0];
        this.datos.execSQL(eliminar);
    }

    // id
    public void eliminarActividad(String datos[]){
        String eliminar = "Delete from Actividad where a_clave = "+datos[0];
        this.datos.execSQL(eliminar);
    }

    // id
    public void eliminarEtiqueta(String datos[]){
        String eliminar = "Delete from Etiqueta where e_clave = "+datos[0];
        this.datos.execSQL(eliminar);
    }
}
