package com.hanait.wellinkalarmapplication.home
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.db.DatabaseManager
import com.hanait.wellinkalarmapplication.model.Item


class SearchAdapter(var context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private val item = ArrayList<Item>()

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemList = item

        private lateinit var searchTextView: TextView
        private lateinit var searchItemLayout: LinearLayout
        private lateinit var searchItemImageView: ImageView
        private lateinit var searchLikeImageView: ImageView

        @SuppressLint("SetTextI18n")
        fun setSearchTextView(item: Item, position: Int) {
            searchTextView = itemView.findViewById(R.id.homeSearchItem_textView_name)
            searchItemLayout = itemView.findViewById(R.id.homeSearchItem_layout)
            searchItemImageView = itemView.findViewById(R.id.homeSearchItem_imageView_item)
            searchLikeImageView = itemView.findViewById(R.id.homeSearchItem_imageView_like)

            // 텍스트 사이즈 30 이상이면 자르기
            var tempItemName = item.itemName
            if(item.itemName?.length!! > 30) {
                tempItemName = tempItemName!!.substring(0 until 30) + "..."
            }
            searchTextView.text = "$tempItemName / ${item.entpName}"

            //약 이미지 넣기
            if(item.itemImage == "")
                searchItemImageView.setImageResource(R.drawable.icon_no_color_empty_image)
            else Glide.with(context).load(item.itemImage).into(searchItemImageView)

            //아이템 클릭
            searchItemLayout.setOnClickListener {
                val intent = Intent(context, SearchDetailActivity::class.java)
                intent.putExtra("SearchData", itemList[position - 1])
                context.startActivity(intent)
            }

            //즐겨찾는 약 등록하기
            searchLikeImageView.setOnClickListener {
                val likeList = DatabaseManager.getInstance(context, "Alarms.db").selectLikeAll()
                for (i in 0 until likeList.size) {
                    if (likeList[i].itemSeq == itemList[position - 1].itemSeq) {
                        Toast.makeText(context, "이미 등록된 약입니다.", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                }
                DatabaseManager.getInstance(context, "Alarms.db").insertLike(itemList[position - 1])
                Toast.makeText(context, "관심 약/약물로 등록되었습니다.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    //로딩 뷰 홀더
    inner class LVH(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    //뷰 타입 가져오기
    override fun getItemViewType(position: Int): Int {
        return when(item[position].itemName) {
            " " -> VIEW_TYPE_LOADING
            else -> VIEW_TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            VIEW_TYPE_ITEM -> {
                VH(LayoutInflater.from(context).inflate(R.layout.home_search_item, parent, false))
            }
            else -> {
                LVH(LayoutInflater.from(context).inflate(R.layout.home_search_loading, parent, false))
            }
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is VH) {
            val tmpData = item[position]
            holder.setSearchTextView(tmpData, position + 1)
        } else {

        }
    }

    fun setList(item: MutableList<Item>) {
        this.item.addAll(item)
        if(item.size == 70) this.item.add(Item(" ", " ", 0, " ", " ", " ", " ", " ", " ", " ", " ", " ", " ")) // progress bar 넣을 자리
    }

    fun deleteLoading(){
        item.removeAt(item.lastIndex) // 로딩이 완료되면 프로그레스바를 지움
    }
}