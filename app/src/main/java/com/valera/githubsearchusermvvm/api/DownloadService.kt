package com.valera.githubsearchusermvvm.api

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.webkit.CookieManager
import android.webkit.URLUtil
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.valera.githubsearchusermvvm.db.AppDataBase
import com.valera.githubsearchusermvvm.model.Downloads
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DownloadService(private val context: Context) : BroadcastReceiver() {

   val TAG = "DownloadService"
   private var downloadID : Long = 0L
   private var login : String = ""
   private var repositoryName : String = ""

   private fun provideRequest(login: String, repositoryName: String) : DownloadManager.Request {
      val url = "https://github.com/$login/$repositoryName/archive/master.zip"
      val nameFile = "${login}_${repositoryName}_"
      val request = DownloadManager.Request(Uri.parse(url))
      val title = URLUtil.guessFileName(url, null, null)
      request.setTitle(nameFile+title)
      request.setDescription("Download File wait minute .. . ... ")
      val cookie = CookieManager.getInstance().getCookie(url)
      request.addRequestHeader("cookie", cookie)
      request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
      request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, nameFile+title)
      return request
   }

   fun provideDownload(login: String, repositoryName: String) {
      this.login = login
      this.repositoryName = repositoryName
      Toast.makeText(context, "Download $repositoryName", Toast.LENGTH_SHORT).show()
      val downloadManager = context.getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
      downloadID = downloadManager.enqueue(provideRequest(login, repositoryName))
      val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
      context.registerReceiver(this, filter)
      Log.d(TAG, "start - :: $downloadID")

   }

   fun getDownloadStatus() : Int {
      val query = DownloadManager.Query()
      query.setFilterById(downloadID)
      val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
      val cursor = downloadManager.query(query)
      if(cursor.moveToFirst()) {
         val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
         return cursor.getInt(columnIndex)
      }
      return DownloadManager.ERROR_UNKNOWN
   }

   override fun onReceive(context: Context?, intent: Intent?) {
      val broadcastDownloadID = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
      if(broadcastDownloadID == downloadID) {
         if(getDownloadStatus() == DownloadManager.STATUS_SUCCESSFUL) {
            Log.d(TAG, "end - :: $downloadID")
            Toast.makeText(context, "Download End $repositoryName", Toast.LENGTH_SHORT).show()
            GlobalScope.launch(Dispatchers.IO) {
               AppDataBase(context!!).downloadsDao().insert(Downloads(login = login, nameRepository = repositoryName))
            }
         } else {
            Toast.makeText(context, "Not Downloaded", Toast.LENGTH_SHORT).show()
         }
      }
   }
}