# LiteRT & MediaPipe
-keep class com.google.ai.edge.litert.** { *; }
-keep class com.google.mediapipe.** { *; }

# Room
-keepclassmembers class * extends androidx.room.RoomDatabase { *; }
-keep @androidx.room.Database class * { *; }

# Hilt — keep generated components
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
