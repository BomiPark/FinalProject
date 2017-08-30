package project.boostcamp.final_project.Adapter;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import project.boostcamp.final_project.Model.LicenseItem;
import project.boostcamp.final_project.R;

public class LicenseItemViewHolder extends RecyclerView.ViewHolder {

    private TextView txt_title;
    private TextView txt_address;
    private TextView txt_copyright;

    public LicenseItemViewHolder(View view) {
        super(view);
        txt_title = (TextView)view.findViewById(R.id.txt_license_title);
        txt_address = (TextView)view.findViewById(R.id.txt_license_address);
        txt_copyright = (TextView)view.findViewById(R.id.txt_license_copyright);
    }

    public void bind(final LicenseItem item){
        txt_title.setText(item.getTitle());
        txt_address.setText(item.getAddress());
        txt_address.setLinkTextColor(Color.BLUE);
        Linkify.addLinks(txt_address, Linkify.WEB_URLS);// under Line
        txt_copyright.setText(item.getCopyright());
    }
}
