package com.amonteiro.a25_05_ampere.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amonteiro.a25_05_ampere.R
import com.amonteiro.a25_05_ampere.model.PictureBean
import com.amonteiro.a25_05_ampere.ui.MyError
import com.amonteiro.a25_05_ampere.ui.theme._25_05_ampereTheme
import com.amonteiro.a25_05_ampere.viewmodel.MainViewModel
import com.amonteiro.a25_05_ampere.viewmodel.MainViewModelPreview
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder

//Code affiché dans la Preview, thème claire, thème sombre
@Preview(showBackground = true, showSystemUi = true)
@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES or android.content.res.Configuration.UI_MODE_TYPE_NORMAL,
    locale = "fr"
)
@Composable
fun SearchScreenPreview() {
    _25_05_ampereTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

            val mainViewModel = MainViewModelPreview()
            mainViewModel.loadFakeData(false, "")

            SearchScreen(modifier = Modifier.padding(innerPadding), mainViewModel= mainViewModel)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES or android.content.res.Configuration.UI_MODE_TYPE_NORMAL,
    locale = "fr"
)
@Composable
fun SearchScreenAllDataPreview() {
    _25_05_ampereTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

            val mainViewModel = MainViewModelPreview()
            mainViewModel.loadFakeData(true, "Une erreur")

            SearchScreen(modifier = Modifier.padding(innerPadding), mainViewModel= mainViewModel)
        }
    }
}

//Composable représentant l'ensemble de l'écran
@Composable
fun SearchScreen(modifier: Modifier = Modifier, mainViewModel: MainViewModel = viewModel ()) {

    var searchText by rememberSaveable { mutableStateOf("") }

    val list = mainViewModel.dataList.collectAsStateWithLifecycle().value//.filter { it.title.contains(searchText) }

    val runInProgress by mainViewModel.runInProgress.collectAsStateWithLifecycle()
    val errorMessage by mainViewModel.errorMessage.collectAsStateWithLifecycle()


    Column(modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {

        SearchBar(
            searchText = searchText,
            onValueChange = { searchText = it},
            onSearch = { mainViewModel.loadWeathers(searchText) }
        )

        MyError(errorMessage= errorMessage)

        AnimatedVisibility(visible = runInProgress){
            CircularProgressIndicator()
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
            items(list.size) {
                PictureRowItem(data = list[it])
            }
        }

        Row {

            Button(
                onClick = { searchText = "" },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    Icons.Filled.Clear,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.clear_filter))
            }
            Button(
                onClick = { mainViewModel.loadWeathers(searchText) },
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Localized description",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(stringResource(R.string.bt_load))
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, searchText: String, onValueChange: (String) -> Unit,  onSearch: (KeyboardActionScope.() -> Unit)? = null) {
    TextField(
        value = searchText, //Valeur affichée
        onValueChange = onValueChange, //Nouveau texte entrée
        leadingIcon = { //Image d'icone
            Icon(
                imageVector = Icons.Default.Search,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null
            )
        },
        singleLine = true,
        label = { //Texte d'aide qui se déplace
            Text("Enter text")
            //Pour aller le chercher dans string.xml
            //Text(stringResource(R.string.placeholder_search))
        },
        //placeholder = { //Texte d'aide qui disparait
        //Text("Recherche")
        //},

        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search), // Définir le bouton "Entrée" comme action de recherche
        keyboardActions = KeyboardActions(onSearch = onSearch), // Déclenche l'action définie
        //Comment le composant doit se placer
        modifier = modifier
            .fillMaxWidth() // Prend toute la largeur
            .heightIn(min = 56.dp) //Hauteur minimum
    )
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable //Composable affichant 1 PictureBean
fun PictureRowItem(modifier: Modifier = Modifier, data: PictureBean) {

    var expended by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .padding(4.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .fillMaxWidth()
    ) {

        //Permission Internet nécessaire
        GlideImage(
            model = data.url,
            //Pour aller le chercher dans string.xml
            //contentDescription = getString(R.string.picture_of_cat),
            //En dur
            contentDescription = "une photo de chat",
            // Image d'attente. Permet également de voir l'emplacement de l'image dans la Preview
            loading = placeholder(R.mipmap.ic_launcher_round),
            // Image d'échec de chargement
            failure = placeholder(R.mipmap.ic_launcher),
            contentScale = ContentScale.Fit,
            //même autres champs qu'une Image classique
            modifier = Modifier
                .heightIn(max = 100.dp) //Sans hauteur il prendra tous l'écran
                .widthIn(max = 100.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expended = !expended
                }
        ) {
            Text(text = data.title, style = MaterialTheme.typography.titleLarge)

            Text(
                text = if (expended) data.longText else data.longText.take(20),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.animateContentSize()
            )

        }
    }
}