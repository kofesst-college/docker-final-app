package me.kofesst.recipsy.server.features.tables

import me.kofesst.recipsy.server.common.models.recipes.Ingredient
import org.ktorm.dsl.QueryRowSet
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.double
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Ingredients : Table<IngredientsTable>(tableName = "ingredients") {
    val id = int("id_ingredient").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val amountType = varchar("amount_type").bindTo { it.amountType }
    val amountValue = double("amount_value").bindTo { it.amountValue }
    val recipeId = int("recipe_id").bindTo { it.recipeId }
}

interface IngredientsTable : Entity<IngredientsTable> {
    companion object : Entity.Factory<IngredientsTable>()

    val id: Int
    val name: String
    val amountType: String
    val amountValue: Double
    val recipeId: Int
}

fun QueryRowSet.toIngredient() = with(get(Ingredients.id)) {
    if (this == null) null
    else Ingredient(
        id = get(Ingredients.id)!!,
        name = get(Ingredients.name)!!,
        amount = with(get(Ingredients.amountValue)!!) {
            when (get(Ingredients.amountType)!!) {
                "liters" -> Ingredient.Amount.Liters(this)
                "milliliters" -> Ingredient.Amount.Milliliters(this)
                "tablespoons" -> Ingredient.Amount.Tablespoons(this)
                "teaspoons" -> Ingredient.Amount.Teaspoons(this)
                "grams" -> Ingredient.Amount.Grams(this)
                "pieces" -> Ingredient.Amount.Pieces(this)
                else -> Ingredient.Amount.ByTaste()
            }
        },
    )
}