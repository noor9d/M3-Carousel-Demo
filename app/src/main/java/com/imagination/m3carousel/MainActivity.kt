package com.imagination.m3carousel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.imagination.m3carousel.ui.theme.M3CarouselTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            M3CarouselTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DashboardContent(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun DashboardContent(
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.surface)
            .verticalScroll(rememberScrollState())
    ) {
        SearchBar(
            modifier = Modifier.padding(16.dp),
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onClear = { searchQuery = "" }
        )
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            text = "Recents",
            style = MaterialTheme.typography.titleLarge
        )
        CarouselExample_MultiBrowse()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            text = "Categories",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        CategoryGrid(modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CategoryGrid(modifier: Modifier = Modifier) {
    val categories = listOf(
        "Arts", "Fashion", "Home", "Books"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.height(160.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        userScrollEnabled = false
    ) {
        items(categories) { category ->
            CategoryItem(name = category)
        }
    }
}

@Composable
fun CategoryItem(name: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.aspectRatio(2.5f),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.onPrimary,
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(R.drawable.ic_search),
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(modifier = Modifier.size(12.dp))
            BasicTextField(
                modifier = Modifier.weight(1f),
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                decorationBox = { inner ->
                    if (query.isEmpty()) {
                        Text(
                            text = "Search...",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                    inner()
                },
            )
            if (query.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarouselExample_MultiBrowse() {
    data class CarouselItem(
        val id: Int,
        @DrawableRes val imageResId: Int,
        val contentDescription: String
    )

    val items = remember {
        listOf(
            CarouselItem(0, R.drawable.cupcake, "cupcake"),
            CarouselItem(1, R.drawable.donut, "donut"),
            CarouselItem(2, R.drawable.eclair, "eclair"),
            CarouselItem(3, R.drawable.froyo, "froyo"),
            CarouselItem(4, R.drawable.gingerbread, "gingerbread"),
            CarouselItem(5, R.drawable.cupcake, "cupcake"),
            CarouselItem(6, R.drawable.donut, "donut"),
            CarouselItem(7, R.drawable.eclair, "eclair"),
            CarouselItem(8, R.drawable.froyo, "froyo"),
            CarouselItem(9, R.drawable.gingerbread, "gingerbread"),
            CarouselItem(10, R.drawable.cupcake, "cupcake"),
            CarouselItem(11, R.drawable.donut, "donut"),
            CarouselItem(12, R.drawable.eclair, "eclair"),
            CarouselItem(13, R.drawable.froyo, "froyo"),
            CarouselItem(14, R.drawable.gingerbread, "gingerbread"),
        )
    }

    val carouselState = rememberCarouselState { items.count() }
    HorizontalMultiBrowseCarousel(
        state = carouselState,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, bottom = 16.dp),
        preferredItemWidth = 186.dp,
        itemSpacing = 8.dp,
        flingBehavior = CarouselDefaults.multiBrowseFlingBehavior(state = carouselState),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { i ->
        val item = items[i]
        Image(
            modifier = Modifier
                .height(205.dp)
                .maskClip(MaterialTheme.shapes.extraLarge),
            painter = painterResource(id = item.imageResId),
            contentDescription = item.contentDescription,
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    M3CarouselTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            DashboardContent(Modifier.padding(innerPadding))
        }
    }
}
