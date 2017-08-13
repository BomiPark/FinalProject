package project.boostcamp.final_project.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import project.boostcamp.final_project.Model.TodoItem;
import project.boostcamp.final_project.R;

public class SimpleTodoItemViewHolder extends RecyclerView.ViewHolder{

    public TextView todo;
    public ImageView check;

    public SimpleTodoItemViewHolder(View view){
        super(view);
        todo = (TextView)view.findViewById(R.id.simple_todo);
        check = (ImageView)view.findViewById(R.id.simple_check);
    }

    public void bind(final TodoItem item){
        todo.setText(item.getTodo());
        if(item.isCompleted() == true)
            check.setImageResource(R.drawable.check);
        else
            check.setImageResource(R.drawable.no_check);
    }
}
