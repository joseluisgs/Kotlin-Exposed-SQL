import entities.CitiesDSL
import entities.UsersDSL
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun DSLVersion() = transaction {
    println("-----------------")
    println("CRUD DSL Version")
    println("-----------------")
    addLogger(StdOutSqlLogger) // // Para que se vea el log de consulas a la base de datos

    // Create 3 formas
    val saintPetersburgId = CitiesDSL.insert {
        it[name] = "St. Petersburg"
    } get CitiesDSL.id

    val munichId = CitiesDSL.insertAndGetId {
        it[name] = "Munich"
    }
    val pragueId = CitiesDSL.insert {
        it.update(name, stringLiteral("   Prague   ").trim().substring(1, 2))
    }[CitiesDSL.id]

    // Read
    val pragueName = CitiesDSL.select { CitiesDSL.id eq pragueId }.single()[CitiesDSL.name]
    println("Prague name: $pragueName")

    println("All cities:")

    for (city in CitiesDSL.selectAll()) {
        println("${city[CitiesDSL.id]}: ${city[CitiesDSL.name]}")
    }

    // Insertamos usuarios
    UsersDSL.insert {
        it[name] = "Andrey"
        it[UsersDSL.cityId] = saintPetersburgId
    }

    UsersDSL.insert {
        it[name] = "Sergey"
        it[UsersDSL.cityId] = munichId
    }

    UsersDSL.insert {
        it[name] = "Eugene"
        it[UsersDSL.cityId] = munichId
    }

    val alexId = UsersDSL.insertAndGetId {
        it[name] = "Alex"
        it[UsersDSL.cityId] = pragueId
    }

    UsersDSL.insert {
        it[name] = "Something"
        it[UsersDSL.cityId] = pragueId
    }

    println("All users sin join:")
    for (user in UsersDSL.selectAll()) {
        println("${user[UsersDSL.id]}: ${user[UsersDSL.name]} from ${user[UsersDSL.cityId]}")
    }

    println("Datos de Alex")
    var alex = UsersDSL.select { UsersDSL.id eq alexId }.single()
    println("${alex[UsersDSL.id]}: ${alex[UsersDSL.name]} from ${alex[UsersDSL.cityId]}")

    // Actualizamos
    UsersDSL.update({ UsersDSL.id eq alexId }) {
        it[name] = "Alexey"
    }
    println("Datos de Alex actualizados")
    alex = UsersDSL.select { UsersDSL.id eq alexId }.single()
    println("${alex[UsersDSL.id]}: ${alex[UsersDSL.name]} from ${alex[UsersDSL.cityId]}")

    println("Borramos a quien tenga \"%thing\"")
    UsersDSL.deleteWhere{ UsersDSL.name like "%thing"}

    println("All users sin join:")
    for (user in UsersDSL.selectAll()) {
        println("${user[UsersDSL.id]}: ${user[UsersDSL.name]} from ${user[UsersDSL.cityId]}")
    }

    println("Join with foreign key ¿Quien vive en St. Ptesburg?:")

    (UsersDSL innerJoin CitiesDSL).slice(UsersDSL.name, UsersDSL.cityId, CitiesDSL.name).
    select { CitiesDSL.name.eq("St. Petersburg") or UsersDSL.cityId.isNull()}.forEach {
        if (it[UsersDSL.cityId] != null) {
            println("${it[UsersDSL.name]} lives in ${it[CitiesDSL.name]}")
        }
        else {
            println("${it[UsersDSL.name]} lives nowhere")
        }
    }

    println("Join with foreign key ¿Donde viven los usuarios?:")
    (UsersDSL innerJoin CitiesDSL).slice(UsersDSL.name, UsersDSL.cityId, CitiesDSL.name)
        .selectAll()
        .forEach {
            println("${it[UsersDSL.name]} lives in ${it[CitiesDSL.name]}")
        }


}