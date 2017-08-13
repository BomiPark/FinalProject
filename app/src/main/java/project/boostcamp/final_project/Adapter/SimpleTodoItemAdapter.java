package project.boostcamp.final_project.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import project.boostcamp.final_project.Model.TodoItem;

public class SimpleTodoItemAdapter extends RecyclerView.Adapter<SimpleTodoItemViewHolder>{

    private Context context;
    private List<TodoItem> itemList = new ArrayList<>();
    private int item_layout;
    private SimpleTodoItemViewHolder viewHolder;

    public SimpleTodoItemAdapter(Context context, RealmResults<TodoItem> itemList, int item_layout){
        this.context = context;
        this.itemList = itemList;
        this.item_layout = item_layout;
    }

    @Override
    public SimpleTodoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(item_layout, parent, false);
        viewHolder = new SimpleTodoItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SimpleTodoItemViewHolder holder, int position) { //todo 클릭리스너등록
        ((SimpleTodoItemViewHolder) holder).bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
