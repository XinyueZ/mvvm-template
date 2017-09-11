# MVVM Template

This repository provides templates which would be based on MVVM for Android application development.

The code-base will be from very begin to complicate use-cases. The purpose of the template is to ease starting Android application development with MVVM. Because of MVVM the templates use Android [databinding](https://developer.android.com/topic/libraries/data-binding/index.html) which can help us to write declarative layouts and minimize the glue code necessary to bind your application logic and layouts. 

# Pattern included

- [Actor](http://www.brianstorti.com/the-actor-model/?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=6127)
-- An implementation of actor model based on [Rx-Android](https://github.com/ReactiveX/RxAndroid)

# Kotlin

Since the official announcement of the Kotlin langauge on Android at Google I/O 2017 the Android Studio has provided stable plugIn for this language and can be very confirmed that the Kotlin would be main language on Android platform. 

All templates would/have been written in *Kotlin* in order to fellow new tech.

# History

- [v0.3](https://github.com/XinyueZ/mvvm-template/tree/feature/v0.3/navi-drawer-bottom): 

> Added application navigation driving through drawer and bottom-bar. Show a list of dummy data with RecyclerView.

1. Support full-screen app. See https://gist.github.com/chrisbanes/73de18faffca571f7292

2. Added drawer-layout for menu and header, also bottom-bar as navigation-view.

3. Support Rx, imported the libraries.

4. minAPI upper to 16 for the library of [Material Design Guideline](https://github.com/TheKhaeng/material-design-guideline).

5. Supported basic transition between activities after platform v21.

6. The ```ViewModel``` can contain some ```ViewModel```s, i.e the ```HomeViewModel``` which can contain ```DrawerSubViewModel``` to handle logical about the navigation-drawer.

7. The ```LifeActivity``` would be created by using binding, the sub-classes of ```LifeActivity``` can now use data-binding.

- [v0.2](https://github.com/XinyueZ/mvvm-template/tree/feature/v0.2/splash-actor-imported): Very early with MVVM, just a home screen. (>=API 14)

  1. Added a splash screen.

  2. Use actor model to navigate app from splash to home.

- [v0.1](https://github.com/XinyueZ/mvvm-template/tree/feature/v0.1/very-early-code-base): Very early with MVVM, just a home screen. (>=API 14)

# Reference

The template has been very inspired by following repositories:

Google's Android architecture  [todo-list](https://github.com/googlesamples/android-architecture)  
Google's Android architecture-components [sample](https://github.com/googlesamples/android-architecture-components)


# License

Copyright 2015 The Android Open Source Project, Inc.

Licensed to the Apache Software Foundation (ASF) under one or more contributor license agreements. See the NOTICE file distributed with this work for additional information regarding copyright ownership. The ASF licenses this file to you under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
