package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> {
//    return allDrivers.subtract(this.trips.map { it.driver } )
    return allDrivers - trips.map { it.driver }
}
/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> {
    val happyPasses: List<Passenger> = this.trips.flatMap { it.passengers }
    val mapPassengerPerTrip: Map<Passenger, Int> = happyPasses
            .groupingBy { it  }
            .eachCount()
            .filter { it.value >= minTrips }
    val result = mapPassengerPerTrip.keys.sortedBy { indexExtract(it) }.toSet()
    return this.allPassengers
            .filter {
                p -> trips.count { p in it.passengers } >= minTrips
            }.toSet()

//    return trips.flatMap(Trip::passengers)
//            .groupBy { passenger -> passenger }
//            .filter { (_, group) -> group.size >= minTrips }
//            .keys
//            .sortedBy { indexExtract(it) }
//            .toSet()

}

private fun indexExtract(it: Passenger) = it.name.substring(2).toInt()

/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> {
    val tripsSuccess: Map<Driver, List<Trip>> = this.trips.groupBy { t -> t.driver }
    if (!tripsSuccess.containsKey(driver)) return emptySet()
    val tripByDriver: List<Trip>? = tripsSuccess[driver]
//    return tripByDriver!!.map { it.passengers }
//            .flatten()
//            .groupingBy { it }
//            .eachCount()
//            .filter { it.value > 1 }.keys

    return trips.filter { it.driver == driver }
            .flatMap(Trip::passengers)
            .groupBy { passenger -> passenger }
            .filterValues { list -> list.size > 1 }
            .keys

}
/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    val allPasses = this.trips.filter { it.discount != null && it.discount > 0.0 }.flatMap { it.passengers }.toSet()
    val allPassesWithout = this.trips.filter { it.discount == null || it.discount == 0.0 }.flatMap { it.passengers }.toSet()
    val result = this.allPassengers
            .filter { allPasses.count() > allPassesWithout.count()  }.toSet()

//    return this.allPassengers
//            .filter { p: Passenger -> this.trips.filter { p in it.passengers && it.discount != null }.count() >
//                    this.trips.filter { p in it.passengers && it.discount == null }.count()
//            }.toSet()

    /*
    // 1 solution
    val (tripsDiscount, tripsWithout) = trips.partition { it.discount != null }
    return allPassengers.filter { p ->
        tripsDiscount.count { p in it.passengers } >
                tripsWithout.count { p in it.passengers }
    }.toSet()
    */

    // 2 solution
    return allPassengers.associate { p ->
        p to trips.filter { trip -> p in trip.passengers }
    }.filterValues { list ->
        val (tripsDiscount, tripsWithout) = list.partition { it.discount != null }
        tripsDiscount.size > tripsWithout.size
    }.keys
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val durationList: List<Int> = this.trips.map { it.duration }
    fun createRange(value: Int): IntRange? {
        val start: Int = (value / 10) * 10
        val end = start + 9
        return start..end
    }
//    var map: List<Pair<IntRange?, Int>> = durationList.associateBy ({ createRange(it) }, { 1 }).toList()
    val map: List<IntRange?> = durationList.map { createRange(it) }.toList()
    val result = map.groupingBy { it }.eachCount().maxBy { it.value }?.key
    // return result

    // solution:
    return trips
            .groupBy {
                val start = it.duration / 10 * 10
                val end = start + 9
                start..end
            }
            .maxBy { (_, list) -> list.size }
            ?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if(this.trips.isEmpty()) return false

    val totalTripsCost = this.trips.map { it.cost }.sum()
    val mapCostByDriverSorted = trips
            .groupBy { it.driver }
            .mapValues { (_, trips) -> trips.sumByDouble { it.cost }}
            .toList()
            .sortedByDescending { (_, value) -> value}.toMap()

    var currentSum = 0.0
    var numberOfDrivers = 0
    for (value in mapCostByDriverSorted.values){
        numberOfDrivers++
        currentSum += value
        if (currentSum >= (totalTripsCost * 0.8)) break
    }
    return numberOfDrivers <= (allDrivers.size * 0.2)

}