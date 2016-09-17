package vandh.example.todoapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;


/**
 * Created by VANDH on 9/16/2016.
 */
public class TaskDbHelper extends SQLiteOpenHelper {
    private static TaskDbHelper instance;
    private final static String DB_NAME = "TaskDb";
    private final static int DB_VERSION = 1;

    static {
        cupboard().register(ToDoItem.class);
    }

    public static synchronized TaskDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new TaskDbHelper(context.getApplicationContext());
        }

        return instance;
    }

    private TaskDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        cupboard().withDatabase(db).createTables();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        cupboard().withDatabase(db).upgradeTables();
    }

    public void createItem(ToDoItem item) {
        SQLiteDatabase db = instance.getWritableDatabase();
        item._id = cupboard().withDatabase(db).put(item);
    }

    public void updateItem(ToDoItem item) {
        SQLiteDatabase db = instance.getWritableDatabase();
        cupboard().withDatabase(db).put(item);
    }

    public void deleteItem(ToDoItem item) {
        SQLiteDatabase db = instance.getWritableDatabase();
        cupboard().withDatabase(db).delete(item);
    }

    public void getItem(long id) {
        SQLiteDatabase db = instance.getReadableDatabase();
        cupboard().withDatabase(db).get(ToDoItem.class, id);
    }

    public ArrayList<ToDoItem> getAllUndone() {
        SQLiteDatabase db = instance.getReadableDatabase();
        ArrayList<ToDoItem> arrayList = new ArrayList<>();
        Cursor items = cupboard().withDatabase(db).query(ToDoItem.class).withSelection("Status = ?", "0").getCursor();

        try {
            QueryResultIterable<ToDoItem> iterate = cupboard().withCursor(items).iterate(ToDoItem.class);
            for (ToDoItem i : iterate)
                arrayList.add(i);
        } finally {
            items.close();
        }

        return arrayList;
    }
}
