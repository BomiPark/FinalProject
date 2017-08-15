package project.boostcamp.final_project.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import project.boostcamp.final_project.Interface.RecyclerItemClickListener;
import project.boostcamp.final_project.Interface.TodoCheckClickListener;
import project.boostcamp.final_project.Interface.TodoMainClickListener;
import project.boostcamp.final_project.Model.TodoItem;


public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemViewHolder> {

    private Context context;
    private List<TodoItem> itemList = new ArrayList<>();
    private int item_layout;
    private TodoItemViewHolder viewHolder;

    private TodoMainClickListener itemClickListener;
    private TodoCheckClickListener checkClickListener;

    public TodoItemAdapter(Context context, RealmResults<TodoItem> itemList, int item_layout,
                           TodoMainClickListener itemClickListener,
                           TodoCheckClickListener checkClickListener){
        this.context = context;
        this.itemList = itemList;
        this.item_layout = item_layout;
        this.itemClickListener = itemClickListener;
        this.checkClickListener = checkClickListener;
    }

    @Override
    public TodoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(item_layout, parent, false);
        viewHolder = new TodoItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodoItemViewHolder holder, int position) {
        ((TodoItemViewHolder) holder).bind(itemList.get(position), itemClickListener, checkClickListener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
