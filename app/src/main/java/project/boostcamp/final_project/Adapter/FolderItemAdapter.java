package project.boostcamp.final_project.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import project.boostcamp.final_project.Model.FolderItem;

public class FolderItemAdapter extends RecyclerView.Adapter<FolderItemViewHolder> {

    private Context context;
    private int item_layout;
    private View view;
    private List<FolderItem> list = new ArrayList<>();
    private FolderItemViewHolder viewHolder;

    public FolderItemAdapter(Context context, RealmResults<FolderItem> list, int item_layout){
        this.context = context;
        this.list = list;
        this.item_layout = item_layout;
    }

    @Override
    public FolderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(item_layout, parent, false);
        viewHolder = new FolderItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FolderItemViewHolder holder, int position) {
        ((FolderItemViewHolder) holder).bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
