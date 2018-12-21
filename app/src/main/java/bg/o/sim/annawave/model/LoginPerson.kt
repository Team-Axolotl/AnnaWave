package bg.o.sim.annawave.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Logged-in user's data and related data-classes.
 *
 * Models are mostly defined by the JSONs of data sent from the backend,
 * because they need to match during deserialization.
 */

data class LoginPerson(
    val actorId: Long,
    val firstName: String?,
    val lastName: String?
) {
    lateinit var permissions: Array<Permission>
}

data class LoginData(
    val person: LoginPerson,
    val language: Language?,
    @JsonProperty("permission.get")
    val permissions: Array<Permission>
) {
    init {
        person.permissions = permissions
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginData

        if (person != other.person) return false
        if (language != other.language) return false
        if (!permissions.contentEquals(other.permissions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = person.hashCode()
        result = 31 * result + (language?.hashCode() ?: 0)
        result = 31 * result + permissions.contentHashCode()
        return result
    }
}

data class Language(
    val languageId: Int,
    val iso2Code: String,
    val name: String,
    val locale: String
)

data class Permission(
    val actionId: String,
    val objectId: String,
    val description: String
)