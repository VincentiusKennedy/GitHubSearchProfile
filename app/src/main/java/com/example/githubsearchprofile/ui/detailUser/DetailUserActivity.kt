package com.example.githubsearchprofile.ui.detailUser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.githubsearchprofile.R
import com.example.githubsearchprofile.data.response.DetailUserResponse
import com.example.githubsearchprofile.databinding.ActivityDetailUserBinding
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {
    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.tab_follower,
            R.string.tab_following
        )
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var detailUserViewModel: DetailUserViewModel
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = intent.getStringExtra("login")
        detailUserViewModel = ViewModelProvider(
            this,
            DetailViewModelFactory.getInstance(this, username ?: "null")
        ).get(DetailUserViewModel::class.java)
        val actionbar = supportActionBar
        actionbar!!.title = "Detail GitHub User"
        actionbar.setDisplayHomeAsUpEnabled(true)

        sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailUserViewModel.getDetailUser()

        detailUserViewModel.isLoading.observe(this) {
            showLoading(false)
        }

        detailUserViewModel.detailUser.observe(this) {
            getDetailUserData(it)
        }
    }

    private fun getDetailUserData(detailUser: DetailUserResponse) {
        val location = detailUser.location ?: "User Belum Menambahkan Lokasi"
        Glide.with(this)
            .load(detailUser.avatarUrl)
            .into(binding.ivUserDetail)
        binding.tvUserNameDetail.text = detailUser.name
        binding.tvUserLoginDetail.text = detailUser.login
        binding.tvFollowersCount.text = formatCount(detailUser.followers!!)
        binding.tvFollowingCount.text = formatCount(detailUser.following!!)
        binding.tvRepositoriesCount.text = detailUser.publicRepos.toString()
        binding.tvUserLocationDetail.text = location
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.loadingBar.visibility = View.VISIBLE
        } else {
            binding.loadingBar.visibility = View.GONE
        }
    }

    private fun formatCount(count: Int): String {
        return when {
            count >= 1000 -> {
                val countInK = count / 1000
                "${countInK}k"
            }

            else -> count.toString()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
