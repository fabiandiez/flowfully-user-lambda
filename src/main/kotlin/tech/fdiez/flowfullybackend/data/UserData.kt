package tech.fdiez.flowfullybackend.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Encrypted
import java.time.Instant

@Document(collection = "users")
data class UserData(
    @Id
    val id: String,

    @Indexed
    val username: String,

    @Encrypted
    val password: String,

    @Encrypted
    var todoistApiToken: String,

    var todoistWebhookUrl: String,

    var todoistUserId: String,

    val createdAt: Instant = Instant.now(),

    )