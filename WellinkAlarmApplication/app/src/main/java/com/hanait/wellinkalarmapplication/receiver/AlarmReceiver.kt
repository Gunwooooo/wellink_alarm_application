package com.hanait.wellinkalarmapplication.receiver

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Build
import android.os.Handler
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.AlarmData

import com.hanait.wellinkalarmapplication.service.AlarmService
import com.hanait.wellinkalarmapplication.setAlarm.SetAlarmPopupActivity

import com.hanait.wellinkalarmapplication.utils.Constants
import com.hanait.wellinkalarmapplication.utils.Constants.ADD_INTENT
import com.hanait.wellinkalarmapplication.utils.Constants.OFF_INTENT
import java.text.SimpleDateFormat
import java.util.*

class AlarmReceiver : BroadcastReceiver(){
    var context : Context? = null
    lateinit var alarmData: AlarmData
    private lateinit var mediaPlayer: MediaPlayer

    override fun onReceive(context: Context?, intent: Intent?) {
        this.context = context
        Log.d("로그", "AlarmReceiver - onReceive : 리시버 호출됨")
        val pendingId = intent?.extras?.getInt("PendingId")!!
        var takenFlag = intent.extras?.getBoolean("takenFlag")
        // this hold information to service
        val intentToService = Intent(context, AlarmService::class.java)
        try {
            val intentType = intent.extras?.getString("intentType")
            when(intentType) {
                ADD_INTENT -> {
                    //데이터 전달받기

                    Log.d("로그", "AlarmReceiver - onReceive : pendingId : $pendingId")
                    alarmData = DatabaseManager.getInstance(this.context!!, "Alarms.db").selectAlarmAsId(pendingId / 4)!!

                    val cal = Calendar.getInstance()
                    val strDate = cal.time.let { Constants.sdf.format(it) }

                    //알람 주기 및 만기일 체크 후 울리게 하기
                    //DB에서 해당하는 약 데이터 가져오기
                    val mAlarmList: ArrayList<AlarmData>
                    mAlarmList = context?.let { DatabaseManager.getInstance(it, "Alarms.db").selectCalendarItemAlarm(strDate) }!!
                    for(i in 0 until mAlarmList.size) {
                        Log.d("로그", "AlarmReceiver - onReceive : 오늘 등록돼있는 알람 리스트 : ${mAlarmList[i]}")
                    }

                    //해당 주기에 맞는 알람이 맞는지 체크
                    var alarmSkipCheck = true
                    for(i in 0 until mAlarmList.size) {
                        //만기일이 지났을 경우 알람 삭제
                        if(mAlarmList[i].expired != "" && strDate > mAlarmList[i].expired) {
                            Log.d("로그", "AlarmReceiver - onReceive : 만기일이 지났습니다.")
                            val alarmIntent = PendingIntent.getBroadcast(context, pendingId, Intent(context, AlarmReceiver::class.java), 0)
                            val alarmManager = context.let { ContextCompat.getSystemService(it, AlarmManager::class.java) }
                            alarmManager?.cancel(alarmIntent)
                        }

                        //오늘 해당하는 알람이 아니면 check = false
                        if(mAlarmList[i].id == (pendingId / 4)) {
                            alarmSkipCheck = false
                        }
                    }
                    if(alarmSkipCheck) {
                        Log.d("로그", "AlarmReceiver - onReceive : 알람 울리는 날이 아닙니다.")
                        return
                    }

                    //복용 여부 확인을 위한 변수
                    takenFlag = false

                    //화면 깨우기
                    turnOnScreen()
                    //노티 띄우기
                    startNotification(pendingId)

                    //벨소리 울리기
                    startMedia()

                    //서비스 인텐트 구성
                    intentToService.putExtra("ON_OFF", ADD_INTENT)
                    intentToService.putExtra("PendingId", pendingId)
                    intentToService.putExtra("takenFlag", takenFlag)



                    //버전 체크 후 서비스 불러오기
                    startService(intentToService)
                }
                OFF_INTENT -> {
                    Log.d("로그", "AlarmReceiver - onReceive : Reciever Off_intent 호출됨")
                    val pendingId = intent.extras?.getInt("PendingId")

                    cancelNofitication(pendingId!!)
                    stopMedia(intent, pendingId)
                    intentToService.putExtra("ON_OFF", OFF_INTENT)
                    intentToService.putExtra("PendingId", pendingId)
                    intentToService.putExtra("takenFlag", takenFlag)

                    //버전 체크 후 서비스 불러오기
                    startService(intentToService)
                }
            }
        } catch (e: Exception) {
            Log.d("로그", "AlarmReceiver - onReceive : $e")
            e.printStackTrace()
        }
    }

