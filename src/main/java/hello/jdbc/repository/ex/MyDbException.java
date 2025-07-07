package hello.jdbc.repository.ex;

public class MyDbException extends RuntimeException { //RuntimeException을 상속받아 언체크(런타임) 예외로 만듦
	public MyDbException() {
	}

	public MyDbException(String message) {
		super(message);
	}

	public MyDbException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyDbException(Throwable cause) {
		super(cause);
	}
}
