# V5 Build recovery checklist

1. `cd ~/projects/XGBUGS-GS`
2. `grep -nE 'sourceCompatibility|targetCompatibility|jvmTarget|versionCode|versionName' app/build.gradle.kts`
3. `cat build.gradle.kts`
4. `git diff -- app/build.gradle.kts build.gradle.kts .github/workflows/android.yml`
5. `git status`
6. Push changes only after confirming the diff includes the intended file changes.

Expected values:
- AGP 8.7.2
- Kotlin Gradle Plugin 2.1.20
- Java source/target 17
- Kotlin jvmTarget 17
- versionCode 5 / versionName 5.0
- CI Gradle 8.9, JDK 17, compile SDK 35

If `git status` is clean and no run with the V5 workflow was triggered, inspect the last commit in GitHub before trying more commands.
