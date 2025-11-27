<script>
import { goto } from '$app/navigation';
import FaClipboardList from 'svelte-icons/fa/FaClipboardList.svelte';

function irParaSolicitacoes() {
  goto('/patologista/solicitacoes');
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
  gap: 16px;
}

.action-button {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 24px;
  background-color: var(--color-primary-blue);
  color: white;
  border: none;
  border-radius: 40px;
  font-weight: 500;
  cursor: pointer;
  transition: background-color 0.2s;
}

.action-button:hover {
  background-color: #165070;
}

.icon {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.icon-wrapper {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.icon .icon-wrapper {
  width: 20px;
  height: 20px;
}
</style>
