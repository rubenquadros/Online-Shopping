package rubenquadros.com.waycool.custom

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.shopping_layout.view.*
import rubenquadros.com.waycool.R
import rubenquadros.com.waycool.models.ShoppingData
import rubenquadros.com.waycool.utility.ApplicationConstants
import rubenquadros.com.waycool.utility.ApplicationUtility

class ShoppingAdapter(val cartLayout: CardView, val cartItems: TextView, val context: Context, private var itemNames: ArrayList<String>, private var discountArray: ArrayList<String>,
                      private var discountPriceArray: ArrayList<String>, private var storageArray: ArrayList<String>, private var typeArray: ArrayList<String>,
                      private var availabilityArray: ArrayList<String>, private var itemImages: ArrayList<String>, private var mrpArray: ArrayList<String>,
                      private var quantityArray: ArrayList<String>, private var stockArray: ArrayList<Boolean>): RecyclerView.Adapter<ShoppingAdapter.ViewHolder>() {

    private var cartCount = 0
    private lateinit var shoppingData: ShoppingData

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.shopping_layout, p0, false)
        shoppingData = ShoppingData()
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  itemNames.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {

        p0.discountText.typeface = ApplicationUtility.fontBold(context)
        p0.discountText.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        if(discountArray[p1] == "") {
            p0.discountText.visibility = View.GONE
        }else{
            p0.discountText.text = discountArray[p1]
        }

        p0.availabilityText.typeface = ApplicationUtility.fontRegular(context)
        p0.availabilityText.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        if(availabilityArray[p1] == "") {
            p0.availabilityText.visibility = View.INVISIBLE
        }else{
            p0.availabilityText.text = availabilityArray[p1]
        }

        p0.itemImage.setImageResource(context.resources.getIdentifier(itemImages[p1], "drawable", context.packageName))

        p0.titleText.typeface = ApplicationUtility.fontBold(context)
        p0.titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_BOLD_SIZE)
        p0.titleText.text = itemNames[p1]

        p0.typeText.typeface = ApplicationUtility.fontRegular(context)
        p0.typeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)

        p0.typeDescription.typeface = ApplicationUtility.fontRegular(context)
        p0.typeDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        p0.typeDescription.text = typeArray[p1]

        p0.storageText.typeface = ApplicationUtility.fontRegular(context)
        p0.storageText.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)

        p0.storageDescription.typeface = ApplicationUtility.fontRegular(context)
        p0.storageDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        p0.storageDescription.text = storageArray[p1]

        p0.quantityText.typeface = ApplicationUtility.fontRegular(context)
        p0.quantityText.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)

        p0.quantityDescription.typeface = ApplicationUtility.fontRegular(context)
        p0.quantityDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        p0.quantityDescription.text = quantityArray[p1]

        p0.actualPrice.typeface = ApplicationUtility.fontRegular(context)
        p0.actualPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        p0.actualPrice.text = mrpArray[p1]

        p0.discountPrice.typeface = ApplicationUtility.fontBold(context)
        p0.discountPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_DISCOUNT)
        if(discountPriceArray[p1] == ""){
            p0.discountPrice.visibility = View.INVISIBLE
        }else{
            p0.discountPrice.text = discountPriceArray[p1]
        }

        p0.addText.typeface = ApplicationUtility.fontBold(context)
        p0.addText.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_BOLD_SIZE)

        p0.stockText.typeface = ApplicationUtility.fontRegular(context)
        p0.stockText.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)

        if(stockArray[p1]) {
            p0.backgroundLayout.setBackgroundColor(context.resources.getColor(R.color.availabilityColor))
            p0.stockText.visibility = View.VISIBLE
            p0.stockText.text = context.getString(R.string.out_of_stock)
            p0.addLayout.background = context.getDrawable(R.drawable.rounded_corners_no_stock)
            p0.addCard.visibility = View.GONE
            p0.addCard.isEnabled = false
            p0.minusCard.visibility = View.GONE
            p0.minusCard.isEnabled = false
            p0.addText.typeface = ApplicationUtility.fontRegular(context)
            p0.addText.text = context.getString(R.string.no_stock)
            p0.addText.setTextColor(context.resources.getColor(R.color.colorBlack))
        }

        p0.addCard.setOnClickListener {
            p0.minusCard.visibility = View.VISIBLE
            shoppingData.itemCount[p1]++
            p0.addText.text = context.resources.getString(R.string.X)+shoppingData.itemCount[p1]
            cartLayout.visibility = View.VISIBLE
            if(!shoppingData.isClicked[p1]) {
                cartCount++
                shoppingData.isClicked[p1] = true
                cartItems.text = cartCount.toString()
            }
        }

        p0.minusCard.setOnClickListener {
            shoppingData.itemCount[p1]--
            if(shoppingData.itemCount[p1] <= 0) {
                shoppingData.itemCount[p1] = 0
                shoppingData.isClicked[p1] = false
                cartCount--
                p0.minusCard.visibility = View.GONE
                p0.addText.text = context.resources.getString(R.string.add)
            }else{
                p0.addText.text = context.resources.getString(R.string.X)+shoppingData.itemCount[p1]
            }
            if(cartCount <= 0) {
                cartCount = 0
                cartLayout.visibility = View.GONE
            }else{
                cartItems.text = cartCount.toString()
            }
        }
    }

    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val discountText = itemView.discountText!!
        val availabilityText = itemView.availabilityText!!
        val itemImage = itemView.itemImage!!
        val titleText = itemView.titleText!!
        val typeText = itemView.typeText!!
        val typeDescription = itemView.typeDescription!!
        val storageText = itemView.storageText!!
        val storageDescription = itemView.storageDescription!!
        val quantityText = itemView.quantityText!!
        val quantityDescription = itemView.quantityDescription!!
        val actualPrice = itemView.actualPrice!!
        val discountPrice = itemView.discountPrice!!
        val minusCard = itemView.minusCard!!
        val addText = itemView.addText!!
        var addCard = itemView.addCard!!
        var stockText = itemView.stockText!!
        var backgroundLayout = itemView.firstSideLinearLayout!!
        var addLayout = itemView.addButton!!
    }

    fun updateAdapter(updatedItemNames: ArrayList<String>, updatedDiscountArray: ArrayList<String>, updatedDiscountPriceArray: ArrayList<String>,
                      updatedStorageArray: ArrayList<String>, updatedTypeArray: ArrayList<String>, updatedAvailabilityArray: ArrayList<String>,
                      updatedItemImages: ArrayList<String>, updatedMrpArray: ArrayList<String>, updatedQuantityArray: ArrayList<String>) {
        itemNames = updatedItemNames
        discountArray = updatedDiscountArray
        discountPriceArray = updatedDiscountPriceArray
        storageArray = updatedStorageArray
        typeArray = updatedTypeArray
        availabilityArray = updatedAvailabilityArray
        itemImages = updatedItemImages
        mrpArray = updatedMrpArray
        quantityArray = updatedQuantityArray
        notifyDataSetChanged()
    }

    fun getOriginalView() {
        itemNames = shoppingData.itemNames
        discountArray = shoppingData.discountArray
        discountPriceArray = shoppingData.discountPriceArray
        storageArray = shoppingData.storageArray
        typeArray = shoppingData.typeArray
        availabilityArray = shoppingData.availabilityArray
        itemImages = shoppingData.itemImages
        mrpArray = shoppingData.mrpArray
        quantityArray = shoppingData.quantityArray
        notifyDataSetChanged()
    }

}