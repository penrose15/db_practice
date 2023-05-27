package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class CheckedTest {

    @Test
    void checked_Catch() {
        Service service = new Service();
        service.callCatch();
        //정상 리턴됨
    }

    @Test
    void checked_throw () throws MyCheckedException {
        Service service = new Service();


        assertThatThrownBy(service::callThrow)
                .isInstanceOf(MyCheckedException.class);
    }

    /*
    * exception을 상속받은 예외는 체크 예외가 된다.
    * */
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }
    /*check 예외는 예외를 잡아서 처리하거나 던지거나 해야 한다.*/
    static class Service {
        Repository repository = new Repository();
        /*
        * 예외를 잡아서 처리하는 코드
        * */
        public void callCatch() {
            try {
                repository.call();
            } catch (MyCheckedException e) {
                //예외처리 로직
                e.printStackTrace();
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }
        /*체크 예외를 밖으로 던지는 코드 체크 예외를 던지려면 throws로 던져야 함*/
        public void callThrow() throws MyCheckedException {
            repository.call();
        }
    }

    static class Repository { //check exception은 반드시 던져야 한다 안그러면 컴파일 에러남
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }
}
