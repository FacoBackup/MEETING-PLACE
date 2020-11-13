package br.meetingplace.server

import br.meetingplace.server.controllers.dependencies.readwrite.chat.ChatRW
import br.meetingplace.server.controllers.dependencies.readwrite.community.CommunityRW
import br.meetingplace.server.controllers.dependencies.readwrite.group.GroupRW
import br.meetingplace.server.controllers.dependencies.readwrite.topic.main.TopicRW
import br.meetingplace.server.controllers.dependencies.readwrite.topic.sub.SubTopicRW
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
                val search = UserSearch.getClass().searchUser(data, rwUser = UserRW.getClass())

                if (search.isEmpty())
                    call.respond("Nothing found.")
                else
                    call.respond(search)
            }

            get("/search/community") {
                val data = call.receive<SimpleOperator>()
                val search = CommunitySearch.getClass().searchCommunity(data, rwCommunity = CommunityRW.getClass())

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

            post("/user/clear/notifications") {
                val data = call.receive<Login>()
                call.respond(Profile.getClass().clearNotifications(data, rwUser = UserRW.getClass()))
            }

            patch("/update/profile") {
                val user = call.receive<ProfileData>()
                call.respond(Profile.getClass().updateProfile(user, rwUser = UserRW.getClass()))
            }
            patch("/follow") {
                val follow = call.receive<SimpleOperator>()
                call.respond(Follow.getClass().follow(follow, rwUser = UserRW.getClass(),rwCommunity = CommunityRW.getClass()))
            }
            patch("/unfollow") {
                val unfollow = call.receive<SimpleOperator>()
                call.respond(Follow.getClass().unfollow(unfollow, rwUser = UserRW.getClass(),rwCommunity = CommunityRW.getClass()))
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
                val chat = call.receive<MessageData>()
                call.respond(SendMessage.getClass().sendMessage(chat, rwChat = ChatRW.getClass(),rwCommunity = CommunityRW.getClass(),rwGroup = GroupRW.getClass(),rwUser = UserRW.getClass()))
            }
            delete("/message") {
                val chat = call.receive<ChatSimpleOperator>()
                call.respond(DeleteMessage.getClass().deleteMessage(chat, rwChat = ChatRW.getClass(),rwCommunity = CommunityRW.getClass(),rwGroup = GroupRW.getClass(),rwUser = UserRW.getClass()))
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
                val topics = UserReader.getClass().getMyThreads(data, rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass())
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
                call.respond(TopicFactory.getClass().create(new, rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass(),rwCommunity = CommunityRW.getClass(),rwSubTopic = SubTopicRW.getClass()))
            }
            delete("/topic") {
                val topic = call.receive<TopicOperationsData>()
                call.respond(Delete.getClass().delete(topic, rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass(),rwCommunity = CommunityRW.getClass(),rwSubTopic = SubTopicRW.getClass()))
            }

            patch("/topic/like") {
                val post = call.receive<TopicOperationsData>()
                call.respond(Like.getClass().like(post,rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass(),rwCommunity = CommunityRW.getClass(),rwSubTopic = SubTopicRW.getClass()))
            }

            patch("/topic/dislike") {
                val post = call.receive<TopicOperationsData>()
                call.respond(Dislike.getClass().dislike(post, rwUser = UserRW.getClass(),rwTopic = TopicRW.getClass(),rwCommunity = CommunityRW.getClass(),rwSubTopic = SubTopicRW.getClass()))
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


