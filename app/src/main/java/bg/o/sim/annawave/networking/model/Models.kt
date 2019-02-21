package bg.o.sim.annawave.networking.model

data class LoginParams(
    val userName: ByteArray,
    val password: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LoginParams

        if (!userName.contentEquals(other.userName)) return false
        if (!password.contentEquals(other.password)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userName.contentHashCode()
        result = 31 * result + password.contentHashCode()
        return result
    }
}