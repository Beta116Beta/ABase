package cc.abase.demo.component.blur

import android.content.Context
import android.content.Intent
import cc.ab.base.ext.loadImgHorizontalBlur
import cc.ab.base.ext.xmlToString
import cc.abase.demo.R
import cc.abase.demo.component.comm.CommBindTitleActivity
import cc.abase.demo.constants.ImageUrls
import cc.abase.demo.databinding.ActivityBlurBinding

/**
 * @Description
 * @Author：Khaos
 * @Date：2021/1/9
 * @Time：10:04
 */
class BlurActivity : CommBindTitleActivity<ActivityBlurBinding>() {
  //<editor-fold defaultstate="collapsed" desc="外部跳转">
  companion object {
    fun startActivity(context: Context) {
      context.startActivity(Intent(context, BlurActivity::class.java))
    }
  }
  //</editor-fold>

  //<editor-fold defaultstate="collapsed" desc="初始化View">
  override fun initContentView() {
    setTitleText(R.string.高斯模糊.xmlToString())
    val url = ImageUrls.image_1125x642
    viewBinding.blurIv1.loadImgHorizontalBlur(url, holderRatio = 1125f / 642, blurRadius = 5f)
    viewBinding.blurIv2.loadImgHorizontalBlur(url, holderRatio = 1125f / 642, blurRadius = 10f, blackWhite = true)
    viewBinding.blurIv3.loadImgHorizontalBlur(url, holderRatio = 1125f / 642, blurRadius = 15f)
  }
  //</editor-fold>
}