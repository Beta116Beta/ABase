package cc.ab.base.utils

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import cc.ab.base.ext.launchError
import com.blankj.utilcode.util.ImageUtils
import kotlinx.coroutines.*
import java.io.File

/**
 * Author:Khaos
 * Date:2020-11-14
 * Time:16:13
 */
object MediaMetadataRetrieverUtils {
  //获取网络视频封面
  fun getNetVideoCover(retriever: MediaMetadataRetriever, cacheFile: File, url: String, call: (bit: Bitmap?) -> Unit) {
    launchError(Dispatchers.IO, handler = { _, _ ->
      retriever.release()
      CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch(Dispatchers.Main) { call.invoke(null) }
    }) {
      retriever.setDataSource(url, HashMap())
      //以微秒为单位
      val bit = retriever.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
      retriever.release()
      CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate).launch(Dispatchers.Main) { call.invoke(bit) }
      //缓存封面
      bit?.let { b -> ImageUtils.save(b, cacheFile, Bitmap.CompressFormat.PNG) }
    }
  }
}