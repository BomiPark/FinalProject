package project.boostcamp.final_project.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import project.boostcamp.final_project.listener.TodoCheckClickListener;
import project.boostcamp.final_project.listener.TodoMainClickListener;
import project.boostcamp.final_project.model.TodoItem;
import project.boostcamp.final_project.R;

public class TodoItemViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout item_main, item_check;
    private TextView textDate, textTodo, textAddress;
    private ImageView todo_check;

    public TodoItemViewHolder(View view) {
        super(view);
        item_main = (LinearLayout) view.findViewById(R.id.item_main);
        item_check = (LinearLayout) view.findViewById(R.id.item_check);
        textTodo = (TextView) view.findViewById(R.id.textTodo);
        textDate = (TextView) view.findViewById(R.id.textDate);
        textAddress = (TextView) view.findViewById(R.id.textAddress);
        todo_check = (ImageView) view.findViewById(R.id.todo_check);
    }

    public void bind(final TodoItem todoItem,
                     final TodoMainClickListener itemClickListener,
                     final TodoCheckClickListener checkClickListener) {

        textDate.setText(todoItem.getDate());
        textTodo.setText(todoItem.getTodo());
        textAddress.setText(todoItem.getAddress());
        if (todoItem.isCompleted())
            todo_check.setImageResource(R.drawable.check);
        else
            todo_check.setImageResource(R.drawable.no_check);

        item_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        });
        item_main.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemClickListener.onLongClick(v, getAdapterPosition());
                return false;
            }
        });
        item_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (todoItem.isCompleted())
                    todo_check.setImageResource(R.drawable.no_check);
                else
                    todo_check.setImageResource(R.drawable.check);
                checkClickListener.onClick(getAdapterPosition());
            }
        });


    }

}
