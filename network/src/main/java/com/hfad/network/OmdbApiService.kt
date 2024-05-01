
import com.hfad.network.OmdbMovie
import retrofit2.http.GET
import retrofit2.http.Query


interface OmdbApiService {
    @GET("/")
    suspend fun searchByTitle(
        @Query("t") title: String,
        @Query("y") year: String? = null,
        @Query("plot") plot: String? = null,
        @Query("r") format: String? = null,
        @Query("apikey") apiKey: String
    ): OmdbMovie
}