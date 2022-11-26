package com.succiue.myapplication.ui.screens.bodies

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.succiue.myapplication.R
import com.succiue.myapplication.ui.viewmodels.LoginViewModel


@Composable
fun ProfileBody(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    context: Context = LocalContext.current.applicationContext,
    name: String,
    onNextButtonClicked: () -> Unit
) {
    val uiState = viewModel.uiState

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        GreetingSection("Profil")

        UserInfos()

        EditButton(context = context, onNextButtonClicked)

        InfoCard(text = name)

        InfoCard(text = "Adresse mail")

        Text(text = "Banques", modifier = Modifier.padding(top = 30.dp, bottom = 10.dp))

        InfoCard(text = name)

        Button(
            enabled = !uiState.loginEnable,
            onClick = { viewModel.logout() })
        {
            Text(text = "Logout")
        }
    }
}

@Composable
fun InfoCard(text: String) {
    ElevatedCard(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text = text, modifier = Modifier.padding(10.dp))
    }
}

@Composable
fun BankCard(text: String) {
    ElevatedCard(
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
    ) {


        Card(
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {

        }
        Text(text = text, modifier = Modifier.padding(10.dp))
    }
}

@Composable
fun UserInfos() {
    val imageUri = rememberSaveable {
        mutableStateOf("")
    }

    val painter = rememberImagePainter(
        if (imageUri.value.isEmpty())
            R.drawable.logo_app_mobile
        else
            imageUri.value
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri.value = it.toString() }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
// User's image
        Image(
            modifier = Modifier
                .size(150.dp)
                .clip(shape = CircleShape)
                .clickable {
                    launcher.launch("image/*")
                },
            painter = painter,//painterResource(id = R.drawable.logo_app_mobile),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditButton(context: Context, onNextButtonClicked: () -> Unit) {
    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        onClick = onNextButtonClicked
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                modifier = Modifier,
                text = "Edit Profile",
                style = TextStyle(
                    fontSize = 15.sp,
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.primary,
            )

            // Edit button

            Icon(
                painter = painterResource(R.drawable.editer),
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 5.dp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}