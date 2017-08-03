package project.boostcamp.final_project.Model;

import android.content.ClipData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qkrqh on 2017-08-01.
 */

public class ItemList {

    List<Item> items;
    int display; // 반환된 검색 결과 갯수 -> 0이면 처리

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public void setItem(List<Item> item) {
        this.items = item;
    }

    public ItemList(){
        items = new ArrayList<>();
    }

    public List<Item> getItem() {
        return items;
    }

    public void setItem(ArrayList<Item> item) {
        this.items = item;
    }

    public String toString(){
        return items.toString();
    }
}
