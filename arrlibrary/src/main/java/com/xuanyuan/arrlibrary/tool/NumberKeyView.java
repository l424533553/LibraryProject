package com.xuanyuan.arrlibrary.tool;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import com.xuanyuan.arrlibrary.R;

/**
 * 数字键盘 封装类 ,直接启用初始化即可使用
 */
public class NumberKeyView extends GridView {
    private static final String[] DATA_DIGITAL = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "删除", "0", "."};
    private MyAdapter mAdapter;
    private Context context;

    public NumberKeyView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public NumberKeyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public NumberKeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    public void initView() {
        mAdapter = new MyAdapter(context, DATA_DIGITAL);
        setAdapter(mAdapter);
    }



    class MyAdapter extends BaseAdapter {
        private Context context;
        private String[] digitals;

        public MyAdapter(Context context, String[] digitals) {
            this.context = context;
            this.digitals = digitals;
        }

        @Override
        public int getCount() {
            return digitals.length;
        }

        @Override
        public Object getItem(int position) {
            return digitals[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.soft_keyborad_item, null);
                holder = new ViewHolder();
                holder.keyBtn = convertView.findViewById(R.id.keyboard_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.keyBtn.setText(digitals[position]);
            return convertView;
        }
    }

    class ViewHolder {
        Button keyBtn;
    }
}
