package br.edu.ifsp.scl.sc3039056.fasttripplanner

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.scl.sc3039056.fasttripplanner.databinding.ActivityTripSummaryBinding

class TripSummaryActivity : AppCompatActivity() {

    private val activityTripSummaryBinding: ActivityTripSummaryBinding by lazy {
        ActivityTripSummaryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(activityTripSummaryBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val destino        = intent.getStringExtra("EXTRA_DESTINO") ?: ""
        val dias           = intent.getIntExtra("EXTRA_DIAS", 0)
        val orcamento      = intent.getDoubleExtra("EXTRA_ORCAMENTO", 0.0)
        val hospedagem     = intent.getStringExtra("EXTRA_HOSPEDAGEM") ?: "economica"
        val temTransporte  = intent.getBooleanExtra("EXTRA_TRANSPORTE", false)
        val temAlimentacao = intent.getBooleanExtra("EXTRA_ALIMENTACAO", false)
        val temPasseios    = intent.getBooleanExtra("EXTRA_PASSEIOS", false)

        val multiplicador = when (hospedagem) {
            "conforto" -> 1.5
            "luxo"     -> 2.2
            else       -> 1.0
        }

        val custoBase = dias * orcamento * multiplicador
        var custoExtras = 0.0
        if (temTransporte)  custoExtras += 300.0
        if (temAlimentacao) custoExtras += 50.0 * dias
        if (temPasseios)    custoExtras += 120.0 * dias
        val custoTotal = custoBase + custoExtras

        val servicosList = mutableListOf<String>()
        if (temTransporte)  servicosList.add("Transporte")
        if (temAlimentacao) servicosList.add("Alimentação")
        if (temPasseios)    servicosList.add("Passeios")
        val servicosTexto = if (servicosList.isEmpty()) "Nenhum" else servicosList.joinToString(", ")

        val hospedagemTexto = when (hospedagem) {
            "conforto" -> "Conforto"
            "luxo"     -> "Luxo"
            else       -> "Econômica"
        }

        activityTripSummaryBinding.destinoTv.text    = "Destino: $destino"
        activityTripSummaryBinding.diasTv.text       = "Duração: $dias dia(s)"
        activityTripSummaryBinding.orcamentoTv.text  = "Orçamento diário: R$ %.2f".format(orcamento)
        activityTripSummaryBinding.hospedagemTv.text = "Hospedagem: $hospedagemTexto"
        activityTripSummaryBinding.servicosTv.text   = "Serviços: $servicosTexto"
        activityTripSummaryBinding.totalTv.text      = "Total estimado: R$ %.2f".format(custoTotal)

        activityTripSummaryBinding.reiniciarBt.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(mainIntent)
        }
    }
}