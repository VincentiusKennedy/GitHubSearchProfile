package com.example.githubsearchprofile.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearchprofile.data.response.UserItems
import com.example.githubsearchprofile.databinding.ActivityMainBinding
import com.example.githubsearchprofile.ui.detailUser.DetailUserActivity
import com.example.githubsearchprofile.ui.listUser.UserAdapter
import com.example.githubsearchprofile.ui.listUser.UserViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.listUser.observe(this) { userList ->
            showRecyclerList(userList)
        }

        userViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        val userAdapter = UserAdapter(emptyList())
        binding.rvUser.adapter = userAdapter

        userViewModel.snackBarText.observe(this) { showSnackBar ->
            if (showSnackBar) {
                val snackbar = Snackbar.make(
                    binding.root,
                    "User Not Found",
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
                userViewModel.resetSnackbar()
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { textView, actionId, event ->
                searchBar.text = searchView.text
                searchView.hide()
                userViewModel.searchUser(searchView.text.toString())
                false
            }
        }
    }

    private fun showRecyclerList(userList: List<UserItems>) {
        val userAdapter = UserAdapter(userList)
        binding.rvUser.adapter = userAdapter

        userAdapter.setOnUserItemClickListener(object : UserAdapter.OnUserItemClickListener{
            override fun onUserItemClick(username: String) {
                val intent = Intent(this@MainActivity, DetailUserActivity::class.java)
                intent.putExtra("login", username)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}