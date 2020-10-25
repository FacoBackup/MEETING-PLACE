package br.meetingplace.management.core.thread.dependencies

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData

interface ThreadInterface {
    fun create(data: ThreadData): String?
    fun like(data: ThreadOperationsData)
    fun dislike(data: ThreadOperationsData)
    fun delete(data: ThreadOperationsData)
}