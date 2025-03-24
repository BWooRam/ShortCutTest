package com.hyundaiht.shortcuttest

import android.annotation.SuppressLint
import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import com.hyundaiht.shortcuttest.ui.ShortCutManager
import com.hyundaiht.shortcuttest.ui.TextWithButton
import com.hyundaiht.shortcuttest.ui.theme.ShortCutTestTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class ShortCutSampleActivity : ComponentActivity() {
    private val tag = javaClass.simpleName
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val shortCutManager = ShortCutManager()
    private lateinit var deviceFavoritesShortCuts: List<ShortCutManager.ShortCut>
    private lateinit var registrationPageShortCuts: List<ShortCutManager.ShortCut>
    private val shortCutName = "short_cut_name"
    private var shortCutEventName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initShortCutEvent(intent)
        deviceFavoritesShortCuts = mutableListOf(
            ShortCutManager.ShortCut(
                id = "FavoritesShortCut1",
                shortLabel = "도어락",
                longLabel = "도어락",
                icon = IconCompat.createWithResource(this, R.drawable.list_ic_door_lock_sensor_on),
                intent = Intent(this, ShortCutSampleActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra(shortCutName, "도어락")
                }
            ),
            ShortCutManager.ShortCut(
                id = "FavoritesShortCut2",
                shortLabel = "조명",
                longLabel = "조명",
                icon = IconCompat.createWithResource(this, R.drawable.list_ic_light_on),
                intent = Intent(this, ShortCutSampleActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra(shortCutName, "조명")
                }
            ),
            ShortCutManager.ShortCut(
                id = "FavoritesShortCut3",
                shortLabel = "가스 밸브",
                longLabel = "가스 밸브",
                icon = IconCompat.createWithResource(this, R.drawable.list_ic_gas_on),
                intent = Intent(this, ShortCutSampleActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra(shortCutName, "가스 밸브")
                }
            )
        )

        registrationPageShortCuts = mutableListOf(
            ShortCutManager.ShortCut(
                id = "RegistrationShortCut1",
                shortLabel = "기기 등록",
                longLabel = "기기 등록",
                icon = IconCompat.createWithResource(this, R.drawable.ic_launcher_background),
                intent = Intent(this, ShortCut1Activity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra(shortCutName, "기기 등록")
                }
            ),
            ShortCutManager.ShortCut(
                id = "RegistrationShortCut2",
                shortLabel = "즐겨 찾기 등록",
                longLabel = "즐겨 찾기 등록",
                icon = IconCompat.createWithResource(this, R.drawable.ic_launcher_background),
                intent = Intent(this, ShortCut2Activity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra(shortCutName, "즐겨 찾기 등록")
                }
            ),
            ShortCutManager.ShortCut(
                id = "RegistrationShortCut3",
                shortLabel = "자동화, 모드 등록",
                longLabel = "자동화, 모드 등록",
                icon = IconCompat.createWithResource(this, R.drawable.ic_launcher_background),
                intent = Intent(this, ShortCut3Activity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra(shortCutName, "자동화, 모드 등록")
                }
            )
        )

        enableEdgeToEdge()
        setContent {
            ShortCutTestTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Yellow)
                ) { innerPadding ->
                    val context = LocalContext.current
                    var rememberScreen by remember { mutableIntStateOf(0) }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Text("Short Cut 이벤트 : $shortCutEventName")
                        TextWithButton("DeviceFavoritesScreen 보여주기") {
                            rememberScreen = 0
                        }

                        TextWithButton("RegistrationPageScreen 보여주기") {
                            rememberScreen = 1
                        }

                        when (rememberScreen) {
                            0 -> {
                                shortCutManager.removeAllShortCuts(context)
                                DeviceFavoritesScreen()
                            }

                            1 -> {
                                shortCutManager.removeAllShortCuts(context)
                                RegistrationPageScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        initShortCutEvent(intent)
    }

    override fun onNewIntent(intent: Intent, caller: ComponentCaller) {
        super.onNewIntent(intent, caller)
        initShortCutEvent(intent)
    }

    private fun initShortCutEvent(intent: Intent) {
        shortCutEventName = intent.getStringExtra(shortCutName) ?: ""
        Log.d(tag, "initShortCutEvent shortCutEventName = $shortCutEventName")
    }

    @SuppressLint("RestrictedApi")
    @Composable
    fun DeviceFavoritesScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            val context = LocalContext.current
            var shortCutInfo by remember { mutableStateOf<List<ShortcutInfoCompat>>(emptyList()) }
            shortCutInfo = shortCutManager.getShortCut(context)

            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            Text(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .wrapContentHeight(),
                text = "적용된 ShortCut 리스트"
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                items(shortCutInfo.size) { index ->
                    val item = shortCutInfo[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        Text(modifier = Modifier.weight(1f), text = item.longLabel.toString())
                        TextButton(modifier = Modifier.wrapContentSize(), onClick = {
                            shortCutManager.removeShortCuts(context, mutableListOf(item.id))
                            shortCutInfo = shortCutManager.getShortCut(context)
                        }) {
                            Text("삭제")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            Text(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .wrapContentHeight(),
                text = "즐겨 찾기 가능한 ShortCut 리스트"
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Black)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            ) {
                items(deviceFavoritesShortCuts.size) { index ->
                    val item = deviceFavoritesShortCuts[index]

                    if (shortCutInfo.find { return@find item.id == it.id } != null)
                        return@items

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        IconCompatImage(iconCompat = item.icon)
                        Text(modifier = Modifier.weight(1f), text = item.longLabel)
                        TextButton(modifier = Modifier.wrapContentSize(), onClick = {
                            shortCutManager.createShortCut(context, item)
                            shortCutInfo = shortCutManager.getShortCut(context)
                        }) {
                            Text("추가")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun RegistrationPageScreen() {
        val context = LocalContext.current

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            items(registrationPageShortCuts.size) { index ->
                val item = registrationPageShortCuts[index]
                shortCutManager.createShortCut(context, item)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(modifier = Modifier.weight(1f), text = item.longLabel)
                }
            }
        }
    }

}

@Composable
fun IconCompatImage(iconCompat: IconCompat) {
    val context = LocalContext.current

    // Drawable → Bitmap → ImageBitmap
    val imageBitmap = remember(iconCompat) {
        val drawable = iconCompat.loadDrawable(context)
        val bitmap = drawable?.toBitmap()
        bitmap?.asImageBitmap()
    }

    imageBitmap?.let {
        Image(
            bitmap = it,
            contentDescription = "IconCompat image",
            modifier = Modifier.size(20.dp)
        )
    }
}