package hello.jdbc.connection;

/**
 * 데이터베이스에 접속하는데 필요한 기본 정보 정리 상수.
 * */
public abstract class ConnectionConst {
    public static final String URL = "jdbc:h2:tcp://localhost/~/test";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";
}
