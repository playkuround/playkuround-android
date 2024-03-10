package com.umc.playkuround.activity

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.umc.playkuround.databinding.ActivityMyPageBinding
import com.umc.playkuround.dialog.LoadingDialog
import com.umc.playkuround.dialog.LogoutDialog
import com.umc.playkuround.dialog.StoryDialog
import com.umc.playkuround.dialog.ThanksDialog
import com.umc.playkuround.network.ScoreAPI
import com.umc.playkuround.network.Top100Response
import com.umc.playkuround.network.UserAPI
import com.umc.playkuround.network.UserProfileResponse
import com.umc.playkuround.util.PlayKuApplication.Companion.user
import java.text.NumberFormat

class MyPageActivity : AppCompatActivity() {

    lateinit var binding : ActivityMyPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUserInfoView()
        initButtons()
    }

    private fun initButtons() {
        binding.myPageBackBtn.setOnClickListener {
            finish()
        }

        binding.myPageStoryBtn.setOnClickListener {
            val storyDialog = StoryDialog(this)
            storyDialog.show()
        }

        binding.myPageLogoutBtn.setOnClickListener {
            logout()
        }

        binding.myPageInstagramBtn.setOnClickListener {
            val instagramUsername = "playkuround_"
            val uri = Uri.parse("http://instagram.com/_u/$instagramUsername")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.instagram.android")
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                val webIntent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(webIntent)
            }
        }

        binding.myPageAdBtn.setOnClickListener {
            backdoor()
        }

        binding.myPageFeedbackBtn.setOnClickListener {
            val websiteUrl = "https://forms.gle/29YH9jz1XxajEFqr6"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }

        binding.myPageSendErrorBtn.setOnClickListener {
            val websiteUrl = "https://forms.gle/z6Dso4xtpJgJnTsm9"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
            startActivity(intent)
        }

        binding.myPageTermsBtn.setOnClickListener {
            val intent = Intent(this, DetailAgree01Activity::class.java)
            startActivity(intent)
        }

        binding.myPagePolicyBtn.setOnClickListener {
            val intent = Intent(this, DetailAgree02Activity::class.java)
            startActivity(intent)
        }

        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val appVersionName = packageInfo.versionName
        binding.myPageVersionTv.text = appVersionName
        binding.myPageVersionTitleTv.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("BGM - Prod. 브금공방\n\n<Back in my days>")
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun logout() {
        val logoutDialog = LogoutDialog(this)
        logoutDialog.setOnSelectListener(object : LogoutDialog.OnSelectListener {
            override fun yes() {
                val userAPI = UserAPI()
                userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
                    override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                        if(isSuccess) {
                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, "로그아웃에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }).logout(user.getAccessToken())
            }
        })
        logoutDialog.show()
    }

    private fun backdoor() {
        val thanksDialog = ThanksDialog(this)
        thanksDialog.show()

        val userAPI = UserAPI()
        userAPI.fakeDoor(user.getAccessToken())
    }

    @SuppressLint("SetTextI18n")
    private fun setUserInfoView() {
        binding.myPageNicknameTv.text = user.nickname + "님"
        binding.myPageMajorTv.text = user.major

        val loadingDialog = LoadingDialog(this)
        loadingDialog.show()

        val scoreAPI = ScoreAPI()
        scoreAPI.setOnResponseListener(object : ScoreAPI.OnResponseListener(){
            @SuppressLint("SetTextI18n")
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, errorLog: String) {
                if(isSuccess) {
                    if(body is Top100Response) {
                        val formatter = NumberFormat.getNumberInstance()
                        val score = "${formatter.format(body.responseData.myRank.score)}점"
                        val rank = "(${formatter.format(body.responseData.myRank.ranking)}위)"
                        if(body.responseData.myRank.ranking == 0)
                            binding.myPageNowScoreTv.text = "$score(-위)"
                        else
                            binding.myPageNowScoreTv.text = score + rank
                    }
                } else {
                    binding.myPageNowScoreTv.text = "0점(-위)"
                }
            }
        }).getTop100(user.getAccessToken())

        val userAPI = UserAPI()
        userAPI.setOnResponseListener(object : UserAPI.OnResponseListener() {
            override fun <T> getResponseBody(body: T, isSuccess: Boolean, err: String) {
                loadingDialog.dismiss()
                if(isSuccess) {
                    if(body is UserProfileResponse) {
                        val formatter = NumberFormat.getNumberInstance()
                        val score = "${formatter.format(body.response.highestScore)}점"
                        val rank = "(${formatter.format(body.response.highestRank)}위)"
                        if(body.response.highestRank == 0)
                            binding.myPageHighestScoreTv.text = "$score(-위)"
                        else
                            binding.myPageHighestScoreTv.text = score + rank
                    }
                } else {
                    binding.myPageHighestScoreTv.text = "0점(-위)"
                }
            }
        }).getUserInfo(user.getAccessToken())
    }

}