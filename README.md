# Journal App

## Introduction

This is a simple Android Application. It's my final challenge as a receipient of 2018's 
[Google Africa challenge scholarship](https://www.udacity.com/google-africa-scholarships).

As part of a 7-day coding challenge, we were tasked in the *Intermediate Android Development* track to build a simple
app where users can pen down their *thoughts* and *feelings*. You can find more info on the challenge in this 
[Google Doc](https://docs.google.com/document/d/1OvRxkhQpAfoclbdDrFu7mZmonA2cqq6Ri163RRKgWpg/edit).

## Features
- Add / Modify journal entries
- Search for journal entries by title or content
- [Google Sign](https://developers.google.com/identity/sign-in/android/start-integrating) in and [Firebase Authentication](https://firebase.google.com/docs/auth/)
- Backup and sync using [Firebase Cloud Firestore](https://firebase.google.com/docs/firestore/) 

## Installation

You can install this app simply by downloading the debug build [here](https://github.com/josephdalughut/journal/blob/master/app/apk/journal-debug.apk?raw=true). 
- Copy the file to a location on your Android device.
- Click on the file to start the installation process.

## Building the Source Code

Alternatively, you can download the complete source code from here and build it from scratch.

#### What you'll need

- [Android studio](https://developer.android.com/studio/) (this code was built using version **3.1.2**, but anything above **2.0.0** should do)
- Source code
- A couple of minutes

### Directions

- Download the source code either from [here](https://github.com/josephdalughut/journal/archive/master.zip) or by cloning using `git`:

      cd your_preferred_directory
      git clone https://github.com/josephdalughut/journal.git

- Fire up **Android Studio** and `Open an existing Android Studio project`
- Navigate to and select the downloaded project folder
- [Add Firebase to your project](https://firebase.google.com/docs/android/setup)
- Sync and build your project
- Build and Run the app on your emulator or device.

## Testing
Units were test-driven
#### Unit Tests
- [Android JUnit](https://developer.android.com/training/testing/junit-runner)
- [Roboletric](http://robolectric.org/)
- [Mockito](http://site.mockito.org/)

- Directory: */app/src/test*

#### Instrumentation & UI Tests
A few Ui tests as well (however I mostly used Robolectric for testing UI units)
- [Espresso](https://developer.android.com/training/testing/espresso/)

- Directory: */app/src/androidTest*

## Screenshots

<p float="left">
  <img src="https://storage.googleapis.com/joey-ng.appspot.com/portfolio/journal/device-2018-07-01-164527.png" width="200" />
  <img src="https://storage.googleapis.com/joey-ng.appspot.com/portfolio/journal/device-2018-07-01-164614.png" width="200" /> 
  <img src="https://storage.googleapis.com/joey-ng.appspot.com/portfolio/journal/device-2018-07-01-164726.png" width="200" />
  <img src="https://storage.googleapis.com/joey-ng.appspot.com/portfolio/journal/device-2018-07-01-164747.png" width="200" />
</p>

## License

    MIT License
    
    Copyright (c) 2018 Joey Dalu
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.

## Acknowledgements

- **ALCWithGoogle 2018 Scholars** for a fun and amazing time working together via *slack* and our meetups in *Yola, Adamawa*.
- **[Andela Learning Community](https://andela.com/alcwithgoogle/)** For their constant support and for coordinating the program.
- **[Google](https://www.google.com) & [Udacity](https://www.udacity.com/)** For the entire scholarship opportunity, course content and support during the program.

I learnt a lot during this program, and it's been an amazing experience. 