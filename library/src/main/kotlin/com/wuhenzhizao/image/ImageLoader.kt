package com.wuhenzhizao.image

import android.content.Context
import android.widget.ImageView
import java.lang.ref.WeakReference

object ImageLoader {
    private lateinit var engine: ImageEngine
    /**
     *Imageloader初始化
     */
    fun init(engineType: EngineType, context: Context) {
        initEngine(engineType)
        engine.initConfig(context.applicationContext)
    }

    fun release(){
        engine.release()
    }

    private fun initEngine(engineType: EngineType) {
        engine = when (engineType) {
            EngineType.Glide -> {
                GlideEngine()
            }

            EngineType.Fresco -> {
                FrescoEngine()
            }

            EngineType.Picasso -> {
                PicassoEngine()
            }
        }
    }

    fun with(context: Context): Builder {
        return Builder(context)
    }

    private fun display(context: Context, url: String, view: ImageView, options: Options) {
        engine.load(context, url, view, options)
    }


    enum class EngineType(val key: String, val engineType: Class<out ImageEngine>) {
        Glide("glide", GlideEngine::class.java),
        Fresco("fresco", FrescoEngine::class.java),
        Picasso("picasso", PicassoEngine::class.java),

    }

    open class Builder(context: Context) {
        private var wrContext: WeakReference<Context> = WeakReference(context)
        private val options: Options = Options()
        private lateinit var url: String
        fun load(url: String): Builder {
            this.url = url
            return this
        }

        fun into(view: ImageView) {
            display(wrContext.get()!!, url, view, options)
        }

        fun placeHolder(resourceId: Int): Builder {
            options.placeHolder = resourceId
            return this
        }

        fun thumbnail(url: String): Builder {
            options.thumbnail = url
            return this
        }

        fun corner(value: Float): Builder {
            options.corner = value
            return this
        }

        fun resize(width: Int, height: Int): Builder {
            val imageInfo = Options.ImageInfo()
            imageInfo.height = height
            imageInfo.width = width
            options.imageInfo = imageInfo
            return this
        }
    }
}