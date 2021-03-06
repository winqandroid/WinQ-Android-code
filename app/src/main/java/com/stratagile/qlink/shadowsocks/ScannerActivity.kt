///*******************************************************************************
// *                                                                             *
// *  Copyright (C) 2017 by Max Lv <max.c.lv@gmail.com>                          *
// *  Copyright (C) 2017 by Mygod Studio <contact-shadowsocks-android@mygod.be>  *
// *                                                                             *
// *  This program is free software: you can redistribute it and/or modify       *
// *  it under the terms of the GNU General Public License as published by       *
// *  the Free Software Foundation, either version 3 of the License, or          *
// *  (at your option) any later version.                                        *
// *                                                                             *
// *  This program is distributed in the hope that it will be useful,            *
// *  but WITHOUT ANY WARRANTY; without even the implied warranty of             *
// *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the              *
// *  GNU General Public License for more details.                               *
// *                                                                             *
// *  You should have received a copy of the GNU General Public License          *
// *  along with this program. If not, see <http://www.gnu.org/licenses/>.       *
// *                                                                             *
// *******************************************************************************/
//
//package com.stratagile.qlink.shadowsocks
//
//import android.app.Activity
//import android.content.Intent
//import android.content.pm.ShortcutManager
//import android.hardware.camera2.CameraAccessException
//import android.hardware.camera2.CameraManager
//import android.os.Build
//import android.os.Bundle
//import android.support.v4.app.NavUtils.shouldUpRecreateTask
//import android.support.v4.app.TaskStackBuilder
//import android.support.v7.app.AppCompatActivity
//import android.support.v7.widget.Toolbar
//import android.util.Log
//import android.util.SparseArray
//import android.view.MenuItem
//import android.widget.Toast
////import androidx.appcompat.app.AppCompatActivity
////import androidx.appcompat.widget.Toolbar
////import androidx.core.app.TaskStackBuilder
////import androidx.core.content.getSystemService
//import androidx.core.util.forEach
//import com.crashlytics.android.Crashlytics
////import com.github.shadowsocks.App.Companion.app
////import com.github.shadowsocks.database.Profile
////import com.github.shadowsocks.database.ProfileManager
////import com.github.shadowsocks.utils.datas
////import com.github.shadowsocks.utils.openBitmap
////import com.github.shadowsocks.utils.printLog
////import com.github.shadowsocks.utils.resolveResourceId
//import com.google.android.gms.common.GoogleApiAvailability
//import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture
//import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic
//import com.google.android.gms.vision.Frame
//import com.google.android.gms.vision.barcode.Barcode
//import com.google.android.gms.vision.barcode.BarcodeDetector
//import com.stratagile.qlink.R
//import com.stratagile.qlink.R.string.finish
//import com.stratagile.qlink.application.AppConfig
//import com.stratagile.qlink.shadowsocks.database.Profile
//import com.stratagile.qlink.shadowsocks.database.ProfileManager
//import com.stratagile.qlink.shadowsocks.utils.datas
//import com.stratagile.qlink.shadowsocks.utils.openBitmap
//import com.stratagile.qlink.shadowsocks.utils.printLog
//import com.stratagile.qlink.shadowsocks.utils.resolveResourceId
//import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever
//
//class ScannerActivity : AppCompatActivity(), Toolbar.OnMenuItemClickListener, BarcodeRetriever {
//    companion object {
//        private const val TAG = "ScannerActivity"
//        private const val REQUEST_IMPORT = 2
//        private const val REQUEST_IMPORT_OR_FINISH = 3
//        private const val REQUEST_GOOGLE_API = 4
//    }
//
//    private lateinit var detector: BarcodeDetector
//
//    private fun navigateUp() {
//        val intent = parentActivityIntent
//        if (shouldUpRecreateTask(intent) || isTaskRoot)
//            TaskStackBuilder.create(this).addNextIntentWithParentStack(intent).startActivities()
//        else finish()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        detector = BarcodeDetector.Builder(this)
//                .setBarcodeFormats(Barcode.QR_CODE)
//                .build()
//        if (!detector.isOperational) {
//            val availability = GoogleApiAvailability.getInstance()
//            val dialog = availability.getErrorDialog(this, availability.isGooglePlayServicesAvailable(this),
//                    REQUEST_GOOGLE_API)
//            if (dialog == null) {
//                Toast.makeText(this, R.string.common_google_play_services_notification_ticker, Toast.LENGTH_SHORT)
//                        .show()
//                finish()
//            } else {
//                dialog.setOnDismissListener { finish() }
//                dialog.show()
//            }
//            return
//        }
//        if (Build.VERSION.SDK_INT >= 25) getSystemService<ShortcutManager>()!!.reportShortcutUsed("scan")
//        if (try {
//                    getSystemService<CameraManager>()?.cameraIdList?.isEmpty()
//                } catch (_: CameraAccessException) {
//                    true
//                } != false) {
//            startImport()
//            return
//        }
//        setContentView(R.layout.layout_scanner)
//        val toolbar = findViewById<Toolbar>(R.id.toolbar)
//        toolbar.title = title
//        toolbar.setNavigationIcon(theme.resolveResourceId(R.attr.homeAsUpIndicator))
//        toolbar.setNavigationOnClickListener { navigateUp() }
//        toolbar.inflateMenu(R.menu.scanner_menu)
//        toolbar.setOnMenuItemClickListener(this)
//        val capture = supportFragmentManager.findFragmentById(R.id.barcode) as BarcodeCapture
//        capture.setCustomDetector(detector)
//        capture.setRetrieval(this)
//    }
//
//    override fun onRetrieved(barcode: Barcode) = runOnUiThread {
//        Profile.findAllUrls(barcode.rawValue, AppConfig.instance.currentProfile).forEach { ProfileManager.createProfile(it) }
//        navigateUp()
//    }
//    override fun onRetrievedMultiple(closetToClick: Barcode?, barcode: MutableList<BarcodeGraphic>?) = check(false)
//    override fun onBitmapScanned(sparseArray: SparseArray<Barcode>?) { }
//    override fun onRetrievedFailed(reason: String?) = Crashlytics.log(Log.WARN, TAG, reason)
//    override fun onPermissionRequestDenied() {
//        Toast.makeText(this, R.string.add_profile_scanner_permission_required, Toast.LENGTH_SHORT).show()
//        startImport()
//    }
//
//    override fun onMenuItemClick(item: MenuItem) = when (item.itemId) {
//        R.id.action_import_clipboard -> {
//            startImport(true)
//            true
//        }
//        else -> false
//    }
//
//    private fun startImport(manual: Boolean = false) = startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).apply {
//        addCategory(Intent.CATEGORY_OPENABLE)
//        type = "image/*"
//        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//    }, if (manual) REQUEST_IMPORT else REQUEST_IMPORT_OR_FINISH)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        when (requestCode) {
//            REQUEST_IMPORT, REQUEST_IMPORT_OR_FINISH -> if (resultCode == Activity.RESULT_OK) {
//                val feature = AppConfig.instance.currentProfile
//                var success = false
//                for (uri in data!!.datas) try {
//                    detector.detect(Frame.Builder().setBitmap(contentResolver.openBitmap(uri)).build())
//                            .forEach { _, barcode ->
//                                Profile.findAllUrls(barcode.rawValue, feature).forEach {
//                                    ProfileManager.createProfile(it)
//                                    success = true
//                                }
//                            }
//                } catch (e: Exception) {
//                    printLog(e)
//                }
//                Toast.makeText(this, if (success) R.string.action_import_msg else R.string.action_import_err,
//                        Toast.LENGTH_SHORT).show()
//                navigateUp()
//            } else if (requestCode == REQUEST_IMPORT_OR_FINISH) navigateUp()
//            else -> super.onActivityResult(requestCode, resultCode, data)
//        }
//    }
//}
