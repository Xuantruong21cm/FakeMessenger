rootProject.name = "Fake Messenger"

rootDir
  .walk()
  .maxDepth(1)
  .filter {
    it.name != rootProject.name && it.name != "androidbase" &&
      (
        it.name != "buildSrc" && it.isDirectory && file("${it.absolutePath}/build.gradle.kts").exists() ||
          it.name != "buildSrc" && it.isDirectory && file("${it.absolutePath}/build.gradle").exists()
        )
  }
  .forEach {
    include(":${it.name}")
  }

File(rootDir, "androidbase")
  .walk()
  .maxDepth(1)
  .filter {
    it.isDirectory && (
            it.name != "buildSrc" && it.isDirectory && file("${it.absolutePath}/build.gradle.kts").exists() ||
                    it.name != "buildSrc" && it.isDirectory && file("${it.absolutePath}/build.gradle").exists()
            )
  }.forEach {
  include(":${it.name}")
  project(":${it.name}").projectDir = it
}