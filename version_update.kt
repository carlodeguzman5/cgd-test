import java.io.File
import java.util.concurrent.TimeUnit
fun main() {
    // Configuration (adjust as needed)
    val appDir = "app" // Path to your app's module directory (e.g., "app")
    val buildGradlePath = "$appDir/build.gradle.kts"  // Or "build.gradle" if using Groovy
    val projectRoot = ".." // Relative path to the project root. Adjust if necessary.
    // --- Step 1: Go into app directory ---
    println("Changing directory to $appDir")
    val processBuilder = ProcessBuilder() // Use ProcessBuilder for better control and error handling
    processBuilder.directory(File(appDir))
    // --- Step 2: Get current versionCode and print it ---
    println("Getting current versionCode...")
    val currentVersionCode = getVersionCode(buildGradlePath)
    println("Current versionCode: $currentVersionCode")
    // --- Step 3: Increment versionCode by one and print it ---
    println("Incrementing versionCode...")
    val newVersionCode = currentVersionCode + 1
    println("New versionCode: $newVersionCode")
    // --- Step 4: Get current versionName and print it ---
    println("Getting current versionName...")
    val currentVersionName = getVersionName(buildGradlePath)
    println("Current versionName: $currentVersionName")

    // --- Step 5: Ask user for new versionName and set it then print it ---
    print("Enter new versionName: ")
    val newVersionName = readlnOrNull()?.trim() ?: ""
    if (newVersionName.isEmpty()) {
        println("Invalid versionName.  Exiting.")
        return
    }
    println("New versionName: $newVersionName")
    // Update build.gradle.kts with new versionCode and versionName
    updateBuildGradle(buildGradlePath, newVersionCode, newVersionName)
    println("$buildGradlePath updated.")
    // --- Step 6: Stage changes to build.gradle ---
    println("Staging changes to build.gradle.kts...")
    executeCommand(processBuilder, "git", "add", "build.gradle.kts")
    // --- Step 7: Go back one directory to project root ---
    println("Changing to Project root ${projectRoot}")
    processBuilder.directory(File(projectRoot))

    // --- Step 8: Commit version update with message Bumped up version ---
    println("Committing changes...")
    executeCommand(processBuilder, "git", "commit", "-m", "Bumped up version")
    // --- Step 9: Tag with versionName ---
    println("Tagging with versionName: $newVersionName")
    executeCommand(processBuilder, "git", "tag", newVersionName)
    // --- Step 10: Push changes ---
    println("Pushing changes...")
    executeCommand(processBuilder, "git", "push", "origin", "HEAD")       // Push the current branch
    executeCommand(processBuilder, "git", "push", "origin", newVersionName)  //Push tags
    println("Version update and push complete!")
}
fun getVersionCode(buildGradlePath: String): Int {
    val buildGradleFile = File(buildGradlePath)
    val versionRegex = Regex("""versionCode\s*=\s*(\d+)""")  // Kotlin DSL
    // val versionRegex = Regex("""versionCode\s+(\d+)""") // Groovy DSL
    val fileText = buildGradleFile.readText()
    val matchResult = versionRegex.find(fileText)
    return matchResult?.groupValues?.get(1)?.toIntOrNull() ?: throw RuntimeException("Could not find versionCode in $buildGradlePath")
}
fun getVersionName(buildGradlePath: String): String {
    val buildGradleFile = File(buildGradlePath)
    val versionRegex = Regex("""versionName\s*=\s*"([^"]+)"""") // Kotlin DSL
    // val versionRegex = Regex("""versionName\s+"([^"]+)"""") // Groovy DSL
    val fileText = buildGradleFile.readText()
    val matchResult = versionRegex.find(fileText)
    return matchResult?.groupValues?.get(1) ?: throw RuntimeException("Could not find versionName in $buildGradlePath")
}
fun updateBuildGradle(buildGradlePath: String, versionCode: Int, versionName: String) {
    val buildGradleFile = File(buildGradlePath)
    val fileText = buildGradleFile.readText()
    val versionCodeRegex = Regex("""(versionCode\s*=\s*)(\d+)""") // Kotlin DSL
    //val versionCodeRegex = Regex("""(versionCode\s+)(\d+)""") // Groovy DSL
    val versionNameRegex = Regex("""(versionName\s*=\s*")([^"]+)(")""") // Kotlin DSL
    // val versionNameRegex = Regex("""(versionName\s+")([^"]+)(")""") // Groovy DSL
    val updatedText = fileText
        .replace(versionCodeRegex, "$1$versionCode")
        .replace(versionNameRegex, "$1$versionName$3")
    buildGradleFile.writeText(updatedText)
}
fun executeCommand(processBuilder: ProcessBuilder, vararg command: String) {
    val process = processBuilder.command(*command).start()
    process.waitFor(60, TimeUnit.SECONDS) // Add a timeout to prevent hanging
    if (process.exitValue() != 0) {
        println("Error executing command: ${command.joinToString(" ")}")
        println("Error output:\n${process.errorStream.bufferedReader().readText()}")
        throw RuntimeException("Command failed: ${command.joinToString(" ")}") // Exit on error
    } else {
        println("Output:\n${process.inputStream.bufferedReader().readText()}")
    }
}