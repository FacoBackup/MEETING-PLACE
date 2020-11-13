package br.meetingplace.server.controllers.dependencies.newRW.group

import br.meetingplace.server.subjects.services.groups.Group
import br.meetingplace.server.subjects.services.owner.OwnerType
import br.meetingplace.server.subjects.services.owner.group.GroupOwnerData
import com.google.gson.GsonBuilder
import java.io.File

class GroupRW: GroupRWInterface {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    override fun delete(data: Group) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/GROUPS/${data.getGroupID()}")
        val file = File(directory)
        try {
            file.delete()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun read(id: String): Group{
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/GROUPS/$id/$id.json")

        val file = File(directory)
        var group = Group(GroupOwnerData("", "", OwnerType.GROUP), "", "", "")
        try {
            val bufferedReader = file.bufferedReader()
            val inputString = bufferedReader.use { it.readText() }
            group = gson.fromJson(inputString, Group::class.java)
        } finally {
            return group
        }
    }

    override fun write(data: Group) {
        val directory = (File("build.gradle").absolutePath.removeSuffix("build.gradle") + "/src/main/kotlin/br/meetingplace/DATA_BASE/GROUPS/${data.getGroupID()}")
        val jsonFile = "$directory/${data.getGroupID()}.json"
        try {
            val file = File(jsonFile)
            val json = gson.toJson(data)

            if (!File(directory).exists())
                File(directory).mkdir()

            file.writeText(json)
        } catch (e: Exception) {
            println(e.message)
        }
    }
}