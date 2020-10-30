package br.meetingplace.management.services.thread.core

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.management.services.thread.dependencies.LikeInterface
import br.meetingplace.management.services.thread.dependencies.mainThread.MainThreadFactory
import br.meetingplace.management.services.thread.dependencies.subThread.SubThreadFactory
import br.meetingplace.management.services.thread.dependencies.mainThread.LikeMainThread
import br.meetingplace.management.services.thread.dependencies.subThread.LikeSubThread
import br.meetingplace.services.community.Community

class ThreadCore private constructor():LikeInterface,Verify, ReadWriteUser, ReadWriteThread, ReadWriteCommunity{

    companion object{
        private val Class = ThreadCore()
        fun getClass() = Class
    }

    private val main = MainThreadFactory.getThreadFactory()
    private val sub = SubThreadFactory.getThreadFactory()

    fun create(data: ThreadData) {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        lateinit var community: Community

        if(verifyLoggedUser(user)){
            when(verifyType(null,data)){
                ThreadType.MAIN->{
                    if(data.idCommunity.isNullOrBlank())
                        main.create(data)
                    else{
                        community = readCommunity(data.idCommunity)
                        if(verifyCommunity(community) ){
                            val idThread = main.create(data)
                            //the verification for data.idCommunity != null already occurred, so don't mind the !!
                            if(loggedUser !in community.getModerators())
                                community.updateThreadsInValidation(idThread!!, null)
                            else
                                community.updateThreadsInValidation(idThread!!, true)
                            writeCommunity(community, community.getId())
                        }
                    }
                }

                ThreadType.SUB->{
                    if(data.idCommunity.isNullOrBlank())
                        sub.create(data)
                    else{
                        community = readCommunity(data.idCommunity)
                        if(verifyCommunity(community) && community.checkThreadApproval(data.idCommunity))
                            sub.create(data)
                    }
                }
            }
        }
    }

    fun delete(data: ThreadOperationsData){
        lateinit var community: Community

        when(verifyType(data,null)){
            ThreadType.MAIN->{ //MAIN
                if(data.idCommunity.isNullOrBlank())
                    main.delete(data)
                else{
                    community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.checkThreadApproval(data.idCommunity)){
                        community.removeApprovedThread(data.idThread)
                        main.delete(data)
                        writeCommunity(community, community.getId())
                    }
                    else if(verifyCommunity(community) && !community.checkThreadApproval(data.idCommunity)){
                        community.updateThreadsInValidation(data.idThread, false)
                        main.delete(data)
                        writeCommunity(community, community.getId())
                    }
                }
            }
            ThreadType.SUB->{ //SUB
                if(data.idCommunity.isNullOrBlank())
                    sub.delete(data)
                else{
                    community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.checkThreadApproval(data.idCommunity))
                        sub.delete(data)
                    writeCommunity(community, community.getId())
                }
            }
        }
    }

    override fun like(data: ThreadOperationsData){
        lateinit var community: Community

        when(verifyType(data,null)){
            ThreadType.MAIN->{ //MAIN
                if(data.idCommunity.isNullOrBlank())
                    LikeMainThread.getLikeOperator().like(data)
                else{
                    community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.checkThreadApproval(data.idCommunity))
                        LikeMainThread.getLikeOperator().like(data)
                }
            }
            ThreadType.SUB->{ //SUB
                if(data.idCommunity.isNullOrBlank())
                    LikeSubThread.getLikeOperator().like(data)
                else{
                    community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.checkThreadApproval(data.idCommunity))
                        LikeSubThread.getLikeOperator().like(data)
                }
            }
        }
    }

    override fun dislike(data: ThreadOperationsData){
        lateinit var community: Community

        when(verifyType(data,null)){
            ThreadType.MAIN->{ //MAIN
                if(data.idCommunity.isNullOrBlank())
                    LikeMainThread.getLikeOperator().dislike(data)
                else{
                    community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.checkThreadApproval(data.idCommunity))
                        LikeMainThread.getLikeOperator().dislike(data)
                }
            }
            ThreadType.SUB->{ //SUB
                if(data.idCommunity.isNullOrBlank())
                    LikeSubThread.getLikeOperator().dislike(data)
                else{
                    community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.checkThreadApproval(data.idCommunity))
                        LikeSubThread.getLikeOperator().dislike(data)
                }
            }
        }
    }



    private fun verifyType(op: ThreadOperationsData?, data: ThreadData?): ThreadType?{
        return if(op != null && data == null){
            if(op.idSubThread.isNullOrBlank())
                ThreadType.MAIN
            else
                ThreadType.SUB
        }
        else if(data != null && op == null){
            if(data.idThread != null)
                ThreadType.SUB
            else
                ThreadType.MAIN
        }
        else null
    }
}