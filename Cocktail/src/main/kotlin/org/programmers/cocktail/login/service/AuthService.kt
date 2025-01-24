package org.programmers.cocktail.login.service

import org.programmers.cocktail.entity.Users
import org.programmers.cocktail.login.dto.KakaoUserResponse
import org.programmers.cocktail.login.dto.LoginResponse
import org.programmers.cocktail.repository.users.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.client.postForEntity

@Service
class AuthService

    (private val restTemplate: RestTemplate) {

    @Autowired
    var usersRepository: UsersRepository? = null

    @Value("\${spring.security.oauth2.client.provider.kakao.token-uri}")
    private val tokenUri: String? = null

    @Value("\${spring.security.oauth2.client.registration.kakao.client-id}")
    private val clientId: String? = null

    @Value("\${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private val redirectUri: String? = null

    @Value("\${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private val userInfoUri: String? = null

    @Value("\${spring.security.oauth2.client.registration.kakao.client-secret}")
    private val clientSecret: String? = null

    fun register(code: String): LoginResponse {
        // 1. 인가 코드를 사용해 Access Token 요청
        val accessToken = getAccessToken(code)
        println("accessToken: $accessToken")

        // 2. Access Token을 사용해 사용자 정보 요청
        val kakaoUser = getKakaoUser(accessToken!!)

        val users = Users(
            kakaoUser?.kakaoAccount?.email!!,
            kakaoUser.kakaoAccount?.profile?.nickname!!, "1234", "not-defined", 0
        )

        if (usersRepository?.findsByEmail(kakaoUser.kakaoAccount?.email) == null) {
                usersRepository?.save(users)
            }

        // 3. 사용자 정보로 LoginResponse 생성
        return LoginResponse(
                accessToken,
                "",  // Refresh Token은 필요시 추가
                kakaoUser.kakaoAccount?.profile?.image?: "기본 이미지 URL",
                kakaoUser.kakaoAccount?.profile?.nickname?: "닉네임 없음",
                kakaoUser.kakaoAccount?.email?: "이메일 없음"
            )
    }

    private fun getAccessToken(code: String): String? {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }

        val body: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>().apply {
            add("grant_type", "authorization_code")
            add("client_id", clientId)
            add("redirect_uri", redirectUri)
            add("code", code)
            add("client_secret", clientSecret)
        }

        val request = HttpEntity(body, headers)

        // `Map<String, Any>`로 응답 타입 지정
        val response: ResponseEntity<Map<String, Any>>? = tokenUri?.let {
            restTemplate.postForEntity(
                it, request, object : ParameterizedTypeReference<Map<String, Any>>() {}
            )
        }

        val responseBody = response?.body
        println("Response Body: $responseBody")

        // 응답 본문에서 access_token 추출
        return responseBody?.get("access_token") as? String
    }


    private fun getKakaoUser(accessToken: String): KakaoUserResponse? {
        val headers = HttpHeaders()
        println("KakaoUserResponse accessToken: $accessToken")
        headers.setBearerAuth(accessToken) // Authorization: Bearer {accessToken}

        headers["Content-Type"] = "application/x-www-form-urlencoded;charset=utf-8"

        val request = HttpEntity<Void>(headers)

        val response = restTemplate.exchange(
            userInfoUri!!,  // URL (https://kapi.kakao.com/v2/user/me)
            HttpMethod.GET,
            request,  // 요청 엔티티 (헤더)
            KakaoUserResponse::class.java // 응답 타입 (KakaoUserResponse 객체로 변환)
        )

        println("HTTP Status Code: " + response.statusCode)
        val kakaoUserResponse = response.body
        println("KakaoUserResponse: $kakaoUserResponse")
        println("KakaoUserResponse id: " + kakaoUserResponse?.id)
        if (kakaoUserResponse != null) {
            println("User Nickname: " + kakaoUserResponse.kakaoAccount?.profile?.nickname)
        }
        if (kakaoUserResponse != null) {
            println("User email: " + kakaoUserResponse.kakaoAccount?.email)
        }
        return response.body
    }
}