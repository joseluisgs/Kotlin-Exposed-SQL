import dao.CityDAO
import dao.UserDAO
import dao.UsersTable
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

fun DAOVersion() = transaction {
    println("-----------------")
    println("CRUD DAO Version")
    println("-----------------")
    addLogger(StdOutSqlLogger) // //

    val stPete = CityDAO.new {
        name = "St. Petersburg"
    }

    val munich = CityDAO.new {
        name = "Munich"
    }

    val a = UserDAO.new {
        name = "a"
        city = stPete
        age = 5
    }

    val b = UserDAO.new {
        name = "b"
        city = stPete
        age = 27
    }

    val c = UserDAO.new {
        name = "c"
        city = munich
        age = 42
    }

    println("Cities: ${CityDAO.all().joinToString {it.name}}")
    println("Users in ${stPete.name}: ${stPete.users.joinToString {it.name}}")
    println("Adults: ${UserDAO.find { UsersTable.age greaterEq 18 }.joinToString {it.name}}")

    println("Actualizando C")
    c.age = 23
    println(UserDAO.findById(c.id))


}
