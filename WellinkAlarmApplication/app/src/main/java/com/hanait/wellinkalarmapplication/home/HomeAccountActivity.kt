package com.hanait.wellinkalarmapplication.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hanait.wellinkalarmapplication.MainActivity
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.SetUserNameActivity
import com.hanait.wellinkalarmapplication.databinding.ActivityHomeAccountBinding
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.Item
import com.hanait.wellinkalarmapplication.utils.Constants.isMediaOn
import com.hanait.wellinkalarmapplication.utils.Constants.isVibrationOn
import com.hanait.wellinkalarmapplication.utils.Constants.prefs
import com.hanait.wellinkalarmapplication.utils.Constants.userName
import com.hanait.wellinkalarmapplication.utils.CustomDialogFragment

class HomeAccountActivity : AppCompatActivity(), View.OnClickListener, CompoundButton.OnCheckedChangeListener{


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
        binding.homeAccountSwitchMedia.setOnCheckedChangeListener(this)
        binding.homeAccountSwitchVibration.setOnCheckedChangeListener(this)


        //벨소리 진동 스위치 셋팅
        binding.homeAccountSwitchMedia.isChecked = isMediaOn == "1"
        binding.homeAccountSwitchVibration.isChecked = isVibrationOn == "1"

        //등록된 약 정보 가져오기
        likeList = DatabaseManager.getInstance(this, "Alarms.db").selectLikeAll()

        setTextAlarmCountAndExplain()
        recyclerViewCreate()

        //toolbar 표시
        setSupportActionBar(binding.homeAccountToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "설정"
    }

    private fun recyclerViewCreate() {
        val likeView = binding.homeAccountRecyclerView
        val likeAdapter = LikeAdapter(this, likeList)
        likeAdapter.setOnItemClickListener(
            object : LikeAdapter.OnItemClickListener {
                override fun onItemClick(v: View, pos: Int) {
                    //상세 정보 보기
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

    override fun onCheckedChanged(v: CompoundButton?, isChecked: Boolean) {
        when(v) {
            binding.homeAccountSwitchMedia -> {
                if(isChecked) {
                    isMediaOn = "1"
                    prefs.setString("media_on", "1")
                } else {
                    isMediaOn = "0"
                    prefs.setString("media_on", "0")
                }
            }
            binding.homeAccountSwitchVibration -> {
                if(isChecked) {
                    isVibrationOn = "1"
                    prefs.setString("vibration_on", "1")
                } else {
                    isVibrationOn = "0"
                    prefs.setString("vibration_on", "0")
                }
            }
        }
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