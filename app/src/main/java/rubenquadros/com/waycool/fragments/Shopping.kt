package rubenquadros.com.waycool.fragments

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import rubenquadros.com.waycool.R
import rubenquadros.com.waycool.custom.MyPagerAdapter
import rubenquadros.com.waycool.custom.ShoppingAdapter
import rubenquadros.com.waycool.models.ShoppingData
import rubenquadros.com.waycool.utility.ApplicationConstants
import rubenquadros.com.waycool.utility.ApplicationUtility


class Shopping : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var shoppingData: ShoppingData
    private lateinit var shoppingAdapter: ShoppingAdapter
    private var updatedItemName = ArrayList<String>()
    private var updatedDiscountArray = ArrayList<String>()
    private var updatedDiscountPriceArray = ArrayList<String>()
    private var updatedStorageArray = ArrayList<String>()
    private var updatedTypeArray = ArrayList<String>()
    private var updatedAvailabilityArray = ArrayList<String>()
    private var updatedItemImages = ArrayList<String>()
    private var updatedMrpArray = ArrayList<String>()
    private var updatedQuantityArray = ArrayList<String>()
    lateinit var cartLayout: CardView
    lateinit var cartItems: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        shoppingData = ShoppingData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_shopping, container, false)
        cartLayout = view.findViewById(R.id.cart_layout)
        cartItems = view.findViewById(R.id.count_badge_notification)
        val myRecyclerView = view.findViewById<RecyclerView>(R.id.myRecyclerView)
        val mLinearLayoutManager = activity?.let { LinearLayoutManager(it) }
        myRecyclerView.layoutManager = mLinearLayoutManager
        shoppingAdapter =
            activity?.let { ShoppingAdapter(cartLayout, cartItems, it, shoppingData.itemNames, shoppingData.discountArray, shoppingData.discountPriceArray,
                shoppingData.storageArray, shoppingData.typeArray, shoppingData.availabilityArray, shoppingData.itemImages, shoppingData.mrpArray,
                shoppingData.quantityArray, shoppingData.isOutOfStock) }!!
        myRecyclerView.adapter = shoppingAdapter
        val myPager = view.findViewById<ViewPager>(R.id.viewPager)
        myPager.setCurrentItem(5, true)
        myPager.offscreenPageLimit = 3
        val adapter = MyPagerAdapter(this, activity!!.supportFragmentManager)
        myPager.adapter = adapter
        myPager.setOnPageChangeListener(adapter)
        myPager.pageMargin = -330
        val searchView = view.findViewById<android.widget.SearchView>(R.id.mySearchView)
        val id = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val textView = searchView.findViewById(id) as TextView
        textView.typeface = ApplicationUtility.fontRegular(activity!!)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()){
                    filterQueryAndUpdateAdapter(query)
                }else{
                    shoppingAdapter.getOriginalView()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()){
                    //filterQueryAndUpdateAdapter(newText)
                }else{
                    shoppingAdapter.getOriginalView()
                }
                return false
            }

        })
        return view
    }

    private fun filterQueryAndUpdateAdapter(query: String) {
        for (i in 0 until shoppingData.itemNames.size) {
            if(shoppingData.itemNames[i].contains(query, true)){
                updatedItemName.add(shoppingData.itemNames[i])
                updatedDiscountArray.add(shoppingData.discountArray[i])
                updatedDiscountPriceArray.add(shoppingData.discountPriceArray[i])
                updatedItemImages.add(shoppingData.itemImages[i])
                updatedAvailabilityArray.add(shoppingData.availabilityArray[i])
                updatedMrpArray.add(shoppingData.mrpArray[i])
                updatedStorageArray.add(shoppingData.storageArray[i])
                updatedTypeArray.add(shoppingData.typeArray[i])
                updatedQuantityArray.add(shoppingData.quantityArray[i])
            }
            shoppingAdapter.updateAdapter(updatedItemName, updatedDiscountArray, updatedDiscountPriceArray, updatedStorageArray, updatedTypeArray,
                                            updatedAvailabilityArray, updatedItemImages, updatedMrpArray, updatedQuantityArray)
        }
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Shopping().apply {
                arguments = Bundle().apply {

                }
            }
    }

}
