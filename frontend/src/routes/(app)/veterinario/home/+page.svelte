<script>
import { goto } from '$app/navigation';
import FaClipboardList from 'svelte-icons/fa/FaClipboardList.svelte';
import FaPlus from 'svelte-icons/fa/FaPlus.svelte';

function irParaSolicitacoes() {
  goto('/veterinario/solicitacoes');
}

function irParaNovaSolicitacao() {
  goto('/veterinario/nova-solicitacao');
}
</script>

<div class="home-content">
  <div class="action-buttons">
    <button class="action-button" onclick={irParaSolicitacoes}>
      <span class="icon">
        <div class="icon-wrapper">
          <FaClipboardList />
        </div>
      </span>
      <span>Solicitações</span>
    </button>
    <button class="action-button" onclick={irParaNovaSolicitacao}>
      <span class="icon">
        <div class="icon-wrapper">
          <FaPlus />
        </div>
      </span>
      <span>Nova solicitação</span>
    </button>
  </div>
</div>

<style>
.home-content {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

@media (min-width: 768px) {
  .action-buttons {
    flex-direction: row;
    gap: 16px;
  }
}

.action-button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 14px 24px;
  background-color: var(--color-primary-green);
  color: white;
  border: none;
  border-radius: 40px;
  font-weight: 500;
  font-size: 15px;
  cursor: pointer;
  transition: background-color 0.2s;
  width: 100%;
  min-height: 48px; /* Touch target adequado */
}

@media (min-width: 768px) {
  .action-button {
    width: auto;
    padding: 12px 24px;
    font-size: 16px;
  }
}

.action-button:hover {
  background-color: #4a9163;
}

.icon {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
}

@media (min-width: 768px) {
  .icon {
    font-size: 20px;
  }
}

.icon-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon .icon-wrapper {
  width: 18px;
  height: 18px;
}

@media (min-width: 768px) {
  .icon .icon-wrapper {
    width: 20px;
    height: 20px;
  }
}
</style>
