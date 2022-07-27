package com.hanait.wellinkalarmapplication.home

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanait.wellinkalarmapplication.MainActivity
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.SetUserNameActivity
import com.hanait.wellinkalarmapplication.databinding.ActivityHomeAccountBinding
import com.hanait.wellinkalarmapplication.databinding.ActivitySetAlarmBinding
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.model.Item
import com.hanait.wellinkalarmapplication.model.SearchData
import com.hanait.wellinkalarmapplication.receiver.AlarmReceiver
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmActivity
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmNameFragment
import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.prefs
import com.hanait.wellinkalarmapplication.utils.Constants.userName
import com.hanait.wellinkalarmapplication.utils.CustomDialogFragment
import java.util.*

class HomeAccountActivity : AppCompatActivity(), View.OnClickListener{


    private lateinit var binding : ActivityHomeAccountBinding
    private lateinit var likeList: ArrayList<Item>

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeAccountBtnReset.setOnClickListener(this)
        binding.homeAccountBtnRename.setOnClickListener(this)
        binding.homeAccountAddAlarm.setOnClickListener(this)

        //등록된 약 정보 가져오기
        likeList = DatabaseManager.getInstance(this, "Alarms.db").selectLikeAll()

        setTextAlarmCountAndExplain()
        recyclerViewCreate()

        //타이틀 사용자 이름으로 설정
        Log.d("로그", "HomeAccountActivity - onCreate :  유저 네임 $userName")
        binding.homeAccountTextViewTitle.text = "${userName}님의 정보"

        //toolbar 표시
        setSupportActionBar(binding.homeAccountToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
    }

    private fun recyclerViewCreate() {
        val likeView = binding.homeAccountRecyclerView
        val likeAdapter = LikeAdapter(this, likeList)
        likeAdapter.setOnItemClickListener(
            object : LikeAdapter.OnItemClickListener {
                override fun onItemClick(v: View, pos: Int) {
                    //상세 정보 보기
                    Log.d("로그", "HomeAccountFragment - recyclerViewCreate : ${likeList[pos - 1]}")
                    val intent = Intent(applicationContext, SearchDetailActivity::class.java)
                    intent.putExtra("SearchData", likeList[pos - 1] )
                    startActivity(intent)
                }
                @SuppressLint("NotifyDataSetChanged")
                override fun onDeleteItem(v: View, pos: Int) {
                    DatabaseManager.getInstance(applicationContext, "Alarms.db").deleteLike(likeList[pos - 1].itemSeq!!)
                    likeList.removeAt(pos - 1)
                    setTextAlarmCountAndExplain()
                    likeAdapter.notifyDataSetChanged()
                    recyclerViewCreate()
                }
            })
        val layoutManager = LinearLayoutManager(this)
        likeView.layoutManager = layoutManager
        likeView.adapter = likeAdapter
    }

    //등록 약물 갯수 및 상단 설명 글 수정
    @SuppressLint("SetTextI18n")
    fun  setTextAlarmCountAndExplain() {
        binding.homeAccountTextViewLikeCount.text = "${likeList.size}개"
        if(likeList.size != 0) {
            binding.homeAccountTextViewExplain.text = "아래 약/약물이 등록되어 있어요"
            return
        }
        binding.homeAccountTextViewExplain.text = "등록된 약/약물이 없어요"
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.homeAccountBtnReset -> {
                showDialog()
            }
            binding.homeAccountBtnRename -> {
                //이름 초기화

                //메인 화면으로 이동
                val intent = Intent(applicationContext, SetUserNameActivity::class.java)
                intent.putExtra("SkipSetAlarm", true)
                startActivity(intent)
                finish()
            }
            binding.homeAccountAddAlarm -> {
                val intent = Intent(applicationContext, HomeActivity::class.java)
                intent.putExtra("GoToSearchFragment", true)
                startActivity(intent)
                finish()
            }
        }
    }

    //toolbar 클릭 리스너
    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog() {
        val customDialog = CustomDialogFragment(R.layout.home_account_reset_dialog, null)
        customDialog.setDialogListener(object: CustomDialogFragment.AccountResetListener {
            override fun onPositiveClicked() {
                //이름 초기화
                prefs.setString("user_name", "")
                //복용 정보 초기화
                DatabaseManager.getInstance(applicationContext, "Alarms.db").resetCalendar()
                //알람 데이터 초기화
                DatabaseManager.getInstance(applicationContext, "Alarms.db").resetAlarm()

                //등록 약 데이터 초기화
                DatabaseManager.getInstance(applicationContext, "Alarms.db").resetLike()

                Toast.makeText(applicationContext, "데이터가 초기화되었습니다.", Toast.LENGTH_SHORT).show()
                
                //메인 화면으로 이동
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onNegativeClicked() {
                customDialog.dismiss()
            }
        })
        fragmentManager?.let { customDialog.show(supportFragmentManager, "home_calendar_dialog") }
    }
}