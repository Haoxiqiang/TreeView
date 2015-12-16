package orchid.treeview.thread;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import orchid.treeview.entity.Course;

/**
 * Created by haoxiqiang on 15/12/9.
 */
public abstract class TreeRunnable implements Runnable, CallBackListener {

    Context mContext;
    LinkedList<Course> mData;
    Handler mHandler = new Handler(Looper.getMainLooper());

    public TreeRunnable(Context applicationContext) {
        this.mContext = applicationContext;
    }

    @Override
    public void run() {
        try {
            JSONObject jsonData = loadJson("gaozhongshuxue.json");
            JSONArray tree = jsonData.getJSONObject("tree").getJSONArray("children");
            mData = new LinkedList<>();
            createTree(null, tree, null, 1);

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    callBack(mData);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void createTree(Course container, JSONArray children, String parentId, int level) throws JSONException {
        if (children != null) {
            int childrenSize = children.length();
            LinkedList<Course> tree = new LinkedList<>();
            for (int i = 0; i < childrenSize; i++) {
                JSONObject item = children.getJSONObject(i);
                Course course = new Course();
                course.id = item.getString("id");
                course.name = item.getString("name");
                course.level = level;
                course.open = false;
                course.parentId = parentId;
                JSONArray children11 = item.getJSONArray("children");
                createTree(course, children11, course.id, level + 1);
                tree.add(course);
                if (container == null) {
                    //如果是 null,那么表示这个是最顶层的节点,需要把这个对象 add 进去
                    mData.add(course);
                }
            }
            if (container != null) {
                container.addChildren(tree);
            }
        }

    }

    private JSONObject loadJson(String transId) throws JSONException, IOException {
        AssetManager mAssetManager = mContext.getAssets();
        InputStream open = mAssetManager.open(transId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(open));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = reader.readLine()) != null) {
            sb.append(new String(s.getBytes(), "UTF-8"));
        }
        reader.close();
        return new JSONObject(sb.toString());
    }
}
