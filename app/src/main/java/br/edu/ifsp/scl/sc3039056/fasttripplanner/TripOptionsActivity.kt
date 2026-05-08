package br.edu.ifsp.scl.sc3039056.fasttripplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.scl.sc3039056.fasttripplanner.databinding.ActivityTripOptionsBinding

class TripOptionsActivity : AppCompatActivity() {

    // viewBinding: gera referências diretas aos componentes do layout activity_trip_options.xml
    private val activityTripOptionsBinding: ActivityTripOptionsBinding by lazy {
        ActivityTripOptionsBinding.inflate(layoutInflater)
    }

    // Armazena os dados recebidos da MainActivity para repassar à próxima tela
    private var destino   = ""
    private var dias      = 0
    private var orcamento = 0.0
    private var modoEconomico = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(activityTripOptionsBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Recebe os dados enviados pela MainActivity via Intent
        destino   = intent.getStringExtra("EXTRA_DESTINO") ?: ""
        dias      = intent.getIntExtra("EXTRA_DIAS", 0)
        orcamento = intent.getDoubleExtra("EXTRA_ORCAMENTO", 0.0)
        // Restaura o estado dos componentes após rotação de tela
        if (savedInstanceState != null) {
            val hospedagemId = savedInstanceState.getInt("hospedagem_id", -1)
            if (hospedagemId != -1) activityTripOptionsBinding.hospedagemRg.check(hospedagemId)
            activityTripOptionsBinding.transporteCb.isChecked  = savedInstanceState.getBoolean("transporte")
            activityTripOptionsBinding.alimentacaoCb.isChecked = savedInstanceState.getBoolean("alimentacao")
            activityTripOptionsBinding.passeiosCb.isChecked    = savedInstanceState.getBoolean("passeios")
            activityTripOptionsBinding.modoEconomicoCb.isChecked = savedInstanceState.getBoolean("modoeconomico")
        } else {
            // Primeira abertura: define econômica como opção padrão
            activityTripOptionsBinding.economicaRb.isChecked = true
        }

        activityTripOptionsBinding.modoEconomicoCb.setOnClickListener {
            modoEconomico = activityTripOptionsBinding.modoEconomicoCb.isChecked
            if (modoEconomico) {
                activityTripOptionsBinding.economicaRb.isChecked = true
                activityTripOptionsBinding.passeiosCb.isChecked = false
                activityTripOptionsBinding.passeiosCb.isEnabled = false
                activityTripOptionsBinding.economicaRb.isEnabled = false
                activityTripOptionsBinding.confortoRb.isEnabled = false
                activityTripOptionsBinding.luxoRb.isEnabled = false
                activityTripOptionsBinding.economicaRb.isChecked = true
            }else{
                activityTripOptionsBinding.passeiosCb.isEnabled = true
                activityTripOptionsBinding.economicaRb.isEnabled = true
                activityTripOptionsBinding.confortoRb.isEnabled = true
                activityTripOptionsBinding.luxoRb.isEnabled = true
            }
        }



        activityTripOptionsBinding.calcularBt.setOnClickListener {
            // Validação: verifica se algum tipo de hospedagem foi selecionado
            if (activityTripOptionsBinding.hospedagemRg.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Selecione o tipo de hospedagem", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Converte o id do RadioButton selecionado para uma string identificadora
            val hospedagem = when (activityTripOptionsBinding.hospedagemRg.checkedRadioButtonId) {
                R.id.economica_rb -> "economica"
                R.id.conforto_rb  -> "conforto"
                R.id.luxo_rb      -> "luxo"
                else              -> "economica"
            }

            // Cria a Intent explícita para a Tela 3 e envia todos os dados como extras
            val tripSummaryIntent = Intent(this, TripSummaryActivity::class.java)
            tripSummaryIntent.putExtra("EXTRA_DESTINO",     destino)
            tripSummaryIntent.putExtra("EXTRA_DIAS",        dias)
            tripSummaryIntent.putExtra("EXTRA_ORCAMENTO",   orcamento)
            tripSummaryIntent.putExtra("EXTRA_HOSPEDAGEM",  hospedagem)
            tripSummaryIntent.putExtra("EXTRA_TRANSPORTE",  activityTripOptionsBinding.transporteCb.isChecked)
            tripSummaryIntent.putExtra("EXTRA_ALIMENTACAO", activityTripOptionsBinding.alimentacaoCb.isChecked)
            tripSummaryIntent.putExtra("EXTRA_PASSEIOS",    activityTripOptionsBinding.passeiosCb.isChecked)
            tripSummaryIntent.putExtra("EXTRA_ECONOMICO", modoEconomico)
            startActivity(tripSummaryIntent)
        }

        // Volta para a Tela 1 removendo esta Activity da pilha
        activityTripOptionsBinding.voltarBt.setOnClickListener {
            finish()
        }
    }

    // Salva o estado dos componentes antes da rotação de tela
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("hospedagem_id", activityTripOptionsBinding.hospedagemRg.checkedRadioButtonId)
        outState.putBoolean("transporte",  activityTripOptionsBinding.transporteCb.isChecked)
        outState.putBoolean("alimentacao", activityTripOptionsBinding.alimentacaoCb.isChecked)
        outState.putBoolean("passeios",    activityTripOptionsBinding.passeiosCb.isChecked)
    }
}