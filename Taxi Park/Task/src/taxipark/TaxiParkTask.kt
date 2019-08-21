package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
    return allDrivers.subtract(this.trips.map { trip -> trip.driver } )
}
/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
//    val tripsSuccess = this.trips.groupBy { t -> t.driver }.filterValues { it.size >= minTrips }
//    if (tripsSuccess.size >= minTrips) return tripsSuccess.flatMap { it. }.toSet()
//    val trips: List<Trip> = tripsSuccess.values.flatMap { it }
    val happyPasses: List<Passenger> = this.trips.flatMap { it.passengers }
    val mapPassengerPerTrip: Map<Passenger, Int> = happyPasses
            .groupingBy { it  }
            .eachCount()
            .filter { it.value >= minTrips }
    val result = (mapPassengerPerTrip.keys)
    return result.sortedBy { it.name.substring(2).toInt() }.toSet()
}

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    val tripsSuccess: Map<Driver, List<Trip>> = this.trips.groupBy { t -> t.driver }
    if (!tripsSuccess.containsKey(driver)) return emptySet()
//return tripsSuccess[driver].flatMap { it.passengers }.toSet()
    val tripByDriver: List<Trip>? = tripsSuccess[driver]
    return tripByDriver!!.map { it.passengers }
            .flatten()
            .groupingBy { it }
            .eachCount()
            .filter { it.value > 1 }.keys
}
/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
        val allPasses = this.trips.filter { it.discount != null }.flatMap { it.passengers }
        return allPasses.toSet()
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val durationList: List<Int> = this.trips.map { it.duration }
    fun createRange(value: Int): IntRange? {
        val int: Int = (value / 10) * 10
        return (int..(int + 9))
    }
//    var map: List<Pair<IntRange?, Int>> = durationList.associateBy ({ createRange(it) }, { 1 }).toList()
    val map: List<IntRange?> = durationList.map { createRange(it) }.toList()
    val result = map.groupingBy { it }.eachCount()
    return result.maxBy { it.value }?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    return true
}