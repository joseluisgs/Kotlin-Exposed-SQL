package dao

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


// Para hacer el DAO necesitamos la tabla y la clase que la mapea
object UsersTable : IntIdTable() {
    val name = varchar("name", 50).index()
    val city = reference("city", CitiesTable)
    val age = integer("age")
}

class UserDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserDAO>(UsersTable)

    var name by UsersTable.name
    var city by CityDAO referencedOn UsersTable.city
    var age by UsersTable.age

    override fun toString(): String {
        return "UserDAO(name='$name', city=${city.name}, age=$age)"
    }


}