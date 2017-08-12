package project.boostcamp.final_project.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import project.boostcamp.final_project.Model.Folder;

public class FolderItemAdapter extends RecyclerView.Adapter<FolderItemViewHolder> {

    Context context;
    int item_layout;
    List<Folder> list = new ArrayList<>();
    FolderItemViewHolder viewHolder;

    public FolderItemAdapter(Context context, RealmResults<Folder> list, int item_layout){
        this.context = context;
        this.list = list;
        this.item_layout = item_layout;
    }

    public FolderItemAdapter(Context context, List<Folder> list, int item_layout){
        this.context = context;
        this.list = list;
        this.item_layout = item_layout;
    }

    @Override
    public FolderItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

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
