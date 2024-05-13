import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.betteryesterday.data.PrefsDataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>()

    // Expose Flow as StateFlow for Composable observation
    private val _showSettings = MutableStateFlow(false)
    val showSettings: Flow<Boolean> get() = _showSettings

    private val _savedTime = MutableStateFlow(System.currentTimeMillis())
    val savedTime: Flow<Long> get() = _savedTime

    init {
        // Initialize values from DataStore
        viewModelScope.launch {
            PrefsDataStoreManager.getNotificationToggle(context).collect {
                _showSettings.value = it
            }
        }

        viewModelScope.launch {
            PrefsDataStoreManager.getSavedTimeFlow(context).collect {
                _savedTime.value = it
            }
        }
    }

    suspend fun saveTime(time: Long) {
        PrefsDataStoreManager.saveSelectedTime(context, time)
        _savedTime.value = time
    }

    suspend fun saveNotificationToggle(enabled: Boolean) {
        PrefsDataStoreManager.saveNotificationToggle(context, enabled)
        _showSettings.value = enabled
    }
}
