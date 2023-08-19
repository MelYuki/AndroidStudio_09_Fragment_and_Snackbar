package be.tftic.web2023.demo09_fragment.services

import be.tftic.web2023.demo09_fragment.models.Movie

class MovieService {

    companion object {
        // Fake Database -> En vrai , utilisez SQLite
        private val movies : MutableList<Movie> = mutableListOf(
            Movie(1, "Avengers: Endgame", null, 2019),
            Movie(2, "Scarface", "S'inspirant directement de la vie d'Al Capone, le film est immédiatement victime de violentes critiques qui lui reprochent la glorification du gangster. Il ne peut sortir que deux ans après la fin du tournage avec un certain nombre de modifications, ce qui ne l'empêche pas de participer très largement à la mythification du parrain de l'Outfit de Chicago dont la légende dépasse la réalité", 1932),
            Movie(3, "Hot Shots!", "Le lieutenant Sean « Topper » Harley est un pilote de chasse talentueux. Il est cependant complexé par le souvenir de son père Buzz qui jadis aurait été responsable de la mort de son coéquipier lors d'une mission aérienne. Retiré du monde dans une réserve indienne, il est recruté par le commandant Block pour une délicate mission au Proche-Orient.", 1991)
        )
    }

    fun getAll() : List<Movie> {
        return movies.toList()
    }

    fun getDetail(movieId : Long) : Movie? {
        val movie =  movies.singleOrNull { movie -> movie.Id == movieId }
        return movie
    }

    fun delete(movieId: Long) : Boolean {
        return movies.removeIf { movie -> movie.Id == movieId}
    }

}