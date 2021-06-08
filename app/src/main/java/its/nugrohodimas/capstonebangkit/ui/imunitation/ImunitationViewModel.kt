package its.nugrohodimas.capstonebangkit.ui.imunitation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import its.nugrohodimas.core.domain.usecase.KiaUseCase

class ImunitationViewModel(private val kiaUseCase: KiaUseCase) : ViewModel() {
    val date =kiaUseCase.getDateVaccine().asLiveData()
}