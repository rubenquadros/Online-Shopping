package rubenquadros.com.waycool.models

data class ShoppingData(
    val discountPriceArray: ArrayList<String> = arrayListOf(
        "Rs. 102",
        "Rs. 49",
        "Rs. 69",
        "",
        "",
        "Rs. 69",
        "",
        "Rs. 30",
        "Rs. 34",
        "Rs. 54"
    ),
    val discountArray: ArrayList<String> = arrayListOf("20%", "15%", "20%", "", "", "10%", "", "5%", "15%", "10%"),
    val itemNames: ArrayList<String> = arrayListOf(
        "Washington Apples",
        "Oranges",
        "Bananas",
        "Grapes",
        "Watermelon",
        "Mangoes",
        "Cherries",
        "Pears",
        "Fig",
        "Kiwi Fruit"
    ),
    val storageArray: ArrayList<String> = arrayListOf(
        "3 days",
        "5 days",
        "4 days",
        "2 days",
        "4 days",
        "6 days",
        "3 days",
        "2 days",
        "5 days",
        "6 days",
        "2 days"
    ),
    val typeArray: ArrayList<String> = arrayListOf(
        "Regular",
        "Premium",
        "Regular",
        "Regular",
        "Regular",
        "Premium",
        "Regular",
        "Premium",
        "Regular",
        "Premium"
    ),
    val availabilityArray: ArrayList<String> = arrayListOf(
        "In Season",
        "",
        "",
        "",
        "",
        "In Season",
        "In Season",
        "",
        "",
        "In Season"
    ),
    val itemImages: ArrayList<String> = arrayListOf(
        "apples",
        "oranges",
        "bananas",
        "grapes",
        "watermelon",
        "mangoes",
        "cherries",
        "pears",
        "figs",
        "kiwi"
    ),
    val mrpArray: ArrayList<String> = arrayListOf(
        "Rs. 128",
        "Rs. 58",
        "Rs. 87",
        "Rs. 75",
        "Rs. 22",
        "Rs. 77",
        "Rs. 162",
        "Rs. 32",
        "Rs. 40",
        "Rs. 60"
    ),
    val quantityArray: ArrayList<String> = arrayListOf(
        "1 kg",
        "500 gms",
        "2 kgs",
        "1 kg",
        "400 gms",
        "1 kg",
        "3 kgs",
        "1 kg",
        "1 kg",
        "2 kgs"
    ),
    val isClicked: ArrayList<Boolean> = arrayListOf(false, false, false, false, false, false, false, false, false, false),
    val itemCount: ArrayList<Int> = arrayListOf(0,0,0,0,0,0,0,0,0,0),
    val isOutOfStock: ArrayList<Boolean> = arrayListOf(false, false, false, false, false, false, false, true, false, false)
) {
    override fun toString(): String {
        return "ShoppingData(discountPriceArray=$discountPriceArray, discountArray=$discountArray, itemNames=$itemNames, storageArray=$storageArray, typeArray=$typeArray, availabilityArray=$availabilityArray, itemImages=$itemImages, mrpArray=$mrpArray, quantityArray=$quantityArray)"
    }
}