package tech.fdiez.flowfullyuserlambda.data


import com.bol.secure.Encrypted
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.data.mongodb.core.index.Indexed

import java.time.Instant
import java.util.Date

@Document(collection = "users")
data class UserData(
    @Id var id: String = "",

    @Indexed val username: String,

    @Encrypted val password: String,

    @Encrypted var todoistApiToken: String,

    var todoistWebhookUrl: String,

    var todoistUserId: String,

    @CreatedDate val createdAt: Date = Date.from(Instant.now()),

    )