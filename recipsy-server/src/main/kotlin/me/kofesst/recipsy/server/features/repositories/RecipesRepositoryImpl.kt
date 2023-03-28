package me.kofesst.recipsy.server.features.repositories

import io.ktor.http.*
import me.kofesst.recipsy.server.common.models.PaginationData
import me.kofesst.recipsy.server.common.models.recipes.Recipe
import me.kofesst.recipsy.server.common.repositories.RecipesRepository
import me.kofesst.recipsy.server.common.resources.utils.ApiException
import me.kofesst.recipsy.server.features.tables.CookingStages
import me.kofesst.recipsy.server.features.tables.Ingredients
import me.kofesst.recipsy.server.features.tables.Recipes
import me.kofesst.recipsy.server.features.utils.getRecipe
import me.kofesst.recipsy.server.features.utils.getRecipes
import org.ktorm.database.Database
import org.ktorm.dsl.*
import org.ktorm.schema.BaseTable

class RecipesRepositoryImpl(
    private val database: Database,
) : RecipesRepository {
    override suspend fun getLatest(paginationData: PaginationData) =
        database.getRecipes(
            orderBy = Recipes.id.desc(),
            paginationData = paginationData
        )

    override suspend fun getOwned(authorId: Int, paginationData: PaginationData) =
        database.getRecipes(
            where = {
                Recipes.authorId eq authorId
            },
            paginationData = paginationData
        )

    override suspend fun get(recipeId: Int): Recipe =
        database.getRecipe(recipeId)
            ?: throw ApiException(HttpStatusCode.BadRequest, "Рецепт не найден")

    override suspend fun create(recipe: Recipe, authorId: Int) {
        val recipeId = database.insertAndGenerateKey(Recipes) {
            set(it.name, recipe.name)
            set(it.imagePath, recipe.imagePath)
            set(it.durationMillis, recipe.cookingDurationMillis)
            set(it.authorId, authorId)
        } as Int
        database.batchInsert(Ingredients) {
            recipe.ingredients.forEach { ingredient ->
                item {
                    set(it.name, ingredient.name)
                    set(it.amountType, ingredient.amount.serialName)
                    set(it.amountValue, ingredient.amount.value)
                    set(it.recipeId, recipeId)
                }
            }
        }
        database.batchInsert(CookingStages) {
            recipe.stages.forEach { stage ->
                item {
                    set(it.position, stage.position)
                    set(it.description, stage.description)
                    set(it.imagePath, stage.imagePath)
                    set(it.recipeId, recipeId)
                }
            }
        }
    }

    override suspend fun update(recipe: Recipe) {
        database.update(Recipes) {
            set(it.name, recipe.name)
            set(it.imagePath, recipe.imagePath)
            set(it.durationMillis, recipe.cookingDurationMillis)
            where { Recipes.id eq recipe.id }
        }
        updateChildren(
            table = Ingredients,
            children = recipe.ingredients,
            updateBlock = {
                set(Ingredients.name, it.name)
                set(Ingredients.amountType, it.amount.serialName)
                set(Ingredients.amountValue, it.amount.value)
                set(Ingredients.recipeId, recipe.id)
                where { Ingredients.id eq it.id }
            },
            insertBlock = {
                set(Ingredients.name, it.name)
                set(Ingredients.amountType, it.amount.serialName)
                set(Ingredients.amountValue, it.amount.value)
                set(Ingredients.recipeId, recipe.id)
            }
        )
        updateChildren(
            table = CookingStages,
            children = recipe.stages,
            updateBlock = {
                set(CookingStages.position, it.position)
                set(CookingStages.description, it.description)
                set(CookingStages.imagePath, it.imagePath)
                set(CookingStages.recipeId, recipe.id)
                where { CookingStages.id eq it.id }
            },
            insertBlock = {
                set(CookingStages.position, it.position)
                set(CookingStages.description, it.description)
                set(CookingStages.imagePath, it.imagePath)
                set(CookingStages.recipeId, recipe.id)
            }
        )
        database.delete(Ingredients) {
            (Ingredients.id notInList recipe.ingredients.map { it.id }) and
                    (Ingredients.recipeId eq recipe.id)
        }
        database.delete(CookingStages) {
            (CookingStages.id notInList recipe.stages.map { it.id }) and
                    (CookingStages.recipeId eq recipe.id)
        }
    }

    private fun <Table : BaseTable<*>, Model : Any> updateChildren(
        table: Table,
        children: List<Model>,
        updateBlock: UpdateStatementBuilder.(Model) -> Unit,
        insertBlock: AssignmentsBuilder.(Model) -> Unit,
    ) {
        val remainChildren = children.toMutableList()
        if (remainChildren.isNotEmpty()) {
            database.batchUpdate(table) {
                children.forEach { child ->
                    item {
                        updateBlock(child)
                    }
                    remainChildren.remove(child)
                }
            }
        }
        if (remainChildren.isNotEmpty()) {
            database.batchInsert(table) {
                remainChildren.forEach { child ->
                    item {
                        insertBlock(child)
                    }
                }
            }
        }
    }
}