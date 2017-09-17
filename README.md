# MVVM Template

This repository provides templates which would be based on MVVM for Android application development.

The code-base will be from very begin to complicate use-cases. The purpose of the template is to ease starting Android application development with MVVM. Because of MVVM the templates use Android [databinding](https://developer.android.com/topic/libraries/data-binding/index.html) which can help us to write declarative layouts and minimize the glue code necessary to bind your application logic and layouts. 

# Pattern included

- General [GOF](https://en.wikipedia.org/wiki/Design_Patterns) patterns
- Repository pattern to provide data
- [Actor](http://www.brianstorti.com/the-actor-model/?utm_source=android-arsenal.com&utm_medium=referral&utm_campaign=6127)
-- An implementation of actor model based on [Rx-Android](https://github.com/ReactiveX/RxAndroid)


# Kotlin

Since the official announcement of the Kotlin langauge on Android at Google I/O 2017 the Android Studio has provided stable plugIn for this language and can be very confirmed that the Kotlin would be main language on Android platform. 

All templates would/have been written in *Kotlin* in order to fellow new tech.

# History

- [v0.4](https://github.com/XinyueZ/mvvm-template/tree/feature/v0.4/repository): in [dev](https://github.com/XinyueZ/mvvm-template/tree/dev)

> The template is using ```product```, ```software licenses``` to demonstrate how to use MVVM to load data and show them on UI. For your own use-case, you can reset them and build your own features.  

> Use repository to populate data instead hard coding in [v0.3](https://github.com/XinyueZ/mvvm-template/tree/feature/v0.3/navi-drawer-bottom)

1. Don't use actors to drive screen. Use [LiveData](https://developer.android.com/topic/libraries/architecture/livedata.html) to change different fragments or views.
2. Use actors only for internal interaction in view-models.
3. Added ```LL``` which is a easy "logcat" to logout with filter "#!#!", you can see line-number in output directly 
i.e 

```
09-14 22:38:05.873 14776-14776/com.template.mvvm D/LL$Companion.d @line: 155: #!#!ProductsRepository::getAllProducts#!#!
09-14 22:38:05.874 14776-14776/com.template.mvvm D/LL$Companion.d @line: 155: #!#!ProductsRemote::getAllProducts#!#!
```

4. Use feature of  [LiveData](https://developer.android.com/topic/libraries/architecture/livedata.html) like switchMap, observe to populate data. (https://developer.android.com/topic/libraries/architecture/livedata.html)
5. The repository would populate data with ```remote```, ```local``` and  ```cache``` .
6. Added [Retrofit](http://square.github.io/retrofit/) to load data, use [Room](https://developer.android.com/topic/libraries/architecture/room.html) to persistent data.
7. Added [Rx-Android](https://github.com/ReactiveX/RxAndroid) to process data from source i.e ```remote``` to the [LiveData](https://developer.android.com/topic/libraries/architecture/livedata.html).

- [v0.3](https://github.com/XinyueZ/mvvm-template/tree/feature/v0.3/navi-drawer-bottom): 

> Added application navigation driving through drawer and bottom-bar. In this version there're two dummy menu-items that will open a products and about-me view.

> To bottom-bar. According to material design the bar always navigate the app between top-level views, well, here I defined item1, item2, item3 and opening fragments for them.

1. Support full-screen app. See https://gist.github.com/chrisbanes/73de18faffca571f7292

2. Added drawer-layout for menu and header, also bottom-bar as navigation-view.

3. Support Rx, imported the libraries.

4. minAPI upper to 16 for the library of [Material Design Guideline](https://github.com/TheKhaeng/material-design-guideline).

5. Supported basic transition between activities after platform v21.

6. The ```ViewModel``` can contain some ```ViewModel```s, i.e the ```HomeViewModel``` which can contain ```DrawerSubViewModel``` to handle logical about the navigation-drawer.

7. The ```LifeActivity``` would be created by using binding, the sub-classes of ```LifeActivity``` can now use data-binding.

8. Added a software-license view.

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
