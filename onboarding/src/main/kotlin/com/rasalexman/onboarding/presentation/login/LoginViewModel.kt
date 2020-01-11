package com.rasalexman.onboarding.presentation.login

import androidx.core.util.PatternsCompat
import androidx.lifecycle.*
import com.mincor.kodi.core.immutableInstance
import com.rasalexman.core.common.extensions.applyForType
import com.rasalexman.core.common.extensions.loadingResult
import com.rasalexman.core.common.extensions.unsafeLazy
import com.rasalexman.core.data.dto.SEvent
import com.rasalexman.core.data.dto.SResult
import com.rasalexman.core.presentation.viewModels.BaseViewModel
import com.rasalexman.coroutinesmanager.CoroutinesProvider
import com.rasalexman.models.inline.toUserEmail
import com.rasalexman.models.inline.toUserPassword
import com.rasalexman.onboarding.R
import com.rasalexman.onboarding.data.SignInEventModel
import com.rasalexman.onboarding.domain.CheckUserRegisteredUseCase

class LoginViewModel : BaseViewModel() {

    private val checkUserRegisteredUseCase: CheckUserRegisteredUseCase by immutableInstance()
    private val loginLiveData by unsafeLazy { MutableLiveData<SignInEventModel>() }

    val emailValidationError by unsafeLazy {
        MutableLiveData<Int>()
    }

    val passwordValidationError by unsafeLazy {
        MutableLiveData<Int>()
    }

    val email by unsafeLazy {
        MutableLiveData<String>()
    }

    val password by unsafeLazy {
        MutableLiveData<String>()
    }

    override val resultLiveData: LiveData<SResult<Boolean>> by unsafeLazy {
        loginLiveData.switchMap {
            liveData(viewModelScope.coroutineContext + CoroutinesProvider.IO) {
                emit(loadingResult())
                emit(checkUserRegisteredUseCase.execute(it))
            }
        }
    }

    override fun processViewEvent(viewEvent: SEvent) {
        viewEvent.applyForType(::validate)
    }

    private fun validate(viewEvent: SEvent.Fetch) {
        val emailToValidate = email.value.orEmpty()
        val passwordToValidate = password.value.orEmpty()

        if(!PatternsCompat.EMAIL_ADDRESS.matcher(emailToValidate).matches()) {
            emailValidationError.value = R.string.error_email_match
        } else if(passwordToValidate.isEmpty()) {
            passwordValidationError.value = R.string.error_password_empty
        } else {
            loginLiveData.value = SignInEventModel(emailToValidate.toUserEmail(), passwordToValidate.toUserPassword())
        }
    }
}