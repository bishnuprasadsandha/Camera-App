# 📸 Camera App (Android - Kotlin, Jetpack Compose, CameraX, Room)

A modern Android app to capture, organize, and store images session-wise using **CameraX** and **Room Database** with a clean Jetpack Compose UI.

---

## ✨ Features
- 📷 **Capture Images per Session** using CameraX API  
- 💾 **App-Specific Storage**:  
  `/Android/data/.../Pictures/Sessions/<SessionID>/`  
- 🗂 **SQLite (Room)** for managing Sessions + Photo Metadata  
- 🔎 **Search** by Name or Session ID  
- 🖼 **Gallery View** with grid-based image preview  
- ⚡ **Smooth performance** with Compose UI & Material Design styling  

---

## 🛠 Tech Stack
- **Language**: Kotlin  
- **UI**: Jetpack Compose + Material 3  
- **Camera**: CameraX API  
- **Database**: Room (SQLite)  
- **Architecture**: MVVM + Repository Pattern  
- **Other**: Coroutines + Flow  

---

## 🚀 Build & Run

1. **Clone the Repository**
   ```bash
   git clone https://github.com/bishnuprasadsandha/camera-app.git


2. **Open in Android Studio (Recommended: Ladybug or newer)**  
3. **Use JDK 17**  
4. **Build & Run** the `app` module on a **physical device**  
   ⚠️ _Camera functionality requires real hardware_

---

## 🔑 Permissions

The app requires the following **runtime permissions**:

- `CAMERA` – to capture photos  
- `READ/WRITE_EXTERNAL_STORAGE` – _only if targeting Android < Q_

---

## 🎥 Demo

📂 Demo Video:  
`/demo/demo.mp4` _(≤ 2 mins walkthrough)_

---

## 🧪 Compatibility

- ✅ Tested on **Android 10 – 14**  
- 📱 Recommended: **Physical Device**  
  _(Emulator camera support is limited)_

---

## 📌 Notes

- 📸 Photos are **stored locally per session**  
- 🗃 Database stores **metadata**: session name, ID, timestamps, file paths  
- 🌐 **Lightweight**, **offline-first** design

---

## 🤝 Contributing

Pull requests are welcome!  
For major changes, please open an issue first to discuss what you’d like to improve.
