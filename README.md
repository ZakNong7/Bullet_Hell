# Tahu Game - Android Port

Game bullet hell shooter yang di-port dari Greenfoot ke Android menggunakan LibGDX framework.

## 🎮 Fitur Game

- **5 Stage Gameplay** dengan tingkat kesulitan yang meningkat
- **Boss Fight** di stage akhir dengan 3 fase berbeda
- **Slow Motion Mechanic** - Tahan tombol slow untuk memperlambat waktu
- **Touch Controls** - Drag pada layar bagian bawah untuk menggerakkan player
- **Sistem Health & Recovery** - Dapatkan HP setelah menyelesaikan stage
- **Multiple Enemy Types** - Enemy biasa, horizontal moving enemy, dan boss
- **Background Music & Sound** - Audio atmosfer untuk setiap scene

## 📋 Prasyarat (macOS)

### 1. Install Java Development Kit (JDK)
```bash
# Install menggunakan Homebrew
brew install openjdk@17

# Tambahkan ke PATH
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# Verifikasi instalasi
java -version
```

### 2. Install Android Studio
1. Download dari https://developer.android.com/studio
2. Install Android Studio
3. Buka Android Studio, pilih "More Actions" → "SDK Manager"
4. Install:
   - Android SDK Platform 34 (Android 14)
   - Android SDK Build-Tools 34.0.0
   - Android SDK Platform-Tools
   - Android SDK Tools

### 3. Setup Android SDK Path
```bash
# Tambahkan ke ~/.zshrc atau ~/.bash_profile
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin

# Reload shell
source ~/.zshrc
```

### 4. Install Gradle (Opsional - Android Studio sudah include)
```bash
brew install gradle
```

## 🔧 Setup Project

### 1. Clone/Extract Project
```bash
cd /path/to/TahuGameAndroid
```

### 2. Setup local.properties
Buat file `local.properties` di root folder project:
```properties
sdk.dir=/Users/[YOUR_USERNAME]/Library/Android/sdk
```

Ganti `[YOUR_USERNAME]` dengan username macOS Anda.

### 3. Sinkronisasi Gradle
```bash
./gradlew clean
./gradlew build
```

## 📱 Menjalankan Game

### Opsi 1: Menggunakan Emulator Android

1. **Buat Emulator di Android Studio**:
   - Buka Android Studio
   - Tools → Device Manager
   - Create Device
   - Pilih device (misal: Pixel 5)
   - Pilih system image (API 34 - Android 14)
   - Finish

2. **Jalankan Game**:
```bash
# Start emulator
./gradlew android:installDebug
./gradlew android:run
```

Atau dari Android Studio:
- Open project di Android Studio
- Pilih emulator device
- Klik Run (▶️)

### Opsi 2: Menggunakan Device Fisik

1. **Enable Developer Options di Android**:
   - Settings → About Phone
   - Tap "Build Number" 7 kali
   - Kembali → Developer Options
   - Enable "USB Debugging"

2. **Connect & Run**:
```bash
# Check device connected
adb devices

# Install & run
./gradlew android:installDebug
./gradlew android:run
```

### Opsi 3: Build APK untuk Distribusi

```bash
# Debug APK
./gradlew android:assembleDebug

# Release APK (signed)
./gradlew android:assembleRelease

# APK akan ada di: android/build/outputs/apk/
```

## 🎮 Cara Bermain

### Kontrol
- **Gerak**: Drag jari Anda di bagian bawah layar
- **Tembak**: Otomatis
- **Slow Motion**: Tahan tombol slow di kanan bawah
- **Pause**: Tombol pause di kanan atas

### Tips
- Gunakan slow motion saat pola bullet terlalu padat
- Energy slow motion akan ter-charge kembali saat tidak digunakan
- Hindari bullet merah musuh
- Setiap stage clear akan memberikan HP recovery

## 🏗️ Struktur Project

```
TahuGameAndroid/
├── android/                    # Android-specific code
│   ├── src/
│   │   └── com/tahugame/android/
│   │       └── AndroidLauncher.java
│   ├── AndroidManifest.xml
│   └── build.gradle
├── core/                       # Game logic (platform-independent)
│   ├── src/com/tahugame/
│   │   ├── TahuGame.java      # Main game class
│   │   ├── screens/           # Game screens
│   │   │   ├── MenuScreen.java
│   │   │   ├── GameScreen.java
│   │   │   ├── EndScreen.java
│   │   │   ├── TutorialScreen.java
│   │   │   └── CreditScreen.java
│   │   ├── actors/            # Game entities
│   │   │   ├── Player.java
│   │   │   ├── Enemy.java
│   │   │   ├── EnemyHorizontal.java
│   │   │   ├── Boss.java
│   │   │   ├── PlayerBullet.java
│   │   │   └── EnemyBullet.java
│   │   └── utils/             # Utilities
│   │       └── ScreenShake.java
│   └── assets/                # Game assets
│       ├── images/            # Sprites & backgrounds
│       └── sounds/            # Music & SFX
└── build.gradle               # Root build config
```

## 🐛 Troubleshooting

### Problem: "SDK location not found"
**Solusi**: Buat file `local.properties` dengan path SDK yang benar

### Problem: "Unsupported Java version"
**Solusi**: Pastikan menggunakan JDK 17:
```bash
java -version  # Harus show version 17.x.x
```

### Problem: "Android SDK not found"
**Solusi**: Set ANDROID_HOME di environment variables

### Problem: Game lag di emulator
**Solusi**: 
- Enable GPU acceleration di emulator settings
- Atau gunakan device fisik untuk performa lebih baik

### Problem: Music tidak keluar
**Solusi**: 
- Check volume device
- Pastikan file MP3 ada di core/assets/sounds/

### Problem: Gradle build failed
**Solusi**:
```bash
# Clear cache dan rebuild
./gradlew clean
./gradlew --stop
./gradlew build
```

## 📝 Perbedaan dari Versi Greenfoot

1. **Touch Controls**: Drag untuk bergerak (bukan WASD)
2. **Slow Motion**: Button hold instead of Shift key
3. **Auto-shooting**: Player menembak otomatis
4. **Portrait Mode**: Orientasi potret untuk mobile
5. **Performance**: Optimized untuk mobile devices

## 🔄 Perubahan dari Greenfoot ke LibGDX

| Greenfoot | LibGDX |
|-----------|--------|
| World | Screen |
| Actor | Custom classes dengan render() |
| act() method | update() method |
| Mouse API | Input.isTouched() & Unproject |
| GreenfootSound | Music & Sound |
| Greenfoot.setWorld() | game.setScreen() |

## 📊 System Requirements

**Minimum**:
- Android 5.0 (API 21) atau lebih tinggi
- 512 MB RAM
- 50 MB storage space

**Recommended**:
- Android 8.0 (API 26) atau lebih tinggi
- 1 GB RAM
- Touch screen device

## 🚀 Next Steps / Improvements

- [ ] Add sound effects untuk tembakan dan hit
- [ ] Implement high score system
- [ ] Add power-ups
- [ ] Multiple difficulty levels
- [ ] Vibration feedback
- [ ] Particle effects
- [ ] More boss patterns
- [ ] Achievement system

## 📄 License

Project ini di-port dari Greenfoot game untuk pembelajaran.

## 👨‍💻 Credits

- Original Greenfoot version: [Your Name]
- Android port menggunakan LibGDX framework
- Assets: Original game assets

## 📞 Support

Jika ada masalah atau pertanyaan:
1. Check troubleshooting section di atas
2. Pastikan semua prasyarat sudah terinstall
3. Verifikasi setup Android SDK

---

**Selamat bermain! 🎮**
