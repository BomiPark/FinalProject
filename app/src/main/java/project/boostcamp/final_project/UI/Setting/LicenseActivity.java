package project.boostcamp.final_project.UI.Setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import project.boostcamp.final_project.Adapter.LicenseItemAdapter;
import project.boostcamp.final_project.Adapter.LicenseItemViewHolder;
import project.boostcamp.final_project.Model.LicenseItem;
import project.boostcamp.final_project.R;

import static project.boostcamp.final_project.R.array.license_title;

public class LicenseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<LicenseItem> itemList = new ArrayList<>();
    private LicenseItemViewHolder viewHolder;
    private LicenseItemAdapter recycler_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);

        init();
    }

    void init(){

        recyclerView = (RecyclerView)findViewById(R.id.license_recycler);
        recycler_adapter = new LicenseItemAdapter(this, getData(), R.layout.item_license);
        recyclerView.setAdapter(recycler_adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    List<LicenseItem> getData(){

        List<String> license_title = Arrays.asList(getResources().getStringArray(R.array.license_title));
        List<String> license_address = Arrays.asList(getResources().getStringArray(R.array.license_address));
        List<String> license_copyright = Arrays.asList(getResources().getStringArray(R.array.license_copyright));

        for(int index=0; index < license_title.size(); index++){
            LicenseItem item = new LicenseItem(license_title.get(index), license_address.get(index), license_copyright.get(index));
            itemList.add(item);
        }

        return itemList;
    }
}
