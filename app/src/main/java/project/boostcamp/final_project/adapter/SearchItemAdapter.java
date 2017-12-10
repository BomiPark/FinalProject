package project.boostcamp.final_project.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import project.boostcamp.final_project.model.SearchItem;
import project.boostcamp.final_project.R;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemViewHolder> {

    private Context context;
    private List<SearchItem> searchItemList = new ArrayList<>();
    private int item_layout;
    private SearchItemViewHolder viewHolder;

    public SearchItemAdapter(Context context, ArrayList<SearchItem> searchItemList, int item_layout) {
        this.context = context;
        this.searchItemList = searchItemList;
        this.item_layout = item_layout;
    }

    @Override
    public SearchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(item_layout, parent, false);
        viewHolder = new SearchItemViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(SearchItemViewHolder holder, int position) {

        if (searchItemList.get(position).isSelected()) {
            holder.bind(searchItemList.get(position));
            holder.background.setBackgroundResource(R.color.click_back);
            holder.title.setTextColor(Color.WHITE);
            holder.address.setTextColor(Color.WHITE);
            Glide.with(context).load(R.drawable.shape_click).into(holder.icon);
            Glide.with(context).load(R.drawable.search_item_line_click).into(holder.line);
            Glide.with(context).load(R.drawable.check_white).into(holder.check);

        } else {
            holder.bind(searchItemList.get(position));
            holder.background.setBackgroundResource(R.color.white);
            holder.title.setTextColor(Color.BLACK);
            holder.address.setTextColor(Color.BLACK);
            Glide.with(context).load(R.drawable.shape).into(holder.icon);
            Glide.with(context).load(R.drawable.search_item_line).into(holder.line);
            Glide.with(context).load(R.drawable.check_red).into(holder.check);

        }

    }


    @Override
    public int getItemCount() {
        return searchItemList.size();
    }

    public void setSearchItemList(ArrayList<SearchItem> searchItemList) {
        this.searchItemList = searchItemList;
    }

}
