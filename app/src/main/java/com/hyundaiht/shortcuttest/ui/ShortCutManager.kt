package com.hyundaiht.shortcuttest.ui

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat

class ShortCutManager {

    /**
     * ShortCut
     *
     * @property id
     * @property shortLabel
     * @property longLabel
     * @property icon
     * @property intent
     */
    data class ShortCut(
        val id: String,
        val shortLabel: String,
        val longLabel: String,
        val icon: IconCompat,
        val intent: Intent,
    )

    /**
     * createShortCut
     *
     * @param context
     * @param shortCut
     */
    fun createShortCut(context: Context, shortCut: ShortCut) {
        val shortcut = ShortcutInfoCompat.Builder(context, shortCut.id)
            .setShortLabel(shortCut.shortLabel)
            .setLongLabel(shortCut.longLabel)
            .setIcon(shortCut.icon)
            .setIntent(shortCut.intent)
            .build()

        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
    }

    /**
     * getShortCut
     *
     * @param context
     * @return
     */
    fun getShortCut(context: Context): List<ShortcutInfoCompat> =
        ShortcutManagerCompat.getDynamicShortcuts(context)

    /**
     * removeShortCuts
     *
     * @param context
     * @param ids
     * @return
     */
    fun removeShortCuts(context: Context, ids: List<String>): Result<Unit> {
        return kotlin.runCatching {
            ShortcutManagerCompat.removeDynamicShortcuts(context, ids)
        }
    }

    fun removeAllShortCuts(context: Context): Result<Unit> {
        return kotlin.runCatching {
            ShortcutManagerCompat.removeAllDynamicShortcuts(context)
        }
    }
}