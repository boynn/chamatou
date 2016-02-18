package cn.chamatou.biz.widget;

import android.widget.ListView;

/**
 * 
 * @Description: 在布局文件中直接替换listview：并且在最外层嵌套一个scrollview即可
 * 
 * @File: ScrollViewWithListView.java
 * 
 */
public class ScrollViewWithListView extends ListView {

	public ScrollViewWithListView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Integer.MAX_VALUE >> 2,如果不设置，系统默认设置是显示两条
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}