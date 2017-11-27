package project.boostcamp.final_project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import project.boostcamp.final_project.listener.TodoCheckClickListener;
import project.boostcamp.final_project.listener.TodoMainClickListener;
import project.boostcamp.final_project.model.TodoItem;
import project.boostcamp.final_project.R;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemViewHolder> {

    private Context context;
    private List<TodoItem> itemList = new ArrayList<>();
    private int item_layout;
    private TodoItemViewHolder viewHolder;
    private static final int EMPTY = 0;
    private static final int NOT_EMPTY = 1;

    private TodoMainClickListener itemClickListener;
    private TodoCheckClickListener checkClickListener;

    public TodoItemAdapter(Context context, RealmResults<TodoItem> itemList, int item_layout,
                           TodoMainClickListener itemClickListener,
                           TodoCheckClickListener checkClickListener) {
        this.context = context;
        this.itemList = itemList;
        this.item_layout = item_layout;
        this.itemClickListener = itemClickListener;
        this.checkClickListener = checkClickListener;
    }

    @Override
    public TodoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == NOT_EMPTY)
            view = LayoutInflater.from(parent.getContext()).inflate(item_layout, parent, false);
        else
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false);

        viewHolder = new TodoItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TodoItemViewHolder holder, int position) {
        if (getItemViewType(position) == NOT_EMPTY)
            ((TodoItemViewHolder) holder).bind(itemList.get(position), itemClickListener, checkClickListener);
    }

    @Override
    public int getItemCount() {
        if (itemList.size() != 0)
            return itemList.size();
        else
            return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.isEmpty())
            return EMPTY;
        else
            return NOT_EMPTY;
    }
}
