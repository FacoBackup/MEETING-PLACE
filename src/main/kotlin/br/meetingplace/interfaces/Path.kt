package br.meetingplace.interfaces

import java.io.File

interface Path {
    fun verifyPath(type: String, fileName: String) = File(File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/logs/$type/$fileName.json").exists()
}