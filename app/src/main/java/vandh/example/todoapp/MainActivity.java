package vandh.example.todoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {
    private TaskDbHelper db;
    private final int REQUEST_CODE = 1;

    private ListView itemListView;
    private ToDoItemAdapter todoAdapter;
    private ArrayList<ToDoItem> todoItemList = new ArrayList<>();
    private Comparator<ToDoItem> comparor = new Comparator<ToDoItem>() {
        @Override
        public int compare(ToDoItem o1, ToDoItem o2) {
            return (o1.Priority - o2.Priority) * 100 + o1.DueDate.compareTo(o2.DueDate) * 10 + o1.Title.compareTo(o2.Title);
        }
    };

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
                if (!item.Status) {
                    todoItemList.set(item.getPosition(), item);
                } else {
                    todoItemList.remove(item.getPosition());
                    Toast.makeText(getApplicationContext(), "Congratulations for finishing it!", Toast.LENGTH_LONG).show();
                }

                db.updateItem(item);
            }

            Collections.sort(todoItemList, comparor);
            todoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_item:
                startEditActivity(null);
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void findControls() {
        this.itemListView = (ListView) findViewById(R.id.itemListView);
    }

    private void initControls() {
        db = TaskDbHelper.getInstance(getApplicationContext());
        // init adapter
        buildToDoItemList();
        Collections.sort(todoItemList, comparor);
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
                confirmDelete(position).show();
                return true;
            }
        });
    }

    private void startEditActivity(ToDoItem toDoItem) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);

        i.putExtra("item", toDoItem);
        startActivityForResult(i, REQUEST_CODE);
    }

    private void buildToDoItemList() {
        this.todoItemList = db.getAllUndone();
    }

    private AlertDialog confirmDelete(final int position) {
        AlertDialog confirmDialog = new AlertDialog.Builder(this)
                .setTitle("Remove this task")
                .setMessage("Are you sure?")
                .setIcon(android.R.drawable.ic_delete)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.deleteItem(todoItemList.get(position));

                        todoItemList.remove(position);
                        todoAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return confirmDialog;

    }

}