package com.example.githubsearchprofile.ui.listUser

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearchprofile.data.response.UserItems
import com.example.githubsearchprofile.data.response.UserResponse
import com.example.githubsearchprofile.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {
    companion object {
        private const val TAG = "UserViewModel"
        private const val EXTRA_NAME = "arif"
    }

    private val _listUser = MutableLiveData<List<UserItems>>()
    val listUser: LiveData<List<UserItems>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _snackBarText = MutableLiveData<Boolean>()
    val snackBarText: LiveData<Boolean> = _snackBarText

    init {
        searchUser(EXTRA_NAME)
        _snackBarText.value = false
    }

    fun searchUser(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getGitHubUser(query)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: Response<UserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val totalCount = response.body()?.totalCount ?: 0
                    if (totalCount > 0) {
                        _listUser.value = response.body()?.items ?: emptyList()
                    } else {
                        _snackBarText.value = true
                    }
                } else {
                    Log.e(TAG, "Failure ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun resetSnackbar() {
        _snackBarText.value = false
    }
}