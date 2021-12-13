# 🔥 CoViTor Watch 🔥
### #Final project of Embedded System Design
- CoViToR Watch App created using Android Studio with Java

⌚⌚⌚⌚⌚

Smartwatch system, made for COVID-19 symptoms monitoring where you can monitor your current health condition such as temperature, oxygen saturation, and heart rate. So when there are any symptoms of COVID-19 you can take the appropriate action.

Stay healthy and keep wearing your mask!! 😷

#

## 🚨 3D Enclosure
To create the 3D enclosure, we used 3D print Creality Ender-3 Pro
with printer settings:

- Slicer App: PrusaSlicer 2.3.1
- Print Setting: 0.28mm (use 0.16mm or 0.20mm for more tidy looks)
- Filament: PLA (please use ABS or something stronger than PLA if you want more rigid)
- Infill: 15%
- Supports: None
- Duration for all parts: 1 hour (0.28mm quality)

<i>*For watch strap please using 28mm size with 30mm Spring Bar</i>

#

Full enclosure:
<p align="left">
  <img src="Watch Enclosure/Full Enclosure Print.jpg" />
</p>


Component arrangement:
<p align="left">
  <img src="Watch Enclosure/Arrangement Component.jpg" />
</p>

#

## 🤖 Arduino
We're using ESP32 Wemos D1 Mini

For some other controllers including controllers which are not ESP family, may need some more configuration.

Library:
- BluetoothSerial
- Adafruit_GFX
- Adafruit_SSD1306
- RTClib
- MAX30100_PulseOximeter
- Protocentral_MAX30205
- OneButton


## For improvement:
When using bluetooth serial, the heart rate and oximeter data are not updating because a problem. On ESP32, somehow the library of MAX30100 and bluetooth serial can't work together. This is because if you use a serial bluetooth module, it will slow down the looping time of the routine. The MAX30100 library is set to update data with a sampling frequency of 100Hz, so it should take less than 10ms to call this function. For that, use a newer module such as MAX30102, to get improvement functionality.


