<script>
// Lógica da página de nova solicitação
let formData = {
  animal: {
    nome: '',
    especie: '',
    idade: '',
    medicamentos: '',
    raca: '',
    sexo: '',
    peso: ''
  },
  tutor: {
    nome: '',
    cpf: '',
    endereco: '',
    bairro: '',
    cidade: '',
    uf: '',
    telefone: ''
  },
  exames: {
    hemograma: false,
    urina: false,
    fezes: false,
    bioquimico: false,
    outros: ''
  }
};

function handleSubmit() {
  // Aqui futuramente enviará os dados para o backend
  console.log("Dados do formulário:", formData);
  alert("Solicitação enviada com sucesso!");
}

function handleVoltar() {
  history.back();
}
</script>

<div class="nova-solicitacao">
  <div class="page-header">
    <button class="voltar-btn" on:click={handleVoltar}>
      <span class="arrow">←</span>
    </button>
    <h1>Solicitação de Exame</h1>
  </div>

  <form on:submit|preventDefault={handleSubmit}>
    <!-- Dados do animal -->
    <section class="form-section">
      <h2>Dados do animal</h2>
      
      <div class="form-grid">
        <div class="form-group">
          <label for="nome_animal">Nome do animal:</label>
          <input 
            type="text" 
            id="nome_animal" 
            bind:value={formData.animal.nome} 
            required
            class="form-input"
          >
        </div>
        
        <div class="form-group">
          <label for="raca">Raça:</label>
          <input 
            type="text" 
            id="raca" 
            bind:value={formData.animal.raca}
            class="form-input"
          >
        </div>
        
        <div class="form-group">
          <label for="especie">Espécie:</label>
          <input 
            type="text" 
            id="especie" 
            bind:value={formData.animal.especie}
            required
            class="form-input"
          >
        </div>
        
        <div class="form-group">
          <label for="sexo">Sexo:</label>
          <input 
            type="text" 
            id="sexo" 
            bind:value={formData.animal.sexo}
            class="form-input"
          >
        </div>
        
        <div class="form-group">
          <label for="idade">Idade:</label>
          <input 
            type="text" 
            id="idade" 
            bind:value={formData.animal.idade}
            class="form-input"
          >
        </div>
        
        <div class="form-group">
          <label for="peso">Peso:</label>
          <input 
            type="text" 
            id="peso" 
            bind:value={formData.animal.peso}
            class="form-input"
          >
        </div>
        
        <div class="form-group full-width">
          <label for="medicamentos">Fez uso de medicamento? Se sim, qual?</label>
          <input 
            type="text" 
            id="medicamentos" 
            bind:value={formData.animal.medicamentos}
            class="form-input"
          >
        </div>
      </div>
    </section>
    
    <!-- Dados do tutor -->
    <section class="form-section">
      <h2>Dados do tutor</h2>
      
      <div class="form-grid">
        <div class="form-group">
          <label for="nome_tutor">Nome completo:</label>
          <input 
            type="text" 
            id="nome_tutor" 
            bind:value={formData.tutor.nome}
            required
            class="form-input"
          >
        </div>
        
        <div class="form-group">
          <label for="cpf">CPF:</label>
          <input 
            type="text" 
            id="cpf" 
            bind:value={formData.tutor.cpf}
            required
            class="form-input"
          >
        </div>
        
        <div class="form-group full-width">
          <label for="endereco">Endereço:</label>
          <input 
            type="text" 
            id="endereco" 
            bind:value={formData.tutor.endereco}
            class="form-input"
          >
        </div>
        
        <div class="form-group">
          <label for="bairro">Bairro:</label>
          <input 
            type="text" 
            id="bairro" 
            bind:value={formData.tutor.bairro}
            class="form-input"
          >
        </div>
        
        <div class="form-group">
          <label for="cidade">Cidade:</label>
          <input 
            type="text" 
            id="cidade" 
            bind:value={formData.tutor.cidade}
            class="form-input"
          >
        </div>
        
        <div class="form-group">
          <label for="uf">UF:</label>
          <input 
            type="text" 
            id="uf" 
            bind:value={formData.tutor.uf}
            maxlength="2"
            class="form-input"
          >
        </div>
        
        <div class="form-group">
          <label for="telefone">Telefone:</label>
          <input 
            type="tel" 
            id="telefone" 
            bind:value={formData.tutor.telefone}
            class="form-input"
          >
        </div>
      </div>
    </section>
    
    <!-- Seleção de exames -->
    <section class="form-section">
      <h2>Exames solicitados</h2>
      
      <div class="exames-grid">
        <div class="form-check">
          <input 
            type="checkbox" 
            id="hemograma" 
            bind:checked={formData.exames.hemograma}
            class="form-checkbox"
          >
          <label for="hemograma">Hemograma completo</label>
        </div>
        
        <div class="form-check">
          <input 
            type="checkbox" 
            id="urina" 
            bind:checked={formData.exames.urina}
            class="form-checkbox"
          >
          <label for="urina">Urina</label>
        </div>
        
        <div class="form-check">
          <input 
            type="checkbox" 
            id="fezes" 
            bind:checked={formData.exames.fezes}
            class="form-checkbox"
          >
          <label for="fezes">Fezes</label>
        </div>
        
        <div class="form-check">
          <input 
            type="checkbox" 
            id="bioquimico" 
            bind:checked={formData.exames.bioquimico}
            class="form-checkbox"
          >
          <label for="bioquimico">Bioquímico</label>
        </div>
        
        <div class="form-group full-width">
          <label for="outros_exames">Outros exames:</label>
          <input 
            type="text" 
            id="outros_exames" 
            bind:value={formData.exames.outros}
            class="form-input"
          >
        </div>
      </div>
    </section>
    
    <div class="form-actions">
      <button type="button" class="cancel-btn" on:click={handleVoltar}>Cancelar</button>
      <button type="submit" class="submit-btn">Enviar solicitação</button>
    </div>
  </form>
