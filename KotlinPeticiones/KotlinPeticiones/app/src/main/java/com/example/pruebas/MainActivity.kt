package com.example.pruebas

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pruebas.Models.NewPost
import com.example.pruebas.Models.Post
import com.example.pruebas.Models.ResultUser
import com.example.pruebas.Models.User
import com.example.pruebas.Network.ApiConsume
import com.example.pruebas.Network.RetrofitHelper
import com.example.pruebas.Network.UsersInterface
import com.example.pruebas.ui.theme.PruebasTheme
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.log
//Crea una variable global de una lista de usuarios para despues mostrarlos en la vista
val listaUsuarios: MutableState<List<User>> = mutableStateOf(listOf())

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PruebasTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
//                    Greeting("Android")
                    LazyColumn{
                        items(listaUsuarios.value.size){ index ->
                            ListaUsuarios(listaUsuarios.value[index])
                            Divider()
                        }
                    }
                }
            }
        }
        consumirUsuario()
//        consumeAPI()
//        consumirAPIPost()
//        consumirAPIPut()
//        consumirApiDelete()
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )

}

@Composable
fun ListaUsuarios(user: User){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Nombre: ${user.name.first} ${user.name.last}",
            style = TextStyle(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "Género: ${user.gender}",
            style = TextStyle(fontStyle = FontStyle.Italic)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PruebasTheme {
        Greeting("Android")
    }
}

fun consumeAPI(){
    val apiConsume = RetrofitHelper.getInstance().create(ApiConsume::class.java)

    val call = apiConsume.getPosts()

    call.enqueue(object : retrofit2.Callback<List<Post>> {
        override fun onResponse(call: retrofit2.Call<List<Post>>, response: retrofit2.Response<List<Post>>) {
            if (response.isSuccessful) {
                val posts = response.body()
                posts?.forEach {
//                    Log.d("peticiones", "Title: ${it.title}, Body: ${it.body}")
                }
            } else {
                Log.d("errorpeticion", "Error: ${response.code()}")
            }
        }

        override fun onFailure(call: retrofit2.Call<List<Post>>, t: Throwable) {
            Log.d("errorpeticion", "Error de conexión: ${t.message}")
        }
    })
}

fun consumirAPIPut(){
    val apiConsume = RetrofitHelper.getInstance().create(ApiConsume::class.java)

    val postUpdate = Post(1, 1, "Prueba")

    val call = apiConsume.updatePost("1", postUpdate)

    call.enqueue(object : retrofit2.Callback<Post> {
        override fun onResponse(call: retrofit2.Call<Post>, response: retrofit2.Response<Post>) {
            if (response.isSuccessful) {
                val post = response.body()
                post?.let {
                    Log.d("peticiones", "Title: ${it.title}, Body: ${it.body}")
                }
            } else {
                Log.d("errorpeticion", "Error: ${response.code()}")
            }
        }

        override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
            Log.d("errorpeticion", "Error de conexión: ${t.message}")
        }
    })
}

fun consumirApiDelete(){
    val apiConsume = RetrofitHelper.getInstance().create(ApiConsume::class.java)


    val call = apiConsume.deletePost(1.toString())

    call.enqueue(object : retrofit2.Callback<Void> {
        override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
            if (response.isSuccessful) {
                Log.d("peticiones", "Post eliminado")
            } else {
                Log.d("errorpeticion", "Error: ${response.code()}")
            }
        }

        override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
            Log.d("errorpeticion", "Error de conexión: ${t.message}")
        }
    })
}

fun consumirAPIPost(){
    val apiConsume = RetrofitHelper.getInstance().create(ApiConsume::class.java)

    val newPost = NewPost("Prueba" 33)

    val call = apiConsume.newPost(newPost)

    call.enqueue(object : retrofit2.Callback<Post> {
        override fun onResponse(call: retrofit2.Call<Post>, response: retrofit2.Response<Post>) {
            if (response.isSuccessful) { 
                val post = response.body()
                post?.let {
                    Log.d("peticiones", "Title: ${it.title}, Body: ${it.body}")
                }
            } else {
                Log.d("errorpeticion", "Error: ${response.code()}")
            }
        }

        override fun onFailure(call: retrofit2.Call<Post>, t: Throwable) {
            Log.d("errorpeticion", "Error de conexión: ${t.message}")
        }
    })
}

fun consumirUsuario(){
    try {
        val apiConsume = RetrofitHelper.getInstance().create(UsersInterface::class.java)

//        val call = apiConsume.getUser()
        val call = apiConsume.getUser(20)

        call.enqueue(object : retrofit2.Callback<ResultUser> {
            override fun onResponse(call: retrofit2.Call<ResultUser>, response: retrofit2.Response<ResultUser>){
                if (response.isSuccessful) {
                    val result = response.body()
                    result?.let { body ->
                        if (body.results.isNotEmpty()) {
                            listaUsuarios.value = body.results
                            val user = body.results[0].name.first
                            Log.d("peticiones", "user $user")
                        } else {
                            Log.d("peticiones", "La lista de resultados está vacía")
                        }
                    } ?: Log.d("peticiones", "La respuesta es nula")

                } else {
                    Log.d("errorpeticion", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<ResultUser>, t: Throwable) {
                Log.d("errorpeticion", "Error de conexión: ${t.message}")
            }
        })
    }catch (ex: Exception){
        Log.d("errorpeticion", "Error de conexión: ${ex.message}")
    }


}