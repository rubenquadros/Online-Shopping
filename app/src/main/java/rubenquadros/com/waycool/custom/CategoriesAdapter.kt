package rubenquadros.com.waycool.custom

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.categories_layout.view.*
import rubenquadros.com.waycool.R
import rubenquadros.com.waycool.activity.HomeScreen
import rubenquadros.com.waycool.models.CategoriesData
import rubenquadros.com.waycool.utility.ApplicationConstants
import rubenquadros.com.waycool.utility.ApplicationUtility

class CategoriesAdapter(val context: Context, var categoriesArray: ArrayList<String>, var images: ArrayList<String>):RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    private lateinit var categoriesData: CategoriesData
    private var moreClicked: Boolean = false

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.categories_layout, p0, false)
        categoriesData = CategoriesData()
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categoriesArray.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.mTextView.typeface = ApplicationUtility.fontRegular(context)
        p0.mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        p0.mTextView.text = categoriesArray[p1]

        p0.mImageView.setImageResource(context.resources.getIdentifier(images[p1], "drawable", context.packageName))
        p0.mImageView.setOnClickListener {
            if(categoriesArray[p1] == "More") {
                categoriesData.categoriesArray.remove("More")
                categoriesData.images.remove("more")
                categoriesData.categoriesArray.add("Electronics")
                categoriesData.images.add("electronics")
                categoriesData.categoriesArray.add("Beverages")
                categoriesData.images.add("beverages")
                categoriesData.categoriesArray.add("Shoes")
                categoriesData.images.add("shoes")
                categoriesData.categoriesArray.add("Apparel")
                categoriesData.images.add("apparel")
                updateAdapter(categoriesData.categoriesArray, categoriesData.images)
            }else{
                p0.mImageView.isEnabled = false
                p0.mCardView.setCardBackgroundColor(Color.parseColor("#2A87C8"))
                p0.mImageView.setBackgroundColor(Color.parseColor("#2A87C8"))
                p0.mImageView.setColorFilter(Color.parseColor("#FFFFFF"))
                Handler().postDelayed({
                    context.startActivity(Intent(context, HomeScreen::class.java))
                }, 100)

            }
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val mCardView = itemView.mCardView
        val mImageView = itemView.mImageView
        val mTextView = itemView.mTextView
    }

    fun updateAdapter(updatedCategories: ArrayList<String>, updatedImages: ArrayList<String>) {
        categoriesArray = updatedCategories
        images = updatedImages
        notifyDataSetChanged()
    }
    fun getOriginalView() {
        categoriesArray = categoriesData.categoriesArray
        images = categoriesData.images
        notifyDataSetChanged()
    }
}