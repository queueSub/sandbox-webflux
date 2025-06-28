package queue.prac.webflux.operators

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

class SequenceTransformOperator {

    // 시퀀스 데이터 변환
    @Test
    @DisplayName("map 은 emit 된 데이터를 mapper Function 으로 변환 후 emit 한다.")
    fun mapTest() {
        StepVerifier
            .create(
                Flux.just("Cat", "Bat", "Hat")
                    .map {
                        if (it.first() == 'B') throw Exception()
                        it.replace("at", "ats")
                    }
                    .onErrorContinue { err, value -> println("$value is error") }) // 기본적으로 예외 발생시 시퀀스가 중단되지만 지속하게 해줌
            .expectNext("Cats", "Hats")
            .verifyComplete()
    }

    @Test
    @DisplayName("flatMap 은 flatMap 의 업스트림에서 emit 된 데이터를 flatMap 이너 시퀀스에서 한개 이상의 데이터로 변환 후, flatten 하여 다운스트림으로 emit")
    // 1차원 배열 emit 데이터의 각 원소를 로우로 늘려 2차원 배열로 만들고 평탄화
    fun flatMapTest() {
        StepVerifier
            .create(
                Flux.just("김", "최")
                    .flatMap { familyName -> Flux.just("민수", "정환", "민지").map { familyName + it } })
            .expectNext("김민수", "김정환", "김민지", "최민수", "최정환", "최민지")
            .verifyComplete()
    }


    // 여러 시퀀스 데이터 병합
    @Test
    @DisplayName("concat 은 퍼블리셔 시퀀스들을 연결하여 순차적으로 emit 한다. 앞선 퍼블리셔의 시퀀스가 종료될 때까지 이후 퍼블리셔는 구독되지 않고 대기한다.")
    fun concatTest() {
        StepVerifier
            .create(Flux.concat(Flux.just(1, 2, 3), Flux.just(4, 5, 6)))
            .expectNext(1, 2, 3, 4, 5, 6) // 시퀀스 순서대로 emit
            .verifyComplete()
    }

    @Test
    @DisplayName("merge 는 concat 과 달리 모든 퍼블리셔가 즉시 구독되어 먼저 emit 된 순서대로 데이터가 병합된다.")
    fun mergeTest() {
        StepVerifier
            .create(
                Flux.merge(
                    Flux.just(1, 2, 3).delayElements(Duration.ofMillis(300)),
                    Flux.just(4, 5, 6).delayElements(Duration.ofMillis(500)),
                    Flux.just(7, 8, 9).delayElements(Duration.ofMillis(700))
                )
                    .doOnNext { println("doOnNext $it") })
            .expectNext(1, 4, 2, 7, 3, 5, 8, 6, 9)
            .verifyComplete()
    }

    @Test
    @DisplayName("zip 은 각 퍼블리셔 시퀀스가 데이터를 하나씩 emit 하면 튜플로 묶어서 emit 한다.")
    fun zipTest() {
        StepVerifier.create(
            Flux.zip(
                Flux.just("김", "최").delayElements(Duration.ofMillis(300)),
                Flux.just("민수", "정환", "민지").delayElements(Duration.ofMillis(500)),
                { t1, t2 -> "${t1}${t2}"} // emit 된 데이터 묶음을 변환 후 emit 할 수 있다.
            )
        )
            .expectNext("김민수", "최정환")
            .verifyComplete()
    }
}