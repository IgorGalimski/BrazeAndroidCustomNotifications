# BrazeAndroidCustomNotifications

Integration manual 

1. Import a package https://github.com/IgorGalimski/BrazeAndroidCustomNotifications/releases
2. Set custom broadcast receiver, unity player and firebase service in appboy manifest

<img width="1248" alt="Screen Shot 2021-03-13 at 16 50 46" src="https://user-images.githubusercontent.com/19680568/111032284-79a8fa00-841c-11eb-94ac-6e0b14ec6f0e.png">
<img width="1280" alt="Screen Shot 2021-03-13 at 16 50 27" src="https://user-images.githubusercontent.com/19680568/111032288-7d3c8100-841c-11eb-9f9c-6f427a781ea1.png">
<img width="1215" alt="Screen Shot 2021-03-13 at 16 50 17" src="https://user-images.githubusercontent.com/19680568/111032289-7e6dae00-841c-11eb-888e-98ae2c138fcc.png">

3. Replace default push backgournd (it's used in case of downloading issues), keep file name
https://github.com/IgorGalimski/BrazeAndroidCustomNotifications/blob/master/Assets/Plugins/Android/res/drawable/notification_background.jpg

4. Send a push from braze with ab_sbg param and url
