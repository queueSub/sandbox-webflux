package queue.prac.webflux.operators

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.LocalDateTime

class SequenceCreateOperatorTest {

    @Test
    @DisplayName("justOrEmpty 는 Null 일 경우 NPE 대신 onComplete 시그널 전송")
    fun justOrEmptyTest() {
        StepVerifier
            .create(Mono.justOrEmpty<Int?>(null))
            .verifyComplete()
    }

    @Test
    @DisplayName("Iterable 구현체를 파라미터로 전달 가능")
    fun fromIterableTest() {
        StepVerifier
            .create(Flux.fromIterable(listOf("korea", "japan", "usa")))
            .expectNext("korea", "japan", "usa")
            .verifyComplete()
    }

    @Test
    @DisplayName("range 는 n부터 1씩 증가하는 수 m개를 emit")
    fun rangeTest() {
        StepVerifier
            .create(Flux.range(3, 5))
            .expectNext(3, 4, 5, 6, 7)
            .verifyComplete()
    }

    @Test
    @DisplayName("defer 는 구독 시점에 데이터를 emit 하는 퍼블리셔를 생성한다. (데이터 emit 을 지연시킨다.)")
    fun deferTest() {
        // just VS defer
        val justMono = Mono.just(LocalDateTime.now()) // 구독 전에 이미 emit (just 는 Hot Publisher)
        val deferMono = Mono.defer { Mono.just(LocalDateTime.now()) }

        StepVerifier.create(justMono) // 위에서 생성한 동일한 LocalDateTime
            .assertNext { time1 ->
                StepVerifier.create(justMono) // 위에서 생성한 동일한 LocalDateTime
                    .assertNext { time2 ->
                        assert(time1.isEqual(time2))
                    }
                    .`as`("just 는 Hot 퍼블리셔로, 구독과 관계없이 데이터를 즉시 emit 한다.")
                    .verifyComplete()
            }
            .verifyComplete()

        StepVerifier.create(deferMono) // LocalDateTime 새롭게 emit
            .assertNext { time1 ->
                StepVerifier.create(deferMono) // LocalDateTime 새롭게 emit
                    .assertNext { time2 ->
                        assert(!time1.isEqual(time2))
                    }
                    .`as`("defer 는 구독 발생 전까지 데이터의 emit 을 지연시킨다.")
                    .verifyComplete()
            }
            .verifyComplete()
    }

    @Test
    @DisplayName("generate 는 동기적으로 데이터를 순차적으로 하나씩 emit 한다.")
    fun generateTest() {
        StepVerifier
            .create(
                Flux.generate (
                    { 0 }, // emit 데이터 초기값 지정
                    { state, sink ->
                        sink.next(state) // synchronousSink 객체를 통해 동기적으로 하나만 emit (0 ~ 10)
                        if(state == 10) {
                            sink.complete() // onComplete signal 발생
                        }
                        state + 1 // next state
                    }
                )
            )
            .expectNext(0)
            .expectNextCount(9)
            .expectNext(10)
            .verifyComplete()
    }


}