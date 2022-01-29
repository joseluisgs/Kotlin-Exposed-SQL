package entities

import org.jetbrains.exposed.dao.id.IntIdTable

// Para hacer DSL solo necesitamos las tablas
// Tabla Usuario con un ID autoincrementable
object UsersDSL : IntIdTable() {
    val name = varchar("name", length = 50)
    val cityId = reference("city_id", CitiesDSL) // Clave Externa a la id de la tabla
}