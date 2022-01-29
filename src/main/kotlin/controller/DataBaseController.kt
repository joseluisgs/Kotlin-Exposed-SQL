package controller

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import dao.CitiesTable
import dao.UsersTable
import entities.CitiesDSL
import entities.UsersDSL
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object DataBaseController {
    fun init() {
        // Aplicamos Hiraki para la conexión a la base de datos
        val config = HikariConfig().apply {
            jdbcUrl         = "jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;"
            driverClassName = "org.h2.Driver"
            username        = "sa"
            password        = ""
            maximumPoolSize = 10
        }
        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)
        println("Database connected")
        createTables()
    }

    private fun createTables() = transaction {
        addLogger(StdOutSqlLogger) // Para que se vea el log de consulas a la base de datos
        SchemaUtils.create(UsersDSL, CitiesDSL, UsersTable, CitiesTable)
        println("Tables created")
    }
}