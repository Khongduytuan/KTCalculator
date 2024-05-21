package com.eagletech.kt.ktcalculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.eagletech.kt.ktcalculator.databinding.ActivityMainBinding
import net.objecthunter.exp4j.ExpressionBuilder
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myData: ManagerData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myData = ManagerData.getInstance(this)
        enterText()
    }

    private fun enterText() {
        binding.clear.setOnClickListener {
            binding.tvIn.text = ""
            binding.tvOut.text = ""
        }

        binding.left.setOnClickListener {
            binding.tvIn.text = toInput("(")
        }
        binding.right.setOnClickListener {
            binding.tvIn.text = toInput(")")
        }
        binding.number0.setOnClickListener {
            binding.tvIn.text = toInput("0")
        }
        binding.number1.setOnClickListener {
            binding.tvIn.text = toInput("1")
        }
        binding.number2.setOnClickListener {
            binding.tvIn.text = toInput("2")
        }
        binding.number3.setOnClickListener {
            binding.tvIn.text = toInput("3")
        }
        binding.number4.setOnClickListener {
            binding.tvIn.text = toInput("4")
        }
        binding.number5.setOnClickListener {
            binding.tvIn.text = toInput("5")
        }
        binding.number6.setOnClickListener {
            binding.tvIn.text = toInput("6")
        }
        binding.number7.setOnClickListener {
            binding.tvIn.text = toInput("7")
        }
        binding.number8.setOnClickListener {
            binding.tvIn.text = toInput("8")
        }
        binding.number9.setOnClickListener {
            binding.tvIn.text = toInput("9")
        }
        binding.dot.setOnClickListener {
            binding.tvIn.text = toInput(".")
        }
        binding.div.setOnClickListener {
            binding.tvIn.text = toInput("÷") // ALT + 0247
        }
        binding.mul.setOnClickListener {
            binding.tvIn.text = toInput("×") // ALT + 0215
        }
        binding.sub.setOnClickListener {
            binding.tvIn.text = toInput("-")
        }
        binding.add.setOnClickListener {
            binding.tvIn.text = toInput("+")
        }

        binding.delete.setOnClickListener {
            deleteLastCharacter()
        }

        binding.topBar.dowloadIcon.setOnClickListener{
            showInfoBuy()
        }

        binding.topBar.menuIcon.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)

        }
        binding.equal.setOnClickListener {
            if (myData.getB() > 0 || myData.isPremium == true) {
                result()
            } else {
                Toast.makeText(
                    this,
                    "Please buy usage",
                    Toast.LENGTH_LONG
                ).show()
            }
            binding.tvIn.text = ""
        }
    }

    private fun toInput(value: String): String {
        return "${binding.tvIn.text}$value"
    }

    private fun deleteLastCharacter() {
        val currentText = binding.tvIn.text.toString()
        if (currentText.isNotEmpty()) {
            binding.tvIn.text = currentText.substring(0, currentText.length - 1)
        }
    }

    private fun checkFormatText(): String {
        var textFormat = binding.tvIn.text.replace(Regex("÷"), "/")
        textFormat = textFormat.replace(Regex("×"), "*")
        return textFormat
    }


    private fun result() {
        try {
            val data = checkFormatText()
            val res = checkExp(data)
            if (res != Double.NaN) {
                binding.tvOut.text = DecimalFormat("0.######").format(res).toString()
                binding.tvOut.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.green
                    )
                )
                myData.removeB()
            } else {
                binding.tvOut.text = "Error"
                binding.tvOut.setTextColor(ContextCompat.getColor(this, R.color.redText))
            }

        } catch (e: Exception) {
            binding.tvOut.text = "Error"
            binding.tvOut.setTextColor(ContextCompat.getColor(this, R.color.redText))
        }
    }

    private fun checkExp(exp: String): Double {
        return try {
            val e = ExpressionBuilder(exp).build()
            e.evaluate()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show()
            Double.NaN
        } catch (e: ArithmeticException) {
            e.printStackTrace()
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT)
                .show()
            Double.NaN
        }
    }


    private fun showInfoBuy() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Your usage")
            .setPositiveButton("Confirm") { dialog, _ -> dialog.dismiss() }
            .create()
        if (myData.isPremium == true) {
            dialog.setMessage("Successfully registered")
        } else {
            dialog.setMessage("You have ${myData.getB()} turns")
        }
        dialog.show()
    }

}