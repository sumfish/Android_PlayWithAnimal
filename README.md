# PlayWithAnimal
Flappy Bird with Voice and Smile

## Download
[app-debug.apk](./app-debug.apk)

## Libraries
- [TarsosDSP](https://github.com/JorenSix/TarsosDSP)
  - realtime audio processing framework
- [ML Kit for Firebase](https://firebase.google.com/docs/ml-kit/)
  - Google’s machine learning package to mobile developers

## Screenshots
- flap by pitch of voice
<img src="./screenshots/1.jpg" alt="flap by pitch of voice" width="400"/>

- use smile or crying
<img src="./screenshots/2.jpg" alt="use smile or crying" width="400"/>

## Game Hints
- If not setting pitch threshold, default is 200Hz(average pitch of girls).
- Based on whether your facial expression matches the icon, birds will become bigger or smaller.

## Demo 
- [Video](https://youtu.be/HcYMKDWV4Wo)
- [Slide](https://ppt.cc/fHwlnx)

## Contributors
- [annie31123](https://github.com/annie31123)
  - write the basic flappy bird game (use the surfaceview to draw the game animation)
- [sumfish](https://github.com/sumfish)
  - use TarsosDSP for pitch detection
- [winonecheng](https://github.com/winonecheng)
  - use ML Kit to recognize facial expressions (smiling)
