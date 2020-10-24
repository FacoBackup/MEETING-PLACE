package br.meetingplace.data.threads

import br.meetingplace.management.core.thread.dependencies.ThreadType

data class ThreadData(var title: String, var body: String, var idThread: String?, var type: ThreadType){}