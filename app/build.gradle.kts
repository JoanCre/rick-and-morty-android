plugins {
    alias(libs.plugins.es.rudo.application.compose)
    alias(libs.plugins.es.rudo.compose.ui)
    alias(libs.plugins.es.rudo.dependency.injection)
}

/** Common Android configurations are already handled by the convention plugins.
 * If module-specific configuration is needed, for example, for app signing (signingConfigs),
 * add it here. */
android {
    namespace = "com.rudo.rickAndMortyApp" // Replace with your actual namespace

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
    }
}

/** The convention plugins (`es.rudo.*`) already provide:
 *   All necessary Jetpack Compose dependencies (UI, Material3, Navigation, Activity, etc.)
 *   Compose compiler plugin.
 *   For dependency injection Hilt and KSP.
 *   Kotlinx Serialization JSON.
 *
 * Add modules or other app-specific dependencies.
 *
 * Example (Room):
 * dependencies {
 *     implementation(project(":profile:))
 *     implementation(libs.androidx.room.ktx)
 *     ksp(libs.androidx.room.compiler)
 * }
 */

dependencies {
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
    implementation(libs.gson)
    implementation(libs.retrofit.converter.gson)

    // Coroutines (from bundle)
    implementation(libs.bundles.coroutines)

    // AndroidX Lifecycle (ViewModel, runtime, compose helpers)
    implementation(libs.bundles.lifecycle)

    // Hilt (compiler for generated code)
    implementation(libs.bundles.dependencyInjection)
    ksp(libs.hilt.android.compiler)

    // Coil
    implementation(libs.coil.compose)

    // Paging 3
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)

    // Room Database
    implementation("androidx.room:room-runtime:2.7.2")
    implementation("androidx.room:room-ktx:2.7.2")
    ksp("androidx.room:room-compiler:2.7.2")
}
