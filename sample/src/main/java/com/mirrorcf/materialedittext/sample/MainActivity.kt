package com.mirrorcf.materialedittext.sample

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.mirrorcf.materialedittext.MaterialEditText
import com.mirrorcf.materialedittext.validation.RegexpValidator

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        initEnableBt()
        initSingleLineEllipsisEt()
        initSetErrorEt()
        initValidationEt()
    }

    private fun initEnableBt() {
        val basicEt = findViewById<View>(R.id.basicEt) as EditText
        val enableBt = findViewById<View>(R.id.enableBt) as Button
        enableBt.setOnClickListener {
            basicEt.isEnabled = !basicEt.isEnabled
            enableBt.text = if (basicEt.isEnabled) "DISABLE" else "ENABLE"
        }
    }

    private fun initSingleLineEllipsisEt() {
        val singleLineEllipsisEt = findViewById<View>(R.id.singleLineEllipsisEt) as EditText
        singleLineEllipsisEt.setSelection(singleLineEllipsisEt.text.length)
    }

    private fun initSetErrorEt() {
        val bottomTextEt = findViewById<View>(R.id.bottomTextEt) as EditText
        val setErrorBt = findViewById<View>(R.id.setErrorBt) as Button
        setErrorBt.setOnClickListener { bottomTextEt.error = "1-line Error!" }
        val setError2Bt = findViewById<View>(R.id.setError2Bt) as Button
        setError2Bt.setOnClickListener { bottomTextEt.error = "2-line\nError!" }
        val setError3Bt = findViewById<View>(R.id.setError3Bt) as Button
        setError3Bt.setOnClickListener { bottomTextEt.error = "So Many Errors! So Many Errors! So Many Errors! So Many Errors! So Many Errors! So Many Errors! So Many Errors! So Many Errors!" }
    }

    private fun initValidationEt() {
        val validationEt = findViewById<View>(R.id.validationEt) as MaterialEditText
        validationEt.addValidator(RegexpValidator("Only Integer Valid!", "\\d+"))
        val validateBt = findViewById<View>(R.id.validateBt) as Button
        validateBt.setOnClickListener { // validate
            validationEt.validate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId
        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)
    }
}