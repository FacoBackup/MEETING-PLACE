package br.meetingplace.server

import br.meetingplace.server.controllers.dependencies.readwrite.user.UserRW
import br.meetingplace.server.controllers.subjects.entities.delete.UserDelete
import br.meetingplace.server.controllers.subjects.entities.factory.UserFactory
import br.meetingplace.server.controllers.subjects.entities.follow.Follow
import br.meetingplace.server.controllers.subjects.entities.profile.Profile
import br.meetingplace.server.controllers.subjects.entities.reader.UserReader
import br.meetingplace.server.controllers.subjects.services.chat.base.delete.DeleteMessage
import br.meetingplace.server.controllers.subjects.services.chat.base.send.SendMessage
import br.meetingplace.server.controllers.subjects.services.chat.extensions.disfavor.DisfavorMessage
import br.meetingplace.server.controllers.subjects.services.chat.extensions.favorite.FavoriteMessage
import br.meetingplace.server.controllers.subjects.services.chat.extensions.quote.QuoteMessage
import br.meetingplace.server.controllers.subjects.services.chat.extensions.share.ShareMessage
import br.meetingplace.server.controllers.subjects.services.chat.reader.ChatReader
import br.meetingplace.server.controllers.subjects.services.community.factory.CommunityFactory
import br.meetingplace.server.controllers.subjects.services.community.moderators.Moderator
import br.meetingplace.server.controllers.subjects.services.group.delete.GroupDelete
import br.meetingplace.server.controllers.subjects.services.group.factory.GroupFactory
import br.meetingplace.server.controllers.subjects.services.group.members.GroupMembers
import br.meetingplace.server.controllers.subjects.services.search.community.CommunitySearch
import br.meetingplace.server.controllers.subjects.services.search.group.GroupSearch
import br.meetingplace.server.controllers.subjects.services.search.user.UserSearch
import br.meetingplace.server.controllers.subjects.services.topic.delete.Delete
import br.meetingplace.server.controllers.subjects.services.topic.dislike.Dislike
import br.meetingplace.server.controllers.subjects.services.topic.factory.TopicFactory
import br.meetingplace.server.controllers.subjects.services.topic.like.Like
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
                val search = UserSearch.getClass().searchUser(data)

                if (search.isEmpty())
                    call.respond("Nothing found.")
                else
                    call.respond(search)
            }

            get("/search/community") {
                val data = call.receive<SimpleOperator>()
                val search = CommunitySearch.getClass().searchCommunity(data)

                if (search == null)
                    call.respond("Nothing found.")
                else
                    call.respond(search)
            }

            //COMMUNITY
            post("/community") {
                val data = call.receive<CreationData>()
                call.respond(CommunityFactory.getClass().create(data))
            }
            patch("/community/approve/group") {
                val data = call.receive<ApprovalData>()
                call.respond(Moderator.getClass().approveGroup(data))
            }
            patch("/community/approve/topic") {
                val data = call.receive<ApprovalData>()
                call.respond(Moderator.getClass().approveTopic(data))
            }

            //USER
            get("/user") {
                val data = call.receive<Login>()
                val user = UserRW.getClass().read(data.email)

                if (user == null)
                    call.respond("Nothing found.")
                else
                    call.respond(user)
            }
            post("/user") {
                val user = call.receive<UserCreationData>()
                when (UserFactory.getClass().create(user)) {
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
                call.respond(UserDelete.getClass().delete(data))
            }

            post("/user/clear/notifications") {
                val data = call.receive<Login>()
                call.respond(Profile.getClass().clearNotifications(data))
            }

            patch("/update/profile") {
                val user = call.receive<ProfileData>()
                call.respond(Profile.getClass().updateProfile(user))
            }
            patch("/follow") {
                val follow = call.receive<SimpleOperator>()
                call.respond(Follow.getClass().follow(follow))
            }
            patch("/unfollow") {
                val unfollow = call.receive<SimpleOperator>()
                call.respond(Follow.getClass().unfollow(unfollow))
            }
            //CHAT
            get("/see/chat") {
                val data = call.receive<ChatFinderOperator>()
                val chat = ChatReader.getClass().seeChat(data)
                if (chat == null || chat.getID().isBlank()) {
                    call.respond("Nothing found.")
                } else
                    call.respond(chat)
            }
            post("/message") {
                val chat = call.receive<MessageData>()
                call.respond(SendMessage.getClass().sendMessage(chat))
            }
            delete("/message") {
                val chat = call.receive<ChatSimpleOperator>()
                call.respond(DeleteMessage.getClass().deleteMessage(chat))
            }

            post("/message/quote") {
                val chat = call.receive<ChatComplexOperator>()
                call.respond(QuoteMessage.getClass().quoteMessage(chat))
            }

            patch("/message/favorite") {
                val chat = call.receive<ChatSimpleOperator>()
                call.respond(FavoriteMessage.getClass().favoriteMessage(chat))
            }
            patch("/message/unFavorite") {
                val chat = call.receive<ChatSimpleOperator>()
                call.respond(DisfavorMessage.getClass().disfavorMessage(chat))
            }
            patch("/message/share") {
                val chat = call.receive<ChatComplexOperator>()
                call.respond(ShareMessage.getClass().shareMessage(chat))
            }

            //TOPICS
            get("/user/topics") {
                val data = call.receive<Login>()
                val topics = UserReader.getClass().getMyThreads(data)
                if (topics.isEmpty())
                    call.respond("Nothing Found.")
                else
                    call.respond(topics)
            }
            get("/user/timeline") {
                val data = call.receive<Login>()
                val topics = UserReader.getClass().getMyTimeline(data)
                if (topics.isEmpty())
                    call.respond("Nothing Found.")
                else
                    call.respond(topics)
            }
            post("/topic") {
                val new = call.receive<TopicData>()
                call.respond(TopicFactory.getClass().create(new))
            }
            delete("/topic") {
                val thread = call.receive<TopicOperationsData>()
                call.respond(Delete.getClass().delete(thread))
            }

            patch("/topic/like") {
                val post = call.receive<TopicOperationsData>()
                call.respond(Like.getClass().like(post))
            }

            patch("/topic/dislike") {
                val post = call.receive<TopicOperationsData>()
                call.respond(Dislike.getClass().dislike(post))
            }

            //GROUPS
            get("/search/group") {
                val data = call.receive<SimpleOperator>()
                val group = GroupSearch.getClass().seeGroup(data)

                if (group == null || group.getChatId().isBlank())
                    call.respond("Nothing found.")
                else
                    call.respond(group)

            }

            post("/group") {
                val group = call.receive<CreationData>()
                call.respond(GroupFactory.getClass().create(group))
            }
            delete("/group") {
                val group = call.receive<SimpleOperator>()
                call.respond(GroupDelete.getClass().delete(group))
            }

            patch("/group/member") {
                val member = call.receive<MemberOperator>()
                call.respond(GroupMembers.getClass().addMember(member))
            }
            patch("/group/member/remove") {
                val member = call.receive<MemberOperator>()
                call.respond(GroupMembers.getClass().removeMember(member))
            }
        }
    }.start(wait = true)
}


