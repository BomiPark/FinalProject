package project.boostcamp.final_project.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import project.boostcamp.final_project.Model.SearchItem;
import project.boostcamp.final_project.R;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemViewHolder>{

    private Context context;
    private List<SearchItem> searchItemList = new ArrayList<>();
    private int item_layout;
    private SearchItemViewHolder viewHolder;

    public SearchItemAdapter(Context context, ArrayList<SearchItem> searchItemList, int item_layout){
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
        if(SearchItemViewHolder.checkPosition == position)
            ((SearchItemViewHolder) holder).bind(searchItemList.get(position));
        else {
            ((SearchItemViewHolder) holder).bind(searchItemList.get(position));
            holder.background.setBackgroundColor(Color.parseColor("#dddddd"));
        }
    }

    @Override
    public int getItemCount() {
        return searchItemList.size();
    }

    public void setSearchItemList(ArrayList<SearchItem> searchItemList){
        this.searchItemList = searchItemList;
    }

    public SearchItem getSelectItem() {
        SearchItem item = new SearchItem();
        item.setTitle("dd");
        return item;
    }

    public static void setLayout(){

    }
}
