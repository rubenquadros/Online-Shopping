package rubenquadros.com.waycool.models

data class CartData (
    val imageArray: ArrayList<String> = arrayListOf("oranges", "bananas", "grapes", "watermelon", "mangoes", "kiwi"),
    val nameArray: ArrayList<String> = arrayListOf("Oranges", "Bananas", "Grapes", "Watermelon", "Mangoes", "Kiwi Fruit"),
    val priceArray: ArrayList<String> = arrayListOf("Rs. 49", "Rs. 69", "", "", "Rs. 30", "Rs. 54"),
    val actualPriceArray: ArrayList<String> = arrayListOf("Rs. 58", "Rs. 87", "Rs. 75", "Rs. 44", "Rs. 77", "Rs. 60"),
    val amountArray: ArrayList<String> = arrayListOf("1 pack", "1 pack", "1 pack", "2 pack", "1 pack", "1 pack"),
    val weightArray: ArrayList<String> = arrayListOf("500 gms", "2 kg", "2 kgs", "800 gms", "1 kg", "2 kgs")
){
    override fun toString(): String {
        return "CartData(imageArray=$imageArray, nameArray=$nameArray, priceArray=$priceArray, actualPriceArray=$actualPriceArray, amountArray=$amountArray, weightArray=$weightArray)"
    }
}