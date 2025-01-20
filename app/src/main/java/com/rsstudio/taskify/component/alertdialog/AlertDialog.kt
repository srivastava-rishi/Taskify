package com.rsstudio.taskify.component.alertdialog

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.rsstudio.taskify.common.alias.AppString

@Composable
fun ShowRationaleDialog(
    context: Context,
    permissionLauncher: ManagedActivityResultLauncher<String, Boolean>
) {
    AlertDialog.Builder(context)
        .setTitle(stringResource(AppString.permission_needed))
        .setMessage(stringResource(AppString.this_permission_is_required_for_reminders_please_grant_it))
        .setPositiveButton(stringResource(AppString.ok)) { _, _ ->
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
        .setNegativeButton(stringResource(AppString.cancel), null)
        .show()
}

@Composable
 fun ShowSettingsDialog(context: Context) {
    AlertDialog.Builder(context)
        .setTitle(stringResource(AppString.permission_needed))
        .setMessage(stringResource(AppString.you_have_denied_this_permission_permanently_please_enable_it_in_the_app_settings))
        .setPositiveButton(stringResource(AppString.go_to_settings)) { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", context.packageName, null)
            }
            context.startActivity(intent)
        }
        .setNegativeButton(stringResource(AppString.cancel), null)
        .show()
}

