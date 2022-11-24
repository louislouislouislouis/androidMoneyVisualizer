package com.succiue.myapplication.ui.screens.bodies

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.succiue.myapplication.R
import com.succiue.myapplication.ui.viewmodels.LoginViewModel


@Composable
fun ProfileBody(
    navController: NavHostController,
    viewModel: LoginViewModel,
    context: Context = LocalContext.current.applicationContext,
    name: String
) {
    val uiState = viewModel.uiState

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        GreetingSection("Profil")

        UserInfos(context = context)

        InfoCard(text = name)

        InfoCard(text = "Adresse mail")

        Spacer(modifier = Modifier.height(20.dp))
        
        Text(text = "Banques", modifier = Modifier.padding(10.dp))

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
private fun UserInfos(context: Context) {

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

        Surface() {
            Row(
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Edit Profile",
                    style = TextStyle(
                        fontSize = 15.sp,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Edit Button", Toast.LENGTH_SHORT).show()
                    }
                )

                // Edit button
                IconButton(
                    modifier = Modifier
                        .weight(weight = 1f, fill = false),
                    onClick = {
                        Toast.makeText(context, "Edit Button", Toast.LENGTH_SHORT).show()
                    }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit Details",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