</div>

<style>
  .nova-solicitacao {
    padding: 0;
  }
  
  .page-header {
    display: flex;
    align-items: center;
    margin-bottom: 24px;
  }
  
  .voltar-btn {
    background: none;
    border: none;
    display: flex;
    align-items: center;
    margin-right: 12px;
    padding: 5px;
    cursor: pointer;
  }
  
  .arrow {
    font-size: 20px;
  }
  
  h1 {
    font-size: 24px;
    font-weight: 600;
    margin: 0;
    color: #333;
  }
  
  h2 {
    font-size: 18px;
    font-weight: 600;
    margin: 0 0 16px 0;
    color: #444;
  }
  
  .form-section {
    background-color: #f2f2f2;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 24px;
  }
  
  .form-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
  }
  
  .form-group {
    display: flex;
    flex-direction: column;
  }
  
  .full-width {
    grid-column: span 2;
  }
  
  label {
    font-size: 14px;
    margin-bottom: 6px;
    color: #444;
    font-weight: 500;
  }
  
  .form-input {
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    font-size: 14px;
    background-color: #e0e0e0;
  }
  
  .exames-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 12px;
  }
  
  .form-check {
    display: flex;
    align-items: center;
    gap: 8px;
  }
  
  .form-checkbox {
    width: 18px;
    height: 18px;
    accent-color: var(--color-primary-green);
  }
  
  .form-actions {
    display: flex;
    justify-content: flex-end;
    gap: 12px;
    margin-top: 30px;
  }
  
  .cancel-btn {
    padding: 10px 24px;
    border: 1px solid #ccc;
    background-color: white;
    border-radius: 20px;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s;
  }
  
  .cancel-btn:hover {
    background-color: #f0f0f0;
  }
  
  .submit-btn {
    padding: 10px 24px;
    border: none;
    background-color: var(--color-primary-green);
    color: white;
    border-radius: 20px;
    font-size: 14px;
    font-weight: 500;
    cursor: pointer;
    transition: background-color 0.2s;
  }
  
  .submit-btn:hover {
    background-color: var(--color-dark-green);
  }
</style>
