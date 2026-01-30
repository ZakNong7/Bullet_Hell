# Setup Guide untuk macOS

## Quick Start untuk MacBook

### 1. Install Dependencies (One-time setup)

```bash
# Install Homebrew (jika belum ada)
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install Java 17
brew install openjdk@17

# Setup Java path
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
echo 'export JAVA_HOME="/opt/homebrew/opt/openjdk@17"' >> ~/.zshrc
source ~/.zshrc

# Verifikasi
java -version  # Should show: openjdk version "17.x.x"
```

### 2. Install Android Studio

```bash
# Download Android Studio
# URL: https://developer.android.com/studio

# Atau install via Homebrew Cask
brew install --cask android-studio
```

**Setelah install Android Studio:**

1. Buka Android Studio
2. Klik "More Actions" → "SDK Manager"
3. Di tab "SDK Platforms", centang:
   - ✅ Android 14.0 (API 34)
   
4. Di tab "SDK Tools", centang:
   - ✅ Android SDK Build-Tools 34
   - ✅ Android SDK Platform-Tools
   - ✅ Android Emulator
   - ✅ Google Play services

5. Klik "Apply" untuk download & install

### 3. Setup Environment Variables

```bash
# Tambahkan ke ~/.zshrc (default shell macOS)
nano ~/.zshrc

# Paste baris ini:
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/platform-tools
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin

# Save (Ctrl+O, Enter, Ctrl+X)

# Reload configuration
source ~/.zshrc

# Verifikasi
echo $ANDROID_HOME  # Should show: /Users/[username]/Library/Android/sdk
adb version        # Should show adb version info
```

### 4. Setup Project

```bash
# Navigate ke project folder
cd /path/to/TahuGameAndroid

# Buat local.properties
echo "sdk.dir=$HOME/Library/Android/sdk" > local.properties

# Test gradle
./gradlew --version

# Jika permission denied:
chmod +x gradlew
```

### 5. Build Project

```bash
# Clean previous builds
./gradlew clean

# Build project (first time might take a while)
./gradlew build

# Jika success, lanjut ke running
```

### 6A. Run dengan Emulator

```bash
# List available emulators
$ANDROID_HOME/emulator/emulator -list-avds

# Jika belum ada emulator, buat dulu di Android Studio:
# Tools → Device Manager → Create Device

# Start emulator (ganti nama sesuai AVD Anda)
$ANDROID_HOME/emulator/emulator -avd Pixel_5_API_34 &

# Wait sampai emulator fully booted, lalu:
./gradlew android:installDebug
adb shell am start -n com.tahugame.android/.AndroidLauncher
```

### 6B. Run dengan Android Studio (Recommended untuk pertama kali)

```bash
# Buka project di Android Studio
open -a "Android Studio" .

# Atau manual:
# 1. Buka Android Studio
# 2. File → Open → Pilih folder TahuGameAndroid
# 3. Wait Gradle sync selesai
# 4. Klik Run button (▶️) di toolbar
# 5. Pilih emulator atau device
```

### 7. Run di Device Fisik (Opsional)

**Di Android Device:**
1. Settings → About Phone
2. Tap "Build Number" 7 kali (enable Developer Mode)
3. Settings → Developer Options
4. Enable "USB Debugging"

**Di MacBook:**
```bash
# Connect device via USB

# Check device terdeteksi
adb devices
# Should show: 
# List of devices attached
# ABCD1234    device

# Install dan run
./gradlew android:installDebug
```

### 8. Build APK untuk Install Manual

```bash
# Build debug APK
./gradlew android:assembleDebug

# APK location:
# android/build/outputs/apk/debug/android-debug.apk

# Install ke device yang terconnect
adb install android/build/outputs/apk/debug/android-debug.apk
```

## Common Issues on macOS

### Issue: "command not found: gradlew"
```bash
chmod +x gradlew
./gradlew clean
```

### Issue: "JAVA_HOME is not set"
```bash
export JAVA_HOME="/opt/homebrew/opt/openjdk@17"
export PATH="$JAVA_HOME/bin:$PATH"
source ~/.zshrc
```

### Issue: "SDK location not found"
```bash
# Create local.properties file
echo "sdk.dir=$HOME/Library/Android/sdk" > local.properties
```

### Issue: Emulator sangat lambat
```bash
# Enable hardware acceleration
# Di Android Studio → Tools → AVD Manager → Edit emulator
# Graphics: Hardware - GLES 2.0
# RAM: 2048 MB minimum

# Atau gunakan ARM64 system image dengan Apple Silicon optimization
```

### Issue: "Unable to install APK"
```bash
# Uninstall old version first
adb uninstall com.tahugame.android

# Then install again
./gradlew android:installDebug
```

## Performance Tips for macOS

1. **Gunakan Apple Silicon emulator** (jika M1/M2/M3 MacBook):
   - Pilih ARM64 system image di AVD Manager
   - Jauh lebih cepat daripada x86 emulation

2. **Allocate more RAM ke emulator**:
   - Edit AVD → Advanced Settings → RAM: 2048 MB

3. **Close unnecessary apps** saat running emulator

4. **Use physical device** untuk testing yang lebih smooth

## Build for Release (Production APK)

```bash
# Generate keystore (one time)
keytool -genkey -v -keystore tahugame-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias tahugame

# Update android/build.gradle dengan signing config

# Build release APK
./gradlew android:assembleRelease

# APK: android/build/outputs/apk/release/android-release.apk
```

## File Structure pada macOS

```
$HOME/Library/Android/sdk/          # Android SDK location
├── build-tools/
├── emulator/
├── platforms/
│   └── android-34/
├── platform-tools/
└── tools/

~/Projects/TahuGameAndroid/         # Your project
├── android/
├── core/
├── gradlew                          # Gradle wrapper (Unix)
├── local.properties                 # SDK path (create this!)
└── README.md
```

## VS Code Setup (Alternative ke Android Studio)

```bash
# Install VS Code
brew install --cask visual-studio-code

# Install extensions:
# - Extension Pack for Java
# - Gradle for Java

# Open project
code /path/to/TahuGameAndroid

# Build via terminal
./gradlew build
```

---

**Tips**: Untuk development yang smooth di macOS, gunakan kombinasi Android Studio (untuk emulator/device management) + Terminal (untuk gradle commands).
