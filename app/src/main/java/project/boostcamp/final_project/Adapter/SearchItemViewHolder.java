package project.boostcamp.final_project.Adapter;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import project.boostcamp.final_project.Model.SearchItem;
import project.boostcamp.final_project.R;


public class SearchItemViewHolder extends RecyclerView.ViewHolder{

    public ImageView icon, check, line;
    public TextView title, address;
    public LinearLayout background;

    public SearchItemViewHolder(View view){
        super(view);
        background = (LinearLayout)view.findViewById(R.id.background);
        icon = (ImageView)view.findViewById(R.id.icon);
        check = (ImageView)view.findViewById(R.id.check);
        title = (TextView)view.findViewById(R.id.title);
        address = (TextView)view.findViewById(R.id.address);
        line=(ImageView)view.findViewById(R.id.line);

    }

    public void bind(final SearchItem searchItem){

        title.setText(searchItem.getTitle());
        address.setText(searchItem.getAddress());
    }

}
