import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Game
import kotlinx.coroutines.launch
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


interface ApiService {
    @GET("riddle")
    suspend  fun getRiddles(): List<Game>

    @GET("id")
    suspend fun getRiddleById(@Path("id") id: Int): Game

    class RiddleViewModel(private val apiService: ApiService) : ViewModel() {
        private val _riddles = MutableLiveData<List<Game>>()
        val riddles: LiveData<List<Game>> = _riddles

        fun fetchRiddles() {
            viewModelScope.launch {
                try {
                    val riddles = apiService.getRiddles()
                    _riddles.value = riddles
                } catch (e: Exception) {
                    // Handle the error
                }
            }
        }
    }
}

