package com.highnes.tour.view.gridview;

import android.widget.GridView;
/***
 * 重写GridView 解决ScrollView与ListView共存导致GridView显示不全的问题
 * 
 * @date 2014-12-11
 * @author FUNY
 * 
 */
public class MyGridView extends GridView  
{  
    public MyGridView(android.content.Context context,  
            android.util.AttributeSet attrs)  
    {  
        super(context, attrs);  
    }  
  
    /** 
     * 设置不滚动 
     */  
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
    {  
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);  
  
    }  
  
}  