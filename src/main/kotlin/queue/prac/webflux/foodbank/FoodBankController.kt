package queue.prac.webflux.foodbank

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import queue.prac.webflux.foodbank.domain.Food
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/api/foodbank")
class FoodBankController(val foodBankService: FoodBankService) {

    @GetMapping("/user/{userId}/food-list")
    fun findUserFoodList(@PathVariable userId: Long): Flux<Food> {
        return foodBankService.findUserFoodList(userId)
    }
}