# FastTripPlanner

Aplicativo Android desenvolvido para a disciplina de Desenvolvimento Mobile (IFSP).
Permite planejar uma viagem calculando o custo estimado com base no destino,
duração, tipo de hospedagem e serviços extras selecionados.

## Telas

### Tela 1 — Dados da Viagem (`MainActivity`)
Entrada do destino, número de dias e orçamento diário.
Realiza validação dos campos antes de avançar para a próxima tela.

### Tela 2 — Opções da Viagem (`TripOptionsActivity`)
Seleção do tipo de hospedagem (econômica, conforto ou luxo) e serviços adicionais
(transporte, alimentação, passeios). Possui botões para calcular ou voltar.
Preserva o estado dos componentes em caso de rotação de tela.

### Tela 3 — Resumo da Viagem (`TripSummaryActivity`)
Exibe todos os dados inseridos e o custo total calculado.
Possui botão para iniciar um novo planejamento.

## Regras de cálculo
custoBase = dias × orçamento × multiplicadorHospedagem

Multiplicadores:

Econômica → 1,0

Conforto  → 1,5

Luxo      → 2,2

Extras:

Transporte  → + R$ 300 (fixo)

Alimentação → + R$ 50 × dias

Passeios    → + R$ 120 × dias

custoTotal = custoBase + extras

## Tecnologias

- Kotlin
- Android SDK (minSdk 26 — Android 8.0+)
- ViewBinding
- Intents explícitas para navegação entre telas

## Como executar

1. Clone o repositório
2. Abra no Android Studio
3. Execute em um emulador ou dispositivo com Android 8.0 ou superior

## 🎥 Demonstração

O vídeo está na pasta /demo