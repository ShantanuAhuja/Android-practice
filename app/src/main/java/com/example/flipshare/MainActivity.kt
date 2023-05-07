package com.example.flipshare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.flipshare.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.PrintWriter
import java.lang.Thread.sleep
import java.net.Socket
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isActive by Delegates.notNull<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
        isActive=false;
    }

    override fun onResume() {
        super.onResume()
        Log.i("onResume","called")

        binding.connectbutton.setOnClickListener{
            Log.i("button","clicked")
            if(!isActive){

                  val ipAddress= binding.serverip.text.toString().trim()
                  val portNumber=binding.serverport.text.toString()
                  if(ipAddress.isEmpty() || portNumber.isEmpty()){
                      val text = "Server IP  or Port Number field is Empty"
                      val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                  }
                  else {
                      Log.i("device", Thread.currentThread().name)
                         connectServer(ipAddress, portNumber.toInt())
                  }
            }
            else {
                binding.connectbutton.text="connect"
                isActive=false
                Log.i("name",Thread.currentThread().name)
            }
        }
    }


    private fun  connectServer(ipAddress :String ,portNumber :Int) {

        CoroutineScope(Dispatchers.IO).launch {
            try {

                val output=binding.writemessage.text.toString()
                val socket = Socket(ipAddress, portNumber)
                val printwriter = PrintWriter(socket.getOutputStream(), true)
                printwriter.write(output)
                isActive=true;
                binding.connectbutton.text="disconnect"
                printwriter.flush()
                printwriter.close()
                socket.close()

           } catch (error: IOException) {

            }
        }



    }
}