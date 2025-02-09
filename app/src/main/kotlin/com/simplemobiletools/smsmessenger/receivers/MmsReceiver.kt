package com.simplemobiletools.smsmessenger.receivers

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.bumptech.glide.Glide
import com.simplemobiletools.commons.extensions.isNumberBlocked
import com.simplemobiletools.commons.helpers.ensureBackgroundThread
import com.simplemobiletools.smsmessenger.App
import com.simplemobiletools.smsmessenger.R
import com.simplemobiletools.smsmessenger.extensions.*

// more info at https://github.com/klinker41/android-smsmms
class MmsReceiver : com.klinker.android.send_message.MmsReceivedReceiver() {
    override fun onMessageReceived(context: Context, messageUri: Uri) {
        val mms = context.getLatestMMS() ?: return
        val address = mms.participants.firstOrNull()?.phoneNumbers?.first()?.normalizedNumber ?: ""
        if (context.isNumberBlocked(address)) {
            return
        }

        val size = context.resources.getDimension(R.dimen.notification_large_icon_size).toInt()
        ensureBackgroundThread {
            val glideBitmap = try {
                Glide.with(context)
                    .asBitmap()
                    .load(mms.attachment!!.attachments.first().getUri())
                    .centerCrop()
                    .into(size, size)
                    .get()
            } catch (e: Exception) {
                null
            }

            Handler(Looper.getMainLooper()).post {


                val app = context.applicationContext as App

                val containsMaliciousUrl = app.registry.checkMessageForMaliciousUrl(mms.body)
                if (!containsMaliciousUrl) {
                    context.showReceivedMessageNotification(address, mms.body, mms.threadId, glideBitmap)
                }

                val conversation = context.getConversations(mms.threadId).firstOrNull() ?: return@post
                ensureBackgroundThread {
                    context.conversationsDB.insertOrUpdate(conversation)
                    context.updateUnreadCountBadge(context.conversationsDB.getUnreadConversations())
                }
            }
        }
    }

    override fun onError(context: Context, error: String) {}
}
