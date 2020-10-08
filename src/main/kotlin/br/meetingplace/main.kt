package br.meetingplace

import br.meetingplace.entities.grupos.*
import br.meetingplace.entities.usuario.*
import br.meetingplace.management.EntitiesManagement

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val SystemV = EntitiesManagement()

fun main (){

    embeddedServer(Netty, 8823) {
        routing {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                }
            }
            get("/user"){
                call.respond(SystemV.getUsers())
            }
            get("/logged"){

                call.respond(SystemV.getUserLogged())
            }
            get("/logged2"){
                call.respond(SystemV.getUserLoggedAuto())
            }
            post("/user/create"){
                val user = call.receive<Profile>()
                call.respond(SystemV.createUser(user))
            }
            post("/user/login"){
                val user = call.receive<Login>()
                call.respond(SystemV.login(user))
            }
            post("/user/follow"){
                val follow = call.receive<Follower>()
                call.respond(SystemV.follow(follow))
            }
            post("/user/unfollow"){
                val follow = call.receive<Follower>()
                call.respond(SystemV.unfollow(follow))
            }
            post("/user/message"){
                val chat = call.receive<Conversation>()
                call.respond(SystemV.messengerUser(chat))
            }


            //Group related
            get("/group"){
                call.respond(SystemV.getGroups())
            }
            post("/group/create"){
                val group = call.receive<Group>()
                call.respond(SystemV.createGroup(group))
            }
            post("/group/member"){
                val member = call.receive<UserMember>()
                call.respond(SystemV.addMember(member))
            }
            post("/group/member/remove"){
                val member = call.receive<UserMember>()
                call.respond(SystemV.removeMember(member))
            }
            post("/group/message"){
                val chatGroup = call.receive<GroupConversation>()
                call.respond(SystemV.messengerGroup(chatGroup))
            }
            //Group related
        }
    }.start(wait = true)
}


