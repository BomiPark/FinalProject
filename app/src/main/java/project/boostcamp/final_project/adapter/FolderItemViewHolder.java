package project.boostcamp.final_project.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import project.boostcamp.final_project.model.FolderItem;
import project.boostcamp.final_project.R;

/**
 * Created by qkrqh on 2017-08-13.
 */

public class FolderItemViewHolder extends RecyclerView.ViewHolder {

    private TextView folder;

    public FolderItemViewHolder(View view){
        super(view);
        folder = (TextView) view.findViewById(R.id.folder);
    }

    public void bind(final FolderItem item){
        folder.setText(item.getFolder());
    }
}
