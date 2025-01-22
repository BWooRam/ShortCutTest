package com.hyundaiht.shortcuttest

import android.app.ComponentCaller
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.hyundaiht.shortcuttest.ui.theme.ShortCutTestTheme

class MainActivity : ComponentActivity() {
    private val tag = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate intent = $intent")

        enableEdgeToEdge()
        setContent {
            ShortCutTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(Modifier.padding(innerPadding)) {
                        //dynamic Shortcut
                        Button(onClick = {
                            createDynamicShortcut()
                        }) {
                            Text("Create Dynamic ShortCut")
                        }
                        Button(onClick = {
                            removeDynamicShortcut()
                        }) {
                            Text("Remove Dynamic ShortCut")
                        }
                        Button(onClick = {
                            checkDynamicShortcut()
                        }) {
                            Text("Check Dynamic ShortCut")
                        }

                        //Pin Shortcut
                        Button(onClick = {
                            requestPinShortcut()
                        }) {
                            Text("Request Pin ShortCut")
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        Log.d(tag, "onNewIntent intent = $intent")
    }

    private fun createDynamicShortcut() {
        val context: Context = this@MainActivity
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.mysite.example.com/")
        )
        val shortcut = ShortcutInfoCompat.Builder(context, "id1")
            .setShortLabel("Website")
            .setLongLabel("Open the website")
            .setIcon(IconCompat.createWithResource(context, R.drawable.ic_launcher_background))
            .setIntent(intent)
            .build()

        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)
    }

    private fun removeDynamicShortcut() {
        ShortcutManagerCompat.removeDynamicShortcuts(this@MainActivity, mutableListOf("id1"))
    }

    private fun checkDynamicShortcut() {
        val shortcuts = ShortcutManagerCompat.getDynamicShortcuts(this@MainActivity)
        for(shortcut in shortcuts){
            Log.d(tag, "checkDynamicShortcut shortcut = $shortcut")
            Log.d(tag, "checkDynamicShortcut shortcut id = ${shortcut.id}")
            Log.d(tag, "checkDynamicShortcut shortcut shortLabel = ${shortcut.shortLabel}")
            Log.d(tag, "checkDynamicShortcut shortcut longLabel = ${shortcut.longLabel}")
        }
    }

    private fun requestPinShortcut() {
        val context: Context = this@MainActivity
        val shortcutManager = getSystemService(ShortcutManager::class.java)

        if (shortcutManager!!.isRequestPinShortcutSupported) {
            // Enable the existing shortcut with the ID "my-shortcut".
            val pinShortcutInfo = ShortcutInfo.Builder(context, "shortcutId1").build()

            // Create the PendingIntent object only if your app needs to be notified
            // that the user let the shortcut be pinned. If the pinning operation fails,
            // your app isn't notified. Assume here that the app implements a method
            // called createShortcutResultIntent() that returns a broadcast intent.
            val pinnedShortcutCallbackIntent =
                shortcutManager.createShortcutResultIntent(pinShortcutInfo)

            // Configure the intent so that your app's broadcast receiver gets the
            // callback successfully. For details, see PendingIntent.getBroadcast().
            val successCallback = PendingIntent.getBroadcast(
                context, /* request code */ 0,
                pinnedShortcutCallbackIntent, /* flags */
                PendingIntent.FLAG_IMMUTABLE
            )

            shortcutManager.requestPinShortcut(
                pinShortcutInfo,
                successCallback.intentSender
            )
        }
    }
}