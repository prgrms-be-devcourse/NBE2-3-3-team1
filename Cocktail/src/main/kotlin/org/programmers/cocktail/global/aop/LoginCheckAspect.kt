package org.programmers.cocktail.global.aop

import jakarta.servlet.http.HttpSession
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.programmers.cocktail.global.exception.UnauthorizedException
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestAttributes
import org.springframework.web.context.request.RequestContextHolder


@Aspect
@Component
class LoginCheckAspect {
    @Before("execution(* org.programmers.cocktail.search.controller.*.*(..))&& @annotation(org.programmers.cocktail.global.annotation.RequireLogin)")
    fun loginCheck() {
        // 현재 요청의 Session 가져오기
        val session = RequestContextHolder.currentRequestAttributes()
            .resolveReference(RequestAttributes.REFERENCE_SESSION) as HttpSession?

        if (session?.getAttribute("semail") == null) {
            //세션이 없거나 로그인이 안된상태로 확인된 경우 예외 발생
            //globalExceptionHandler에서 HTTP 401 반환하도록 처리 필요
            throw UnauthorizedException()
        }
    }
}