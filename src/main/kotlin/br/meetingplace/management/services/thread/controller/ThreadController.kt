package br.meetingplace.management.services.thread.controller

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.management.services.thread.dependencies.LikeInterface
import br.meetingplace.management.services.thread.dependencies.mainThread.MainThreadFactory
import br.meetingplace.management.services.thread.dependencies.subThread.SubThreadFactory
import br.meetingplace.management.services.thread.dependencies.mainThread.LikeMainThread
import br.meetingplace.management.services.thread.dependencies.subThread.LikeSubThread
import br.meetingplace.services.community.Community

class ThreadController private constructor():LikeInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = ThreadController()
        fun getClass() = Class
    }

    private val main = MainThreadFactory.getThreadFactory()
    private val sub = SubThreadFactory.getThreadFactory()

    fun create(data: ThreadData) {
        val loggedUser = rw.readLoggedUser().email
        val user = rw.readUser(loggedUser)
        lateinit var community: Community

        if(verify.verifyUser(user)){
            when(verifyType(null,data)){
                ThreadType.MAIN->{
                    if(data.idCommunity.isNullOrBlank())
                        main.create(data)
                    else{
                        community = rw.readCommunity(data.idCommunity)
                        if(verify.verifyCommunity(community) ){
                            val idThread = main.create(data)
                            //the verification for data.idCommunity != null already occurred, so don't mind the !!
                            //the verify community method already insures that the id and name are different of null so don't mind the !!
                            if(loggedUser !in community.getModerators())
                                community.updateThreadsInValidation(idThread!!, null)
                            else
                                community.updateThreadsInValidation(idThread!!, true)
                            rw.writeCommunity(community, community.getId()!!)
                        }
                    }
                }

                ThreadType.SUB->{
                    if(data.idCommunity.isNullOrBlank())
                        sub.create(data)
                    else{
                        community = rw.readCommunity(data.idCommunity)
                        if(verify.verifyCommunity(community) && community.checkThreadApproval(data.idCommunity))
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
                    community = rw.readCommunity(data.idCommunity)
                    if(verify.verifyCommunity(community) && community.checkThreadApproval(data.idCommunity)){
                        community.removeApprovedThread(data.idThread)
                        main.delete(data)
                        //the verify community method already insures that the id and name are different of null so don't mind the !!
                        rw.writeCommunity(community, community.getId()!!)
                    }
                    else if(verify.verifyCommunity(community) && !community.checkThreadApproval(data.idCommunity)){
                        community.updateThreadsInValidation(data.idThread, false)
                        main.delete(data)
                        //the verify community method already insures that the id and name are different of null so don't mind the !!
                        rw.writeCommunity(community, community.getId()!!)
                    }
                }
            }
            ThreadType.SUB->{ //SUB
                if(data.idCommunity.isNullOrBlank())
                    sub.delete(data)
                else{
                    community = rw.readCommunity(data.idCommunity)
                    if(verify.verifyCommunity(community) && community.checkThreadApproval(data.idCommunity))
                        sub.delete(data)
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
                    community = rw.readCommunity(data.idCommunity)
                    if(verify.verifyCommunity(community) && community.checkThreadApproval(data.idCommunity))
                        LikeMainThread.getLikeOperator().like(data)
                }
            }
            ThreadType.SUB->{ //SUB
                if(data.idCommunity.isNullOrBlank())
                    LikeSubThread.getLikeOperator().like(data)
                else{
                    community = rw.readCommunity(data.idCommunity)
                    if(verify.verifyCommunity(community) && community.checkThreadApproval(data.idCommunity))
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
                    community = rw.readCommunity(data.idCommunity)
                    if(verify.verifyCommunity(community) && community.checkThreadApproval(data.idCommunity))
                        LikeMainThread.getLikeOperator().dislike(data)
                }
            }
            ThreadType.SUB->{ //SUB
                if(data.idCommunity.isNullOrBlank())
                    LikeSubThread.getLikeOperator().dislike(data)
                else{
                    community = rw.readCommunity(data.idCommunity)
                    if(verify.verifyCommunity(community) && community.checkThreadApproval(data.idCommunity))
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