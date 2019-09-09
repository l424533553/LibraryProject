package com.xuanyuan.libraryproject

import android.app.Application
import android.content.Context
import android.util.Log
import io.multimoon.colorful.*

/**
 * 作者：罗发新
 * 时间：2019/5/22 0022    星期三
 * 邮件：424533553@qq.com
 * 说明：
 *
 *  ThemeColor 主题色
 *  ThemeColor.RED
 *  ThemeColor.PINK
 *  ThemeColor.PURPLE
 *  ThemeColor.DEEP_PURPLE
 *  ThemeColor.INDIGO
 *  ThemeColor.BLUE
 *  ThemeColor.LIGHT_BLUE
 *  ThemeColor.CYAN
 *  ThemeColor.TEAL
 *  ThemeColor.GREEN
 *  ThemeColor.LIGHT_GREEN
 *  ThemeColor.LIME
 *  ThemeColor.YELLOW
 *  ThemeColor.AMBER
 *  ThemeColor.ORANGE
 *  ThemeColor.DEEP_ORANGE
 *  ThemeColor.BROWN
 *  ThemeColor.GREY
 *  ThemeColor.BLUE_GREY
 *  ThemeColor.WHITE
 *  ThemeColor.BLACK
 *
 */

class ColorApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initColorSelf()
//        initColorSelf(applicationContext)
    }

    private fun initColorSelf() {
        //初始化默认的颜色背景
        // translucent 开启半透明    // useDarkTheme 开启暗主题
        val defaults = Defaults(
                primaryColor = ThemeColor.BLUE,
                accentColor  =  ThemeColor.GREEN,
                useDarkTheme = false,
                translucent  = false)
        initColorful(this, defaults)
    }

    /**
     * 初始化 颜色设置
    */
    private fun initColorSelf(context: Context) {
        var myCustomColor1 = CustomThemeColor(
                context,
                R.style.my_custom_primary_color,
                R.style.my_custom_primary_dark_color,
                R.color.md_red_200, // <= use the color you defined in my_custom_primary_color
                R.color.md_red_400 // <= use the color you defined in my_custom_primary_dark_color
        )
        // used as accent color, dark color is irrelevant...
        var myCustomColor2 = CustomThemeColor(
                context,
                R.style.my_custom_accent_color,
                R.style.my_custom_accent_color,
                R.color.md_yellow_700, // <= use the color you defined in my_custom_accent_color
                R.color.md_yellow_700 // <= use the color you defined in my_custom_accent_color
        )
        //use this custom theme color object like you would use any ThemeColor.<COLOR> enum object, e.g.
        var defaults = Defaults(
                primaryColor = myCustomColor1,
                accentColor = myCustomColor2,
                useDarkTheme = false,
                translucent = true
                , customTheme = 1
        )

//        val defaults = Defaults(
//                primaryColor = ThemeColor.BLUE,
//                accentColor = ThemeColor.  GREEN,
//                useDarkTheme = false,
//                translucent = false)

        initColorful(this, defaults)
    }

    /**
     * 可以默认修改 color 的设置值
     */
    public fun setColorful(context: Context) {
        Colorful().edit()
                .setPrimaryColor(ThemeColor.RED)
                .setAccentColor(ThemeColor.BLUE)
                .setDarkTheme(false)
                .setTranslucent(true)
                .apply(context) {
                    //一旦主题修改完后，可以进行相关操作
                }
    }

    /**
     * 可以融合自己的主题颜色风格。
     */
    private fun setColorful2(context: Context) {
        Colorful().edit()
                .setPrimaryColor(ThemeColor.RED)
                .setAccentColor(ThemeColor.BLUE)
                .setDarkTheme(true)
                .setTranslucent(true)
                .apply(context) {
                    //一旦主题修改完后，可以进行相关操作
                }
    }


    private fun test() {
        val a = Colorful().getPrimaryColor().getColorPack().dark().asInt()
        Log.d("555", "a=$a")

        val b = Colorful().getAccentColor().getColorPack().normal().asHex()
        Log.d("555", "b=$b")
        //将返回boolean是否启用黑暗主题的值
        val c = Colorful().getDarkTheme()
        Log.d("555", "c=$c")
        //将返回boolean当前样式是否启用了transluceny 的值。
        val d = Colorful().getTranslucent()
        Log.d("555", "d=$d")

    }

}