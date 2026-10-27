package com.example.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.NumberFormat
import java.util.Locale

data class Pizza(
    val nombre: String,
    val precio: Int
)

data class IngredienteExtra(
    val nombre: String,
    val precio: Int
)

class MainActivity : AppCompatActivity() {

    private val pizzas = listOf(
        Pizza("Pepperoni", 8490),
        Pizza("Margarita", 7490),
        Pizza("Hawaiana", 8990),
        Pizza("Napolitana", 7990)
    )

    private val extras = listOf(
        IngredienteExtra("Queso extra", 1000),
        IngredienteExtra("Champiñones", 800),
        IngredienteExtra("Tocino", 1200),
        IngredienteExtra("Aceitunas", 700)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinnerPizza = findViewById<Spinner>(R.id.spinnerPizza)
        val checkQueso = findViewById<CheckBox>(R.id.checkQueso)
        val checkChampinones = findViewById<CheckBox>(R.id.checkChampinones)
        val checkTocino = findViewById<CheckBox>(R.id.checkTocino)
        val checkAceitunas = findViewById<CheckBox>(R.id.checkAceitunas)
        val buttonCalcular = findViewById<Button>(R.id.buttonCalcular)
        val buttonLimpiar = findViewById<Button>(R.id.buttonLimpiar)
        val textResultado = findViewById<TextView>(R.id.textResultado)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            pizzas.map { pizza ->
                getString(
                    R.string.formato_pizza_spinner,
                    pizza.nombre,
                    formatearPrecio(pizza.precio)
                )
            }
        )
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinnerPizza.adapter = adapter

        val checks = listOf(
            checkQueso,
            checkChampinones,
            checkTocino,
            checkAceitunas
        )

        buttonCalcular.setOnClickListener {
            val pizzaSeleccionada = pizzas[spinnerPizza.selectedItemPosition]

            val extrasSeleccionados = extras.filterIndexed { index, _ ->
                checks[index].isChecked
            }

            val totalExtras = extrasSeleccionados.sumOf { it.precio }
            val total = pizzaSeleccionada.precio + totalExtras

            val textoExtras = if (extrasSeleccionados.isEmpty()) {
                getString(R.string.extras_ninguno)
            } else {
                val listaExtras = extrasSeleccionados.joinToString("\n") { extra ->
                    getString(R.string.formato_extra, extra.nombre)
                }
                getString(R.string.formato_extras, listaExtras)
            }

            textResultado.text = buildString {
                appendLine(
                    getString(
                        R.string.formato_pizza,
                        pizzaSeleccionada.nombre,
                        formatearPrecio(pizzaSeleccionada.precio)
                    )
                )
                appendLine(textoExtras)
                appendLine()
                append(
                    getString(
                        R.string.formato_total,
                        formatearPrecio(total)
                    )
                )
            }
        }

        buttonLimpiar.setOnClickListener {
            spinnerPizza.setSelection(0)
            checks.forEach { checkBox ->
                checkBox.isChecked = false
            }
            textResultado.text = getString(R.string.pedido_vacio)
        }
    }

    private fun formatearPrecio(precio: Int): String {
        val formato = NumberFormat.getNumberInstance(Locale("es", "CL"))
        return "$${formato.format(precio)}"
    }
}