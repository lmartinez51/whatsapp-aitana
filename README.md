# MiniAitana

MiniAitana is an Android application that serves as an autonomous, on-device AI assistant for messaging apps (like WhatsApp). It acts as an "Auto-Pilot" that can read incoming notifications, process them locally using a Large Language Model (LLM), and automatically generate and send context-aware replies on your behalf.

## Features

- **On-Device AI Inference:** Uses Google's LiteRT (formerly TensorFlow Lite) to run LLM inference completely offline and locally on your Android device. No cloud APIs required.
- **Notification Intercepting:** Leverages the Android `NotificationListenerService` to securely read incoming messages and extract conversation context.
- **Foreground AI Engine:** Utilizes a Foreground Service (`AiEngineForegroundService`) to maintain the AI model in memory for quick responses without getting killed by the system.
- **Quick Settings Tile:** Easily toggle the "AutoPilot" mode on and off straight from your Android Quick Settings panel.
- **Custom Prompts & Rules:** Create custom system prompts and templates to define the personality and behavior of your AI assistant.
- **Contact Whitelisting:** Selectively enable the AI auto-pilot only for specific contacts to avoid replying to sensitive or professional conversations.
- **Conversation History:** Stores logs of past AI interactions and uses them to maintain context in ongoing conversations.

## Tech Stack

MiniAitana is built using modern Android development best practices and libraries:

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Dependency Injection:** Dagger Hilt
- **Local Database:** Room Database
- **Preferences:** Jetpack DataStore
- **AI Inference Engine:** Google LiteRT (`com.google.ai.edge.litertlm`)
- **Coroutines & Flow:** For asynchronous operations and reactive programming.

## Setup & Configuration

1. **Model Requirement:** Since MiniAitana runs inference locally, you need a compatible LiteRT (TFLite) LLM model file on your device. Ensure you have the model file downloaded to your device storage.
2. **App Permissions:** The app requires the following permissions to function correctly:
   - **Notification Access:** To read incoming messages.
   - **Foreground Service:** To keep the AI engine running while responding.
   - **Storage Access:** To read the local model file.
3. **Configuration:** Open the app to set the path to your downloaded model file, define your system prompt, and configure your contact whitelist.

## Architecture Highlights

- `MiniAitanaLiteRtEngine`: The core wrapper around LiteRT that handles loading the model, managing the conversation context, and generating text safely.
- `AitanaNotificationListenerService`: Listens for incoming messaging notifications and triggers the reply flow if the contact is whitelisted.
- `AiEngineForegroundService`: Orchestrates the AI engine lifecycle, ensuring the Heavy LLM model is only loaded when necessary and kept alive during active conversations using a leasing mechanism.
- `AutoPilotTileService`: Provides a seamless way to enable or disable the AI engine from the quick settings menu.

## Disclaimer

This application is designed for experimental and personal use. Automatic replies can sometimes be unpredictable depending on the underlying LLM. Please use responsibly and ensure you comply with the Terms of Service of the messaging applications you are integrating with.
