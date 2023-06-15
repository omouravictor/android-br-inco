<p align="center">
  <img src="https://github.com/omouravictor/rates-br/blob/master/rates-br-icon.png" alt="app-icon">
</p>

<h1 align="center">Rates BR</h1>

## [Download it on Google Play here](https://play.google.com/store/apps/details?id=io.sensify.sensor)

## Preview

Some application screens:

![app-screens](https://github.com/omouravictor/rates-br/assets/64164023/d1e22fd0-b524-477f-9622-68b6d03b70b5)

## Architecture

- MVVM

## Techs used

- [Navigation](https://developer.android.com/jetpack/androidx/releases/navigation)
- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [OkHttp](https://square.github.io/okhttp/)
- [Retrofit2](https://square.github.io/retrofit/)
- [Room](https://developer.android.com/training/data-storage/room)
- [Swiperefreshlayout](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout)
- [DataStore](https://developer.android.com/jetpack/androidx/releases/datastore)
- [Mockito](https://site.mockito.org/)

## About this project

In this application you can:

- Convert the main currencies to the Real!
- Follow the index of stock exchanges around the world!
- Follow the bitcoin price on the main brokerages!
- Using the app offline, with the last data received!

## Why?

This project is part of my personal portfolio, so, I'll be happy if you could provide me any
feedback about the project, code, structure or anything that you can report that could make me a
better developer!

## How to run?

- Open the project with Android Studio.
- Go to https://console.hgbrasil.com/keys, create an account and generate a free key for HG Finance
  API.
- Access project root folder (rates-br) and create a file with name "api.properties".
- Add the line below on "api.properties" file including your HG Finance API key.

```
API_KEY_HG_FINANCE="YOUR-API-KEY"
```

- That's all :)
