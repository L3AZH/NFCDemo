package com.l3azh.nfcdemo

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.l3azh.nfcdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object{
        val TAG = MainActivity::class.java.simpleName
    }
    lateinit var binding: ActivityMainBinding
    lateinit var nfcAdapter: NfcAdapter
    lateinit var pendingIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        if (nfcAdapter == null) {
            Snackbar.make(binding.root, "This device not support nfc", Snackbar.LENGTH_LONG).show()
            finish()
        }

        readFromIntent(intent)

        pendingIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
        )
        val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        readFromIntent(intent!!)
    }

    private fun readFromIntent(intent: Intent) {
        if(NfcAdapter.ACTION_TAG_DISCOVERED == intent.action){
            val myTag:Tag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag
            val rawMsg = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            var msg = mutableListOf<NdefMessage>()
            if(rawMsg != null){
                Log.e(TAG, "readFromIntent: ===============")
                for(i in rawMsg.indices){
                    msg.add(i, rawMsg[i] as NdefMessage)
                }
                Log.e(TAG, "readFromIntent: ===============")
            }
        }
    }
}