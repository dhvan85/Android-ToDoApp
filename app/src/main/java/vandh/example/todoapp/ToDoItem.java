package vandh.example.todoapp;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by VANDH on 9/16/2016.
 */
public class ToDoItem implements Serializable {
    public Long _id;
    public String Title;
    public int Priority;
    public Date DueDate;
    public boolean Status;
    public String Description;

    private int position = -1;

    public ToDoItem() {
    }

    public ToDoItem(String _title) {
        this.Title = _title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
