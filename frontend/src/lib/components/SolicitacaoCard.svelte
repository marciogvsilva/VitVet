<script>
import StatusBadge from './StatusBadge.svelte';

let { solicitacao, onclick } = $props();

// Formata data para exibição
function formatarData(dataISO) {
  const data = new Date(dataISO);
  return data.toLocaleDateString('pt-BR', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  });
}

// Formata lista de exames
let examesTexto = $derived(
  solicitacao.exames?.map(e => e.nome).join(', ') || 'N/A'
);
</script>

<button class="solicitacao-card" onclick={onclick}>
  <div class="card-header">
    <div>
      <h3 class="protocolo">#{solicitacao.protocolo}</h3>
      <p class="animal">{solicitacao.animal?.nome || 'Animal não informado'}</p>
    </div>
    <StatusBadge status={solicitacao.status} />
  </div>
  
  <div class="card-body">
    <div class="info-row">
      <span class="label">Espécie:</span>
      <span class="value">{solicitacao.animal?.especie || 'N/A'}</span>
    </div>
    
    <div class="info-row">
      <span class="label">Tutor:</span>
      <span class="value">{solicitacao.animal?.tutor?.nomeCompleto || 'N/A'}</span>
    </div>
    
    <div class="info-row">
      <span class="label">Exames:</span>
      <span class="value exames">{examesTexto}</span>
    </div>
    
    <div class="info-row">
      <span class="label">Suspeita:</span>
      <span class="value">{solicitacao.suspeitaClinica}</span>
    </div>
    
    <div class="info-row">
      <span class="label">Data:</span>
      <span class="value">{formatarData(solicitacao.dataSolicitacao)}</span>
    </div>
  </div>
  
  <div class="card-footer">
    <span class="ver-detalhes">Ver detalhes →</span>
  </div>
</button>

<style>
.solicitacao-card {
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  padding: 14px;
  transition: all 0.2s ease;
  cursor: pointer;
  text-align: left;
  width: 100%;
  display: block;
}

@media (min-width: 768px) {
  .solicitacao-card {
    border-radius: 12px;
    padding: 20px;
  }
}

.solicitacao-card:hover {
  border-color: #9ca3af;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f3f4f6;
  gap: 8px;
}

@media (min-width: 768px) {
  .card-header {
    margin-bottom: 16px;
    padding-bottom: 12px;
  }
}

.protocolo {
  font-size: 16px;
  font-weight: 700;
  color: #1f2937;
  margin: 0 0 4px 0;
}

@media (min-width: 768px) {
  .protocolo {
    font-size: 18px;
  }
}

.animal {
  font-size: 13px;
  color: #6b7280;
  margin: 0;
  font-weight: 500;
}

@media (min-width: 768px) {
  .animal {
    font-size: 14px;
  }
}

.card-body {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 12px;
}

@media (min-width: 768px) {
  .card-body {
    gap: 10px;
    margin-bottom: 16px;
  }
}

.info-row {
  display: flex;
  gap: 6px;
  font-size: 13px;
  flex-wrap: wrap;
}

@media (min-width: 768px) {
  .info-row {
    gap: 8px;
    font-size: 14px;
    flex-wrap: nowrap;
  }
}

.label {
  color: #6b7280;
  font-weight: 600;
  min-width: 70px;
  flex-shrink: 0;
}

@media (min-width: 768px) {
  .label {
    min-width: 80px;
  }
}

.value {
  color: #1f2937;
  flex: 1;
  word-break: break-word;
}

.value.exames {
  font-style: italic;
  color: #4b5563;
}

.card-footer {
  padding-top: 10px;
  border-top: 1px solid #f3f4f6;
}

@media (min-width: 768px) {
  .card-footer {
    padding-top: 12px;
  }
}

.ver-detalhes {
  color: #5DB578;
  font-size: 13px;
  font-weight: 600;
  display: inline-flex;
  align-items: center;
  gap: 4px;
}

@media (min-width: 768px) {
  .ver-detalhes {
    font-size: 14px;
  }
}

.solicitacao-card:hover .ver-detalhes {
  gap: 8px;
}
</style>

