package br.meetingplace.management.core.thread.dependencies

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData

interface ThreadInterface {
    fun like(data: ThreadOperationsData, type: ThreadType)
    fun dislike(data: ThreadOperationsData, type: ThreadType)
    fun create(data: ThreadData, type: ThreadType)
    fun delete(data: ThreadOperationsData, type: ThreadType)
}