import java.util.concurrent.ThreadLocalRandom

data class CityPair(val city1: Int, val city2: Int)

class City(val id: Int, val city: String, val x: Double, val y: Double) {
    override fun toString() = city
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as City

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }

}

object CitiesAndDistances {

    val citiesById = CitiesAndDistances::class.java.getResource("cities.csv").readText().lines()
            .asSequence()
            .map { it.split(",") }
            .map { City(it[0].toInt(), it[1], it[2].toDouble(), it[3].toDouble()) }
            .map { it.id to it }
            .toMap()

    val citiesByString = citiesById.entries.asSequence()
            .map { it.value.city to it.value }
            .toMap()

    val cities = citiesById.values.toList()

    val distances = CitiesAndDistances::class.java.getResource("distances.csv").readText().lines()
            .asSequence()
            .map { it.split(",") }
            .map { CityPair(it[0].toInt(), it[1].toInt()) to it[2].toDouble() }
            .toMap()

    val distancesByStartCityId = distances.entries.asSequence()
            .map { it.key.city1 to (cities[it.key.city2] to it.value) }
            .groupBy({it.first},{it.second})

    val randomCity get() = ThreadLocalRandom.current().nextInt(0,CitiesAndDistances.cities.count()).let { CitiesAndDistances.cities[it] }
}

operator fun Map<CityPair,String>.get(city1: Int, city2: Int) = get(CityPair(city1,city2))
operator fun Map<CityPair,String>.get(city1: City, city2: City) = get(CityPair(city1.id,city2.id))
