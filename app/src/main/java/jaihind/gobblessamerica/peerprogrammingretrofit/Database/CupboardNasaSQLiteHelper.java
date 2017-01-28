package jaihind.gobblessamerica.peerprogrammingretrofit.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import jaihind.gobblessamerica.peerprogrammingretrofit.Model.Nasa;


/**
 * Created by nande on 1/24/2017.
 */

public class CupboardNasaSQLiteHelper extends SQLiteOpenHelper {
    private static final String Database_name="nasa.db";
    private static final int Database_version = 1;


    static {
        //cupboard().register(Nasa.class);
    }

    public CupboardNasaSQLiteHelper(Context context){
        super(context,Database_name, null, Database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

       // cupboard().withDatabase(db).createTables();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       // cupboard().withDatabase(db).dropAllTables();

        //cupboard().withDatabase(db).upgradeTables();
    }
}
