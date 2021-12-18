# CoViTor Watch APP
### <i>!!This app created using android studio and java languange</i>

#

## How this app work?
In our embedded system (CoViTor Smartwatch), we using bluetooth serial (Bluetooth 2). So we can communicate using Bluetooth Socket from android.
Basically, to use bluetooth communication to embedded systems is to use an outputstream to send data and an inputstream to get data. We just have to manage how both of them can work to form a useful application.
For monitoring sequence, we use the way of asking when needed so embedded system not always send data. Firstly the app will send command that embedded system know what to send. After that embedded system will send sensor data to app, and app will parse byte data to string and integer for next process.

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
4. sync graddle (if necessary), and you success to import this app and ready to reverse engineering to your needs!

<i>if need help, feel free to contact us! by MatthewBrandon21 / rellpa.</i>

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
Maybe UI/UX design can be more prettier. FYI we use figma to design UI of this app.