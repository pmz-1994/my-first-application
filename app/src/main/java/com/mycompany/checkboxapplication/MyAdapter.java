package com.mycompany.checkboxapplication;

import android.content.Context;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyAdapter extends BaseAdapter {

    private List<Test> list;
    // 用SparseBooleanArray来代替map
    private SparseBooleanArray isSelected;
    private Context context;
    /**
     * 全选回调接口
     */
    private CheckedAllListener mListener;

    /**
     * 当所有CheckBox全选时回调
     */
    public interface CheckedAllListener {
        void CheckAll(SparseBooleanArray checkAll);
    }

    void setCheckedAllListener(CheckedAllListener listener) {
        this.mListener = listener;
    }

    MyAdapter(List<Test> list, Context context, SparseBooleanArray isSelected) {
        this.context = context;
        this.list = list;
        this.isSelected = isSelected;
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        for (int i = 0; i < list.size(); i++) {
            isSelected.put(i, false);
        }
    }

    private SparseBooleanArray getIsSelected() {
        return isSelected;
    }

    void setIsSelected(SparseBooleanArray isSelected) {
        this.isSelected = isSelected;
    }

    static class HolderView {
        CheckBox mCb_button;
        TextView mTv_name;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Test getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        HolderView holderView;
        if (convertView == null) {
            holderView = new HolderView();
            // 得到资源文件
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_view_item, null);
            holderView.mCb_button = convertView.findViewById(R.id.cb_button);
            holderView.mTv_name = convertView.findViewById(R.id.tv_name);
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }
        // 给控件赋值
        final Test item = getItem(position);
        if (item != null) {
            holderView.mTv_name.setText(item.getName());
            holderView.mCb_button.setChecked(isSelected.get(position));
        }

        /*
         * 增加checkbox的改变事件，每个item的点击事件
         */
        holderView.mCb_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    Log.i("isPressed", position + " is Pressed.");
                    System.out.println("======>>>>>> " + position + " is Pressed.");
                    if (isChecked) {
                        Toast.makeText(context, "选择item: " + position, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "取消选择item: " + position, Toast.LENGTH_SHORT).show();
                    }
                    isSelected.put(position, isChecked);
                    // 监听回调，是否改变全选按钮的状态
                    mListener.CheckAll(isSelected);
                }
                item.setCheck(isChecked);
            }
        });
        return convertView;
    }
}
