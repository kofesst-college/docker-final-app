package me.kofesst.recipsy.server.features.utils

import me.kofesst.recipsy.server.common.models.PaginationData
import me.kofesst.recipsy.server.common.models.recipes.Recipe
import me.kofesst.recipsy.server.features.tables.*
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.expression.ArgumentExpression
import org.ktorm.expression.OrderByExpression
import org.ktorm.schema.BooleanSqlType
import org.ktorm.schema.ColumnDeclaring

fun Database.getRecipe(id: Int): Recipe? {
    val joinedRecipe = from(Recipes)
        .leftJoin(Ingredients, on = Recipes.id eq Ingredients.recipeId)
        .leftJoin(CookingStages, on = Recipes.id eq CookingStages.recipeId)
        .leftJoin(Users, on = Users.id eq Recipes.authorId)
        .select()
        .where { Recipes.id eq id }
        .map { it.toRecipe() }
    if (joinedRecipe.isEmpty()) return null
    val recipe = joinedRecipe.first()
    return Recipe(
        id = recipe.id,
        name = recipe.name,
        imagePath = recipe.imagePath,
        cookingDurationMillis = recipe.cookingDurationMillis,
        ingredients = joinedRecipe
            .map { it.ingredients }
            .flatten()
            .distinctBy { it.id }
            .sortedBy { it.id },
        stages = joinedRecipe.map { it.stages }
            .flatten()
            .distinctBy { it.id }
            .sortedBy { it.id },
        author = recipe.author
    )
}

fun Database.getRecipes(
    where: () -> ColumnDeclaring<Boolean> = {
        ArgumentExpression(true, BooleanSqlType)
    },
    orderBy: OrderByExpression = Recipes.id.desc(),
    paginationData: PaginationData,
): List<Recipe> {
    val pagedRecipeIds = from(Recipes)
        .select()
        .where(where)
        .orderBy(orderBy)
        .limit(paginationData.offset, paginationData.limit)
        .map { it[Recipes.id] ?: -1 }
    val joinedRecipes = from(Recipes)
        .leftJoin(Ingredients, on = Recipes.id eq Ingredients.recipeId)
        .leftJoin(CookingStages, on = Recipes.id eq CookingStages.recipeId)
        .leftJoin(Users, on = Users.id eq Recipes.authorId)
        .select()
        .where {
            where() and (Recipes.id inList pagedRecipeIds.ifEmpty { listOf(-1) })
        }
        .orderBy(orderBy)
        .map { it.toRecipe() }
    return joinedRecipes
        .groupBy { it.id }
        .mapValues { (id, rows) ->
            Recipe(
                id = id,
                name = rows[0].name,
                imagePath = rows[0].imagePath,
                author = rows[0].author,
                cookingDurationMillis = rows[0].cookingDurationMillis,
                ingredients = rows
                    .map { it.ingredients }
                    .flatten()
                    .distinctBy { it.id }
                    .sortedBy { it.id },
                stages = rows
                    .map { it.stages }
                    .flatten()
                    .distinctBy { it.id }
                    .sortedBy { it.position },
            )
        }
        .values
        .toList()
        .distinctBy { it.id }
}