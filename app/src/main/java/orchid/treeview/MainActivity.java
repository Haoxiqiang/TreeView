package orchid.treeview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

import orchid.treeview.animator.TreeAnimator;
import orchid.treeview.entity.Course;
import orchid.treeview.thread.IOService;
import orchid.treeview.thread.TreeRunnable;


public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    ViewAdapter mViewAdapter;
    LinkedList<Course> mData = new LinkedList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mViewAdapter = new ViewAdapter();
        mRecyclerView.setAdapter(mViewAdapter);
        mRecyclerView.setItemAnimator(new TreeAnimator());

        //这里把数据置入一个子线程中去读取处理,知道结果返回之后通知 Adapter
        IOService.getInstance().execute(new TreeRunnable(getApplicationContext()) {
            @Override
            public void callBack(LinkedList<Course> list) {
                mData.clear();
                mData.addAll(list);
                mViewAdapter.notifyDataSetChanged();
            }
        });
    }

    class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.TextViewHolder> {

        @Override
        public TextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TextViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
        }

        @Override
        public void onBindViewHolder(TextViewHolder holder, final int position) {
            final Course course = mData.get(position);

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.setMargins((int) (48 * (course.level - 0.5f)), layoutParams.topMargin, layoutParams.rightMargin, layoutParams.bottomMargin);

            holder.tv.setText(String.format("Lv%s : %s", course.level, course.name));
            holder.tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dispatchClick(mData, course)) {
                        //TODO
                    }
                    //TODO
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }


        int size = 0;

        public boolean dispatchClick(LinkedList<Course> container, Course course) {

            if (container == null || course == null) {
                return false;
            }

            if (course.hasChild()) {

                int insertPosition = container.indexOf(course) + 1;

                if (course.open) {
                    size = 0;
                    removeAllChildren(container, course);
                    notifyItemRangeRemoved(insertPosition, size);
                } else {
                    course.open = true;
                    container.addAll(insertPosition, course.children);
                    notifyItemRangeInserted(insertPosition, course.children.size());
                }

                return true;
            }

            return false;
        }

        private void removeAllChildren(LinkedList<Course> container, Course course) {

            course.open = false;
            int childrenSize = course.children.size();
            for (Course tree11 : course.children) {
                if (tree11.hasChild() && tree11.open) {
                    tree11.open = false;
                    removeAllChildren(container, tree11);
                }
            }

            size += childrenSize;
            container.removeAll(course.children);
        }


        class TextViewHolder extends RecyclerView.ViewHolder {

            TextView tv;

            public TextViewHolder(View itemView) {
                super(itemView);
                tv = (TextView) itemView.findViewById(R.id.tv);
            }
        }
    }
}
