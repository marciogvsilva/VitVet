<script>
let { size = 40, color = '#5DB578' } = $props();
</script>

<div class="spinner-container">
  <div class="spinner" style="width: {size}px; height: {size}px; border-color: {color}20; border-top-color: {color};"></div>
</div>

<style>
.spinner-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 40px;
}

.spinner {
  border: 3px solid;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>

