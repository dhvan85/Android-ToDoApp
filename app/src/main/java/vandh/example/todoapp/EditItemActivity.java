package vandh.example.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    private EditText textItemName;
    private int position;

    private void findItems() {
        textItemName = (EditText) findViewById(R.id.textItemName);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        findItems();

        String text = getIntent().getStringExtra("editText");
        position = getIntent().getIntExtra("position", -1);

        textItemName.setText(text);
        textItemName.setSelection(textItemName.getText().length());
    }

    public void onSubmit(View v) {
        Intent i = new Intent();

        i.putExtra("position", position);
        i.putExtra("newValue", textItemName.getText().toString());

        setResult(10, i);
        this.finish();

    }
}
