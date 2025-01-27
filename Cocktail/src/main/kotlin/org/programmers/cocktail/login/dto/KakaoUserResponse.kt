package org.programmers.cocktail.login.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KakaoUserResponse(
    var id: Long? = null,
    @JsonProperty("kakao_account")
    var kakaoAccount: KakaoAccount? = null
) {
    data class KakaoAccount(
        var profile: Profile? = null,
        var email: String? = null
    ) {
        data class Profile(
            var nickname: String? = null,
            @JsonProperty("profile_image_url")
            var image: String? = null
        )
    }
}