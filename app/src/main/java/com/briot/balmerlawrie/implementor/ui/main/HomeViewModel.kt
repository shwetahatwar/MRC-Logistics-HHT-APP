package com.briot.balmerlawrie.implementor.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.briot.balmerlawrie.implementor.repository.remote.RemoteRepository
//import com.briot.balmerlawrie.implementor.repository.remote.RoleAccessRelation
import java.net.SocketException
import java.net.SocketTimeoutException

class HomeViewModel : ViewModel() {
    val TAG = "HomeViewModel"

    val networkError: LiveData<Boolean> = MutableLiveData<Boolean>()

}
