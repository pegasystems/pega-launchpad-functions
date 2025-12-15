import org.gradle.api.Project

// When applied, this script will read flags from project extra properties and add
// common dependencies accordingly. Modules must set flags BEFORE applying this script, for example:
// extra["useCommonTestLibraries"] = true
// extra["useAnnotations"] = true
// apply(from = rootProject.file("gradle/common-dependencies.gradle.kts"))

val jv = extra["PegaLaunchpadFunctionsJunitVersion"].toString()
val av = extra["annotationsVersion"].toString()
val gv = extra["gsonVersion"].toString()
val jacksonV = extra["jacksonVersion"].toString()

fun hasFlag(name: String): Boolean {
    return try {
        extra.has(name) && (extra[name] == true || extra[name] == "true")
    } catch (e: Exception) {
        false
    }
}

// Add JUnit BOM and basic JUnit setup
if (hasFlag("useCommonTestLibraries")) {
    dependencies {
        add("testImplementation", platform("org.junit:junit-bom:$jv"))
        add("testImplementation", "org.junit.jupiter:junit-jupiter")
        add("testRuntimeOnly", "org.junit.jupiter:junit-jupiter-engine:$jv")
    }
}

// Add additional JUnit modules when requested
if (hasFlag("useFullJunit")) {
    dependencies {
        add("testImplementation", "org.junit.jupiter:junit-jupiter-api:$jv")
        add("testImplementation", "org.junit.jupiter:junit-jupiter-params:$jv")
    }
}

// Annotations
if (hasFlag("useAnnotations")) {
    dependencies {
        add("compileOnly", "org.jetbrains:annotations:$av")
    }
}

// Gson
if (hasFlag("useGson")) {
    dependencies {
        add("implementation", "com.google.code.gson:gson:$gv")
    }
}

// Jackson implementation
if (hasFlag("useJacksonImplementation")) {
    dependencies {
        add("implementation", "com.fasterxml.jackson.core:jackson-databind:$jacksonV")
    }
}

// Jackson test libraries + compileOnly annotation
if (hasFlag("useJacksonTestLibraries")) {
    dependencies {
        add("compileOnly", "com.fasterxml.jackson.core:jackson-annotations:$jacksonV")
        add("testImplementation", "com.fasterxml.jackson.core:jackson-annotations:$jacksonV")
        add("testImplementation", "com.fasterxml.jackson.core:jackson-core:$jacksonV")
        add("testImplementation", "com.fasterxml.jackson.core:jackson-databind:$jacksonV")
    }
}
