package bg.o.sim.annawave.model

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Logged-in user's data and related data-classes.
 *
 * Models are mostly defined by the JSONs of data sent from the backend,
 * because they need to match during deserialization.
 */

abstract class BaseEntity{
    var id : String? = null
}

data class LoginPerson(
    val loanApplicationCount: Int,
    val loanCount: Int,
    val accountCount: Int,
    val tasks: Array<Task>
) : BaseEntity()

data class Task(
    val status: String,
    val type: TaskType,
    val client: Client
) : BaseEntity()

data class Client(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
) : BaseEntity()

enum class TaskType {
    LoanApplication, Loan, Account
}

//data class LoginPerson(
//    val actorId: Long,
//    val firstName: String?,
//    val lastName: String?
//) {
//    lateinit var permissions: Array<Permission>
//}
//
//data class LoginData(
//    val person: LoginPerson,
//    val language: Language?,
//    @JsonProperty("permission.get")
//    val permissions: Array<Permission>
//)
//
//data class Language(
//    val languageId: Int,
//    val iso2Code: String,
//    val name: String,
//    val locale: String
//)
//
//data class Permission(
//    val actionId: String,
//    val objectId: String,
//    val description: String
//)