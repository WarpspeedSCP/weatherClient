import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import model.WeatherData
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt
import kotlin.system.exitProcess

fun main() {

    Fuel.get("https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22")
        .responseString { request, response, result ->
            when (result) {
                is Result.Failure -> println("An error occurred during init: ${result.getException()}")
                is Result.Success -> processResult(result.get())
            }
        }.join()
}

private fun processResult(data: String) {
    val jsonData = Json(JsonConfiguration(ignoreUnknownKeys = true))
        .parse(WeatherData.serializer(), data)

    val dateTimeParseFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val dateParseFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val displayFormat = DateTimeFormatter.ofPattern("dd MMM yyyy")


    val dateRange = "${
        jsonData.list.minBy { it.dtStr }?.let { LocalDate.parse(it.dtStr, dateTimeParseFormat).format(displayFormat) }
    } and ${
        jsonData.list.maxBy { it.dtStr }?.let { LocalDate.parse(it.dtStr, dateTimeParseFormat).format(displayFormat) }
    }"

    val introMsg = """${jsonData.city.name} weather forecast for 4 days
Enter an option from the following to get the relevant data for any date between $dateRange.

Press 1 to get the weather for a particular date.
Press 2 to get wind speed for a particular date.
Press 3 to get pressure data for a particular date.
Press 0 to exit.
"""

    print(introMsg)

    while (true) {

        print("Enter a choice: ")
        val choice = readLine()!!

        when (choice) {
            "0" -> exitProcess(0)

            "1" -> {
                print("Enter a date between $dateRange in the format DD-MM-YYYY: ")

                var date = readLine()!!

                val dateObj = LocalDate.parse(date, dateParseFormat)

                val weatherData = jsonData.list.find {
                    val currDate = LocalDate.parse(it.dtStr, displayFormat)
                    currDate.toEpochDay() == dateObj.toEpochDay() && it.weather.isNotEmpty()
                }

                weatherData?.let { println("The weather for $date in ${jsonData.city.name} is: ${weatherData.weather[0].main}") } ?:
                    run { println("The date $date is not found between $dateRange. Please try again with a valid date.") }
            }

            "2" -> {
                print("Enter a date between $dateRange in the format DD-MM-YYYY: ")

                var date = readLine()!!

                val dateObj = LocalDate.parse(date, displayFormat)

                val avgWind = jsonData.list
                    .filter {
                        val currDate = LocalDate.parse(it.dtStr, displayFormat)
                        currDate.toEpochDay() == dateObj.toEpochDay()
                    }
                    .map { it.wind.speed }
                    .average()

                if (avgWind.isNaN() || avgWind.roundToInt() == 0 )
                    println("Average wind speed for $date is %.2f.".format(avgWind))
                else println("The date $date is not found between $dateRange. Please try again with a valid date.")
            }

            "3" -> {
                print("Enter a date between $dateRange in the format DD-MM-YYYY: ")

                val date = readLine()!!

                val dateObj = LocalDate.parse(date, displayFormat)

                val avgPressure = jsonData.list
                    .filter {
                        val currDate = LocalDate.parse(it.dtStr, displayFormat)
                        currDate.toEpochDay() == dateObj.toEpochDay()
                    }
                    .map { it.main.pressure }
                    .average()

                if (avgPressure.isNaN() || avgPressure.roundToInt() == 0 )
                    println("Average pressure for $date is %.2f.".format(avgPressure))
                else println("The date $date is not found between $dateRange. Please try again with a valid date.")
            }
            else -> println("Invalid choice. Please enter a number between 0 and 3")
        }
    }
}
