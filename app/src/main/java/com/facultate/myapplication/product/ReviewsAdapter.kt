package com.facultate.myapplication.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facultate.myapplication.R

class ReviewsAdapter : RecyclerView.Adapter<ReviewsAdapter.MyViewHolder>() {

    private val reviewsArray = arrayListOf(
        Review(),
        Review(),
        Review(),
        Review(),
        Review()
    )

    override fun getItemCount(): Int {
        return reviewsArray.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = reviewsArray[position]

        holder.reviewUserName.text = currentItem.reviewUserName
        holder.reviewRating.rating = currentItem.reviewRating
        holder.reviewDate.text = currentItem.reviewDate
        holder.reviewTitle.text = currentItem.reviewTitle
        holder.reviewBody.text = currentItem.reviewBody

        // Set reviewImage using an image loading library like Glide or Picasso
        // Glide.with(holder.itemView.context).load(currentItem.reviewImage).into(holder.reviewImage)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReviewsAdapter.MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return MyViewHolder(itemView)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val reviewImage = itemView.findViewById<ImageView>(R.id.image_view_review_user_image)
        val reviewUserName = itemView.findViewById<TextView>(R.id.text_view_review_username)
        val reviewRating = itemView.findViewById<RatingBar>(R.id.rating_review_product)
        val reviewDate = itemView.findViewById<TextView>(R.id.text_view_review_date)
        val reviewTitle = itemView.findViewById<TextView>(R.id.text_view_review_title)
        val reviewBody = itemView.findViewById<TextView>(R.id.text_view_review_body)

    }

    data class Review(
        val reviewImage: String = "",
        val reviewUserName: String = "John Smith",
        val reviewRating: Float = 1.5f,
        val reviewDate: String = "01.01.2023",
        val reviewTitle: String = "Review Title",
        val reviewBody: String = "Dictumst scelerisque ut commodo dis. Risus ac tellus sapien gravida sit elementum dui eget nunc. Eu arcu montes, sit elit, maecenas feugiat. Urna, habitant suspendisse suspendisse pharetra nec. Nibh mauris nullam nec mattis."
    )
}
