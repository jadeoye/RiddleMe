package com.example.riddleme

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

class RiddleActivity : AppCompatActivity() {
    val riddles = listOf(
        "I speak without a mouth and hear without ears. I have no body, but I come alive with wind. What am I?" to "An echo",
        "You measure my life in hours and I serve you by expiring. I die when I'm thin, I’m very quick when I'm fat. The wind is my enemy. What am I?" to "A candle",
        "I have cities, but no houses. I have mountains, but no trees. I have water, but no fish. What am I?" to "A map",
        "What can travel around the world while staying in a corner?" to "A stamp",
        "What has keys but can’t open locks?" to "A piano"
    )

    var displayedRiddleIndex: Int = 0;
    private lateinit var currentRiddle: Pair<String, String>
    private val CHANNEL_ID = "riddle_notification_channel"
    private val NOTIFICATION_ID = 1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_riddle)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        createNotificationChannel()

        var name = intent.getStringExtra("name") ?: ""
        val nameField: TextView = findViewById(R.id.nameField)
        nameField.text = "Hello, $name!"

        val nextRiddleButton: Button = findViewById(R.id.nextRiddleButton)
        val getAnswerButton: Button = findViewById(R.id.getAnswerButton)
        val backButton: Button = findViewById(R.id.backButton)

        currentRiddle = getRiddle(displayedRiddleIndex)
        setRiddleQuestion(currentRiddle)

        nextRiddleButton.setOnClickListener {
            displayedRiddleIndex += 1
            currentRiddle = getRiddle(displayedRiddleIndex)
            setRiddleQuestion(currentRiddle)
        }

        getAnswerButton.setOnClickListener {
            showNotification()
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun getRiddle(displayedRiddleIndex: Int) : Pair<String, String> {
        return riddles[displayedRiddleIndex]
    }

    private fun setRiddleQuestion(riddle: Pair<String, String>) {
        val questionField: TextView = findViewById(R.id.questionField)
        questionField.text = riddle.first
    }

    private fun showNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Answer is ready")
            .setContentText("The answer to your riddle is:")
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(currentRiddle.second))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Riddle Notification"
            val descriptionText = "Channel for riddle notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}