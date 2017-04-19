package org.demo.cn.widget;


import java.util.ArrayList;

import org.demo.cn.utils.NameTouchMovementMethod;
import org.demo.cn.utils.StringUtils;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class CommentTextView extends TextView {
    private static final String REPLE_FLAG_START = "�ظ�@";
    private static final String REPLE_FLAG_END = "��";
    private static final String REPLE = "�ظ�";

    public CommentTextView(Context context) {
        super(context);
    }

    public CommentTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setData(String data, final OnSpanonClickListener listener) {
        setText("");
        if (data.startsWith(REPLE_FLAG_START) || data.contains(REPLE_FLAG_END)) {
            setText(REPLE);
            //�ҵ��ǳƽ�����λ��
            int p = data.indexOf(REPLE_FLAG_END);
            //��ȡ�ǳ�
            String nickName = "@" + data.substring(REPLE_FLAG_START.length(), p);
            SpannableString spannableString = new SpannableString(nickName);
            spannableString.setSpan(new NameClickableSpan(nickName.substring(1)) {
                @Override
                public void onClick(View widget) {
                    listener.onClick(getName());
                }
            }, 0, nickName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            append(spannableString);
            data = data.substring(p, data.length());
        }
        //�ж�@�����
        if (data.contains("@")) {
            String[] arr = StringUtils.splitWithFlag(data, "@");
            ArrayList<String> dataList = getArray(arr);
            for (String str : dataList) {
                if (str.startsWith("@")) {
                    SpannableString spannableString = new SpannableString(str);
                    spannableString.setSpan(new NameClickableSpan(str.substring(1)) {
                        @Override
                        public void onClick(View widget) {
                            listener.onClick(getName());
                        }
                    }, 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    append(spannableString);
                } else {
                    append(str);
                }
            }
        } else {
            append(data);
        }
        setHighlightColor(Color.TRANSPARENT); //���õ�������ɫΪ͸���������һֱ���ָ���
        setMovementMethod(new NameTouchMovementMethod());//��ʼ��Ӧ����¼�
    }


    /**
     * ��һ���и��ַ���
     *
     * @param arr
     * @return
     */
    private ArrayList<String> getArray(String[] arr) {
        ArrayList<String> newDatArrayList = new ArrayList<>();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].contains("@")) {
                String myChar = arr[i];
                int p = myChar.indexOf(" ");
                if (p == -1) {
                    newDatArrayList.add(arr[i]);
                } else {
                    newDatArrayList.add(myChar.substring(0, p));
                    newDatArrayList.add(myChar.substring(p));
                }
            } else {
                newDatArrayList.add(arr[i]);
            }
        }
        return newDatArrayList;

    }


    public interface OnSpanonClickListener {
        void onClick(String spanStr);
    }
}
