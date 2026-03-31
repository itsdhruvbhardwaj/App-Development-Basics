package com.example.myapplication.repository

import com.example.myapplication.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RecipeRepository {
    private val sampleRecipes = listOf(
        Recipe(
            id = "1",
            title = "Chocolate Lava Cake",
            imageUrl = "https://images.unsplash.com/photo-1541919329513-35f7af297129?q=80&w=2070&auto=format&fit=crop",
            ingredients = listOf(
                "1/2 cup unsalted butter",
                "4 ounces semi-sweet chocolate",
                "2 large eggs",
                "2 large egg yolks",
                "1/4 cup sugar",
                "2 teaspoons flour"
            ),
            instructions = listOf(
                "Preheat oven to 425°F (218°C).",
                "Melt butter and chocolate together.",
                "Whisk eggs, egg yolks, and sugar until thick.",
                "Fold in chocolate mixture and flour.",
                "Divide among ramekins and bake for 12-14 minutes."
            )
        ),
        Recipe(
            id = "2",
            title = "Strawberry Cheesecake",
            imageUrl = "https://images.unsplash.com/photo-1533134242443-d4fd215305ad?q=80&w=2070&auto=format&fit=crop",
            ingredients = listOf(
                "2 cups graham cracker crumbs",
                "1/2 cup melted butter",
                "4 (8 oz) packages cream cheese",
                "1 1/4 cups sugar",
                "1 cup sour cream",
                "1 tablespoon vanilla extract",
                "4 eggs",
                "2 cups sliced strawberries"
            ),
            instructions = listOf(
                "Mix crumbs and butter; press into pan.",
                "Beat cream cheese and sugar until smooth.",
                "Add sour cream and vanilla.",
                "Add eggs one at a time, beating just until combined.",
                "Bake at 325°F (163°C) for 1 hour.",
                "Top with strawberries after cooling."
            )
        ),
        Recipe(
            id = "3",
            title = "Apple Pie",
            imageUrl = "https://images.unsplash.com/photo-1568571780765-9276ac8b75a2?q=80&w=2042&auto=format&fit=crop",
            ingredients = listOf(
                "Pastry for double-crust pie",
                "6 cups thinly sliced peeled apples",
                "3/4 cup sugar",
                "2 tablespoons all-purpose flour",
                "3/4 teaspoon ground cinnamon",
                "1/8 teaspoon ground nutmeg",
                "1 tablespoon lemon juice"
            ),
            instructions = listOf(
                "Preheat oven to 425°F (218°C).",
                "Mix apples, sugar, flour, spices, and lemon juice.",
                "Line pie plate with bottom crust; add filling.",
                "Cover with top crust and seal edges.",
                "Bake for 40-45 minutes until golden brown."
            )
        ),
        Recipe(
            id = "4",
            title = "Tiramisu",
            imageUrl = "https://images.unsplash.com/photo-1571877227200-a0d98ea607e9?q=80&w=1974&auto=format&fit=crop",
            ingredients = listOf(
                "6 egg yolks",
                "3/4 cup sugar",
                "2/3 cup milk",
                "1 1/4 cups heavy cream",
                "1/2 teaspoon vanilla extract",
                "1 pound mascarpone cheese",
                "1/4 cup strong brewed coffee",
                "2 tablespoons rum",
                "2 (3 oz) packages ladyfingers",
                "1 tablespoon cocoa powder"
            ),
            instructions = listOf(
                "Whisk egg yolks and sugar; add milk and cook until boiling.",
                "Beat heavy cream and vanilla until stiff peaks form.",
                "Whisk mascarpone into yolk mixture; fold in whipped cream.",
                "Combine coffee and rum; dip ladyfingers briefly.",
                "Layer ladyfingers and cream; dust with cocoa powder.",
                "Refrigerate for at least 6 hours."
            )
        ),
        Recipe(
            id = "5",
            title = "Lemon Bars",
            imageUrl = "https://images.unsplash.com/photo-1551106652-a5bcf4b29ab6?q=80&w=1965&auto=format&fit=crop",
            ingredients = listOf(
                "2 cups all-purpose flour",
                "1/2 cup powdered sugar",
                "1 cup butter, softened",
                "4 eggs",
                "2 cups white sugar",
                "1/4 cup all-purpose flour",
                "2 lemons, juiced"
            ),
            instructions = listOf(
                "Preheat oven to 350°F (175°C).",
                "Mix 2 cups flour, powdered sugar, and butter; press into pan.",
                "Bake for 20 minutes.",
                "Whisk eggs, sugar, 1/4 cup flour, and lemon juice.",
                "Pour over crust and bake for another 20-25 minutes.",
                "Cool and dust with powdered sugar."
            )
        )
    )

    fun getRecipes(): Flow<List<Recipe>> = flowOf(sampleRecipes)

    fun getRecipeById(id: String): Recipe? = sampleRecipes.find { it.id == id }
}
