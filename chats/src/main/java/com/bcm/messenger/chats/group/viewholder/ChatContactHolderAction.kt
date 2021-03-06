package com.bcm.messenger.chats.group.viewholder

import com.bcm.messenger.chats.components.ContactCardView
import com.bcm.messenger.chats.group.logic.GroupMessageLogic
import com.bcm.messenger.common.core.AmeGroupMessage
import com.bcm.messenger.common.grouprepository.model.AmeGroupMessageDetail
import com.bcm.messenger.common.mms.GlideRequests

/**
 * Created by bcm.social.01 on 2018/10/23.
 */
class ChatContactHolderAction() : BaseChatHolderAction<ContactCardView>() {

    override fun bindData(message: AmeGroupMessageDetail, body: ContactCardView, glideRequests: GlideRequests, batchSelected: Set<AmeGroupMessageDetail>?) {
        body.setContact(message.gid, message.message.content as AmeGroupMessage.ContactContent, message.isSendByMe)
    }

    override fun resend(messageRecord: AmeGroupMessageDetail) {
        GroupMessageLogic.messageSender.resendContactMessage(messageRecord)
    }
}