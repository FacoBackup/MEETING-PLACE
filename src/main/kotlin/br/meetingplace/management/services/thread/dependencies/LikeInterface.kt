package br.meetingplace.management.services.thread.dependencies

import br.meetingplace.data.threads.ThreadOperationsData

interface LikeInterface {
    fun like(data: ThreadOperationsData)
    fun dislike(data: ThreadOperationsData)
}