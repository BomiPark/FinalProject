package project.boostcamp.final_project.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;


public class TodoItemViewHolder extends RecyclerView.ViewHolder {

    public TextView textDate, textTodo, textAddress;

    public TodoItemViewHolder(View view) {
        super(view);
        textTodo = (TextView)view.findViewById(R.id.textTodo);
        textDate = (TextView)view.findViewById(R.id.textDate);
        textAddress = (TextView)view.findViewById(R.id.textAddress);

    }

    public void bind(final TodoItem todoItem){

        textDate.setText(todoItem.getDate());
        textTodo.setText(todoItem.getTodo());
        textAddress.setText(todoItem.getAddress());
    }
}
