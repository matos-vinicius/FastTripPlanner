package br.edu.ifsp.scl.sc3039056.fasttripplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.scl.sc3039056.fasttripplanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // viewBinding: gera referências diretas aos componentes do layout activity_main.xml
    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita o modo edge-to-edge para ocupar toda a tela, incluindo barras do sistema
        enableEdgeToEdge()
        setContentView(activityMainBinding.root)
        // Ajusta o padding para que o conteúdo não fique atrás das barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        activityMainBinding.avancarBt.setOnClickListener {
            // Lê e remove espaços extras dos campos de entrada
            val destino = activityMainBinding.destinoEt.text.toString().trim()
            val diasStr = activityMainBinding.diasEt.text.toString().trim()
            val orcamentoStr = activityMainBinding.orcamentoEt.text.toString().trim()

            // Validação: verifica se os campos foram preenchidos
            if (destino.isEmpty()) {
                Toast.makeText(this, "Informe o destino", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (diasStr.isEmpty()) {
                Toast.makeText(this, "Informe o número de dias", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (orcamentoStr.isEmpty()) {
                Toast.makeText(this, "Informe o orçamento diário", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dias = diasStr.toInt()
            val orcamento = orcamentoStr.toDouble()

            // Validação: verifica se os valores numéricos são positivos
            if (dias <= 0) {
                Toast.makeText(this, "Número de dias deve ser maior que zero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (orcamento <= 0) {
                Toast.makeText(this, "Orçamento deve ser maior que zero", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Cria a Intent explícita para a Tela 2 e envia os dados como extras
            val tripOptionsIntent = Intent(this, TripOptionsActivity::class.java)
            tripOptionsIntent.putExtra("EXTRA_DESTINO", destino)
            tripOptionsIntent.putExtra("EXTRA_DIAS", dias)
            tripOptionsIntent.putExtra("EXTRA_ORCAMENTO", orcamento)
            startActivity(tripOptionsIntent)
        }
    }
}