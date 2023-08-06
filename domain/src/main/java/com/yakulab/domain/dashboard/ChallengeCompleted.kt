package com.yakulab.domain.dashboard



data class ChallengeCompleted (
    var id: String? = null,
    var name: String? = null,
    var tipo: String? = null,
    var url: String? = null,
    var author_email:String?= null,
    var author_name:String?= null,
    var author_lastname:String?= null,
    var timestamp: Long? = null,
    var challenge_id: String? = null,
    var post_blocked: List<String>? = null
)

enum class TypeGroup(val value:String) {
    ANIMALS("ANIMALS"),
    PLANTS("PLANTS"),
    FRUITS("FRUITS")
}
