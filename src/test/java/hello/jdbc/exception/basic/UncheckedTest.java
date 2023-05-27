package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class UncheckedTest {
    /*RuntimeException 상속받은 예외는 언체크 예외가 된다.*/
    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String message) {
            super(message);
        }
    }
    @Test
    void unchecked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw () {
        Service service = new Service();
        Assertions.assertThatThrownBy(service::callThrow)
                        .isInstanceOf(MyUncheckedException.class);
//        service.callThrow();
    }

    /*
    * Unchecked 예외는 예외를 잡거나 던지지 않아도 된다
    * 안잡으면 자동으로 밖으로 던짐
    * */
    static class Service {
        Repository repository = new Repository();
        /*
        * 필요한 경우 예외를 잡아서 던지면 된다.
        * */
        public void callCatch() {
            try {
                repository.call();
            } catch(MyUncheckedException e) {
                //예외 처리 로직
                log.info("예외처리={}",e.getMessage(), e);
            }

        }
        /*
        * 예외를 잡지 않아도 상위로 넘어간다.
        * 체크 예외와 다르기 throws 예외 선언을 하지 않아도 된다.
        * */
        public void callThrow() {
            repository.call();
        }
    }

    static class Repository {
        public void call() {
            throw new MyUncheckedException("ex");
        }
    }




}