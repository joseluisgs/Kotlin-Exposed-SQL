package entities

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

// Tabla Usuario con un ID autoincrementable
object Users : IntIdTable() {
    val name = varchar("name", length = 50)
    val cityId = reference("city_id", Cities) // Clave Externa a la id de la tabla
}