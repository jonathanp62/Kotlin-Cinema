package cinema

const val SMALL_CINEMA_MAX_OCCUPANCY = 60
const val CHEAP_SEAT = 8
const val EXPENSIVE_SEAT = 10

const val SEAT_AVAILABLE = 'S'
const val SEAT_OCCUPIED = 'B'

/**
 * The main function.
 */
fun main() {
    val input = getTotalRowsAndSeats()
    val rows = input.first
    val seats = input.second

    val occupancy = CharArray(rows * seats) { SEAT_AVAILABLE } // Initially all the seats are available

    while (true) {
        when (printMenuAndGetSelection()) {
            0 -> break
            1 -> printCinema(rows, seats, occupancy)
            2 -> buyATicket(rows, seats, occupancy)
            3 -> printStatistics(rows, seats, occupancy)
        }
    }
}

/**
 * Print the statistics.
 *
 * @param   rows        The number of rows
 * @param   seats       The number of seats in each row
 * @param   occupancy   A character array containing occupancy
 */
fun printStatistics(rows: Int, seats: Int, occupancy: CharArray) {
    val numberOfPurchasedTickets = getNumberOfPurchasedTickets(occupancy)
    val numberOfPurchasedTicketsPercentage = (numberOfPurchasedTickets.toDouble() / occupancy.size) * 100.0
    val currentIncome = getCurrentIncome(rows, seats, occupancy)
    val totalIncome = getTotalIncome(rows, seats)

    println()
    println("Number of purchased tickets: $numberOfPurchasedTickets")
    println("Percentage: ${"%.2f".format(numberOfPurchasedTicketsPercentage)}%")
    println("Current income: \$$currentIncome")
    println("Total income: \$$totalIncome")
}

/**
 * Get the number of purchased tickets.
 *
 * @param   occupancy   A character array containing occupancy
 * @return              The number of purchased tickets
 */
fun getNumberOfPurchasedTickets(occupancy: CharArray): Int {
    var purchasedTickets = 0

    for (character in occupancy) {
        if (character == SEAT_OCCUPIED) {
            ++purchasedTickets
        }
    }

    return purchasedTickets
}

/**
 * Get the current income.
 *
 * @param   rows        The number of rows
 * @param   seats       The number of seats in each row
 * @param   occupancy   A character array containing occupancy
 * @return              The current income
 */
fun getCurrentIncome(rows: Int, seats: Int, occupancy: CharArray): Int {
    var currentIncome = 0

    for (i in occupancy.indices) {
        val row = if (i == 0) {
            1
        } else {
            (i / seats) + 1
        }

        if (occupancy[i] == SEAT_OCCUPIED) {
            currentIncome += calculateTicketPrice(rows, seats, row)
        }
    }

    return currentIncome
}

/**
 * Get the total income.
 *
 * @param   rows        The number of rows
 * @param   seats       The number of seats in each row
 * @return              The total income
 */
fun getTotalIncome(rows: Int, seats: Int): Int {
    var totalIncome = 0

    for (row in 1..rows) {
        for (seat in 1..seats) {
            val ticketPrice = if (rows * seats <= SMALL_CINEMA_MAX_OCCUPANCY) {
                EXPENSIVE_SEAT
            } else {
                if (row <= (rows / 2)) {
                    EXPENSIVE_SEAT
                } else {
                    CHEAP_SEAT
                }
            }  // Expensive if the row is in the front, otherwise cheap

            totalIncome += ticketPrice
        }
    }

    return totalIncome
}

/**
 * Buy a ticket.
 *
 * @param   rows        The number of rows
 * @param   seats       The number of seats in each row
 * @param   occupancy   A character array containing occupancy
 */
fun buyATicket(rows: Int, seats: Int, occupancy: CharArray) {
    while (true) {
        val seatCoordinates = getSeatCoordinates()
        val seatRowNumber = seatCoordinates.first
        val seatNumber = seatCoordinates.second
        val index = ((seatRowNumber - 1) * rows) + (seatNumber - 1)

        if (seatRowNumber > rows || seatNumber > seats) {
            println("Wrong input!")
            continue
        }

        if (occupancy[index] == SEAT_AVAILABLE) {
            occupancy[index] = SEAT_OCCUPIED

            val ticketPrice = calculateTicketPrice(rows, seats, seatRowNumber)

            println("Ticket price: \$$ticketPrice")
            break
        } else {
            println("That ticket has already been purchased!")
        }
    }
}

/**
 * Print the menu and return the selection.
 *
 * @return  The selection (0, 1, 2)
 */
fun printMenuAndGetSelection(): Int {
    println()
    println("1. Show the seats")
    println("2. Buy a ticket")
    println("3. Statistics")
    println("0. Exit")

    return readln().toInt()
}

/**
 * Function to prompt the user for the
 * total number of rows and seats in
 * each row. A Pair object is returned.
 *
 * @return  A pair the first of which is the number of rows with the second being the number of seats in each row
 */
fun getTotalRowsAndSeats(): Pair<Int, Int> {
    println("Enter the number of rows:")

    val rows = readln().toInt()

    println("Enter the number of seats in each row:")

    val seats = readln().toInt()

    return Pair(rows, seats)
}

/**
 * Function to prompt the user for a
 * row number and a seat number in
 * that row. A Pair object is returned.
 *
 * @return  A pair the first of which is the row number with the second being the seat number in that row
 */
fun getSeatCoordinates(): Pair<Int, Int> {
    println()
    println("Enter a row number:")

    val rowNumber = readln().toInt()

    println("Enter a seat number in that row:")

    val seatNumber = readln().toInt()

    return Pair(rowNumber, seatNumber)
}

/**
 * Print the cinema based on the
 * number of rows and seats in each row
 * along with the occupancy.
 *
 * @param   rows        The number of rows
 * @param   seats       The number of seats in each row
 * @param   occupancy   A character array containing occupancy
 */
fun printCinema(rows: Int, seats: Int, occupancy: CharArray) {
    println()
    print("Cinema:\n ")

    for (n in 1.. seats)
        print(" $n")

    for (row in 1.. rows) {
        print("\n$row")

        for (seat in 1..seats) {
            print(" ${occupancy[((row - 1) * rows) + (seat - 1)]}")
        }
    }

    println()
}

/**
 * Calculate the ticket price based on the
 * size of the cinema and the location of
 * the seat.
 *
 * @param   rows        The number of rows
 * @param   seats       The number of seats in each row
 * @param   rowNumber   The row number of the booked seat
 * @return              The ticket price
 */
fun calculateTicketPrice(rows: Int, seats: Int, rowNumber: Int): Int {
    val ticketPrice = if ((rows * seats) <= SMALL_CINEMA_MAX_OCCUPANCY) {
        EXPENSIVE_SEAT
    } else {
        if (rowNumber <= (rows / 2)) EXPENSIVE_SEAT else CHEAP_SEAT  // Expensive if the row is in the front, otherwise cheap
    }

    return ticketPrice
}
