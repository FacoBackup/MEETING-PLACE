package br.meetingplace.management

import br.meetingplace.interfaces.file.ReadFile
import br.meetingplace.interfaces.file.WriteFile
import br.meetingplace.interfaces.utility.Generator
import br.meetingplace.interfaces.utility.Path
import br.meetingplace.interfaces.utility.Refresh
import br.meetingplace.management.interfaces.groups.Group
import br.meetingplace.management.interfaces.Login
import br.meetingplace.management.interfaces.groups.GroupChat
import br.meetingplace.management.interfaces.users.Profile
import br.meetingplace.management.interfaces.users.UserChat
import br.meetingplace.management.interfaces.thread.MainThread
import br.meetingplace.management.interfaces.thread.SubThread

class Core private constructor(): ReadFile, WriteFile, Refresh, Generator,
    Path,Login, br.meetingplace.management.interfaces.users.User,
    Profile, UserChat, MainThread, SubThread, Group, GroupChat{

    companion object{
        private val core = Core()
        fun returnCore () = core
    }
}