package rubenquadros.com.waycool.custom

import android.content.Context
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.cart_layout.view.*
import rubenquadros.com.waycool.R
import rubenquadros.com.waycool.utility.ApplicationConstants
import rubenquadros.com.waycool.utility.ApplicationUtility

class CartAdapter(val context: Context, val nameArray: ArrayList<String>, val imageArray: ArrayList<String>,
                  val amountArray: ArrayList<String>, val weightArray: ArrayList<String>,
                  val actualPriceArray: ArrayList<String>, val priceArray: ArrayList<String>): RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.cart_layout, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return nameArray.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.itemImageView.setImageResource(context.resources.getIdentifier(imageArray[p1], "drawable", context.packageName))

        p0.itemLabel.typeface = ApplicationUtility.fontBold(context)
        p0.itemLabel.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_BOLD_SIZE)
        p0.itemLabel.text = nameArray[p1]

        p0.amount.typeface = ApplicationUtility.fontRegular(context)
        p0.amount.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        p0.amount.text = amountArray[p1]

        p0.weight.typeface = ApplicationUtility.fontRegular(context)
        p0.weight.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        p0.weight.text = weightArray[p1]

        p0.price.typeface = ApplicationUtility.fontRegular(context)
        p0.price.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        p0.price.text = actualPriceArray[p1]

        p0.discount.typeface = ApplicationUtility.fontRegular(context)
        p0.discount.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        if(priceArray[p1] == "") {
            p0.discount.visibility = View.GONE
        }else{
            p0.price.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            p0.discount.text = priceArray[p1]
        }
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val itemImageView = itemView.itemImageView
        val itemLabel = itemView.itemLabel
        val amount = itemView.amount
        val weight = itemView.weight
        val price = itemView.price
        val discount = itemView.discount
    }
}