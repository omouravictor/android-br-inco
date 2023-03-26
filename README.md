<p align="center">
  <img src="https://github.com/omouravictor/rates-br/blob/master/rates-br-icon.png" alt="app-icon">
</p>

<h1 align="center">Rates BR</h1>
<p align="center">A financial app for Brazil.</p>

## Preview

Some application screens:

![app-screens](https://user-images.githubusercontent.com/64164023/227753715-065e3936-c801-435a-af29-3cc93383ee6e.png)

## Architecture

- [MVVM](https://medium.com/android-dev-br/arquiteturas-em-android-mvvm-kotlin-android-architecture-components-databinding-lifecycle-d5e7a9023cf3)

## Techs Used

- [Room](https://developer.android.com/training/data-storage/room)
- [Retrofit2](https://square.github.io/retrofit/)
- [DataStore](https://developer.android.com/jetpack/androidx/releases/datastore)
- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [OkHttp](https://square.github.io/okhttp/)
- [Swiperefreshlayout](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout)
- [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)

## About this project

In this application you can:

  - Convert the main currencies to the Real!
  - Follow the index of stock exchanges around the world!
  - Follow the bitcoin price on the main brokerages!
  - Using the app offline, with the last data received!

## Why?

This project is part of my personal portfolio, so, I'll be happy if you could provide me any feedback about the project, code, structure or anything that you can report that could make me a better developer!

## How to run?

- Open the project with Android Studio.
- Go to https://hgbrasil.com, create an account and generate a key for HG Finance API.
- Access project root folder (rates-br) and create a file with name "api.properties".
- Add the line below on "api.properties" file including your HG Finance API key.
```
API_KEY_HG="your-api-key"
```
- That's all :)
