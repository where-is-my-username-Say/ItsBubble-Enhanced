package com.example.itsbubble

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UIStateManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREFS_NAME = "ItsBubblePrefs"
        const val KEY_SELECTED_GAME_URL = "selected_game_url"
        const val KEY_SELECTED_GAME_PACKAGE = "selected_game_package"
        const val KEY_BUBBLE_X = "bubble_x_percent"
        const val KEY_BUBBLE_Y = "bubble_y_percent"
        const val KEY_MAP_X = "map_x"
        const val KEY_MAP_Y = "map_y"
        const val KEY_MAP_WIDTH = "map_width"
        const val KEY_MAP_HEIGHT = "map_height"
        const val KEY_BUBBLE_SIZE = "bubble_size"
        const val KEY_BUBBLE_ALPHA = "bubble_alpha"
        const val DEFAULT_BUBBLE_SIZE = 150
        const val DEFAULT_MAP_WIDTH = 1000
        const val DEFAULT_MAP_HEIGHT = 1500
    }

    var selectedGameUrl: String
        get() = prefs.getString(KEY_SELECTED_GAME_URL, "https://act.hoyolab.com/ys/app/interactive-map") ?: "https://act.hoyolab.com/ys/app/interactive-map"
        set(value) = prefs.edit { putString(KEY_SELECTED_GAME_URL, value) }

    var selectedGamePackage: String?
        get() = prefs.getString(KEY_SELECTED_GAME_PACKAGE, null)
        set(value) = prefs.edit { putString(KEY_SELECTED_GAME_PACKAGE, value) }

    var bubblePosition: Pair<Float, Float>
        get() = prefs.getFloat(KEY_BUBBLE_X, 0.1f) to prefs.getFloat(KEY_BUBBLE_Y, 0.1f)
        set(value) = prefs.edit {
            putFloat(KEY_BUBBLE_X, value.first)
            putFloat(KEY_BUBBLE_Y, value.second)
        }

    var mapPosition: Pair<Int, Int>
        get() = prefs.getInt(KEY_MAP_X, 100) to prefs.getInt(KEY_MAP_Y, 100)
        set(value) = prefs.edit {
            putInt(KEY_MAP_X, value.first)
            putInt(KEY_MAP_Y, value.second)
        }

    var mapSize: Pair<Int, Int>
        get() = prefs.getInt(KEY_MAP_WIDTH, DEFAULT_MAP_WIDTH) to prefs.getInt(KEY_MAP_HEIGHT, DEFAULT_MAP_HEIGHT)
        set(value) = prefs.edit {
            putInt(KEY_MAP_WIDTH, value.first)
            putInt(KEY_MAP_HEIGHT, value.second)
        }

    var bubbleSize: Int
        get() = prefs.getInt(KEY_BUBBLE_SIZE, DEFAULT_BUBBLE_SIZE)
        set(value) = prefs.edit { putInt(KEY_BUBBLE_SIZE, value) }

    var bubbleAlpha: Float
        get() = prefs.getFloat(KEY_BUBBLE_ALPHA, 1.0f)
        set(value) = prefs.edit { putFloat(KEY_BUBBLE_ALPHA, value) }
}
