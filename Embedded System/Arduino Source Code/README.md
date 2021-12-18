# 🤖 Arduino
We're using ESP32 Wemos D1 Mini

For some other controllers including controllers which are not ESP family, may need some more configuration.

How to use this code
- Install and open arduino IDE
- Install esp32 board family, using board manager
- Clone and open "CovitorWatch.ino"
- Flash to esp32 controller using available port
- Done

Features:
- Motion detector with one button
- Menu page
- Clock page
- Heart Rate, Blood Saturation, and Temparature readings page.
- Bluetooth Sync Mode
- QR code
- Blank screen for power saving

Library:
- BluetoothSerial
- Adafruit_GFX
- Adafruit_SSD1306
- RTClib
- MAX30100_PulseOximeter
- Protocentral_MAX30205
- OneButton

## <i>*How to use</i>
- double click to OK and go to menu page.
- triple click on any page to go QR code page.
- Long click on any page to go blank screen, use double click to exit blank screen mode.


## <i>*For improvement</i>
When using bluetooth serial, the heart rate and oximeter data are not updating because a problem. On ESP32, somehow the library of MAX30100 and bluetooth serial can't work together. This is because if you use a serial bluetooth module, it will slow down the looping time of the routine. The MAX30100 library is set to update data with a sampling frequency of 100Hz, so it should take less than 10ms to call this function. For that, use a newer module such as MAX30102, to get improvement functionality.
