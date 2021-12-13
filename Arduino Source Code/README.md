# Source Code for Arduino
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
When using bluetooth serial, the heart rate and oximeter data are not updating because a problem. On esp32, somehow the library of max30100 and bluetooth serial can't work together. This is because if you use a serial bluetooth module, it will slow down the looping time of the routine. The max30100 library is set to update data with a sampling frequency of 100Hz, so it should take less than 10ms to call this function.
For that, use a newer module such as MAX30102, to get improvement functionality.
