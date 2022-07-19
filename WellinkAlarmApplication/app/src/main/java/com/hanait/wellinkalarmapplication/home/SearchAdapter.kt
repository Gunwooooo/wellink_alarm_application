package com.hanait.wellinkalarmapplication.home
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.model.Item
import com.hanait.wellinkalarmapplication.model.SearchData


class SearchAdapter(var context: Context, var data: ArrayList<Item>) :
    RecyclerView.Adapter<SearchAdapter.VH>() {

    //adapter 클릭 리스너 외부 처리를 위한 인터페이스
    private var mListener: OnItemClickListener? = null

    public interface OnItemClickListener {
        fun onItemClick(v:View, pos:Int)
        fun onLikeClick(v:View, pos:Int)
    }
    public fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var searchTextView: TextView
        private lateinit var searchItemLayout: LinearLayout
        private lateinit var searchItemImageView: ImageView
        private lateinit var searchLikeImageView: ImageView

        fun setSearchTextView(item: Item, position: Int) {
            Log.d("로그", "VH - setSearchTextView : $item")
            searchTextView = itemView.findViewById(R.id.homeSearchItem_textView_name)
            searchItemLayout = itemView.findViewById(R.id.homeSearchItem_layout)
            searchItemImageView = itemView.findViewById(R.id.homeSearchItem_imageView_item)
            searchLikeImageView = itemView.findViewById(R.id.homeSearchItem_imageView_like)

            searchTextView.text = item.itemName

            Log.d("로그", "VH - setSearchTextView : ${item.itemImage}")
            //약 이미지 넣기
            Glide.with(context).load(item.itemImage).into(searchItemImageView)

            //아이템 클릭
            searchItemLayout.setOnClickListener {
                mListener?.onItemClick(it, position)
            }

            searchLikeImageView.setOnClickListener {
                mListener?.onLikeClick(it, position)
            }
        
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(context).inflate(R.layout.home_search_item, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val tmpData = data[position]
        holder.setSearchTextView(tmpData, position + 1)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}