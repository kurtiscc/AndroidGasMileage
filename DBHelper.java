package com.example.kurtiscc.gasmileage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kurtiscc on 1/22/2015.
 */
public class DBHelper extends SQLiteOpenHelper {

        // Database Version
        public static final int DATABASE_VERSION = 2;
        // Database Name
        public static final String DATABASE_NAME = "GasDB.db";
        public static final String GAS_TABLE_NAME = "gas";
        public static final String GAS_COLUMN_ID = "id";
        public static final String GAS_COLUMN_DATE = "date";
        public static final String GAS_COLUMN_ODOMETER = "odometer";
        public static final String GAS_COLUMN_GALLONS = "gallons";
        public static final String GAS_COLUMN_PRICE = "price";
        public static final String GAS_COLUMN_LOCATION = "location";
        public static final String GAS_COLUMN_LAT = "lat";
        public static final String GAS_COLUMN_LNG = "lng";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create gas table
        String CREATE_GAS_TABLE = "CREATE TABLE gas ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "date TEXT, " +
                "odometer TEXT, " +
                "gallons TEXT, " +
                "price TEXT, " +
                "location TEXT, " +
                "lat TEXT, " +
                "lng TEXT ) ";

        // create gas table
        db.execSQL(CREATE_GAS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older gas table if existed
        db.execSQL("DROP TABLE IF EXISTS gas");

        // create fresh gas table
        this.onCreate(db);
    }

    public boolean insertGas(String date, String odometer, String gallons, String price, String location, String lat, String lng) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("date", date);
        contentValues.put("odometer", odometer);
        contentValues.put("gallons", gallons);
        contentValues.put("price", price);
        contentValues.put("location", location);
        contentValues.put("lat", lat);
        contentValues.put("lng", lng);

        db.insert("gas", null, contentValues);
        db.close();
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from gas where id ="+id+"", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, GAS_TABLE_NAME);
        return numRows;
    }


    public Integer deleteGas(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("gas", "id = ? ", new String[] { Integer.toString(id)});
    }


    public List<Gas> getAllGas()
    {
        List<Gas> list = new ArrayList<Gas>();
        //ArrayList<String> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select * from gas;", null );
        if(res.moveToFirst() ) {

            do {
                Gas gas = new Gas();
                gas.setId(res.getInt(0));
                gas.setDate(res.getString(1));
                gas.setOdometer(res.getInt(2));
                gas.setGallons(res.getInt(3));
                gas.setPrice(res.getInt(4));
                gas.setLocation(res.getString(5));
                gas.setLat(res.getString(6));
                gas.setLng(res.getString(7));

                list.add(gas);

        } while (res.moveToNext());


        }
        res.close();
        db.close();
        return list;
    }
}
