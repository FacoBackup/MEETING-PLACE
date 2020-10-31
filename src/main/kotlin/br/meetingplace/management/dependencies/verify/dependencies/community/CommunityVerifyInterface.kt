package br.meetingplace.management.dependencies.verify.dependencies.community

import br.meetingplace.services.community.Community
import br.meetingplace.services.community.data.Report

interface CommunityVerifyInterface {
    fun verifyReport(report: Report): Boolean
    fun verifyCommunity(community: Community): Boolean
}