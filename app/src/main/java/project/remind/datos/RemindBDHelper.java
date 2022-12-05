package project.remind.datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class RemindBDHelper extends SQLiteOpenHelper{
    Context context;

    public RemindBDHelper(Context context){
        super(context, "Remind.db", null, 1);
        this.context = context;
    }
    //"c_nombre text not null unique, " +
    @Override public void onCreate(SQLiteDatabase sqLiteDatabase){
        String tCategoria= "Create table Categoria (" +
                "c_clave integer primary key, " +
                "c_nombre text not null, " +
                "unique(c_clave))";
        String tActividad= "Create table Actividad (" +
                "a_clave integer primary key, " +
                "a_nombre text not null, " +
                "c_clave integer not null references Categoria(c_clave)" +
                "on delete cascade," +
                "unique(a_clave))";
        String tEtiqueta= "Create table Etiqueta (" +
                "e_clave integer primary key, " +
                "e_nombre text not null, " +
                "e_enlace text, " +
                "a_clave integer not null references Actividad(a_clave)" +
                "on delete cascade," +
                "unique(e_clave))";
        String tContrasena = "Create table Persona (" +
                "p_clave integer primary key, " +
                "p_contrasena text, " +
                "unique(p_clave))";
        String insertar = "Insert into Persona (p_contrasena) values ('')";

        sqLiteDatabase.execSQL(tContrasena);
        sqLiteDatabase.execSQL(tCategoria);
        sqLiteDatabase.execSQL(tActividad);
        sqLiteDatabase.execSQL(tEtiqueta);
        sqLiteDatabase.execSQL(insertar);
    }

    @Override public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){}

    @Override public void onOpen(SQLiteDatabase db) {
        String eliminar1 = "Drop table if exists Etiqueta",
               eliminar2 = "Drop table if exists Actividad",
               eliminar3 = "Drop table if exists Categoria",
               eliminar4 = "Drop table if exists Persona";

        super.onOpen(db); db.execSQL("PRAGMA foreign_keys = ON;");

        //Toast.makeText(context, "Llegao", Toast.LENGTH_SHORT).show();

        /*db.execSQL(eliminar1);
        db.execSQL(eliminar2);
        db.execSQL(eliminar3);
        db.execSQL(eliminar4);
        onCreate(db);*/
    }
}
