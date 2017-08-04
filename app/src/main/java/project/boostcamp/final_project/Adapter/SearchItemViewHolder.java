package project.boostcamp.final_project.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import project.boostcamp.final_project.Model.Item;
import project.boostcamp.final_project.R;


public class SearchItemViewHolder extends RecyclerView.ViewHolder{

    public ImageView icon, check;
    public TextView title, address;

    public SearchItemViewHolder(View view){
        super(view);
        icon = (ImageView)view.findViewById(R.id.icon);
        check = (ImageView)view.findViewById(R.id.check);
        title = (TextView)view.findViewById(R.id.title);
        address = (TextView)view.findViewById(R.id.address);
    }

    public void bind(final Item item){

        title.setText(item.getTitle());
        address.setText(item.getAddress());
    }
}
