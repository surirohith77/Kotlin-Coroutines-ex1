package com.rs.coroutines_ex1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rs.coroutines_ex1.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tv.setOnClickListener {
            //setNewText("Click!")

            CoroutineScope(IO).launch {
                fakeApiRequest()
            }
        }

    }


    private suspend fun fakeApiRequest() {
       // logThread("fakeApiRequest")

        val result1 = getResult1FromApi() // wait until job is done
        println("debug $result1")

       // binding.tv.setText(" $result1")
        // app will crash bcoz we are running in bg thread

        // to fix this we are using below fun which shows the data in main thread
        setTextOnMainThread(result1)

        val result2 = getResult2FromApi(result1) // wait until job is done
        setTextOnMainThread("Got $result2")
    }


    private suspend fun getResult1FromApi(): String {
        logThread("getResult1FromApi")
        delay(10000) // Does not block thread. Just suspends the coroutine inside the thread
        return "Result #1"
    }



    private fun logThread(methodName: String){
        println("debug: ${methodName}: ${Thread.currentThread().name}")
    }

    private fun setNewText(input: String){
        val newText = binding.tv.text.toString() + "\n$input"
        binding.tv.text = newText
    }

    private suspend fun setTextOnMainThread(input: String) {
        // this will move the coroutine from bg to main thread, it will fix the crash
        withContext (Main) {
            setNewText(input)
        }
    }

    private suspend fun getResult2FromApi(result1 : String): String {
        logThread("getResult2FromApi $result1")
        delay(1000)
        return "Result #2"
    }
}