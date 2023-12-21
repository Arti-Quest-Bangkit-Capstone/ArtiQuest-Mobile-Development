package com.thequest.artiquest.view.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.thequest.artiquest.R

class AdapterSilder(private val context: Context, private val dataSlider: List<String?>) :
    PagerAdapter() {

    override fun getCount(): Int {
        return dataSlider.size
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.item_slide, container, false)

        val imgViewSlider: ImageView = view.findViewById(R.id.iv_slider)
        context.let {
            Glide.with(it)
                .load(dataSlider[position])
                .into(imgViewSlider)
        }

        container.addView(view, 0)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}