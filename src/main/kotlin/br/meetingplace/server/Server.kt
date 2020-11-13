package br.meetingplace.server

import br.meetingplace.server.dto.CreationData
import br.meetingplace.server.dto.Login
import br.meetingplace.server.dto.MemberOperator
import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.dto.chat.ChatComplexOperator
import br.meetingplace.server.dto.chat.ChatFinderOperator
import br.meetingplace.server.dto.chat.ChatSimpleOperator
import br.meetingplace.server.dto.chat.MessageData
import br.meetingplace.server.dto.community.ApprovalData
import br.meetingplace.server.dto.topics.TopicData
import br.meetingplace.server.dto.topics.TopicOperationsData
import br.meetingplace.server.dto.user.ProfileData
import br.meetingplace.server.dto.user.UserCreationData
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*


fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 3000

    embeddedServer(Netty, port) {
        routing {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                }
            }
            //SEARCH
            get("/search/user") {
                val data = call.receive<SimpleOperator>()
                val search = searchSystem.searchUser(data)

                if (search.isEmpty())
                    call.respond("Nothing found.")
                else
                    call.respond(search)
            }

            get("/search/community") {
                val data = call.receive<SimpleOperator>()
                val search = searchSystem.searchCommunity(data)

                if (search == null)
                    call.respond("Nothing found.")
                else
                    call.respond(search)
            }

            //COMMUNITY
            post("/community") {
                val data = call.receive<CreationData>()
                call.respond(communitySystem.create(data))
            }
            patch("/community/approve/group") {
                val data = call.receive<ApprovalData>()
                call.respond(communitySystem.approveGroup(data))
            }
            patch("/community/approve/thread") {
                val data = call.receive<ApprovalData>()
                call.respond(communitySystem.approveTopic(data))
            }

            //USER
            get("/user") {
                val data = call.receive<Login>()
                val user = userSystem.getMyUser(data)

                if (user.getEmail() == "")
                    call.respond("Nothing found.")
                else
                    call.respond(user)
            }
            post("/user") {
                val user = call.receive<UserCreationData>()
                when (userSystem.create(user)) {
                    true -> {
                        call.respond("Created successfully.")
                    }
                    false -> {
                        call.respond("Something went wrong.")
                    }
                }
            }
            delete("/user") {
                val data = call.receive<Login>()
                call.respond(userSystem.delete(data))
            }

            post("/user/clear/notifications") {
                val data = call.receive<Login>()
                userSystem.clearNotifications(data)
                call.respond("Done.")
            }

            patch("/update/profile") {
                val user = call.receive<ProfileData>()
                call.respond(userSystem.updateProfile(user))
            }
            patch("/follow") {
                val follow = call.receive<SimpleOperator>()
                call.respond(userSystem.follow(follow))
            }
            patch("/unfollow") {
                val unfollow = call.receive<SimpleOperator>()
                call.respond(userSystem.unfollow(unfollow))
            }
            //CHAT
            get("/see/chat") {
                val data = call.receive<ChatFinderOperator>()
                val chat = chatSystem.seeChat(data)
                if (chat == null || chat.getID().isBlank()) {
                    call.respond("Nothing found.")
                } else
                    call.respond(chat)
            }
            post("/message") {
                val chat = call.receive<MessageData>()
                call.respond(chatSystem.sendMessage(chat))
            }
            delete("/message") {
                val chat = call.receive<ChatSimpleOperator>()
                call.respond(chatSystem.deleteMessage(chat))
            }

            post("/message/quote") {
                val chat = call.receive<ChatComplexOperator>()
                call.respond(chatSystem.quoteMessage(chat))
            }

            patch("/message/favorite") {
                val chat = call.receive<ChatSimpleOperator>()
                call.respond(chatSystem.favoriteMessage(chat))
            }
            patch("/message/unFavorite") {
                val chat = call.receive<ChatSimpleOperator>()
                call.respond(chatSystem.disfavorMessagr(chat))
            }
            patch("/message/share") {
                val chat = call.receive<ChatComplexOperator>()
                call.respond(chatSystem.shareMessage(chat))
            }

            //TOPICS
            get("/user/see/topics") {
                val data = call.receive<Login>()
                val threads = userSystem.getMyThreads(data)
                if (threads.isEmpty())
                    call.respond("Nothing Found.")
                else
                    call.respond(threads)
            }
            get("/user/see/timeline") {
                val data = call.receive<Login>()
                val threads = userSystem.getMyTimeline(data)
                if (threads.isEmpty())
                    call.respond("Nothing Found.")
                else
                    call.respond(threads)
            }
            post("/topic") {
                val new = call.receive<TopicData>()
                call.respond(threadSystem.create(new))
            }
            delete("/topic") {
                val thread = call.receive<TopicOperationsData>()
                call.respond(threadSystem.delete(thread))
            }

            patch("/thread/like") {
                val post = call.receive<TopicOperationsData>()
                call.respond(threadSystem.like(post))
            }

            patch("/thread/dislike") {
                val post = call.receive<TopicOperationsData>()
                call.respond(threadSystem.dislike(post))
            }

            //GROUPS
            get("/search/group") {
                val data = call.receive<SimpleOperator>()
                val group = searchSystem.seeGroup(data)

                if (group == null || group.getChatId().isBlank())
                    call.respond("Nothing found.")
                else
                    call.respond(group)

            }

            post("/group") {
                val group = call.receive<CreationData>()
                call.respond(groupSystem.create(group))
            }
            delete("/group") {
                val group = call.receive<SimpleOperator>()
                call.respond(groupSystem.delete(group))
            }

            patch("/group/member") {
                val member = call.receive<MemberOperator>()
                call.respond(groupSystem.addMember(member))
            }
            patch("/group/member/remove") {
                val member = call.receive<MemberOperator>()
                call.respond(groupSystem.removeMember(member))
            }
        }
    }.start(wait = true)
}


