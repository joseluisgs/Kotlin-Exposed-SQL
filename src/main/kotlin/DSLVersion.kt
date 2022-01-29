import entities.Cities
import entities.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun DSLVersion() = transaction {
    println("-----------------")
    println("CRUD DSL Version")
    println("-----------------")
    addLogger(StdOutSqlLogger) // // Para que se vea el log de consulas a la base de datos

    // Create 3 formas
    val saintPetersburgId = Cities.insert {
        it[name] = "St. Petersburg"
    } get Cities.id

    val munichId = Cities.insertAndGetId {
        it[name] = "Munich"
    }
    val pragueId = Cities.insert {
        it.update(name, stringLiteral("   Prague   ").trim().substring(1, 2))
    }[Cities.id]

    // Read
    val pragueName = Cities.select { Cities.id eq pragueId }.single()[Cities.name]
    println("Prague name: $pragueName")

    println("All cities:")

    for (city in Cities.selectAll()) {
        println("${city[Cities.id]}: ${city[Cities.name]}")
    }

    // Insertamos usuarios
    Users.insert {
        it[name] = "Andrey"
        it[Users.cityId] = saintPetersburgId
    }

    Users.insert {
        it[name] = "Sergey"
        it[Users.cityId] = munichId
    }

    Users.insert {
        it[name] = "Eugene"
        it[Users.cityId] = munichId
    }

    val alexId = Users.insertAndGetId {
        it[name] = "Alex"
        it[Users.cityId] = pragueId
    }

    Users.insert {
        it[name] = "Something"
        it[Users.cityId] = pragueId
    }

    println("All users sin join:")
    for (user in Users.selectAll()) {
        println("${user[Users.id]}: ${user[Users.name]} from ${user[Users.cityId]}")
    }

    println("Datos de Alex")
    var alex = Users.select { Users.id eq alexId }.single()
    println("${alex[Users.id]}: ${alex[Users.name]} from ${alex[Users.cityId]}")

    // Actualizamos
    Users.update({ Users.id eq alexId }) {
        it[name] = "Alexey"
    }
    println("Datos de Alex actualizados")
    alex = Users.select { Users.id eq alexId }.single()
    println("${alex[Users.id]}: ${alex[Users.name]} from ${alex[Users.cityId]}")

    println("Borramos a quien tenga \"%thing\"")
    Users.deleteWhere{ Users.name like "%thing"}

    println("All users sin join:")
    for (user in Users.selectAll()) {
        println("${user[Users.id]}: ${user[Users.name]} from ${user[Users.cityId]}")
    }

    println("Join with foreign key ¿Quien vive en St. Ptesburg?:")

    (Users innerJoin Cities).slice(Users.name, Users.cityId, Cities.name).
    select { Cities.name.eq("St. Petersburg") or Users.cityId.isNull()}.forEach {
        if (it[Users.cityId] != null) {
            println("${it[Users.name]} lives in ${it[Cities.name]}")
        }
        else {
            println("${it[Users.name]} lives nowhere")
        }
    }

    println("Join with foreign key ¿Donde viven los usuarios?:")
    (Users innerJoin Cities).slice(Users.name, Users.cityId, Cities.name)
        .selectAll()
        .forEach {
            println("${it[Users.name]} lives in ${it[Cities.name]}")
        }


}