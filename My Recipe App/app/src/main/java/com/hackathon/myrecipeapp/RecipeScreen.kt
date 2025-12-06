package com.hackathon.myrecipeapp

import android.text.Layout
import androidx.compose.animation.core.copy
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import java.security.AllPermission
import java.time.format.TextStyle
import androidx.compose.foundation.lazy.grid.items // <<< ADD THIS IMPORT

@Composable
fun RecipeScreen(modifier: Modifier = Modifier){
    val recipeViewModel: MainViewModel = viewModel()
    val viewstate by recipeViewModel.categoriesState

    Box(modifier = Modifier.fillMaxSize())
    {
        when{
            viewstate.loading->{
                CircularProgressIndicator(modifier.align(Alignment.Center))
            }

            viewstate.error!=null->
            {
                Text(text = "Error Loading Categories")
            }
            else ->
            {
                CategoryScreen(categories = viewstate.list)
            }
        }
    }
}

@Composable
fun CategoryScreen(categories: List<Category>)
{
    LazyVerticalGrid(GridCells.Fixed(2), modifier = Modifier.fillMaxSize())
    {
        items(categories) {
            category ->
            CategoryItem(category = category)
        }
    }
}

@Composable
fun CategoryItem(category: Category)
{

    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally)
    {
        Image(
            painter = rememberAsyncImagePainter(category.strCategoryThumb),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().aspectRatio(1f)
        )
        Text(
            text = category.strCategory,
            color = MaterialTheme.colorScheme.onSurface, // Use theme colors for better dark/light theme support
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), // <-- THE FIX
            modifier = Modifier.padding(top = 8.dp)
        )

    }
}