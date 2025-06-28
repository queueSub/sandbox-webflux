package queue.prac.webflux.operators

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class OperatorTest {

    @Test
    @DisplayName("기본 테스트")
    fun helloReactor(){
        StepVerifier
            .create(Mono.just("Hello Reactor!"))
            .expectNext("Hello Reactor!")
            .expectComplete()
            .verify()
    }
}