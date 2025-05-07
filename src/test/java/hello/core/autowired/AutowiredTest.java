package hello.core.autowired;

import hello.core.member.Member;
import io.micrometer.common.lang.Nullable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Optional;

public class AutowiredTest {

    @Test
    void AutowiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }

    static class TestBean{
        //호출 안됨
        @Autowired(required = false)
        public void setNoBean1(Member member) {
            System.out.println("setNoBean1 = " + member);
        }

        //null 호출
        @Autowired
        public void setNoBean2(@Nullable Member member) {
            System.out.println("setNoBean2 = " + member);
        }

        //Optional.empty 호출
        @Autowired(required = false)
        public void setNoBean3(Optional<Member> member) {
            System.out.println("setNoBean3 = " + member);
        }

        //@Autowired에 빨간 밑줄로 오류가 있다고 뜨지만 실제 테스트를 돌리면 오류가 나지 않음.
        //ai에 따르며 "코드 상에서 @Autowired는 Member 타입의 빈을 주입하려고 시도하지만, 현재 Member 타입의 빈이 스프링 컨테이너에 없기 때문에 해당 주입 대상이 없어서 발생하는 오류입니다."
        //다양한 방식으로 "빈이 없어도 괜찮다"고 명시해두어 발생한다고 함.
    }
}
