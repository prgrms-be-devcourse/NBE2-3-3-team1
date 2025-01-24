package org.programmers.cocktail.login.controller

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpSession
import lombok.extern.slf4j.Slf4j
import org.programmers.cocktail.entity.Users
import org.programmers.cocktail.login.dto.CocktailsDto
import org.programmers.cocktail.login.dto.LoginResponse
import org.programmers.cocktail.login.dto.UserRegisterDto
import org.programmers.cocktail.login.service.AuthService
import org.programmers.cocktail.login.service.LoginService
import org.programmers.cocktail.repository.cocktail_lists.CocktailListsRepository
import org.programmers.cocktail.repository.cocktails.CocktailsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.view.RedirectView

@Controller
@Slf4j
class LoginController {
    @Autowired
    private val cocktailListsRepository: CocktailListsRepository? = null

    @Autowired
    private val cocktailsRepository: CocktailsRepository? = null

    @Autowired
    private val loginService: LoginService? = null

    @Autowired
    private val authService: AuthService? = null

    @GetMapping("/kakao_login")
    fun kakao_login(): String {
        val client_id = "566c4aba4b5d2b88d82e241bda9f0c37"
        val redirect_uri = "http://localhost:8080/login/oauth2/code/kakao"

        val login_url = ("https://kauth.kakao.com/oauth/authorize?response_type=code"
                + "&client_id=" + client_id
                + "&redirect_uri=" + redirect_uri
                + "&scope=profile_nickname, profile_image, account_email")

        return "redirect:$login_url"
    }

    // 클라이언트의 카카오 로그인 요청 - 카카오 로그인 버튼 클릭
    @GetMapping("/login/oauth2/code/kakao")
    fun loginByKakao(
        @RequestParam("code") code: String,
        request: HttpServletRequest
    ): RedirectView {
        val loginResponse: LoginResponse? = authService?.register(code)

        val session = request.session
        session.setAttribute("semail", loginResponse?.email)
        System.out.println("semail: " + loginResponse?.email)

        println("authService 호출")

        val redirectUrl = "/"
        println("Redirect URL: $redirectUrl")
        return RedirectView(redirectUrl)
    }

    // 클라이언트의 카카오 로그아웃 요청 - 카카오 로그아웃 버튼 클릭
    @GetMapping("/kakao_logout")
    fun kakao_logout(): String {
        val client_id = "566c4aba4b5d2b88d82e241bda9f0c37"
        val redirect_uri = "http://localhost:8080/logout/oauth2/code/kakao"
        val logout_url = ("https://kauth.kakao.com/oauth/logout"
                + "?client_id=" + client_id
                + "&logout_redirect_uri=" + redirect_uri)

        return "redirect:$logout_url"
    }

    @GetMapping("/logout/oauth2/code/kakao")
    fun logoutByKakao(request: HttpServletRequest): String {
        val session = request.session

        session.invalidate()

        return "redirect:/"
    }

    @GetMapping("/register")
    fun showRegisterForm(): String {
        return "user/register"
    }

    // 회원가입
    @PostMapping("/register_ok")
    fun register_ok(
        @RequestParam("email") email: String,
        @RequestParam("name") name: String,
        @RequestParam("password") password: String,
        @RequestParam("gender") gender: String,
        @RequestParam("age") age: Int

    ): ResponseEntity<Map<String, Any>> {
        println("register_ok 호출")
        val passwordEncoder = BCryptPasswordEncoder()

        // 비밀번호 암호화
        val encodedPassword = passwordEncoder.encode(password)

        // UserRegisterDto에 사용자입력값 세팅하기
        val to: UserRegisterDto = UserRegisterDto()
        to.email = email
        to.name = name
        to.password = encodedPassword
        to.gender = gender
        to.age = age

        val users: Users? = loginService?.findByEmail(to.email)

        val response: MutableMap<String, Any> = HashMap()

        var flag = 2

        if (users != null) {
            flag = 1
            response["message"] = "존재하는 아이디입니다."
        } else {
            flag = 0
            loginService?.insert(to)
            response["message"] = "회원가입이 성공적으로 완료되었습니다."
        }

        response["flag"] = flag

        return ResponseEntity.ok(response)
    }

    @GetMapping("/login")
    fun showLoginPage(session: HttpSession): String {
        return if (session.getAttribute("semail") != null) {
            "redirect:/"
        } else {
            "user/login"
        }
    }


    @PostMapping("/login_ok")
    fun login_ok(
        @RequestParam("email") email: String,
        @RequestParam("password") password: String,
        session: HttpSession

    ): ResponseEntity<Map<String, Any>> {
        println("login_ok 호출")

        val encoder = BCryptPasswordEncoder()

        //DB에서 이메일, 패스워드 가져오기
        val users: Users? = loginService?.findByEmail(email)

        val response: MutableMap<String, Any> = HashMap()

        var flag = 3

        if (users != null) {
            // encoder.matches( raw password, DB에서 가져온 password)
            if ((email == users.email && encoder.matches(password, users.password))) {
                flag = 0

                // session 부여
                session.setAttribute("semail", email)

                response["message"] = "로그인 성공"
            } else if ((email == users.email) && password == "1234") {
                response["message"] = "다른 방법으로 로그인한 계정이 존재합니다.\n다른 방법으로 로그인 후 마이페이지에서 비밀번호를 변경해주세요."
            } else {
                flag = 1
                response["message"] = "이메일 또는 비밀번호가 일치하지 않습니다."
            }
        } else {
            flag = 2
            response["message"] = "존재하지 않는 회원입니다."
        }

        response["flag"] = flag

        return ResponseEntity.ok(response)
    }

