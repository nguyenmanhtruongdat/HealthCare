package com.example.healthcare;

import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class AppointmentReminderService extends FirebaseMessagingService {



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if the message contains data payload.
        if (remoteMessage.getData().size() > 0) {
            // Get the appointment details from the data payload.
            String appointmentTitle = remoteMessage.getData().get("title");
            String appointmentDate = remoteMessage.getData().get("date");

            // Build the notification message.
            String message = "Reminder: Your appointment \"" + appointmentTitle + "\" is due on " + appointmentDate;

            // Create the notification.
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default")
                    .setSmallIcon(R.drawable.baseline_notifications_active_24)
                    .setContentTitle("Appointment Reminder")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            // Show the notification.
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(0, builder.build());
        }
    }

    @Override
    public void onCreate() {
        // Get the Firebase Cloud Messaging token for the user.
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            // Construct the message data payload.
            Map<String, String> data = new HashMap<>();
            data.put("title", "Dentist Appointment");
            data.put("date", "2023-05-01");

            // Construct the message.
            RemoteMessage message = new RemoteMessage.Builder(token)
                    .setData(data)
                    .build();

            // Send the message to the user's device.
            FirebaseMessaging.getInstance().send(message);
        }).addOnFailureListener(e -> {
            // Handle failure to get the token.
            Log.e("FCM", "Failed to get FCM token", e);
        });

    }
}
