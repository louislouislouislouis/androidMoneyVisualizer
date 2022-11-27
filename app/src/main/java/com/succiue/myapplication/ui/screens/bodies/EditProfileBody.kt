package com.succiue.myapplication.ui.screens.bodies

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.succiue.myapplication.ui.viewmodels.LoginViewModel


@Composable
fun EditProfileBody(
    modifier: Modifier,
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

        Text(
            text = "Informations - click to edit",
            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
        )

        EditInfoCard(name)

        EditInfoCard("email.adress@gmail.com")

        Text(text = "Banques", modifier = Modifier.padding(top = 30.dp, bottom = 5.dp))

        BankCard(text = name)

        AddBank()

        Button(onClick = onNextButtonClicked) {
            Text(text = "Save Changes")
        }
    }
}

@Composable
fun EditInfoCard(text: String) {
    ElevatedCard(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        TextField(
            description = text,
        )
    }
}

@Composable
fun TextField(description: String) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    androidx.compose.material.TextField(
        value = text,
        onValueChange = {
            text = it
        },
        //placeholder = { androidx.compose.material.Text(stringResource(description)) },
        placeholder = { Text(description) },
        singleLine = true,
        colors = TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.secondary,
            disabledTextColor = MaterialTheme.colorScheme.secondary,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent
        )
    )
}

@Composable
fun AddBank() {
    ElevatedCard(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Icon(
                Icons.Filled.Add,
                modifier = Modifier
                    .size(24.dp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Text(text = "Add a Bank", modifier = Modifier.padding(start = 10.dp))

        }
    }
}