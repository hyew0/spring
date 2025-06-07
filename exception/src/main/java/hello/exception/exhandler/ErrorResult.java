package hello.exception.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResult {
    private String code;
    private String message;
}
/*
예외가 발생했을 때 API 응답으로 사용하는 객체를 정의했다.
* */
