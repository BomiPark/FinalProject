package project.boostcamp.final_project.listener;

import android.view.View;

/**
 * Created by qkrqh on 2017-08-15.
 */

public interface TodoMainClickListener {

    void onItemClick(View view, int position);

    void onLongClick(View view, int position);
}
