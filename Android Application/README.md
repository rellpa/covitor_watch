# CoViTor Watch APP
### <i>!!This app created using android studio and java languange</i>

#

## How this app work?
In our embedded system (CoViTor Smartwatch), we use bluetooth serial (Bluetooth 2). So we can communicate using Bluetooth Socket from android. Basically, using bluetooth serial communication to an embedded system is to use an outputstream to send data and an inputstream to get data. We just need to manage how the two work together to form a useful application. For the monitoring sequence, we use a query method when needed so that the embedded system does not always send data. First the application will send a command that the embedded system knows what to send. After that the embedded system will send sensor data to the application, and the application will parse the byte data into strings or integers for further processing.

<p align="left">
  <img width="400" src="Demo App/Code1.jpg" />
</p>
#

## How to use this project ?
For demo purpose (if you use same firmware as this embbeded system) you can install APK app on your android phone.

If you want reverse engineering this project app:
1. Install android studio on your computer
2. Clone "CovitorWatchEmbedded" folder
3. Import that folder to android studio
4. Sync graddle (if necessary), and you success to import this app and ready to reverse engineering to your needs!

<i>if need help, feel free to contact us! MatthewBrandon21 / rellpa.</i>

#

Some screenshoot of this app:
<p align="left">
  <img width="200" src="Demo App/Splash Screen.jpg" />
  <img width="200" src="Demo App/Home Screen.jpg" />
  <img width="200" src="Demo App/Readings 2.jpg" />
</p>
<i>For full screenshoot you can see in "Demo App" folder.</i>

#

### <i>*For improvement<i/>
Maybe UI/UX design can be more prettier. FYI we use figma to design UI of this app and linear layout in xml layout.