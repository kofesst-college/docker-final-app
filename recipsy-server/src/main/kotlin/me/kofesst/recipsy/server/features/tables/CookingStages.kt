package me.kofesst.recipsy.server.features.tables

import me.kofesst.recipsy.server.common.models.recipes.CookingStage
import org.ktorm.dsl.QueryRowSet
import org.ktorm.entity.Entity
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

object CookingStages : Table<CookingStagesTable>(tableName = "cooking_stages") {
    val id = int("id_stage").primaryKey().bindTo { it.id }
    val imagePath = varchar("image_path").bindTo { it.imagePath }
    val description = varchar("description").bindTo { it.description }
    val position = int("position").bindTo { it.position }
    val recipeId = int("recipe_id").bindTo { it.recipeId }
}

interface CookingStagesTable : Entity<CookingStagesTable> {
    companion object : Entity.Factory<CookingStagesTable>()

    val id: Int
    val imagePath: String?
    val description: String
    val position: Int
    val recipeId: Int
}

fun QueryRowSet.toCookingStage() = with(get(CookingStages.id)) {
    if (this == null) null
    else CookingStage(
        id = get(CookingStages.id)!!,
        position = get(CookingStages.position)!!,
        description = get(CookingStages.description)!!,
        imagePath = get(CookingStages.imagePath),
        recipeId = get(CookingStages.recipeId)!!,
    )
}