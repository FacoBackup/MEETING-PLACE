package br.meetingplace.management.services.community.dependencies.factory

import br.meetingplace.data.Data
import br.meetingplace.data.community.CommunityData

interface CommunityFactoryInterface {
    fun create(data: CommunityData)
    fun delete(data: Data)
}