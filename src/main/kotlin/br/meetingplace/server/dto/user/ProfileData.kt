package br.meetingplace.server.dto.user

import br.meetingplace.server.dto.Login

data class ProfileData(val gender: String?, val nationality: String?, val about: String?, val login: Login)