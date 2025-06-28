package queue.prac.webflux.operators

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class TestExample {

    @Test
    @DisplayName("기본 테스트")
    fun helloReactor() {
        StepVerifier
            .create(Mono.just("Hello Reactor!"))
            .expectNext("Hello Reactor!")
            .expectComplete()
            .verify() // Trigger (구독 및 기댓값 평가)
    }

    @Test
    @DisplayName("as 가 로그로 출력된다.")
    fun asTest() {
        StepVerifier.create(Flux.just("Hi", "Reactor"))
            .expectSubscription()
            .`as`("# expect Subscription") // 기댓값 평가에 대한 설명, 실패시 로그 출력
            .expectNext("Hello")
            .`as`("# expect Hello")
            .expectNext("Reactor")
            .`as`("# expect Reactor")
            .verifyComplete()
    }

    @Test
    @DisplayName("0으로 나눠 에러가 발생한다.")
    fun verifyErrorTest() {
        StepVerifier
            .create(Flux.just(2, 4, 6, 8, 10).zipWith(Flux.just(2, 2, 2, 2, 0)) { x, y -> x / y })
            .expectSubscription()
            .expectNext(1)
            .expectNext(2)
            .expectNext(3,4) // 한개 이상의 emit 데이터 평가 가능
            .expectError()
            .verify()
//            .verifyError() 와 같음
    }

    @Test
    @DisplayName("emit 데이터 개수를 검증한다.")
    fun expectNextCountTest() {
        StepVerifier
            .create( Flux.range(1, 1000).take(516))
            .expectSubscription()
            .expectNext(1) // 첫 숫자
            .expectNextCount(514) // count 개 숫자가 emit 되었음을 기대
            .expectNext(516) // 마지막 숫자
            .expectComplete()
            .verify()
    }
}