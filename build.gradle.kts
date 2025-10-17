// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktlint) apply true
    alias(libs.plugins.detekt) apply true
}

// Configure ktlint for all projects
subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.3.1")
        android.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(false)

        filter {
            exclude("**/build/**")
        }
    }
}

// Configure detekt
detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(file("$rootDir/config/detekt/detekt.yml"))
    baseline = file("$rootDir/config/detekt/baseline.xml")
}

tasks.register("formatKotlin") {
    group = "formatting"
    description = "Format Kotlin code using ktlint"
    dependsOn("ktlintFormat")
}

tasks.register("verifyStyle") {
    group = "verification"
    description = "Verify code style using ktlint and detekt"
    dependsOn("ktlintCheck", "detekt")
}
