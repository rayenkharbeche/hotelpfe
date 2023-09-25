import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapplication.Repository.CommandeRepository
import com.example.hotelapplication.data.RequestStatus
import com.example.hotelapplication.model.Command
import kotlinx.coroutines.launch

class CommandeViewModel(val commandeRepository: CommandeRepository, val application: Application) : ViewModel() {
    private var isLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { value = false }
    private var errorMessage: MutableLiveData<HashMap<String, String>> = MutableLiveData()

    private val _commandCreated = MutableLiveData<Command?>()
    val commandCreated: LiveData<Command?> get() = _commandCreated


    fun createCommand(body: Command) {
        viewModelScope.launch {
            commandeRepository.createCommand(body).collect {
                when (it) {
                    is RequestStatus.Waiting -> {
                        isLoading.value = true
                    }
                    is RequestStatus.Success -> {
                        isLoading.value = false
                        Log.d("CommandeViewModel", "Command created successfully: ${it.data}")
                        _commandCreated.value = body // Emit the CommandResponse object.
                    }
                    is RequestStatus.Error -> {
                        isLoading.value = false
                        errorMessage.value = it.message
                    }
                }
            }
        }

}




/*  private val repository = CommandeRepository()

    private val _commandCreated = MutableLiveData<Boolean>()
    val commandCreated: LiveData<Boolean> get() = _commandCreated

    fun createCommand(command: Command) {
        viewModelScope.launch {
            try {
                repository.createCommand(command)
                _commandCreated.value = true
            } catch (e: Exception) {
                _commandCreated.value = false

            }
        }
    }*/
}
