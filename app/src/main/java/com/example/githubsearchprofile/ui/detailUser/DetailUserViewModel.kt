package com.example.githubsearchprofile.ui.detailUser

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubsearchprofile.data.response.DetailUserResponse
import com.example.githubsearchprofile.data.response.UserItems
import com.example.githubsearchprofile.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(private val selectedUser: String) : ViewModel() {
    companion object {
        private const val TAG = "DetailUserViewModel"
    }

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDetailUser() {
        if (selectedUser != null) {
            _isLoading.value = true
            val client = ApiConfig.getApiService().getDetailUser(selectedUser)
            client.enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _detailUser.value = response.body()
                    } else {
                        Log.e(TAG, "Failure ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

    private val _userFollower = MutableLiveData<List<UserItems>>()
    val userFollower: LiveData<List<UserItems>> = _userFollower

    fun getFollowerUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(selectedUser)
        client.enqueue(object : Callback<List<UserItems>> {
            override fun onResponse(
                call: Call<List<UserItems>>,
                response: Response<List<UserItems>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _userFollower.value = response.body()
                }
            }
            override fun onFailure(call: Call<List<UserItems>>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
    private val _userFollowing = MutableLiveData<List<UserItems>>()
    val userFollowing: LiveData<List<UserItems>> = _userFollowing


    fun getFollowingUser() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowing(selectedUser)
        client.enqueue(object : Callback<List<UserItems>> {
            override fun onResponse(
                call: Call<List<UserItems>>,
                response: Response<List<UserItems>>
            ) {
                _isLoading.value = false

                if (response.isSuccessful) {
                    _userFollowing.value = response.body()
                }
            }
            override fun onFailure(call: Call<List<UserItems>>, t: Throwable) {
                _isLoading.value = false
            }
        })
    }
}

class DetailViewModelFactory private constructor(
    private val selectedUser: String
) :
    ViewModelProvider.Factory{

    companion object {
        @Volatile
        private var instance: DetailViewModelFactory? = null

        fun getInstance(context: Context, selectedUser: String): DetailViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: DetailViewModelFactory(
                    selectedUser
                )
            }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(DetailUserViewModel::class.java)->{
                DetailUserViewModel(selectedUser) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}
