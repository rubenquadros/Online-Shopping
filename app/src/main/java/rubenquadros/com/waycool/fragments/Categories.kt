package rubenquadros.com.waycool.fragments

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import rubenquadros.com.waycool.R
import rubenquadros.com.waycool.custom.CategoriesAdapter
import rubenquadros.com.waycool.models.CategoriesData
import rubenquadros.com.waycool.utility.ApplicationConstants
import rubenquadros.com.waycool.utility.ApplicationUtility

class Categories : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var categoriesData: CategoriesData
    private lateinit var categoriesAdapter: CategoriesAdapter
    private var updatedCategories = ArrayList<String>()
    private var updatedImages = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        categoriesData = CategoriesData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_categories, container, false)
        val catTextView = view.findViewById<TextView>(R.id.catTextView)
        catTextView.typeface = activity?.let { ApplicationUtility.fontBold(it) }
        catTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_BOLD_SIZE)
        val mRecyclerView = view.findViewById<RecyclerView>(R.id.mRecyclerView)
        val gridManager = GridLayoutManager(activity, 3)
        mRecyclerView.layoutManager = gridManager
        categoriesAdapter = activity?.let { CategoriesAdapter(it, categoriesData.categoriesArray, categoriesData.images) }!!
        mRecyclerView.adapter = categoriesAdapter
        val searchView = view.findViewById<android.widget.SearchView>(R.id.searchView)
        val id = searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
        val textView = searchView.findViewById(id) as TextView
        textView.typeface = ApplicationUtility.fontRegular(activity!!)
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, ApplicationConstants.FONT_REGULAR_SIZE)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()){
                    filterQueryAndUpdateAdapter(query)
                }else{
                    categoriesAdapter.getOriginalView()
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()){
                    //filterQueryAndUpdateAdapter(newText)
                }else{
                    categoriesAdapter.getOriginalView()
                }
                return false
            }

        })
        return view
    }

    private fun filterQueryAndUpdateAdapter(query: String) {
        for (i in 0 until categoriesData.categoriesArray.size) {
            if(categoriesData.categoriesArray.elementAt(i).contains(query, true)){
                updatedCategories.add(categoriesData.categoriesArray.elementAt(i))
                updatedImages.add(categoriesData.images.elementAt(i))
            }
            categoriesAdapter.updateAdapter(updatedCategories, updatedImages)
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
            Categories().apply {
                arguments = Bundle().apply {

                }
            }
    }
}
