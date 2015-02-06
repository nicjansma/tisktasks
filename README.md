# TiskTasks for Todoist

Copyright 2015 Nic Jansma

http://nicj.net

Licensed under the MIT license

## Intro

This is the source code for the [_TiskTasks for Todoist_](http://tisktasks.com) Android app.

TiskTasks was first released in 2011, before the [official Todoist app](https://play.google.com/store/apps/details?id=com.todoist) was available.

The app was $0.99 until 2015-02-05, but is now __free__ and __open-source__.

It's available on the [Google Play](https://play.google.com/store/apps/details?id=com.nicjansma.tisktasks) and the [Amazon AppStore](http://www.amazon.com/Nicholas-Jansma-TiskTasks-for-Todoist/dp/B005HFAJI6/).

Honestly, the [official Todoist app](https://play.google.com/store/apps/details?id=com.todoist) has more features and is better supported.  For example, TiskTasks does not offer Offline support.  Feel free to use this app for reference, inspiration, or the basis of your cooler app.

## Using

You'll need a few Android library projects to build:
1. [Android Support Library: android-support-v7-appcompat](https://developer.android.com/tools/support-library/index.html)
2. [Google Play Services: google-play-services_lib](http://developer.android.com/google/play-services/setup.html)
3. [My Java / Android utilties project: com.nicjansma.library](https://github.com/nicjansma/com.nicjansma.library)

## Tests

There is a test project under `test`, using [Robotium](https://code.google.com/p/robotium/).  

The tests simply do basic UI validation of the Account and About screens, so they could use some improvement.

## Version History

Please see: http://tisktasks.com/version-history/