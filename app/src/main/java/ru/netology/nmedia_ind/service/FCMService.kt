package ru.netology.nmedia_ind.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import ru.netology.nmedia_ind.R

class FCMService : FirebaseMessagingService() {

    private val action = "action"
    private val content = "content"
    private val channelId = "remote"
    private val gson = Gson()

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_remote_name)
            val descriptionText = getString(R.string.channel_remote_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
//        println(Gson().toJson(message))
        try {
            message.data[action]?.let {
                when (Action.valueOf(it)) {
                    Action.LIKE -> handleLike(
                        gson.fromJson(
                            message.data[content],
                            Like::class.java
                        )
                    )
                    Action.POST -> handlePost(
                        gson.fromJson(
                            message.data[content],
                            Post::class.java
                        )
                    )
                }
            }
        } catch (e: IllegalArgumentException) {
            println("Wrong action!")
            return
        } catch (e: Exception) {
            println("Something went wrong")
            return
        }
    }

    override fun onNewToken(token: String) {
        println("new token: $token")
    }

    private fun handleLike(content: Like) {
//        val intent = Intent(this, AppActivity::class.java).apply {
//            putExtra(Intent.EXTRA_TEXT, "extra text")
//        }
//        val contentIntent = PendingIntent.getActivity(
//            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
//        )
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_liked,
                    content.userName,
                    content.postAuthor,
                )
            )
            .setContentText("Content text")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            //.setContentIntent(contentIntent)
            .build()

        NotificationManagerCompat.from(this)
            .notify(1, notification)
    }

    private fun handlePost(content: Post) {

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(
                getString(
                    R.string.notification_user_posted,
                    content.userName,
                    content.content
                )
            )
            .setContentText(content.content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content.content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(this)
            .notify(2, notification)
    }
}

enum class Action {
    LIKE,
    POST
}

data class Like(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val postAuthor: String,
)

data class Post(
    val userId: Long,
    val userName: String,
    val postId: Long,
    val content: String
)