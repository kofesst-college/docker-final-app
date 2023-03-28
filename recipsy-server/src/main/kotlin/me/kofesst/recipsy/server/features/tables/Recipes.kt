package me.kofesst.recipsy.server.features.tables

import me.kofesst.recipsy.server.common.models.recipes.Recipe
import org.ktorm.dsl.QueryRowSet
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object Recipes : Table<RecipesTable>(tableName = "recipes") {
    val id = int("id_recipe").primaryKey().bindTo { it.id }
    val name = varchar("name").bindTo { it.name }
    val imagePath = varchar("image_path").bindTo { it.imagePath }
    val durationMillis = int("cooking_duration_millis").bindTo { it.cookingDurationMillis }
    val authorId = int("author_id").bindTo { it.authorId }
}

interface RecipesTable : Entity<RecipesTable> {
    companion object : Entity.Factory<RecipesTable>()

    val id: Int
    val name: String
    val imagePath: String?
    val authorId: Int
    val cookingDurationMillis: Int
}

fun QueryRowSet.toRecipe() = Recipe(
    id = get(Recipes.id)!!,
    name = get(Recipes.name)!!,
    imagePath = get(Recipes.imagePath),
    cookingDurationMillis = get(Recipes.durationMillis)!!,
    author = toUser(),
    ingredients = listOfNotNull(toIngredient()),
    stages = listOfNotNull(toCookingStage())
)