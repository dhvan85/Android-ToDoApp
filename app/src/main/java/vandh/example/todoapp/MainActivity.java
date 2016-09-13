package vandh.example.todoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> todoItems;
    ArrayAdapter<String> aToDoAdapter;
    ListView lvItems;
EditText edText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateArrayItems();
        
        lvItems = (ListView) findViewById(R.id.lvItems);
        edText = (EditText) findViewById(R.id.etEditText);

        lvItems.setAdapter(aToDoAdapter);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoItems.remove(position);
                aToDoAdapter.notifyDataSetChanged();
                return true;
            }
        });
    }

    private void populateArrayItems() {
        todoItems = new ArrayList<String>();

        todoItems.add("Item 1");
        todoItems.add("Item 2");
        todoItems.add("Item 3");

        aToDoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItems);
    }


    public void onAddItem(View view) {
        aToDoAdapter.add(edText.getText().toString());
        edText.setText("");

    }
}
