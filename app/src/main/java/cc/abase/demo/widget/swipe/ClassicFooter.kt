package cc.abase.demo.widget.swipe

import android.content.Context
import android.util.AttributeSet

/**
 * Description:
 * @author: Khaos
 * @date: 2020/5/12 21:03
 */
class ClassicFooter @kotlin.jvm.JvmOverloads constructor(
  context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : com.billy.android.swipe.refresh.ClassicFooter(context, attrs, defStyleAttr) {
  override fun onReset() {}
}