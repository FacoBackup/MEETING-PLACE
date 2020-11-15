package br.meetingplace.server

import br.meetingplace.server.controllers.readwrite.chat.ChatRW
import br.meetingplace.server.controllers.readwrite.community.CommunityRW
import br.meetingplace.server.controllers.readwrite.group.GroupRW
import br.meetingplace.server.controllers.readwrite.topic.TopicRW
import br.meetingplace.server.controllers.readwrite.user.UserRW
import br.meetingplace.server.controllers.subjects.entities.delete.UserDelete
import br.meetingplace.server.controllers.subjects.entities.factory.UserFactory
import br.meetingplace.server.controllers.subjects.entities.social.Social
import br.meetingplace.server.controllers.subjects.entities.profile.Profile
import br.meetingplace.server.controllers.subjects.entities.reader.UserReader
import br.meetingplace.server.controllers.subjects.services.chat.delete.DeleteMessage
import br.meetingplace.server.controllers.subjects.services.chat.send.SendMessage
import br.meetingplace.server.controllers.subjects.services.chat.disfavor.DisfavorMessage
import br.meetingplace.server.controllers.subjects.services.chat.favorite.FavoriteMessage
import br.meetingplace.server.controllers.subjects.services.chat.quote.QuoteMessage
import br.meetingplace.server.controllers.subjects.services.chat.share.ShareMessage
import br.meetingplace.server.controllers.subjects.services.chat.reader.ChatReader
import br.meetingplace.server.controllers.subjects.services.community.factory.CommunityFactory
import br.meetingplace.server.controllers.subjects.services.community.moderators.Moderator
import br.meetingplace.server.controllers.subjects.services.group.delete.GroupDelete
import br.meetingplace.server.controllers.subjects.services.group.factory.GroupFactory
import br.meetingplace.server.controllers.subjects.services.group.members.GroupMembers
import br.meetingplace.server.controllers.subjects.services.search.group.GroupSearch
import br.meetingplace.server.controllers.subjects.services.search.user.UserSearch
import br.meetingplace.server.controllers.subjects.services.topic.delete.DeleteTopic
import br.meetingplace.server.controllers.subjects.services.topic.dislike.DislikeTopic
import br.meetingplace.server.controllers.subjects.services.topic.factory.TopicFactory
import br.meetingplace.server.controllers.subjects.services.topic.like.LikeTopic
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
import br.meetingplace.server.dto.topics.TopicIdentifier
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
    val port = System.getenv("PORT")?.toInt() ?: 8080

    embeddedServer(Netty, port) {
        routing {
            install(ContentNegotiation) {
                gson {
                    setPrettyPrinting()
                }
            }
            //SEARCH
            get("/search/topic"){
                val data = call.receive<TopicIdentifier>()
                val search = if(!data.subTopicID.isNullOrBlank()) TopicRW.getClass().read(data.subTopicID,data.mainTopicID)
                else TopicRW.getClass().read(data.mainTopicID, null)

                if (search == null)
                    call.respond("Nothing found.")
                else
                    call.respond(search)
            }
            get("/search/user") {
                val data = call.receive<SimpleOperator>()
                val search = UserSearch.getClass().searchUser(data, rwUser = UserRW.getClass())

                if (search.isEmpty())
                    call.respond("Nothing found.")
                else
                    call.respond(search)
            }

            get("/search/community") {
                val data = call.receive<SimpleOperator>()
                val search = CommunityRW.getClass().read(data.identifier.ID)

                if (search == null)
                    call.respond("Nothing found.")
                else
                    call.respond(search)
            }

            //COMMUNITY
            post("/community") {
                val data = call.receive<CreationData>()
                call.respond(CommunityFactory.getClass().create(data, rwUser = UserRW.getClass(), rwCommunity = CommunityRW.getClass()))
            }
            patch("/community/approve/group") {
                val data = call.receive<ApprovalData>()
                call.respond(Moderator.getClass().approveGroup(data, rwCommunity = CommunityRW.getClass(), rwUser = UserRW.getClass(), rwGroup = GroupRW.getClass()))
            }
            patch("/community/approve/topic") {
                val data = call.receive<ApprovalData>()
                call.respond(Moderator.getClass().approveTopic(data, rwCommunity = CommunityRW.getClass(), rwUser = UserRW.getClass(), rwTopic = TopicRW.getClass()))
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
                when (UserFactory.getClass().create(user, rwUser = UserRW.getClass())) {
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
                call.respond(UserDelete.getClass().delete(data, rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass()))
            }

            patch("/clear") {
                val data = call.receive<Login>()
                call.respond(Profile.getClass().clearNotifications(data, rwUser = UserRW.getClass()))
            }

            patch("/update/profile") {
                val user = call.receive<ProfileData>()
                call.respond(Profile.getClass().updateProfile(user, rwUser = UserRW.getClass()))
            }
            patch("/follow") {
                val follow = call.receive<SimpleOperator>()
                call.respond(Social.getClass().follow(follow, rwUser = UserRW.getClass(),rwCommunity = CommunityRW.getClass()))
            }
            patch("/unfollow") {
                val unfollow = call.receive<SimpleOperator>()
                call.respond(Social.getClass().unfollow(unfollow, rwUser = UserRW.getClass(),rwCommunity = CommunityRW.getClass()))
            }
            //CHAT
            get("/see/chat") {
                val data = call.receive<ChatFinderOperator>()
                val chat = ChatReader.getClass().seeChat(data, rwCommunity = CommunityRW.getClass(),rwGroup = GroupRW.getClass(),rwUser = UserRW.getClass(),rwChat = ChatRW.getClass())
                if (chat == null || chat.getID().isBlank()) {
                    call.respond("Nothing found.")
                } else
                    call.respond(chat)
            }
            post("/message") {
                val data = call.receive<MessageData>()
                call.respond(SendMessage.getClass().sendMessage(data, rwChat = ChatRW.getClass(),rwCommunity = CommunityRW.getClass(),rwGroup = GroupRW.getClass(),rwUser = UserRW.getClass()))
            }
            delete("/message") {
                val data = call.receive<ChatSimpleOperator>()
                call.respond(DeleteMessage.getClass().deleteMessage(data, rwChat = ChatRW.getClass(),rwCommunity = CommunityRW.getClass(),rwGroup = GroupRW.getClass(),rwUser = UserRW.getClass()))
            }

            post("/message/quote") {
                val data = call.receive<ChatComplexOperator>()
                call.respond(QuoteMessage.getClass().quoteMessage(data, rwUser = UserRW.getClass(),rwGroup = GroupRW.getClass(),rwCommunity = CommunityRW.getClass(),rwChat = ChatRW.getClass()))
            }

            patch("/message/favorite") {
                val data = call.receive<ChatSimpleOperator>()
                call.respond(FavoriteMessage.getClass().favoriteMessage(data, rwUser = UserRW.getClass(),rwGroup = GroupRW.getClass(),rwCommunity = CommunityRW.getClass(),rwChat = ChatRW.getClass()))
            }
            patch("/message/disfavor") {
                val data = call.receive<ChatSimpleOperator>()
                call.respond(DisfavorMessage.getClass().disfavorMessage(data, rwUser = UserRW.getClass(),rwGroup = GroupRW.getClass(),rwCommunity = CommunityRW.getClass(),rwChat = ChatRW.getClass()))
            }
            patch("/message/share") {
                val data = call.receive<ChatComplexOperator>()
                call.respond(ShareMessage.getClass().shareMessage(data, rwUser = UserRW.getClass(),rwGroup = GroupRW.getClass(),rwCommunity = CommunityRW.getClass(),rwChat = ChatRW.getClass()))
            }

            //TOPICS
            get("/user/topics") {
                val data = call.receive<Login>()
                val topics = UserReader.getClass().getMyTopics(data, rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass())
                if (topics.isEmpty())
                    call.respond("Nothing Found.")
                else
                    call.respond(topics)
            }
            get("/user/timeline") {
                val data = call.receive<Login>()
                val topics = UserReader.getClass().getMyTimeline(data,rwTopic = TopicRW.getClass(),rwUser = UserRW.getClass())
                if (topics.isEmpty())
                    call.respond("Nothing Found.")
                else
                    call.respond(topics)
            }
            post("/topic") {
                val new = call.receive<TopicData>()
                call.respond(TopicFactory.getClass().create(new, rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass(),rwCommunity = CommunityRW.getClass()))
            }
            delete("/topic") {
                val topic = call.receive<TopicOperationsData>()
                call.respond(DeleteTopic.getClass().delete(topic, rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass(),rwCommunity = CommunityRW.getClass()))
            }

            patch("/topic/like") {
                val post = call.receive<TopicOperationsData>()
                call.respond(LikeTopic.getClass().like(post,rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass(),rwCommunity = CommunityRW.getClass()))
            }

            patch("/topic/dislike") {
                val post = call.receive<TopicOperationsData>()
                call.respond(DislikeTopic.getClass().dislike(post, rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass(),rwCommunity = CommunityRW.getClass()))
            }

            //GROUPS
            get("/search/group") {
                val data = call.receive<SimpleOperator>()
                val group = GroupSearch.getClass().seeGroup(data, rwUser = UserRW.getClass(), rwGroup = GroupRW.getClass())

                if (group == null || group.getChatID().isBlank())
                    call.respond("Nothing found.")
                else
                    call.respond(group)

            }

            post("/group") {
                val group = call.receive<CreationData>()
                call.respond(GroupFactory.getClass().create(group,rwUser = UserRW.getClass(), rwGroup = GroupRW.getClass(), rwCommunity = CommunityRW.getClass(),rwChat = ChatRW.getClass()))
            }
            delete("/group") {
                val group = call.receive<SimpleOperator>()
                call.respond(GroupDelete.getClass().delete(group,rwUser = UserRW.getClass(), rwGroup = GroupRW.getClass(), rwCommunity = CommunityRW.getClass(),rwChat = ChatRW.getClass()))
            }

            patch("/group/member") {
                val member = call.receive<MemberOperator>()
                call.respond(GroupMembers.getClass().addMember(member, rwUser = UserRW.getClass(), rwGroup = GroupRW.getClass(), rwCommunity = CommunityRW.getClass()))
            }
            patch("/group/member/remove") {
                val member = call.receive<MemberOperator>()
                call.respond(GroupMembers.getClass().removeMember(member, rwUser = UserRW.getClass(), rwGroup = GroupRW.getClass(), rwCommunity = CommunityRW.getClass()))
            }
        }
    }.start(wait = true)
}


