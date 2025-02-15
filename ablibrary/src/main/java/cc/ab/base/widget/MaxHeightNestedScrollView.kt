package cc.ab.base.widget

import android.content.Context
import android.util.AttributeSet
import androidx.core.widget.NestedScrollView
import kotlin.math.min

/**
 * @Description 可以设置最大高度的ScrollView；需要在XML设置
 * 1.android:scrollbars="vertical"
 * 2.android:fadeScrollbars="false"
 * 3.android:overScrollMode="ifContentScrolls"
 * @Author：Khaos
 * @Date：2021-06-24
 * @Time：22:03
 */
class MaxHeightNestedScrollView @JvmOverloads constructor(c: Context, a: AttributeSet? = null, d: Int = 0) : NestedScrollView(c, a, d) {
  private val mMaxHeight = 240
  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val child = getChildAt(0)
    child.measure(widthMeasureSpec, heightMeasureSpec)
    setMeasuredDimension(child.measuredWidth, min(child.measuredHeight, mMaxHeight))
  }
}