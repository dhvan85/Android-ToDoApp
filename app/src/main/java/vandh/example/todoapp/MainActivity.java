package vandh.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 1;
    private final String FILE_NAME = "todo.txt";

    private ListView itemListView;
    private EditText newItemText;
    private ToDoItemAdapter todoAdapter;
    private ArrayList<ToDoItem> todoItemList = new ArrayList<>();
    private TaskDbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findControls();
        initControls();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            ToDoItem item = (ToDoItem) data.getSerializableExtra("item");

            if (item.getPosition() < 0) {
                todoItemList.add(todoItemList.size(), item);
                db.createItem(item);
            } else {
                todoItemList.set(item.getPosition(), item);
                db.updateItem(item);
            }

            todoAdapter.notifyDataSetChanged();
        }
    }

    private void findControls() {
        this.itemListView = (ListView) findViewById(R.id.itemListView);
        this.newItemText = (EditText) findViewById(R.id.newItemText);
    }

    private void initControls() {
        db = TaskDbHelper.getInstance(getApplicationContext());
        // init adapter
        buildToDoItemList();
        this.todoAdapter = new ToDoItemAdapter(this, this.todoItemList);

        // init ListView
        this.itemListView.setAdapter(todoAdapter);

        // click to edit
        this.itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToDoItem item = todoItemList.get(position);

                item.setPosition(position);
                startEditActivity(item);
            }
        });

        // long-click to remove
        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                db.deleteItem(todoItemList.get(position));

                todoItemList.remove(position);
                todoAdapter.notifyDataSetChanged();

                return true;
            }
        });
    }

    public void onAddItem(View view) {
        startEditActivity(null);
    }

    private void startEditActivity(ToDoItem toDoItem) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);

        i.putExtra("item", toDoItem);
        startActivityForResult(i, REQUEST_CODE);
    }

    private void buildToDoItemList() {
        this.todoItemList = db.getAllUndone();
    }
}