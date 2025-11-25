# 🧮 BMI Hesaplayıcı (Beden Kitle İndeksi)

## 📱 Genel Bilgi
Bu proje, **Mobil Programlama** dersi kapsamında geliştirilen basit bir **Android uygulamasıdır**.  Uygulama, kullanıcının boy ve kilosunu alarak **BMI (Body Mass Index)** değerini hesaplar ve sonucu **kategorik olarak** gösterir.  

---

## 🚀 Özellikler

## 🔸 Temel Özellikler
- Kullanıcıdan **boy (cm/m)** ve **kilo (kg)** girişi alır.  
- “**Hesapla**” butonuna tıklanınca BMI değeri hesaplanır:  
  BMI = kilo/boy^2
- Sonuç **kategoriye göre renklendirilir:**
  | Kategori | Aralık | Renk |
  |-----------|---------|------|
  | Zayıf | < 18.5 | Mavi |
  | Normal | 18.5–24.9 | Yeşil |
  | Fazla Kilolu | 25–29.9 | Turuncu |
  | Obez | ≥ 30 | Kırmızı |
- “**Temizle**” butonu tüm alanları sıfırlar.

---

## 🔸 Teknik Bilgiler

| Özellik | Değer |
|----------|--------|
| **Dil** | Kotlin |
| **Minimum SDK** | 24 (Android 7.0) |
| **ViewBinding** | ✅ Aktif |
| **Tema** | Material 3 (DayNight) |
| **Veri Saklama** | SharedPreferences |
| **Proje Tipi** | XML tabanlı (ViewBinding) |
| **IDE** | Android Studio Koala (veya üstü) |
