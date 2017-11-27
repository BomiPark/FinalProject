package project.boostcamp.final_project.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qkrqh on 2017-08-01.
 */

public class SearchItemList {

    @SerializedName("items")
    private List<SearchItem> searchItems;
    private int display; // 반환된 검색 결과 갯수 -> 0이면 처리

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public void setItem(List<SearchItem> searchItem) {
        this.searchItems = searchItem;
    }

    public SearchItemList(){
        searchItems = new ArrayList<>();
    }

    public List<SearchItem> getItem() {
        return searchItems;
    }

    public void setItem(ArrayList<SearchItem> searchItem) {
        this.searchItems = searchItem;
    }

    public String toString(){
        return searchItems.toString();
    }
}
