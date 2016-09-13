package vandh.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private final String FILE_NAME = "todo.txt";
    private final int REQUEST_CODE = 1;

    private ArrayList<String> todoItemList = new ArrayList<>();
    private ArrayAdapter<String> todoAdapter;
    private ListView itemListView;
    private EditText newItemText;

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
            todoItemList.set(data.getIntExtra("index", -1), data.getStringExtra("newText"));
            todoAdapter.notifyDataSetChanged();

            saveItemsToFile();
        }
    }

    private void findControls() {
        this.itemListView = (ListView) findViewById(R.id.itemListView);
        this.newItemText = (EditText) findViewById(R.id.newItemText);
    }

    private void initControls() {
        // init adapter
        readItemsFromFile();
        this.todoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, this.todoItemList);

        // init ListView
        this.itemListView.setAdapter(todoAdapter);

        // click to edit
        this.itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startEditActivity(position, todoItemList.get(position));
            }
        });

        // long-click to remove
        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItemList.remove(position);
                todoAdapter.notifyDataSetChanged();

                saveItemsToFile();
                return true;
            }
        });
    }

    private void startEditActivity(int index, String text) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);

        i.putExtra("index", index);
        i.putExtra("currentText", text);

        startActivityForResult(i, REQUEST_CODE);
    }

    private void readItemsFromFile() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, FILE_NAME);

        try {
            if (file.exists()) {
                todoItemList = new ArrayList<>(FileUtils.readLines(file));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveItemsToFile() {
        File filesDir = getFilesDir();
        File file = new File(filesDir, FILE_NAME);

        try {
            FileUtils.writeLines(file, todoItemList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onAddItem(View view) {
        todoAdapter.add(newItemText.getText().toString());
        newItemText.setText("");

        saveItemsToFile();
    }
}