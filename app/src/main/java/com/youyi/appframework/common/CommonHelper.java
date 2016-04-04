package com.youyi.appframework.common;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.youyi.appframework.ui.dialog.LoadingDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rain on 2016/3/11.
 */
public class CommonHelper {

    /**
     * 当ScrollView与ListView冲突的时候，用这个方法设置listView的高
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 选择数字
     *
     * @param context
     * @param text
     * @param min
     * @param max
     */
    public static void numberPicker(Context context, final EditText text, int min, int max) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        int position = 0;
        final String[] items = new String[max - min + 1];
        for (int i = 0; i < items.length; i++) {
            String value = String.valueOf(i + 30);
            items[i] = value;
            if (text.getText().toString().equals(value)) {
                position = i;
            }
        }
        builder.setSingleChoiceItems(items, position, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                text.setText(items[which]);
                dialog.dismiss();
            }
        });
        builder.show();
    }

    /**
     * @param context
     * @param date    文本编辑器，规定日期格式：yyyy-MM-dd
     */
    public static void datePicker(Context context, final EditText date) {
        String[] dateArray = null;
        if (date.getText() != null && !date.getText().toString().equals("")) {
            dateArray = date.getText().toString().split("-");
        } else {
            dateArray = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).split("-");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DatePicker datePicker = new DatePicker(context);
        datePicker.setCalendarViewShown(true);
        // datePicker.setMaxDate(System.currentTimeMillis());
        datePicker.init(Integer.valueOf(dateArray[0]), Integer.valueOf(dateArray[1]) - 1, Integer.valueOf(dateArray[2]), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker picker, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                date.setText(sdf.format(calendar.getTime()));
            }
        });
        builder.setView(datePicker);
        builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    /**
     * @param context
     * @param date    文本编辑器，规定日期格式：yyyy-MM-dd
     */
    public static void datePicker(Context context, final TextView date) {
        String[] dateArray = null;
        if (date.getText() != null && !date.getText().toString().equals("")) {
            dateArray = date.getText().toString().split("-");
        } else {
            dateArray = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).split("-");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        DatePicker datePicker = new DatePicker(context);
        datePicker.setCalendarViewShown(true);
        // datePicker.setMaxDate(System.currentTimeMillis());
        datePicker.init(Integer.valueOf(dateArray[0]), Integer.valueOf(dateArray[1]) - 1, Integer.valueOf(dateArray[2]), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker picker, int year, int month, int day) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                date.setText(sdf.format(calendar.getTime()));
            }
        });
        builder.setView(datePicker);
        builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 获取本地drawable 转换到bitmap
     *
     * @param resId
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitMapFromDrawable(Context context, int resId, int width, int height) {
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), resId);
        if (width <= 0 || height <= 0) {
            return bm;
        }
        return Bitmap.createScaledBitmap(bm, width, height, false);
    }

    /**
     * show loading
     */
    public static void showLoading(Context context, String msg) {
        LoadingDialog.show(context, msg);
    }

    /**
     * 持续显示loading
     *
     * @param context
     * @param msg
     */
    public static void showLoadingLast(Context context, String msg) {
        LoadingDialog.showLast(context, msg);
    }

    /**
     * hide loading
     */
    public static void hideLoading() {
        LoadingDialog.dispose();
    }

    /**
     * dp转像素
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 像素转dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

}
