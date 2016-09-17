package vandh.example.todoapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by VANDH on 9/16/2016.
 */
public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {
    public ToDoItemAdapter(Context context, List<ToDoItem> objects) {
        super(context, 0, objects);
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDoItem currentItem = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_item, parent, false);

        TextView title = (TextView) convertView.findViewById(R.id.titleText);
        TextView status = (TextView) convertView.findViewById(R.id.statusText);
        TextView dueDate = (TextView) convertView.findViewById(R.id.dueDateText);

        title.setText(currentItem.Title);
        dueDate.setText(dateFormat.format(currentItem.DueDate));

        if (currentItem.Priority == 1)
            status.setBackgroundColor(Color.rgb(0xff, 0x39, 0x39));
        else if (currentItem.Priority == 2)
            status.setBackgroundColor(Color.rgb(0x3c, 0x3c, 0xff));
        else
            status.setBackgroundColor(Color.rgb(0x67, 0x7c, 0xff));

        return convertView;
    }
}
