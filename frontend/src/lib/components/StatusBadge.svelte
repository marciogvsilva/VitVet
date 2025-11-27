<script>
// Componente para exibir badge de status colorido
let { status } = $props();

// Define cores baseadas no status
const statusConfig = {
  RECEBIDO: {
    bg: '#DBEAFE',
    text: '#1E40AF',
    label: 'Recebido'
  },
  EM_ANALISE: {
    bg: '#FEF3C7',
    text: '#92400E',
    label: 'Em Análise'
  },
  CONCLUIDO: {
    bg: '#D1FAE5',
    text: '#065F46',
    label: 'Concluído'
  },
  CANCELADO: {
    bg: '#FEE2E2',
    text: '#991B1B',
    label: 'Cancelado'
  }
};

let config = $derived(statusConfig[status] || statusConfig.RECEBIDO);
</script>

<span 
  class="status-badge"
  style="background-color: {config.bg}; color: {config.text};"
>
  {config.label}
</span>

<style>
.status-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
</style>

