package queue.prac.webflux.foodbank

import org.springframework.stereotype.Service
import queue.prac.webflux.foodbank.domain.Food
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuples
import java.time.Duration

@Service
class FoodBankService {

    fun findUserFoodList(userId: Long): Flux<Food> {
        return Mono.zip(
            findAllUserFood(userId).collectList(), // 10s
            getAllFoodList().collectList() // 8s
        )
            .flatMapMany { tuple ->
                val foodMap = tuple.t2.associateBy { it.id }
                val userFoodIds = tuple.t1
                Flux.fromIterable(userFoodIds)
                    .flatMap {
                        foodMap[it]?.let { food -> Mono.just(food) } ?: Mono.empty()
                    }
            }
    }

    fun findAllUserFood(userId: Long): Flux<Long> {
        return Flux.just(
            Tuples.of(1L, 3L),
            Tuples.of(1L, 7L),
            Tuples.of(2L, 12L),
            Tuples.of(4L, 15L),
            Tuples.of(5L, 1L),
            Tuples.of(6L, 9L),
            Tuples.of(7L, 4L),
            Tuples.of(8L, 18L),
            Tuples.of(1L, 6L),
            Tuples.of(10L, 20L),
            Tuples.of(11L, 2L),
            Tuples.of(12L, 8L),
            Tuples.of(13L, 14L),
            Tuples.of(14L, 11L),
            Tuples.of(15L, 5L),
            Tuples.of(16L, 16L),
            Tuples.of(17L, 10L),
            Tuples.of(18L, 19L),
            Tuples.of(19L, 13L),
            Tuples.of(20L, 17L)
        )
            .filter { it.t1 == userId }
            .map { it.t2 }
            .delaySequence(Duration.ofSeconds(10))
    }

    fun getAllFoodList(): Flux<Food> {
        return Flux.just(
            Food(1, "Kimchi"),
            Food(2, "Bread"),
            Food(3, "Egg"),
            Food(4, "Chicken"),
            Food(5, "Beef"),
            Food(6, "Noodle"),
            Food(7, "Pork"),
            Food(8, "Shrimp"),
            Food(9, "Tuna"),
            Food(10, "Bean"),
            Food(11, "Cabbage"),
            Food(12, "Carrot"),
            Food(13, "Cauliflower"),
            Food(14, "Chili"),
            Food(15, "Eggplant"),
            Food(16, "Mushroom"),
            Food(17, "Okra"),
            Food(18, "Potato"),
            Food(19, "Tomato"),
            Food(20, "Zucchini")
        )
            .delaySequence(Duration.ofSeconds(8))
    }
}