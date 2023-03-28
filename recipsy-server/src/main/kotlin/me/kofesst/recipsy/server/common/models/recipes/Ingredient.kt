package me.kofesst.recipsy.server.common.models.recipes

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Ingredient(
    val id: Int = -1,
    val name: String = "",
    val amount: Amount = Amount.ByTaste(),

    @Transient
    val recipeId: Int = 0,
) {
    @Serializable
    sealed interface Amount {
        val serialName: String
        val value: Double

        @Serializable
        @SerialName("by-taste")
        class ByTaste : Amount {
            @Transient
            override val serialName: String = "by-taste"

            @Transient
            override val value: Double = -1.0
        }

        @Serializable
        @SerialName("liters")
        data class Liters(override val value: Double) : Amount {
            @Transient
            override val serialName: String = "liters"
        }

        @Serializable
        @SerialName("milliliters")
        data class Milliliters(override val value: Double) : Amount {
            @Transient
            override val serialName: String = "milliliters"
        }

        @Serializable
        @SerialName("tablespoons")
        data class Tablespoons(override val value: Double) : Amount {
            @Transient
            override val serialName: String = "tablespoons"
        }

        @Serializable
        @SerialName("teaspoons")
        data class Teaspoons(override val value: Double) : Amount {
            @Transient
            override val serialName: String = "teaspoons"
        }

        @Serializable
        @SerialName("grams")
        data class Grams(override val value: Double) : Amount {
            @Transient
            override val serialName: String = "grams"
        }

        @Serializable
        @SerialName("pieces")
        data class Pieces(override val value: Double) : Amount {
            @Transient
            override val serialName: String = "pieces"
        }
    }
}
