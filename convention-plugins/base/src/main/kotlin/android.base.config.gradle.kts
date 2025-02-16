import com.android.build.gradle.ProguardFiles
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import ru.crazerr.conventionplugins.base.androidConfig
import ru.crazerr.conventionplugins.base.libs
import ru.crazerr.conventionplugins.base.kotlinJvmCompilerOptions
import ru.crazerr.conventionplugins.base.projectJavaVersion

androidConfig {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = projectJavaVersion
        targetCompatibility = projectJavaVersion
    }
}

kotlinJvmCompilerOptions {
    jvmTarget.set(JvmTarget.fromTarget(projectJavaVersion.toString()))
}