# RxPermissions
[![](https://jitpack.io/v/bamboolife/RxPermissions.svg)](https://jitpack.io/#bamboolife/RxPermissions)

This library allows the usage of RxJava with the new Android M permission model.

## Setup
To use this library your minSdkVersion must be >= 19.

1. 在根build.gradle中添加
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```
2. 在module中添加
```gradle
dependencies {
    implementation 'com.github.bamboolife:RxPermissions:1.0.0'
}
```
## Usage

Create a `RxPermissions` instance :

```java
final RxPermissions rxPermissions = new RxPermissions(this); // where this is an Activity or Fragment instance
```

**NOTE:** `new RxPermissions(this)` the `this` parameter can be a FragmentActivity or a Fragment. If you are using `RxPermissions` inside of a fragment you should pass the fragment instance(`new RxPermissions(this)`) as constructor parameter rather than `new RxPermissions(fragment.getActivity())` or you could face a `java.lang.IllegalStateException: FragmentManager is already executing transactions`.  

Example : request the CAMERA permission (with Retrolambda for brevity, but not required)

```java
// Must be done during an initialization phase like onCreate
rxPermissions
    .request(Manifest.permission.CAMERA)
    .subscribe(granted -> {
        if (granted) { // Always true pre-M
           // I can control the camera now
        } else {
           // Oups permission denied
        }
    });
```

If you need to trigger the permission request from a specific event, you need to setup your event
as an observable inside an initialization phase.

You can use [JakeWharton/RxBinding](https://github.com/JakeWharton/RxBinding) to turn your view to
an observable (not included in the library).

Example :

```java
// Must be done during an initialization phase like onCreate
RxView.clicks(findViewById(R.id.enableCamera))
    .compose(rxPermissions.ensure(Manifest.permission.CAMERA))
    .subscribe(granted -> {
        // R.id.enableCamera has been clicked
    });
```

If multiple permissions at the same time, the result is combined :

```java
rxPermissions
    .request(Manifest.permission.CAMERA,
             Manifest.permission.READ_PHONE_STATE)
    .subscribe(granted -> {
        if (granted) {
           // All requested permissions are granted
        } else {
           // At least one permission is denied
        }
    });
```

You can also observe a detailed result with `requestEach` or `ensureEach` :

```java
rxPermissions
    .requestEach(Manifest.permission.CAMERA,
             Manifest.permission.READ_PHONE_STATE)
    .subscribe(permission -> { // will emit 2 Permission objects
        if (permission.granted) {
           // `permission.name` is granted !
        } else if (permission.shouldShowRequestPermissionRationale) {
           // Denied permission without ask never again
        } else {
           // Denied permission with ask never again
           // Need to go to the settings
        }
    });
```

You can also get combined detailed result with `requestEachCombined` or `ensureEachCombined` :

```java
rxPermissions
    .requestEachCombined(Manifest.permission.CAMERA,
             Manifest.permission.READ_PHONE_STATE)
    .subscribe(permission -> { // will emit 1 Permission object
        if (permission.granted) {
           // All permissions are granted !
        } else if (permission.shouldShowRequestPermissionRationale)
           // At least one denied permission without ask never again
        } else {
           // At least one denied permission with ask never again
           // Need to go to the settings
        }
    });
```
