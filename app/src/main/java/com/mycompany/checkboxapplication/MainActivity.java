package com.mycompany.checkboxapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.KeyEvent.KEYCODE_VOLUME_DOWN;
import static android.view.KeyEvent.KEYCODE_VOLUME_UP;

public class MainActivity extends AppCompatActivity implements MyAdapter.CheckedAllListener {

    MyAdapter adapter;
    ListView listView;
    List<Test> list;
    CheckBox btn_selectAll;
    Button btn_selectOthers;
    Button btn_cancel;
    Button btn_execute;

    SparseBooleanArray isChecked;
    // 判断是否全选按钮按下
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 找到组件
        listView = findViewById(R.id.list_view);
        btn_selectAll = findViewById(R.id.radio_button_selectAll);
        btn_selectOthers = findViewById(R.id.button_selectOthers);
        btn_cancel = findViewById(R.id.button_cancel);
        btn_execute = findViewById(R.id.button_execute);

        isChecked = new SparseBooleanArray();
        list = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            Test test = new Test();
            test.setName("test" + i);
            list.add(test);
            // 初始化
            // isChecked.put(i, false);
        }
        adapter = new MyAdapter(list,this, isChecked);
        adapter.setCheckedAllListener(this);
        listView.setAdapter(adapter);

        btn_selectOthers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遍历list，将已选的设为未选，未选的设为已选
                for (int i = 0; i < list.size(); i++) {
                    /*if (MyAdapter.getIsSelected().get(i)) {
                        MyAdapter.getIsSelected().put(i, false);
                    } else {
                        MyAdapter.getIsSelected().put(i, true);
                    }*/
                    if (isChecked.get(i)) {
                        isChecked.put(i, false);
                    } else {
                        isChecked.put(i, true);
                    }
                }
                adapter.setIsSelected(isChecked);
                CheckAll(isChecked);
                // 更新Adapter，刷新ListView的显示
                adapter.notifyDataSetChanged();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalItem = 0;
                // 遍历list的长度，将已选的按钮设为未选
                for (int i = 0; i < list.size(); i++) {
                    /*if (MyAdapter.getIsSelected().get(i)) {
                        MyAdapter.getIsSelected().put(i, false);
                    }*/
                    if (isChecked.get(i)) {
                        isChecked.put(i, false);
                        totalItem++;
                    }
                }
                if (totalItem > 0) {
                    Toast.makeText(MainActivity.this, "共取消选择 " + totalItem + " 项item", Toast.LENGTH_SHORT).show();
                }
                adapter.setIsSelected(isChecked);
                CheckAll(isChecked);
                adapter.notifyDataSetChanged();
            }
        });
        /*
            想要ListView的item点击事件生效需要修改xml中的设置为
            android:clickable="true"
            android:focusable="false"
        */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("点击", "item: " + position + " 被点击");
                System.out.println("======>>>>>> item: " + position + " 被点击");
                MyAdapter.HolderView holderView = (MyAdapter.HolderView) view.getTag();
                boolean isChecked = holderView.mCb_button.isChecked();
                holderView.mCb_button.setChecked(!isChecked);
                MainActivity.this.isChecked.put(position, !isChecked);
                if (isChecked) {
                    Toast.makeText(MainActivity.this, "取消选择 item: " + position, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "选择 item: " + position, Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("长按", "item: " + position + " 被长按");
                System.out.println("======>>>>>> item: " + position + " 被长按");
                MyAdapter.HolderView holderView = (MyAdapter.HolderView) view.getTag();
                boolean isChecked = holderView.mCb_button.isChecked();
                holderView.mCb_button.setChecked(!isChecked);
                if (isChecked) {
                    Toast.makeText(MainActivity.this, "取消选择item: " + position, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "选择item: " + position, Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        /*// 按键弹起触发的事件
        btn_selectAll.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KEYCODE_VOLUME_DOWN) {
                    btn_selectAll.setFocusable(false);
                    btn_selectOthers.setFocusable(true);
                    btn_selectOthers.requestFocus();
                    btn_selectOthers.findFocus();
                    return true;
                }
                return false;
            }
        });

        btn_selectOthers.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KEYCODE_VOLUME_DOWN) {
                    btn_selectOthers.setFocusable(false);
                    btn_cancel.setFocusable(true);
                    btn_cancel.requestFocus();
                    btn_cancel.findFocus();
                    return true;
                } else if (keyCode == KEYCODE_VOLUME_UP) {
                    btn_selectOthers.setFocusable(false);
                    btn_selectAll.setFocusable(true);
                    btn_selectAll.requestFocus();
                    btn_selectAll.findFocus();
                    return true;
                }
                return false;
            }
        });

        // 焦点改变事件
        btn_selectOthers.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    btn_selectOthers.setBackgroundResource(R.drawable.my_button_onfocus);
                }else {
                    btn_selectOthers.setBackgroundResource(R.drawable.my_button_normal);
                }
            }
        });

        btn_cancel.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KEYCODE_VOLUME_DOWN) {
                    btn_cancel.setFocusable(false);
                    btn_execute.setFocusable(true);
                    btn_execute.requestFocus();
                    btn_execute.findFocus();
                    return true;
                } else if (keyCode == KEYCODE_VOLUME_UP) {
                    btn_cancel.setFocusable(false);
                    btn_selectOthers.setFocusable(true);
                    btn_selectOthers.requestFocus();
                    btn_selectOthers.findFocus();
                    return true;
                }
                return false;
            }
        });

        btn_cancel.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    btn_cancel.setBackgroundResource(R.drawable.my_button_onfocus);
                }else {
                    btn_cancel.setBackgroundResource(R.drawable.my_button_normal);
                }
            }
        });

        btn_execute.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KEYCODE_VOLUME_UP) {
                    btn_execute.setFocusable(false);
                    btn_cancel.setFocusable(true);
                    btn_cancel.requestFocus();
                    btn_cancel.findFocus();
                    return true;
                }
                return false;
            }
        });

        btn_execute.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    btn_execute.setBackgroundResource(R.drawable.my_button_onfocus);
                }else {
                    btn_execute.setBackgroundResource(R.drawable.my_button_normal);
                }
            }
        });*/
    }

    /*
     * 全选按钮的点击事件
     */
    public void SelectAll(View view) {
        flag = btn_selectAll.isChecked();
        System.out.println("======>>>>>> " + flag);
        if(flag) {
            for(int i = 0; i < list.size(); i++) {
                isChecked.put(i, true);
                // MyAdapter.getIsSelected().put(i, true);
            }
            Toast.makeText(MainActivity.this, "共选择 " + list.size() + " 项item", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < list.size(); i++) {
                isChecked.put(i, false);
                // MyAdapter.getIsSelected().put(i, false);
            }
            Toast.makeText(MainActivity.this, "共取消选择 " + list.size() + " 项item", Toast.LENGTH_SHORT).show();

        }
        adapter.setIsSelected(isChecked);
        // 为什么这行注释不影响全选？？？难道是adapter.notifyDataSetChanged()的原因？
        // CheckAll(isChecked);
        adapter.notifyDataSetChanged();
    }

    /*
     * 执行按钮的点击事件
     */
    public void ExecuteAll(View view) {
        int totalItem = 0;
        for(int i = 0; i < list.size(); i++) {
            if(isChecked.get(i)) {
                Log.i("isPressed", i + " is Pressed.");
                System.out.println("======>>>>>> " + i + " is Pressed.");
                totalItem++;
            }
        }
        if (totalItem > 0) {
            Toast.makeText(MainActivity.this, "共选择执行 " + totalItem + " 项item", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 全选按钮的回调事件，是否进行全选
     * @param checkAll 修改全选按钮需要检查的参数，实际上就是Adapter中的SparseBooleanArray
     */
    @Override
    public void CheckAll(SparseBooleanArray checkAll) {
        int indexOfFalse = checkAll.indexOfValue(false);
        int indexOfTrue = checkAll.indexOfValue(true);
        System.out.println("indexOfFalse: " + indexOfFalse + " ---- " + "indexOfTrue: " + indexOfTrue);
        // 判断SparseBooleanArray是否含有true
        if (indexOfFalse < 0) {
                if(!btn_selectAll.isChecked()) {
                this.flag = true;
                btn_selectAll.setChecked(true);
            }
        } else {
            if(btn_selectAll.isChecked()) {
                this.flag = false;
                btn_selectAll.setChecked(false);
            }
        }
    }

    /*// 按键弹起触发的事件
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KEYCODE_VOLUME_UP:
                System.out.println("a");
                break;
            case KEYCODE_VOLUME_DOWN:
                System.out.println("b");
                break;
        }
        return super.onKeyUp(keyCode, event);
    }*/
}
