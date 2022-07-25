package com.suhaozdemir.kotlincalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {

    private var textInput : TextView? = null
    private var btnClear : Button? = null
    private var btnDelete : Button? = null
    private var btnDot : Button? = null
    private var btnPlus : Button? = null
    private var btnMinus : Button? = null
    private var btnMultiply : Button? = null
    private var btnDivide : Button? = null
    private var btnPercentage : Button? = null
    private var btnEquals : Button? = null
    private var btnNegative : Button? = null
    private var isLastNumeric : Boolean = false
    private var isOperator : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textInput = findViewById(R.id.txtInput)
        btnClear = findViewById(R.id.btnClear)
        btnDelete = findViewById(R.id.btnDelete)
        btnDot = findViewById(R.id.btnDot)
        btnPlus = findViewById(R.id.btnPlus)
        btnMinus = findViewById(R.id.btnMinus)
        btnMultiply = findViewById(R.id.btnMultiply)
        btnDivide = findViewById(R.id.btnDivide)
        btnPercentage = findViewById(R.id.btnPercentage)
        btnEquals = findViewById(R.id.btnEquals)
        btnNegative = findViewById(R.id.btnNegative)

        btnClear?.setOnClickListener {
            onClear()
        }

        btnDelete?.setOnClickListener {
            onDelete()
        }

        btnDot?.setOnClickListener {
            onDecimalPoint(textInput?.text.toString())
        }

        btnPlus?.setOnClickListener {
            onOperator("+")
        }

        btnMinus?.setOnClickListener {
            onOperator("-")
        }

        btnMultiply?.setOnClickListener {
            onOperator("x")
        }

        btnDivide?.setOnClickListener {
            onOperator("/")
        }

        btnPercentage?.setOnClickListener {
            onOperator("%")
        }

        btnEquals?.setOnClickListener {
            onEquals()
        }

        btnNegative?.setOnClickListener {
            if(textInput?.text.toString().isNotEmpty() && !isOperator)
            textInput?.text = removeZeroAfterOperation((textInput?.text.toString().toDouble() * -1).toString())

        }
    }

    fun onDigit(view:View){
        textInput?.append((view as Button).text)
        isLastNumeric = true
    }

    private fun onClear(){
        textInput?.text = ""
        isLastNumeric = false
        isOperator = false
    }

    private fun onDelete(){
        textInput?.text = textInput?.text?.dropLast(1)
    }

    private fun onDecimalPoint(value : String){
        if(!value.contains(".") && value.isNotEmpty()){
            textInput?.append(".")
            isLastNumeric = false
        }
    }

    private fun isOperatorAdded(value : String) : Boolean{
        return if (value.startsWith("-")){
           false
        }else{
            value.contains("+")
                    || value.contains("-")
                    || value.contains("x")
                    || value.contains("/")
                    || value.contains("%")
        }
    }

    private fun onOperator(value : String){
        textInput?.text?.let {
            if(it.isNotEmpty() && !isOperatorAdded(it.toString()) && isLastNumeric){
                textInput?.append(value)
                isLastNumeric = false
                isOperator = true
            }
        }
    }

    private fun splitOperation(splitValue: List<String>, prefix: String) : List<String>{
        var numberOne = splitValue[0]
        val numberTwo = splitValue[1]

        if(prefix.isNotEmpty())
        {
            numberOne = prefix + numberOne
        }

        return listOf(numberOne, numberTwo)
    }

    private fun findOperator(value : String) : String{
        var operator = "+"

        if(value.contains("+"))
            operator = "+"
        if(value.contains("-"))
            operator = "-"
        if(value.contains("x"))
            operator = "x"
        if(value.contains("/"))
            operator = "/"
        if(value.contains("%"))
            operator = "%"

        return operator
    }


    private fun onEquals(){
        var value = textInput?.text.toString()
        var prefix = ""

        if(isLastNumeric && isOperator){
            try {
                val operator = findOperator(value)

                if(value.startsWith("-")){
                    prefix = "-"
                    value = value.substring(1)
                }

                val splitValue = splitOperation(value.split(operator), prefix)
                val result = when(operator){
                    "+" -> splitValue[0].toDouble() + splitValue[1].toDouble()
                    "-" -> splitValue[0].toDouble() - splitValue[1].toDouble()
                    "x" -> splitValue[0].toDouble() * splitValue[1].toDouble()
                    "/" -> splitValue[0].toDouble() / splitValue[1].toDouble()
                    else -> "ERROR"
                }

                textInput?.text = removeZeroAfterOperation(result.toString())
                isOperator = false


            }catch (e: ArithmeticException){
                e.printStackTrace()
            }
        }
        else if(!isLastNumeric && findOperator(value) == "%"){
            val splitValue = splitOperation(value.split("%"), prefix)
            val result = splitValue[0].toDouble() / 100

            textInput?.text = removeZeroAfterOperation(result.toString())
        }
    }

    private fun removeZeroAfterOperation(result: String) : String{
        var value = result
        if(result.endsWith(".0"))
            value = result.substring(0, result.length-2)

        return value
    }
}