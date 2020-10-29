package br.meetingplace.management.services.thread.dependencies

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData

interface ThreadFactoryInterface {
    fun create(data: ThreadData): String?
    fun delete(data: ThreadOperationsData)
}