    //서비스 버전 체크하기
    private fun startService(intentToService: Intent) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) this.context!!.startForegroundService(intentToService)
        else this.context!!.startService(intentToService)
    }

    //화면 깨우기
    private fun turnOnScreen() {
        val powerManager = this.context?.getSystemService(POWER_SERVICE) as PowerManager
        val wakeLock = powerManager.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or
                    PowerManager.ACQUIRE_CAUSES_WAKEUP or
                    PowerManager.ON_AFTER_RELEASE, "app:alarm"
        )
        wakeLock.acquire(10*60*1000L /*10 minutes*/)
    }

    private fun cancelNofitication(pendingId: Int) {
        Log.d("로그", "AlarmReceiver - cancelNofitication : cancelNotification호출됨   $pendingId")
        val notificationManager = this.context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancel(pendingId)
    }

    //노티 만들기
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun startNotification(pendingId: Int) {
        //팝업 인텐트 설정
        val popupIntent = Intent(this.context, SetAlarmPopupActivity::class.java).apply {
            this.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        popupIntent.putExtra("PendingId", pendingId)
        val popupPendingIntent = PendingIntent.getActivity(this.context, 0, popupIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val title = makeNotificationTitle(alarmData, pendingId)
        val message = "배너를 클릭하고 '복용' 버튼을 꼭 눌러주세요."
        val notification = NotificationCompat.Builder(this.context!!, AlarmService.CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.drug)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(popupPendingIntent)
            .setAutoCancel(true)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(message)
            )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(pendingId, notification)
        }
        Log.d("로그", "AlarmService - startNotification : 알람 펜딩 아이디 : $pendingId")
//        startForeground(pendingId, notification.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(pendingId: Int, notification: NotificationCompat.Builder) {
        Log.d("로그", "AlarmService - createNotificationChannel : createNotification 호출됨!!!")
        val notificationChannel = NotificationChannel(AlarmService.CHANNEL_ID, "MyApp notification", NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "App Tests"
        val notificationManager = this.context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            notificationChannel)
        notificationManager.notify(pendingId, notification.build())
    }

    //노티 알람 타이틀 만들기
    @SuppressLint("SimpleDateFormat")
    fun makeNotificationTitle(alarmData: AlarmData, pendingId: Int):  String {
        var maen = ""
        var ampm = "오전"
        val cal = Calendar.getInstance()
        when(pendingId % 4) {
            0 -> {
                maen = "아침"
                if(alarmData.mampm == 1) ampm = "오후"
                cal.set(Calendar.HOUR_OF_DAY, alarmData.mhour)
                cal.set(Calendar.MINUTE, alarmData.mminute)
            }
            1-> {
                maen = "점심"
                if(alarmData.aampm == 1) ampm = "오후"
                cal.set(Calendar.HOUR_OF_DAY, alarmData.ahour)
                cal.set(Calendar.MINUTE, alarmData.aminute)
            }
            2 -> {
                maen = "저녁"
                if(alarmData.eampm == 1) ampm = "오후"
                cal.set(Calendar.HOUR_OF_DAY, alarmData.ehour)
                cal.set(Calendar.MINUTE, alarmData.eminute)
            }
            3 -> {
                maen = "취침전"
                if(alarmData.nampm == 1) ampm = "오후"
                cal.set(Calendar.HOUR_OF_DAY, alarmData.nhour)
                cal.set(Calendar.MINUTE, alarmData.nminute)
            }
        }
        return "$maen - $ampm ${SimpleDateFormat("H:mm").format(cal.time)} ${alarmData.name} 먹을 시간이에요!"
    }

    //알림 소리 끄기
    private fun stopMedia(intent: Intent, pendingId: Int) {
        val pendingId2 = intent.extras?.getInt("PendingId")
        val uri = Settings.System.DEFAULT_ALARM_ALERT_URI
        mediaPlayer = MediaPlayer.create(this.context!!, uri)
        if(mediaPlayer.isPlaying && pendingId == pendingId2) {
            mediaPlayer.stop()
            mediaPlayer.reset()
        }
    }

    //알림 소리 켜기
    private fun startMedia() {
        //알람 소리 플레이
        val uri = Settings.System.DEFAULT_ALARM_ALERT_URI
        mediaPlayer = MediaPlayer.create(this.context!!, uri)
        if(!mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.start()
        }
    }
}