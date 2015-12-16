package orchid.treeview.thread;

import java.util.LinkedList;

import orchid.treeview.entity.Course;

/**
 * Created by haoxiqiang on 15/12/9.
 */
public interface CallBackListener {
    void callBack(LinkedList<Course> list);
}
