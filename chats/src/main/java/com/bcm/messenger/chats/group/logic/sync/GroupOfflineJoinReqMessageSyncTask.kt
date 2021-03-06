package com.bcm.messenger.chats.group.logic.sync

import com.bcm.messenger.chats.group.logic.GroupLogic
import com.bcm.messenger.common.provider.AMESelfData
import com.bcm.messenger.utility.logger.ALog

class GroupOfflineJoinReqMessageSyncTask(val gid:Long, private var delay:Long = 300, private val needConfirm:Boolean) {
    fun execute(result: (succeed: Boolean) -> Unit) {
        val pageSize = 100L

        syncPage("", pageSize) {
            finished, succeed ->
            ALog.i("GroupOfflineJoinReqMessageSyncTask"," execute join req $gid succeed:$succeed finished:$finished")
            result(succeed)
        }
    }

    private fun syncPage(start:String, count:Long, result: (finished:Boolean, succeed: Boolean) -> Unit) {
        val queryUid = AMESelfData.uid
        GroupLogic.fetchJoinRequestList(gid, delay, start, count) { succeed, list ->
            ALog.i("GroupOfflineJoinReqMessageSyncTask","syncPage join req $gid result:$succeed size:${list.size}")

            if (queryUid != AMESelfData.uid) {
                return@fetchJoinRequestList
            }

            if (succeed && !needConfirm) {
                if (succeed && list.isNotEmpty()) {
                    GroupLogic.autoReviewJoinRequest(gid, list) { ok, error ->
                        ALog.i("GroupOfflineJoinReqMessageSyncTask", "syncPage autoReviewJoinRequest by offline message succeed:$ok, error:$error")
                    }
                }
            }

            if (!succeed) {
                ALog.i("GroupOfflineJoinReqMessageSyncTask"," syncPage join req $gid failed")
                result(false, false)
            } else {
                if (list.size.toLong() != count) {
                    result(true, true)
                } else {
                    syncPage(list.last().uid, count, result)
                }
            }
        }
    }
}