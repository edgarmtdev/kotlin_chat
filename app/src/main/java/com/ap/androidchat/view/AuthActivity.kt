package com.ap.androidchat.view

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ap.androidchat.R
import com.ap.androidchat.viewmodel.AuthViewModel

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = AuthViewModel()


        viewModel.isLoggedIn.observe(this, Observer {
            if(it){
                val intent = Intent(this, ChatsActivity::class.java)

                startActivity(intent)
            }
        })

        setContent {
            LoginScreen() { email, password, name, picture, isLogin ->
                viewModel.auth(email = email, password = password, isLogin = isLogin, name = name, profilePic = picture)
            }
        }
    }
}

@Composable
fun LoginScreen(auth:(email: String, password: String, name:String, picture: String, isLogin:Boolean) -> Unit) {
    val email = remember {
        mutableStateOf("")
    }
    val password = remember {
        mutableStateOf("")
    }
    val name = remember {
        mutableStateOf("")
    }
    val picture = remember {
        mutableStateOf("")
    }
    val isLogin = remember {
        mutableStateOf(true)
    }
    val context = LocalContext.current
    val viewModel = AuthViewModel()

    Scaffold(backgroundColor = colorResource(id = R.color.secondaryDarkColor)) {
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.social),
                contentDescription = "image",
                Modifier
                    .padding(top = 50.dp))
            Card(
                Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .padding(top = 70.dp,),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(40.dp)
                        .height(120.dp)
                ) {
                  Row() {
                      if (isLogin.value) {
                          Text(
                              text = "Login",
                              fontWeight = FontWeight.Medium,
                              fontSize = 32.sp)
                      } else {
                          Text(
                              text = "Register",
                              fontWeight = FontWeight.Medium,
                              fontSize = 32.sp)
                      }
                      Spacer(modifier = Modifier.width(64.dp))
                      Switch(checked = isLogin.value, onCheckedChange = {
                          isLogin.value = it
                      })
                  }
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(top = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        EmailText(
                            value = email.value){  newValue ->
                            email.value = newValue
                        }

                        PasswordText(value = password.value){ newValue ->
                            password.value = newValue
                        }

                        if (!isLogin.value) {
                            NameField(value = name.value, changed = {
                                name.value = it
                            })

                            PicField(value = picture.value, changed =  {
                                picture.value = it
                            })
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            auth(email.value, password.value, name.value, picture.value, isLogin.value)
                            showToastMessage(context,"Session iniciada correctamente")
                        })
                        {
                            if(isLogin.value){
                                Text("Iniciar sesión")
                            }else{
                                Text("Registrate")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmailText(value: String, changed: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = changed,
        label = { Text("Email") },
        placeholder = { Text(text = "Escribe tu email")},
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun PasswordText(value: String, changed: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = changed,
        label = { Text("Password") },
        placeholder = { Text(text = "Escribe tu password")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun NameField(value: String, changed: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = changed,
        label = { Text("Name") },
        placeholder = { Text(text = "Escribe tu nombre")},
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun PicField(value: String, changed: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = changed,
        label = { Text("Profile Pic") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth()
    )
}

private fun showToastMessage(context: Context, message:String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthActivity() {
    LoginScreen { email, password, isLogin, name, picture -> }
}