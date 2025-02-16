import ru.crazerr.conventionplugins.base.androidConfig

plugins {
    id("android.base.config")
}

androidConfig {
    buildFeatures {
        // enables a Compose tooling support in the Android Studio
        compose = true
    }
}