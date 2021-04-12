package com.example.lab2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), View.OnClickListener, View.OnLongClickListener {

    private var currentOperation: String = ""
    var result: Float = 0f
    private var format: String = "Float"
    private val settingsRequestCode = 0x01

//rewerwerwe
    //utyrtyrty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner: Spinner = findViewById(R.id.operationSpinner)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val op1Button = findViewById<Button>(R.id.firstOperationButton)
                val op2Button = findViewById<Button>(R.id.secondOperationButton)
                when (position) {
                    0 -> {
                        op1Button.text = "+"
                        op2Button.text = "-"
                    }
                    1 -> {
                        op1Button.text = "*"
                        op2Button.text = "/"
                    }
                    else -> {
                        op1Button.text = "+"
                        op2Button.text = "-"
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        val calcButton: Button = findViewById(R.id.calcButton)
        calcButton.setOnClickListener(this)

        calcButton.setOnLongClickListener(this)

        val fab = findViewById<FloatingActionButton>(R.id.clearButton)
        fab.setOnClickListener {
            clearCalculator()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settingsItem -> startSettingsActivity()
            R.id.shareItem -> shareResult()
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun shareResult() {
    val shareIntent: Intent = Intent(Intent.ACTION_SEND)
        shareIntent.setType("text/plain")
            .putExtra(Intent.EXTRA_TEXT,getExpressionResult())
        if(shareIntent.resolveActivity(packageManager) != null){
            startActivity(shareIntent)
        }
    }

    private fun getExpressionResult(): String? {
        val firstNum = (findViewById<EditText>(R.id.firstNumber).text).toString().toFloatOrNull() ?: 0f
        val secondNum = (findViewById<EditText>(R.id.secondNumber).text).toString().toFloatOrNull() ?: 0f
        val equation = "${firstNum} $currentOperation ${secondNum} = "
        return when (currentOperation) {
            "+" -> "$equation ${firstNum + secondNum}"
            "-" -> "$equation ${firstNum - secondNum}"
            "*" -> "$equation ${firstNum * secondNum}"
            "/" -> "$equation ${firstNum / secondNum}"
            else -> "error"
        }
    }

    private fun startSettingsActivity() {
        val intent: Intent = Intent(this, SettingsActivity::class.java)
        intent.putExtra(getString(R.string.numberFormat), format)
        startActivityForResult(intent, settingsRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == settingsRequestCode && resultCode == RESULT_OK){
            format = data?.getStringExtra(getString(R.string.numberFormat)) ?: "Float"

        }
        super.onActivityResult(requestCode, resultCode, data)

    }
    private fun clearCalculator() {
        currentOperation = " "
        result = 0f
        updateOperation(currentOperation)
        updateResult(getString(R.string.result))
        findViewById<EditText>(R.id.firstNumber).text.clear()
        findViewById<EditText>(R.id.secondNumber).text.clear()
        Snackbar.make(
            findViewById(R.id.mainContainer),
            getString(R.string.clear_msg),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    fun selectOperation(view: View) {
        currentOperation = (view as Button).text.toString()
        updateOperation(currentOperation)
    }

    private fun updateOperation(operation: String) {
        val operationTxt = findViewById<TextView>(R.id.operatorSymbol)
        operationTxt.text = operation
    }

    override fun onClick(p0: View?) {
        val firstNum = (findViewById<EditText>(R.id.firstNumber).text).toString().toFloatOrNull()
            ?: 0f
        val secondNum = (findViewById<EditText>(R.id.secondNumber).text).toString().toFloatOrNull()
            ?: 0f
        val resultStr: String = getResult(firstNum, secondNum)
        updateResult(resultStr)
    }

    private fun updateResult(resultStr: String) {
        val resultTextView = findViewById<TextView>(R.id.resultTxtView)
        resultTextView.text = resultStr
    }

    private fun getResult(
        firstNum: Float,
        secondNum: Float,
        updateResult: Boolean = false
    ): String {

        val prevResult = if (updateResult) {
            result
        } else {
            if (currentOperation.equals("/") || currentOperation.equals("*")) 1f else 0f

        }
        result = when (currentOperation) {
            "+" -> prevResult + firstNum + secondNum
            "-" -> prevResult - firstNum - secondNum
            "*" -> prevResult * firstNum * secondNum
            "/" -> prevResult / firstNum / secondNum
            else -> 0f

        }

        return "${getString(R.string.result)} $result"
    }

    override fun onLongClick(v: View?): Boolean {

        val firstNum = (findViewById<EditText>(R.id.firstNumber).text).toString().toFloatOrNull()
            ?: 0f
        val secondNum = (findViewById<EditText>(R.id.secondNumber).text).toString().toFloatOrNull()
            ?: 0f
        val resultStr: String = getResult(firstNum, secondNum, true)
        updateResult(resultStr)

        return true
    }
}