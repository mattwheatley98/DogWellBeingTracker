# Dog Well-Being Tracker
Since embarking (pun intended) on my journey of becoming an Android developer, I've always wanted to create a dog-related app. After some brainstorming, I came up with the idea for a dog wellbeing tracker that also allowed me to practice new and important concepts I learned in Android development. Some of the concepts I wanted to practice were Android Room, dependency injection with Dagger-Hilt, cleaner and more reusable code (especially for composables), and clean architecture with an MVVM design pattern. After creating my Novi, Michigan Tour app, which, admittedly, doesn't have many use cases, I wanted to create something practical that myself and others can benefit from. 

The main features of this app are as follows:
1. A home screen that displays a list of your dogs and their daily wellbeing summary with the aforementioned widgets. Clicking on a dog's picture will "select" them to be used by the app's trackers (can also be done by clicking on their picture in the top bar). If the list of dogs is empty, users will be prompted to add a dog in a separate screen.
2. A screen for adding your dog's information to the local database that includes their: name, age, breed, weight, daily calorie intake, sex, and picture. 
3. The ability to edit and delete your dog's information from the local database.
4. Bathroom tracking that tracks the date, time, type, and notes you have for your dog's bathroom breaks. The widget-styled summary for this feature displays the daily number of times your dog has peed and/or pooped.
5. Food tracking that tracks the date, notes, calories, and meal type for your dog's meals. The widget-styled summary for this feature is a linear progress indicator that progresses based on the dog's daily running calorie count relative to their daily maximum calorie limit.
6. Walk tracking that tracks the date, start time, duration (in minutes), and notes for your dog's walks. The widget-styled summary for this feature identifies how many walks your dog has been for the current day.
7. In addition to fields for user input, each tracker screen features a daily log. All entries that have a date matching the device's local date will appear here. All entries, regardless of date (you can add entries retroactively), will go into a comprehensive log that can be accessed on each tracker's screen. Daily logs will automatically reset when the device's local date changes.

## Demonstrations
Add Dog  | Screenshot
------------- | -------------
<img src="https://user-images.githubusercontent.com/113391095/216479280-b7396186-1442-48d2-a6d7-fe610b66c6b1.gif" width = 350 height = 700>  |  <img src="https://user-images.githubusercontent.com/113391095/216480100-4ed2e1aa-0205-4c7d-85e7-c4d8644776d0.png" width = 350 height = 700>

Bathroom Tracker  | Screenshot
------------- | -------------
<img src="https://user-images.githubusercontent.com/113391095/216479337-dc79ebf1-660f-40fc-8291-15bb77264d51.gif" width = 350 height = 700>  |  <img src="https://user-images.githubusercontent.com/113391095/216480170-c123ca7e-8584-4f8d-aa5b-5a53066d48e7.png" width = 350 height = 700>

Food Tracker  | Screenshot
------------- | -------------
<img src="https://user-images.githubusercontent.com/113391095/216479397-366267cb-ab42-4326-ac5d-c6ea4ed3cd76.gif" width = 350 height = 700>  |  <img src="https://user-images.githubusercontent.com/113391095/216480309-bf7d9ea5-c3a1-428e-92ad-213b893c1d98.png" width = 350 height = 700>

Walk Tracker  | Screenshot
------------- | -------------
<img src="https://user-images.githubusercontent.com/113391095/216479425-661b7661-8662-4819-8d1c-534450a49b3e.gif" width = 350 height = 700>  |  <img src="https://user-images.githubusercontent.com/113391095/216480331-53cf6d0f-3b34-4874-af1e-0840ca177ffa.png" width = 350 height = 700>

Dog Information  | Screenshot
------------- | -------------
<img src="https://user-images.githubusercontent.com/113391095/216479361-e32ce1f2-ff2e-40b3-8e4e-1fcc8eac95cd.gif" width = 350 height = 700>  |  <img src="https://user-images.githubusercontent.com/113391095/216480210-2d1f14fb-8626-41f6-9959-8db6d6328313.png" width = 350 height = 700>

Multiple Dog Profiles  | Screenshot
------------- | -------------
<img src="https://user-images.githubusercontent.com/113391095/216479312-0b10b3aa-9da4-4e6d-8860-9bb7b3d16c35.gif" width = 350 height = 700>  |  <img src="https://user-images.githubusercontent.com/113391095/216480137-d4f71e86-9eaf-40c1-b096-c470130a3ad4.png" width = 350 height = 700>

