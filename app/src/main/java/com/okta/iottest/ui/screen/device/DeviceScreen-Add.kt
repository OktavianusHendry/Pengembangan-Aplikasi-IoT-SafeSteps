package com.okta.iottest.ui.screen.device

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.CompoundBarcodeView
import com.okta.iottest.R
import com.okta.iottest.ui.components.BottomBar
import com.okta.iottest.ui.theme.OnPrimaryContainer
import com.okta.iottest.ui.theme.PrimaryContainer
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDeviceScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var scanFlag by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }
    var lastReadBarcode by remember { mutableStateOf<String?>(null) }
    var torchState by remember { mutableStateOf(false) }
    var recomposeFlag by remember { mutableIntStateOf(Random.nextInt()) }
    var preview: CompoundBarcodeView? = null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan New Device") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Continue",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                },
                actions = {
                    // Add action icons here
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.onPrimary,)
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            key (recomposeFlag){
                AndroidView(
                    factory = { context ->
                        val preview = CompoundBarcodeView(context)
                        preview.setStatusText("")
                        preview.cameraSettings.isAutoTorchEnabled = torchState
                        preview.apply {
                            val capture = CaptureManager(context as Activity, this)
                            capture.initializeFromIntent(context.intent, null)
                            capture.decode()
                            this.decodeContinuous { result ->
                                if (scanFlag) {
                                    return@decodeContinuous
                                }
                                scanFlag = true
                                result.text?.let { barCodeOrQr ->
                                    lastReadBarcode = result.text
                                    scanFlag = true
                                    showResult = true
                                }
                            }
                            this.resume()
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            preview?.pauseAndWait()
        }
    }
}

