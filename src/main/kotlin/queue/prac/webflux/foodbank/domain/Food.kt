package queue.prac.webflux.foodbank.domain

class Food(val id: Long, val name: String) {

    override fun toString(): String = "$id $name"
}