package dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

// Para hacer el DAO necesitamos la tabla y la clase que la mapea
object CitiesTable: IntIdTable() {
    val name = varchar("name", 50)
}

class CityDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CityDAO>(CitiesTable)

    var name by CitiesTable.name
    val users by UserDAO referrersOn UsersTable.city
}