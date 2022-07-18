package com.hanait.wellinkalarmapplication.home
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.model.AlarmData
import com.hanait.wellinkalarmapplication.model.SearchData


class SearchAdapter(var context: Context, var data: ArrayList<SearchData>) :
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
        private lateinit var searchLikeImageView: ImageView

        fun setSearchTextView(searchData: SearchData, position: Int) {
            searchTextView = itemView.findViewById(R.id.homeSearchItem_textView_name)
            searchItemLayout = itemView.findViewById(R.id.homeSearchItem_layout)
            searchLikeImageView = itemView.findViewById(R.id.homeSearchItem_imageView_like)

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
        holder.setSearchTextView(tmpData, position+1)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}