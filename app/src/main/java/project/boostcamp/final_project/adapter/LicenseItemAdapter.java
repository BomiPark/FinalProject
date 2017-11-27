package project.boostcamp.final_project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import project.boostcamp.final_project.model.LicenseItem;

public class LicenseItemAdapter extends RecyclerView.Adapter<LicenseItemViewHolder>{

    private Context context;
    private int item_layout;
    private View view;
    private List<LicenseItem> list = new ArrayList<>();
    private LicenseItemViewHolder viewHolder;

    public LicenseItemAdapter(Context context, List<LicenseItem> list, int item_layout){
        this.context = context;
        this.list = list;
        this.item_layout  = item_layout;
    }

    @Override
    public LicenseItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(item_layout, parent, false);
        viewHolder = new LicenseItemViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LicenseItemViewHolder holder, final int position) {
        ((LicenseItemViewHolder)holder).bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
