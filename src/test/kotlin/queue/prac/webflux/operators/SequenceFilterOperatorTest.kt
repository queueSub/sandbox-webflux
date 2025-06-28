package queue.prac.webflux.operators

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration

class SequenceFilterOperatorTest {

    @Test
    @DisplayName("filter 는 조건에 일치하는 데이터만 Downstream 으로 emit 한다.")
    fun fileterTest() {
        StepVerifier
            .create(Flux.range(1, 30).filter { num -> num % 3 == 0 })
            .expectNext(3, 6, 9, 12, 15, 18, 21, 24, 27, 30)
            .verifyComplete()
    }

    @Test
    @DisplayName("skip 은 emit 된 데이터 중 파라미터 숫자 개수 만큼 스킵 후 나머지를 emit 한다.")
    fun skipTest() {
        StepVerifier
            .create(Flux.range(1, 20).skip(15)) // 15 개 데이터를 스킵
//            .create(Flux.interval(Duration.ofSeconds(1)).skip(Duration.ofSeconds(3))) // 특정 시간동안 발생하는 데이터만 스킵도 가능하다.
            .expectNext(16, 17, 18, 19, 20)
            .verifyComplete()
    }

    @Test
    @DisplayName("take 는 입력받은 숫자 만큼만 데이터를 n개 emit 한다.")
    fun takeTest() {
        StepVerifier
            .create(Flux.range(1, 10).take(5))
//            .create(Flux.interval(Duration.ofSeconds(1)).take(Duration.ofSeconds(3))) // 특정 시간동안 발생하는 데이터만 emit 가능
            .expectNext(1, 2, 3, 4, 5)
            .verifyComplete()
    }

    @Test
    @DisplayName("takeLast 는 마지막에 emit 된 데이터부터 n개 emit 한다.")
    fun takeLastTest() {
        StepVerifier
            .create(Flux.range(1, 10).takeLast(3))
            .expectNext(8, 9, 10)
            .verifyComplete()
    }

    @Test
    @DisplayName("takeUntil 은 Predicate 가 true 가 될 때까지 emit")
    fun takeUntilTest() {
        StepVerifier
            .create(Flux.range(1, 10).takeUntil { it == 3 }) // 주의: 3도 포함!
            .expectNext(1, 2, 3)
            .verifyComplete()
    }

    @Test
    @DisplayName("takeWhile 은 Predicate 가 true 인 동안만 emit")
    fun takeWhileTest() {
        StepVerifier
            .create(Flux.range(1, 10).takeWhile { it <= 3 })
            .expectNext(1, 2, 3)
            .verifyComplete()
    }

    @Test
    @DisplayName("next 는 데이터 중 첫번째 데이터만 emit")
    fun nextTest() {
        StepVerifier
            .create(Flux.range(1, 3).next())
            .expectNext(1)
            .verifyComplete()

        // 만약 데이터가 비어있다면 empty Mono 를 emit 한다.
        StepVerifier
            .create(
                Flux
                    .fromIterable(emptyList<Int>())
                    .next()
                    .switchIfEmpty(Mono.just(999))
            )
            .expectNext(999)
            .verifyComplete()
    }
}