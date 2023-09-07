package com.example.githubsearchprofile.ui.follow

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubsearchprofile.data.response.UserItems
import com.example.githubsearchprofile.databinding.FragmentFollowBinding
import com.example.githubsearchprofile.ui.detailUser.DetailUserActivity
import com.example.githubsearchprofile.ui.detailUser.DetailUserViewModel
import com.example.githubsearchprofile.ui.listUser.UserAdapter

class FollowFragment : Fragment() {
    companion object {
        const val ARG_POSITION = "POSITION"
        const val ARG_USERNAME = "USERNAME"
        private const val TAG = "FollowFragment"
    }

    private var position: Int = 0
    private lateinit var binding: FragmentFollowBinding
    private lateinit var followViewModel : DetailUserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        followViewModel = ViewModelProvider(requireActivity()).get(DetailUserViewModel::class.java)
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(activity, layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        followViewModel.userFollower.observe(viewLifecycleOwner) { userFollow ->
            showFollowUser(userFollow)
        }

        followViewModel.userFollowing.observe(viewLifecycleOwner) { userFollow ->
            showFollowUser(userFollow)
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun showFollowUser(userFollow: List<UserItems>) {
        val adapter = UserAdapter(userFollow)
        binding.rvFollow.adapter = adapter
        adapter.setOnUserItemClickListener(object : UserAdapter.OnUserItemClickListener {
            override fun onUserItemClick(username: String) {
                val intent = Intent(requireActivity(), DetailUserActivity::class.java)
                intent.putExtra("login", username)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        arguments?.let {
            position = it.getInt(ARG_POSITION)
            if (position == 1) {
                if (followViewModel.userFollower.value.isNullOrEmpty()){
                    followViewModel.getFollowerUser()
                } else {
                    showFollowUser(followViewModel.userFollower.value!!)
                }
            } else {
                if (followViewModel.userFollowing.value.isNullOrEmpty()){
                    followViewModel.getFollowingUser()
                } else {
                    showFollowUser(followViewModel.userFollowing.value!!)
                }
            }
        }
    }

}