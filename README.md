# Bullet Hell - Android Port

Game bullet hell shooter yang di-port dari Greenfoot ke Android menggunakan LibGDX framework.

## 🎮 Fitur Game

- **5 Stage Gameplay** dengan tingkat kesulitan yang meningkat
- **Boss Fight** di stage akhir dengan 3 fase berbeda
- **Slow Motion Mechanic** - Tahan tombol slow untuk memperlambat waktu
- **Touch Controls** - Drag pada layar bagian bawah untuk menggerakkan player
- **Sistem Health & Recovery** - Dapatkan HP setelah menyelesaikan stage
- **Multiple Enemy Types** - Enemy biasa, horizontal moving enemy, dan boss
- **Background Music & Sound** - Audio atmosfer untuk setiap scene

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

- Original Greenfoot version: Ahmad Zakky Nurfauzan
- Android port menggunakan LibGDX framework
- Assets: pixabay: DJLofi, Backgroundmusicmaster, jakob_welik (Sounds), AI Generated, Canva

---

**Selamat bermain! 🎮**
