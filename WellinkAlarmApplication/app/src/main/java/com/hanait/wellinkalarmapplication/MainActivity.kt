package com.hanait.wellinkalarmapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.afollestad.materialdialogs.MaterialDialog
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmActivity
import com.hanait.wellinkalarmapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainBtnStart.setOnClickListener(this)
    }

    //종료 메세지 다이어로그
    @Override
    override fun onKeyDown(keyCode:Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                val dialog = MaterialDialog(this, MaterialDialog.DEFAULT_BEHAVIOR)
                dialog.title(null, "알림")
                dialog.message(null, "어플리케이션을 종료하시겠습니까?", null)
                dialog.positiveButton(null, "예") {
                    finishAffinity()
                }
                dialog.negativeButton(null, "아니요") {}
                dialog.show()
                false
            }
            else -> false
        }
    }

    override fun onClick(v: View?) {
        when(v) {
            binding.mainBtnStart -> {
                val intent = Intent(this, SetAlarmActivity::class.java)
                startActivity(intent)
            }
        }
    }
}