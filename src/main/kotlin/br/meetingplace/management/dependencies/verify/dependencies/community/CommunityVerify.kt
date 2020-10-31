package br.meetingplace.management.dependencies.verify.dependencies.community

import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.community.Community
import br.meetingplace.services.community.data.Report

class CommunityVerify private constructor(): CommunityVerifyInterface{
    companion object{
        private val Class= CommunityVerify()
        fun getClass ()= Class
    }

    override fun verifyReport(report: Report): Boolean {
        return report.reportId.isNotBlank()
    }

    override fun verifyCommunity(community: Community): Boolean{
        return !community.getId().isNullOrBlank()  && !community.getName().isNullOrBlank()
    }
}