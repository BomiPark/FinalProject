package project.boostcamp.final_project.Interface;

import project.boostcamp.final_project.Model.TodoItem;

/**
 * Created by qkrqh on 2017-07-20.
 */

public interface FragmentChangeListener {

    void changeFragment(int now, int to, TodoItem item);

    int setStatus(int now);

    TodoItem getCurrentItem();

}
