package br.meetingplace.management

import br.meetingplace.management.interfaces.file.ReadFile
import br.meetingplace.management.interfaces.file.WriteFile
import br.meetingplace.management.interfaces.utility.Generator
import br.meetingplace.management.interfaces.utility.Path
import br.meetingplace.management.interfaces.utility.Refresh
import br.meetingplace.management.interfaces.group.Group
import br.meetingplace.management.interfaces.Login
import br.meetingplace.management.interfaces.group.GroupChat
import br.meetingplace.management.interfaces.user.Profile
import br.meetingplace.management.interfaces.user.UserChat
import br.meetingplace.management.interfaces.thread.MainThread
import br.meetingplace.management.interfaces.thread.SubThread

class Core private constructor(): ReadFile, WriteFile, Refresh, Generator,
    Path,Login, br.meetingplace.management.interfaces.user.User,
    Profile, UserChat, MainThread, SubThread, Group, GroupChat{

    companion object{
        private val core = Core()
        fun returnCore () = core
    }
}