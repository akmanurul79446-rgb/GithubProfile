package id.ac.polbeng.akmanurul.githubprofile.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.request.RequestOptions
import id.ac.polbeng.akmanurul.githubprofile.R
import id.ac.polbeng.akmanurul.githubprofile.databinding.ActivityMainBinding
import id.ac.polbeng.akmanurul.githubprofile.helpers.Config
import id.ac.polbeng.akmanurul.githubprofile.models.GithubUser
import id.ac.polbeng.akmanurul.githubprofile.viewmodels.MainViewModel
import id.ac.polbeng.akmanurul.githubprofile.GlideApp

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        mainViewModel.githubUser.observe(this) { user ->
            setUserData(user)
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.btnSearchUserLogin.setOnClickListener {
            val userLogin = binding.etSearchUserLogin.text.toString().trim()

            val query = if (userLogin.isNotEmpty()) {
                userLogin
            } else {
                Config.DEFAULT_USER_LOGIN
            }

            mainViewModel.searchUser(query)
        }
    }

    private fun setUserData(githubUser: GithubUser) {
        binding.tvUser.text = githubUser.toString()

        GlideApp.with(this)
            .load(githubUser.avatarUrl)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.ic_baseline_image_24)
                    .error(R.drawable.ic_baseline_broken_image_24)
            )
            .into(binding.imgUser)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility =
            if (isLoading) View.VISIBLE else View.GONE
    }
}
