<script>
/**
 * Container para renderizar todos os toasts ativos
 */
import { toastStore } from '$lib/stores/toast';
import Toast from './Toast.svelte';

let toasts = $state([]);

toastStore.subscribe(value => {
  toasts = value;
});
</script>

<div class="toast-container">
  {#each toasts as toast (toast.id)}
    <Toast 
      message={toast.message} 
      type={toast.type} 
      duration={toast.duration}
      onclose={() => toastStore.remove(toast.id)}
    />
  {/each}
</div>

<style>
.toast-container {
  position: fixed;
  top: 12px;
  right: 12px;
  left: 12px;
  z-index: 9999;
  display: flex;
  flex-direction: column;
  gap: 8px;
  pointer-events: none;
}

@media (min-width: 768px) {
  .toast-container {
    top: 20px;
    right: 20px;
    left: auto;
    gap: 12px;
  }
}

.toast-container :global(.toast) {
  pointer-events: all;
}
</style>

