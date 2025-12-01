<script>
/**
 * Componente Toast simples para notificações
 * Compatível com Svelte 5
 */
import { onMount } from 'svelte';
import FaCheckCircle from 'svelte-icons/fa/FaCheckCircle.svelte';
import FaExclamationCircle from 'svelte-icons/fa/FaExclamationCircle.svelte';
import FaInfoCircle from 'svelte-icons/fa/FaInfoCircle.svelte';
import FaTimes from 'svelte-icons/fa/FaTimes.svelte';

let { message, type = 'info', duration = 3000, onclose } = $props();

let visible = $state(true);

onMount(() => {
  const timer = setTimeout(() => {
    visible = false;
    setTimeout(() => {
      onclose?.();
    }, 300);
  }, duration);

  return () => clearTimeout(timer);
});

function fechar() {
  visible = false;
  setTimeout(() => {
    onclose?.();
  }, 300);
}
</script>

<div class="toast" class:visible class:success={type === 'success'} class:error={type === 'error'} class:info={type === 'info'}>
  <div class="toast-icon">
    {#if type === 'success'}
      <FaCheckCircle />
    {:else if type === 'error'}
      <FaExclamationCircle />
    {:else}
      <FaInfoCircle />
    {/if}
  </div>
  <p class="toast-message">{message}</p>
  <button class="toast-close" onclick={fechar} aria-label="Fechar">
    <FaTimes />
  </button>
</div>

<style>
.toast {
  position: fixed;
  top: 12px;
  right: 12px;
  left: 12px;
  min-width: auto;
  max-width: 500px;
  padding: 12px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  display: flex;
  align-items: center;
  gap: 10px;
  z-index: 9999;
  opacity: 0;
  transform: translateY(-100px);
  transition: all 0.3s ease;
}

@media (min-width: 768px) {
  .toast {
    top: 20px;
    right: 20px;
    left: auto;
    min-width: 300px;
    padding: 16px;
    gap: 12px;
    transform: translateX(400px);
  }
}

.toast.visible {
  opacity: 1;
  transform: translateY(0);
}

@media (min-width: 768px) {
  .toast.visible {
    transform: translateX(0);
  }
}

.toast.success {
  border-left: 4px solid #10b981;
}

.toast.error {
  border-left: 4px solid #ef4444;
}

.toast.info {
  border-left: 4px solid #3b82f6;
}

.toast-icon {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

@media (min-width: 768px) {
  .toast-icon {
    width: 24px;
    height: 24px;
  }
}

.toast.success .toast-icon {
  color: #10b981;
}

.toast.error .toast-icon {
  color: #ef4444;
}

.toast.info .toast-icon {
  color: #3b82f6;
}

.toast-message {
  flex: 1;
  margin: 0;
  font-size: 13px;
  color: #1f2937;
  line-height: 1.4;
}

@media (min-width: 768px) {
  .toast-message {
    font-size: 14px;
    line-height: 1.5;
  }
}

.toast-close {
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: none;
  border: none;
  cursor: pointer;
  color: #6b7280;
  padding: 0;
  flex-shrink: 0;
}

.toast-close:hover {
  color: #1f2937;
}
</style>

