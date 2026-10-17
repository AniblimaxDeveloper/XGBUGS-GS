# Build recovery checklist

If GitHub Actions fails:

1. Confirm the newest run is for the newest commit on `main`.
2. Confirm the step **Verify JVM targets are aligned** succeeds.
3. Confirm the workflow log shows Java 17 and Gradle 8.9.
4. If `compileDebugKotlin` fails, copy the complete `What went wrong` section and the first Kotlin compiler error. Do not create a new file ending in `.ktso`; the Android module config is `app/build.gradle.kts`.
5. If the build succeeds but the artifact is absent, inspect **Upload debug APK** and confirm it references `app/build/outputs/apk/debug/app-debug.apk`.
