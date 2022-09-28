package tech.fdiez.flowfullyuserlambda.event.outgoing

import tech.fdiez.flowfullyuserlambda.data.UserData

data class UserDataEvent(

    val username: String,

    var todoistApiToken: String,

    var todoistWebhookUrl: String,

    var todoistUserId: String,

    ) {
    companion object {
        fun from(userData: UserData): UserDataEvent {
            return UserDataEvent(
                username = userData.username,
                todoistApiToken = userData.todoistApiToken,
                todoistWebhookUrl = userData.todoistWebhookUrl,
                todoistUserId = userData.todoistUserId
            )
        }
    }
}