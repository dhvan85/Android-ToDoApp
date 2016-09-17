package vandh.example.todoapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Calendar;

public class EditItemActivity extends AppCompatActivity {
    private int priority;
    private ToDoItem item;
    private TextView priority1;
    private TextView priority2;
    private TextView priority3;
    private EditText titleText;
    private EditText noteText;
    private NumberPicker number1;
    private NumberPicker number2;
    private NumberPicker number3;

    private Calendar calendar = Calendar.getInstance();

    private void findControls() {
        this.priority1 = (TextView) findViewById(R.id.textView1);
        this.priority2 = (TextView) findViewById(R.id.textView2);
        this.priority3 = (TextView) findViewById(R.id.textView3);

        this.titleText = (EditText) findViewById(R.id.titleText);
        this.noteText = (EditText) findViewById(R.id.noteText);

        this.number1 = (NumberPicker) findViewById(R.id.numberPicker1);
        this.number2 = (NumberPicker) findViewById(R.id.numberPicker2);
        this.number3 = (NumberPicker) findViewById(R.id.numberPicker3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        findControls();
        initControls();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.edit_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_item:
                submit(false);
                break;
            case R.id.cancel_item:
                finish();
                break;
            case R.id.done_item:
                submit(true);
                break;
        }
        return true;
    }

    private void initControls() {
        this.item = (ToDoItem) getIntent().getSerializableExtra("item");

        this.number1.setMinValue(2015);
        this.number1.setMaxValue(2017);
        this.number1.setValue(calendar.get(Calendar.YEAR));

        this.number2.setMinValue(1);
        this.number2.setMaxValue(12);
        this.number2.setValue(calendar.get(Calendar.MONTH) + 1);

        this.number3.setMinValue(1);
        this.number3.setMaxValue(31);
        this.number3.setValue(calendar.get(Calendar.DAY_OF_MONTH));


        if (item != null) {
            this.titleText.setText(item.Title);
            this.noteText.setText(item.Description);

            calendar.setTime(item.DueDate);

            this.number1.setValue(calendar.get(Calendar.YEAR));
            this.number2.setValue(calendar.get(Calendar.MONTH) + 1);
            this.number3.setValue(calendar.get(Calendar.DAY_OF_MONTH));

            this.priority = item.Priority;

            if (item.Priority == 1)
                priority1.setTextColor(Color.WHITE);
            else if (item.Priority == 2)
                priority2.setTextColor(Color.WHITE);
            else
                priority3.setTextColor(Color.WHITE);
        } else {
            this.priority = 1;
            priority1.setTextColor(Color.WHITE);
        }
    }

    public void onClick_Priority(View v) {
        TextView i = (TextView) v;

        priority1.setTextColor(Color.BLACK);
        priority2.setTextColor(Color.BLACK);
        priority3.setTextColor(Color.BLACK);

        i.setTextColor(Color.WHITE);

        if (i.getText().equals("1"))
            priority = 1;
        else if (i.getText().equals("2"))
            priority = 2;
        else
            priority = 3;
    }

    public void submit(boolean status) {
        Intent i = new Intent();

        if (item == null) {
            item = new ToDoItem();
        }

        item.Title = this.titleText.getText().toString();
        item.Description = this.noteText.getText().toString();

        calendar.set(Calendar.YEAR, this.number1.getValue());
        calendar.set(Calendar.MONTH, this.number2.getValue() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, this.number3.getValue());

        item.DueDate = calendar.getTime();
        item.Priority = priority;

        item.Status = status;

        i.putExtra("item", item);
        setResult(1, i);

        finish();
    }
}