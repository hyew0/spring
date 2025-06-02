package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //    @GetMapping("/")
    public String home() {
        return "home";
    }

    //@GetMapping("/")
    public String homeLogin(
            @CookieValue(name = "memberId", required = false) Long memberId,
            Model model
    ) {

        if (memberId == null) {
            return "home";
        }

        //로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }
    /*
    로직 분석
    - 로그인 쿠키( memberId )가 없는 사용자는 기존 home 으로 보낸다. 추가로 로그인 쿠키가 있어도 회원이 없으면 home 으로 보낸다.
    - 로그인 쿠키( memberId )가 있는 사용자는 로그인 사용자 전용 홈 화면인 loginHome 으로 보낸다. 추가로 홈화면에 화원 관련 정보도 출력해야 해서 member 데이터도 모델에 담아서 전달한다.
    * */

    //@GetMapping("/")
    public String homeLoginV2(
            HttpServletRequest request,
            Model model
    ) {

        //세션 관리자에 저장된 회원 정보 조회
        Member member = (Member)sessionManager.getSession(request);
        if (member == null) {
            return "home";
        }

        //로그인
        model.addAttribute("member", member);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {
        //세션이 없으면 home
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "home";
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
    /**
     request.getSession(false) :
        request.getSession() 를 사용하면 기본 값이 create: true 이므로, 로그인 하지 않을 사용자도 의미없는 세션이 만들어진다.
        따라서 세션을 찾아서 사용하는 시점에는 create: false 옵션을 사용해서 세션을 생성하지 않아야 한다.
     session.getAttribute(SessionConst.LOGIN_MEMBER) :
        로그인 시점에 세션에 보관한 회원 객체를 찾는다.
     * */
}