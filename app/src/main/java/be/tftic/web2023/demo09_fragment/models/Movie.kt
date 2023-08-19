package be.tftic.web2023.demo09_fragment.models

import java.time.LocalDate

data class Movie(
    val Id : Long,
    val Title : String,
    val Description : String?,
    val ReleaseYear : Int?
) {
    override fun toString(): String {
        return Title
    }
}
