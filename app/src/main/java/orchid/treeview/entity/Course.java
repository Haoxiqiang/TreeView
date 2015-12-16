package orchid.treeview.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;

/**
 * Created by haoxiqiang on 15/12/9.
 */
public class Course {

    //标识
    @Expose
    @SerializedName("id")
    public String id;
    //描述信息
    @Expose
    @SerializedName("name")
    public String name;
    //层级
    public int level;
    //是否打开的状态
    public boolean open;
    //父节点的标识
    public String parentId;
    //子节点的容器
    public LinkedList<Course> children = new LinkedList<>();

    public boolean hasChild() {
        return children != null && children.size() > 0;
    }

    public void addChildren(LinkedList<Course> children) {
        this.children.clear();
        this.children.addAll(children);
    }

}
