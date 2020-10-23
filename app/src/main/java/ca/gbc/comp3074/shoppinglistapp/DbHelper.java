package ca.gbc.comp3074.shoppinglistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "items_db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // only executed once in the lifetime of the application
        db.execSQL(Item.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // executed when you change the version and want to introduce new changes to DB structure
        db.execSQL("DROP TABLE IF EXISTS " +Item.TABLE_NAME);
        onCreate(db);
    }

    public long insertItem(String item){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Item.COLUMN_ITEM, item);

        long id = db.insert(Item.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public Item getItem(long id){
        SQLiteDatabase db = getReadableDatabase();

        Item item  = null;

        Cursor cursor = db.query(Item.TABLE_NAME,
                new String[] {Item.COLUMN_ID, Item.COLUMN_ITEM, Item.COLUMN_TIMESTAMP},
                Item.COLUMN_ID+"=?",
                new String[] {String.valueOf(id)},
                null, null, null, null
        );

        if(cursor != null && cursor.moveToFirst()){
            item = new Item(
                    cursor.getInt(cursor.getColumnIndex(Item.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Item.COLUMN_ITEM)),
                    cursor.getString(cursor.getColumnIndex(Item.COLUMN_TIMESTAMP))
            );
            cursor.close();
        }

        db.close();
        return item;
    }

    public List<Item> getAllItems(){
        SQLiteDatabase db = getReadableDatabase();

        List<Item> list = new ArrayList<>();

        String query = "SELECT * FROM " + Item.TABLE_NAME +
                " ORDER BY " + Item.COLUMN_ID;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor != null && cursor.moveToFirst()){
            do {
                Item item = new Item(
                        cursor.getInt(cursor.getColumnIndex(Item.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(Item.COLUMN_ITEM)),
                        cursor.getString(cursor.getColumnIndex(Item.COLUMN_TIMESTAMP))
                );
                list.add(item);
            }while(cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return list;
    }

    public int updateItem(Item item){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Item.COLUMN_ITEM, item.getItem());

        int c = db.update(Item.TABLE_NAME, values,
                Item.COLUMN_ID+"=?",
                new String[]{String.valueOf(item.getId())});

        db.close();
        return c;
    }

    public int deleteItem(Item item){
        SQLiteDatabase db = getWritableDatabase();

        int c = db.delete(Item.TABLE_NAME,
                Item.COLUMN_ID+"=?",
                new String[]{String.valueOf(item.getId())});

        db.close();
        return c;
    }
}
