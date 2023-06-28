package com.hanait.wellinkalarmapplication.home
import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hanait.wellinkalarmapplication.R
import com.hanait.wellinkalarmapplication.model.Item


class LikeAdapter(var context: Context, var data: ArrayList<Item>) :
    RecyclerView.Adapter<LikeAdapter.VH>() {

    //adapter 클릭 리스너 외부 처리를 위한 인터페이스
    private var mListener: OnItemClickListener? = null

    public interface OnItemClickListener {
        fun onItemClick(v:View, pos:Int)
        fun onDeleteItem(v:View, pos:Int)
    }
    public fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mListener = listener
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var likeNameTextView: TextView
        private lateinit var likeLayout: LinearLayout
        private lateinit var likeImageView: ImageView
        private lateinit var likeDeleteImageView: ImageView

        @SuppressLint("CutPasteId", "SetTextI18n")
        fun setLikeTextView(likeData: Item, position: Int) {
            likeNameTextView = itemView.findViewById(R.id.homeLikeItem_textView_name)
            likeImageView = itemView.findViewById(R.id.homeLikeItem_imageView_item)
            likeDeleteImageView = itemView.findViewById(R.id.homeLikeItem_imageView_delete)
            likeLayout = itemView.findViewById(R.id.homeLikeItem_layout)

            // 텍스트 사이즈 30 이상이면 자르기
            var tempItemName = likeData.itemName
            if(likeData.itemName?.length!! > 30) {
                tempItemName = tempItemName!!.substring(0 until 30) + "..."
            }
            likeNameTextView.text = "$tempItemName / ${likeData.entpName}"

            //약 이미지 넣기
            if(likeData.itemImage == "")
                likeImageView.setImageResource(R.drawable.icon_no_color_empty_image)
            else Glide.with(context).load(likeData.itemImage).into(likeImageView)


            likeDeleteImageView.setOnClickListener {
                mListener?.onDeleteItem(it, position)
            }
            likeLayout.setOnClickListener {
                mListener?.onItemClick(it, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(context).inflate(R.layout.home_like_item, parent, false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val tmpData = data[position]
        holder.setLikeTextView(tmpData, position+1)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}