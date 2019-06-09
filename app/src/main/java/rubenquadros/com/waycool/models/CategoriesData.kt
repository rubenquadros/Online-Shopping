package rubenquadros.com.waycool.models

data class CategoriesData (
    val categoriesArray: ArrayList<String> = arrayListOf("Baby Care", "Fruits", "Vegetables", "Beauty", "Medicine", "Pet Care", "Dry Fruits", "Snacks", "More"),
    val images: ArrayList<String> = arrayListOf("baby", "fruits", "veg", "beauty", "medicine", "pets", "dryfruits", "snacks", "more")
){
    override fun toString(): String {
        return "CategoriesData(categoriesArray=$categoriesArray, images=$images)"
    }
}