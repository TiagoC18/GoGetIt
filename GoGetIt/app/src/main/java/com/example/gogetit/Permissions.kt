package com.example.gogetit

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun RequestLocationPermissions(
    onPermissionsGranted: @Composable () -> Unit,
    onPermissionsDenied: () -> Unit
) {
    var permissionState by remember { mutableStateOf(PermissionState.NOT_DETERMINED) }

    val context = LocalContext.current
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allPermissionsGranted = permissions.entries.all { it.value == true }
        permissionState = if (allPermissionsGranted) PermissionState.GRANTED else PermissionState.DENIED
    }

    LaunchedEffect(Unit) {
        val fineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

        permissionState = when {
            fineLocationPermission == PackageManager.PERMISSION_GRANTED -> PermissionState.GRANTED
            coarseLocationPermission == PackageManager.PERMISSION_GRANTED -> PermissionState.GRANTED
            else -> PermissionState.NOT_DETERMINED
        }
    }

    when (permissionState) {
        PermissionState.NOT_DETERMINED -> {
            SideEffect {
                requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
            }
        }
        PermissionState.GRANTED -> {
            onPermissionsGranted()
        }
        PermissionState.DENIED -> {
            onPermissionsDenied()
        }
    }
}

enum class PermissionState {
    NOT_DETERMINED,
    GRANTED,
    DENIED
}
