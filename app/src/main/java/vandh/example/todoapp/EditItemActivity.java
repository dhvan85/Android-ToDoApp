package vandh.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    private int index;
    private EditText editItemText;

    private void findControls() {
        this.editItemText = (EditText) findViewById(R.id.editItemText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        findControls();
        initControls();
    }

    private void initControls() {
        this.index = getIntent().getIntExtra("index", -1);
        this.editItemText.setText(getIntent().getStringExtra("currentText"));
        this.editItemText.setSelection(this.editItemText.getText().length());
    }

    public void onSubmit(View v) {
        Intent i = new Intent();

        i.putExtra("index", this.index);
        i.putExtra("newText", this.editItemText.getText().toString());

        setResult(1, i);
        this.finish();
    }
}