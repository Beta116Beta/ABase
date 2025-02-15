package cc.abase.demo.rxhttp.config

import cc.ab.base.config.PathConfig
import cc.ab.base.ext.logE
import cc.ab.base.utils.CharlesUtils
import cc.ab.base.utils.MyGsonUtil
import cc.abase.demo.config.HeaderManger
import cc.abase.demo.constants.api.WanUrls
import cc.abase.demo.rxhttp.interceptor.TokenInterceptor
import com.ayvytr.okhttploginterceptor.LoggingInterceptor
import okhttp3.OkHttpClient
import okhttp3.OkHttpClient.Builder
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.cahce.CacheMode
import rxhttp.wrapper.converter.GsonConverter
import rxhttp.wrapper.param.Param
import rxhttp.wrapper.ssl.HttpsUtils
import rxhttp.wrapper.utils.GsonUtil
import java.io.File
import java.lang.reflect.Field
import java.util.concurrent.TimeUnit

/**
 * Description:
 * @author: Khaos
 * @date: 2020/3/4 15:13
 */
object RxHttpConfig {
  private var hasInit = false

  fun init() {
    if (hasInit) return
    hasInit = true
    initRxHttp()
  }

  //去除无意义的参数key，这里把header的共同参数剔除
  private val noCacheKeys = arrayOf(
    "Connection",
    "Accept",
    "Content-Type",
    "Charset",
  )

  //初始化RxHttp https://github.com/liujingxing/rxhttp/wiki
  private fun initRxHttp() {
    val mode = CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE
    RxHttpPlugins.init(getDefaultOkHttpClient()) //自定义OkHttpClient对象
      .setDebug(false) //是否开启调试模式，开启后，logcat过滤RxHttp，即可看到整个请求流程日志
      .setConverter(GsonConverter.create(MyGsonUtil.buildGson()))
      .setCache(File(PathConfig.API_CACHE_DIR), 10 * 1024 * 1024L, mode, 1 * 60 * 60 * 1000L)  //配置缓存目录，最大size及缓存模式 (设置最大缓存为10M，缓存有效时长为1小时)
      .setExcludeCacheKeys(*noCacheKeys) //设置一些key，不参与cacheKey的组拼
      //.setResultDecoder(Function)//设置数据解密/解码器，非必须
      //.setConverter(IConverter)//设置全局的转换器，非必须
      .setOnParamAssembly { p: Param<*> -> //设置公共参数/请求头回调
        p.add("platform", "RxHttp")
        p.addAllHeader(HeaderManger.getStaticHeaders()) //添加公共参数
        if (p.httpUrl.host == WanUrls.HOST) { //只给WanAndroid的接口添加Token
          if (!HeaderManger.noTokenUrls.any { s -> (p.httpUrl).toString().contains(s, true) }) { //如果没有在排除列表，则添加
            HeaderManger.getTokenPair()?.let { p.addHeader(it.first, it.second) }
          }
        }
        p
      }
    //反射修改内部gson
    try {
      val gsonField: Field = GsonUtil::class.java.getDeclaredField("gson")
      gsonField.isAccessible = true
      gsonField.set(null, MyGsonUtil.buildGson())
      "GsonUtil的gson替换成功".logE()
    } catch (e: Exception) {
      e.printStackTrace()
      "GsonUtil的gson替换失败".logE()
    }
  }

  //OkHttpClient
  private fun getDefaultOkHttpClient(): OkHttpClient {
    val builder = getOkHttpClient()
    builder.addInterceptor(TokenInterceptor())
    builder.addInterceptor(LoggingInterceptor(isShowAll = true))
    return builder.build()
  }

  //其他配置获取Okhttp对象
  fun getOkHttpClient(): Builder {
    val sslParams = HttpsUtils.getSslSocketFactory()
    val builder = Builder()
      //.cookieJar(CookieStore())//如果启用自动管理，则不需要在TokenInterceptor中进行保存和initRxHttp()进行读取
      .connectTimeout(30, TimeUnit.SECONDS)
      .readTimeout(30, TimeUnit.SECONDS)
      .writeTimeout(30, TimeUnit.SECONDS)
      .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) //添加信任证书
      .hostnameVerifier { _, _ -> true } //忽略host验证
    val util = CharlesUtils.getInstance()
    util.setOkHttpCharlesSSL(builder, util.getCharlesInputStream("charles.pem"))
    return builder
  }
}