    @PostMapping("/login_complete")
    fun login_complete(session: HttpSession): ResponseEntity<Map<String, Any>> {
        println("login_complete 호출")
        val response: MutableMap<String, Any> = HashMap()
        var flag = 2

        if (session.getAttribute("semail") != null) {
            flag = 0
        } else {
            flag = 1
            response["message"] = "로그인해야 합니다."
        }

        response["flag"] = flag

        return ResponseEntity.ok(response)
    }


    // session 확인 후 session 존재하면(=로그인 되어 있으면) mypage로 이동
    @PostMapping("/logout_ok")
    fun logout_ok(session: HttpSession): String {
        // 홈페이지, 소셜 로그인 세션 둘 다 만료시킴
        session.invalidate()

        return "redirect:/login"
    }


    @GetMapping("/mypage")
    fun showMyPage(session: HttpSession, model: Model): String {
        println("mypage 호출")

        val email = session.getAttribute("semail") as String
        println("mypage email: $email")

        // 세션이 있다면
        if (email != null) {
            val users: Users? = loginService?.findByEmail(email)


            val cocktailLists = cocktailListsRepository!!.findAll()
            val cocktails = cocktailsRepository!!.findAll()

            val cocktailsDtos: MutableList<CocktailsDto> = ArrayList<CocktailsDto>()

            for (cocktail in cocktails) {
                val cocktailsTo: CocktailsDto = CocktailsDto()
                cocktailsTo.id = cocktail?.id
                cocktailsTo.name = cocktail?.name
                cocktailsTo.image_url = cocktail?.image_url
                cocktailsDtos.add(cocktailsTo)
            }

            if (users != null) {
                // 세션에서 이메일을 통해 DB에서 유저 정보를 가져오기
                val to: UserRegisterDto = UserRegisterDto()
                to.id = users.id
                to.email = users.email
                to.name = users.name
                to.password = users.password

                // 모델에 유저 정보를 담아서 뷰로 전달
                model.addAttribute("to", to)

                // user, cocktails, cocktaillists 조인해서 user_id타고
                // cocktaillists에 저장된 cocktail_id를 타서 cocktails에 가서 칵테일 정보 가져오기
                val ct: MutableList<CocktailsDto> = ArrayList<CocktailsDto>()

                for ( cl in cocktailLists ) {
                    if ( cl != null ) {
                        if ( users.id == cl.users?.id ) {
                            for ( c in cocktailsDtos ) {
                                if ( cl.cocktails?.id == c.id ) {
                                    ct.add(c)
                                }
                            }
                        }
                    }
                }

                for (c in ct) {
                    println("CocktailsDto: $c")
                }

                model.addAttribute("ct", ct)

                return "user/mypage"
            }
        }

        return "redirect:/login"
    }

    // 이메일은 변경 X
    // 이메일로 유저의 Id를 가져와 비밀번호와 이름을 수정
    @PostMapping("/modify_ok")
    fun modify_ok(
        @RequestParam("email") email: String?,
        @RequestParam("name") name: String?,
        @RequestParam("password") password: String?,
        session: HttpSession
    ): ResponseEntity<Map<String, Any>> {
        println("modify_ok 호출")
        val response: MutableMap<String, Any> = HashMap()

        var flag = 2

        val users: Users? = loginService?.findByEmail(email)

        val encoder = BCryptPasswordEncoder()
        val encodedPassword = encoder.encode(password)


        val result: Int? = loginService?.updateUser(name, encodedPassword, users?.id)

        if (result != null) {
            if (result > 0) {
                flag = 0
                session.invalidate()
                response["message"] = "계정 정보 수정 성공"
            } else {
                flag = 1
                response["message"] = "계정 정보 수정 실패\n로그인 페이지로 돌아갑니다."
            }
        }


        response["flag"] = flag

        return ResponseEntity.ok(response)
    }


    @GetMapping("/withdrawalPage")
    fun showWithdrawalPage(): String {
        return "user/withdrawalPage"
    }

    @GetMapping("/withdrawalCompletePage")
    fun showWithdrawalCompletePage(): String {
        return "user/withdrawal_complete"
    }

    @PostMapping("/withdrawal_ok")
    fun userWithdrawal(
        @RequestParam("email") email: String,
        @RequestParam("password") password: String?,
        session: HttpSession
    ): ResponseEntity<Map<String, Any>> {
        println("withdrawal_ok 호출")
        val encoder = BCryptPasswordEncoder()

        val response: MutableMap<String, Any> = HashMap()

        var flag = 2

        val users: Users? = loginService?.findByEmail(email)
        System.out.println("users.getEmail(): " + users?.email)
        System.out.println("users.password(): " + users?.password)

        var result : Int? = 0
        if (email == users?.email && encoder.matches(password, users.password)) {
            result = users.id?.let { loginService?.deleteUser(it) }
            session.invalidate()
        }

        if (result == 0) {
            flag = 1
            response["message"] = "탈퇴 실패"
        } else {
            flag = 0
            response["message"] = "탈퇴 성공"
        }

        response["flag"] = flag

        return ResponseEntity.ok(response)
    }
}