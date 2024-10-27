package kz.app.roomretrofit

import kotlinx.coroutines.runBlocking
import kz.app.roomretrofit.data.network.MovieApiData
import kz.app.roomretrofit.data.network.MovieApiService
import kz.app.roomretrofit.data.network.MovieResponse
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ApiServiceTest {

    @Mock
    private lateinit var apiService: MovieApiService

    @Test
    fun `test getTopRatedMovies returns correct response`() = runBlocking {
        val mockMoviesResponse = MovieResponse(
            results = listOf(
                MovieApiData(
                    id = 1,
                    title = "Movie Title",
                    overview = "Movie Overview",
                    voteAverage = 8.5,
                    releaseDate = "2024-01-01",
                    posterPath = "/poster-path"
                )
            )
        )

        `when`(apiService.getTopRatedMovies()).thenReturn(mockMoviesResponse)

        val response = apiService.getTopRatedMovies()

        assertEquals(1, response.results.size)
        assertEquals("Movie Title", response.results.first().title)
//        assertEquals(2, response.results.first().id) // FAKE FAIL TEST
    }

    @Test
    fun `test getTopRatedMovies returns empty list`() = runBlocking {
        val mockMoviesResponse = MovieResponse(results = emptyList())

        `when`(apiService.getTopRatedMovies()).thenReturn(mockMoviesResponse)

        val response = apiService.getTopRatedMovies()

        assertEquals(0, response.results.size)
    }
}
