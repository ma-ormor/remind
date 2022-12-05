package project.remind.modulos;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import project.remind.Principal;
import project.remind.datos.RemindBD;

public class Controlador{
    Principal principal;
    RemindBD baseDatos;
    Context context;

    public Controlador(Principal principal){
        this.principal = principal;
        context = principal.getApplicationContext();
        baseDatos = new RemindBD(context);
    }

    public void sinNombre(){
        Cursor fila = baseDatos.seleccionarTCatorias();
        String [][] datos = new String[fila.getCount()][2];
        Categoria nueva;
        int i = 0;

        if(!fila.moveToFirst()) return;

        do{
            datos[i][0] = fila.getString(0);
            datos[i++][1] = fila.getString(1);

            nueva = categoria(new String[]{datos[i-1][0], datos[i-1][1]});
            sinNombre2(nueva);
            principal.agregarCategoria(nueva);
        }while(fila.moveToNext());
    }
    // Agregar actividades
    public void sinNombre2(Categoria categoria){
        String cClave = categoria.datos[0];
        Cursor fila = baseDatos.seleccionarTActividades(new String[]{cClave});
        String [][] datos = new String[fila.getCount()][3];
        Actividad nueva;
        int i = 0;

        if(!fila.moveToFirst()) return;
        //Toast.makeText(context, cClave, Toast.LENGTH_SHORT).show();

        do{
            datos[i] = new String[]{fila.getString(0),fila.getString(1),fila.getString(2)};
            nueva = actividad(categoria, datos[i++]);
            sinNombre3(nueva);
            categoria.agregarActividad(nueva);
        }while(fila.moveToNext());
    }
    // Agregar etiquetas
    public void sinNombre3(Actividad actividad){
        String aClave = actividad.datos[0];
        Cursor fila = baseDatos.seleccionarTEtiquetas(new String[]{aClave});
        String [][] datos = new String[fila.getCount()][4];
        int i = 0;

        if(!fila.moveToFirst()) return;

        do{
            datos[i] = new String[]{
                fila.getString(0),
                fila.getString(1),
                fila.getString(2),
                fila.getString(3)
            };
            actividad.agregarEtiqueta(etiqueta(actividad, datos[i++]));
        }while(fila.moveToNext());
    }

    private Categoria categoria(String datos[]){
        return new Categoria(principal, datos, true, context);
    }

    private Actividad actividad(Categoria categoria, String datos[]){
        return new Actividad(categoria, datos, true, context);
    }

    private Etiqueta etiqueta(Actividad actividad, String datos[]){
        return new Etiqueta(actividad, datos, true, context);
    }

    public boolean hayContrasena(){
        return baseDatos.seleccionarTContrasenas().getCount() > 0;
    }
}
