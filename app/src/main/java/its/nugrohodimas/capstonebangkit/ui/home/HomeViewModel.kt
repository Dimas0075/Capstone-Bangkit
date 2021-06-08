package its.nugrohodimas.capstonebangkit.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import its.nugrohodimas.core.domain.usecase.KiaUseCase

class HomeViewModel(kiaUseCase: KiaUseCase) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}