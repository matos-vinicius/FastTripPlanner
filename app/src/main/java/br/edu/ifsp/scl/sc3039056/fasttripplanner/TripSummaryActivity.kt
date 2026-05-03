package br.edu.ifsp.scl.sc3039056.fasttripplanner

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.edu.ifsp.scl.sc3039056.fasttripplanner.databinding.ActivityTripSummaryBinding

class TripSummaryActivity : AppCompatActivity() {

    // viewBinding: gera referências diretas aos componentes do layout activity_trip_summary.xml
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

        // Recebe todos os dados enviados pelas telas anteriores via Intent
        val destino        = intent.getStringExtra("EXTRA_DESTINO") ?: ""
        val dias           = intent.getIntExtra("EXTRA_DIAS", 0)
        val orcamento      = intent.getDoubleExtra("EXTRA_ORCAMENTO", 0.0)
        val hospedagem     = intent.getStringExtra("EXTRA_HOSPEDAGEM") ?: "economica"
        val temTransporte  = intent.getBooleanExtra("EXTRA_TRANSPORTE", false)
        val temAlimentacao = intent.getBooleanExtra("EXTRA_ALIMENTACAO", false)
        val temPasseios    = intent.getBooleanExtra("EXTRA_PASSEIOS", false)

        // Define o multiplicador de custo conforme o tipo de hospedagem escolhido
        val multiplicador = when (hospedagem) {
            "conforto" -> 1.5
            "luxo"     -> 2.2
            else       -> 1.0  // econômica
        }

        // Calcula o custo base: dias × orçamento diário × multiplicador da hospedagem
        val custoBase = dias * orcamento * multiplicador

        // Calcula os custos extras conforme os serviços selecionados
        var custoExtras = 0.0
        if (temTransporte)  custoExtras += 300.0          // valor fixo
        if (temAlimentacao) custoExtras += 50.0 * dias    // valor por dia
        if (temPasseios)    custoExtras += 120.0 * dias   // valor por dia

        val custoTotal = custoBase + custoExtras

        // Monta a lista de serviços selecionados para exibição
        val servicosList = mutableListOf<String>()
        if (temTransporte)  servicosList.add("Transporte")
        if (temAlimentacao) servicosList.add("Alimentação")
        if (temPasseios)    servicosList.add("Passeios")
        val servicosTexto = if (servicosList.isEmpty()) "Nenhum" else servicosList.joinToString(", ")

        // Converte o identificador interno da hospedagem para nome amigável
        val hospedagemTexto = when (hospedagem) {
            "conforto" -> "Conforto"
            "luxo"     -> "Luxo"
            else       -> "Econômica"
        }

        // Preenche os TextViews com os dados e resultados calculados
        activityTripSummaryBinding.destinoTv.text    = "Destino: $destino"
        activityTripSummaryBinding.diasTv.text       = "Duração: $dias dia(s)"
        activityTripSummaryBinding.orcamentoTv.text  = "Orçamento diário: R$ %.2f".format(orcamento)
        activityTripSummaryBinding.hospedagemTv.text = "Hospedagem: $hospedagemTexto"
        activityTripSummaryBinding.servicosTv.text   = "Serviços: $servicosTexto"
        activityTripSummaryBinding.totalTv.text      = "Total estimado: R$ %.2f".format(custoTotal)

        // Reinicia o planejamento voltando à Tela 1 e limpando toda a pilha de Activities
        activityTripSummaryBinding.reiniciarBt.setOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(mainIntent)
        }
    }
}