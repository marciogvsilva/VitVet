package br.com.vitvet.service;

import br.com.vitvet.model.ResultadoExame;
import br.com.vitvet.model.SolicitacaoExame;
import br.com.vitvet.model.Usuario;
import br.com.vitvet.model.enums.StatusSolicitacao;
import br.com.vitvet.repository.ResultadoExameRepository;
import br.com.vitvet.repository.SolicitacaoExameRepository;
import br.com.vitvet.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResultadoExameService {

    @Autowired
    private ResultadoExameRepository resultadoRepository;

    @Autowired
    private SolicitacaoExameRepository solicitacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public ResultadoExame adicionarLaudo(Long solicitacaoId, String observacoes, MultipartFile arquivoPdf) {
        SolicitacaoExame solicitacao = solicitacaoRepository.findById(solicitacaoId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada."));

        String emailPatologista = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario patologista = usuarioRepository.findByEmail(emailPatologista)
                .orElseThrow(() -> new EntityNotFoundException("Patologista não encontrado."));

        String nomeArquivo = fileStorageService.storeFile(arquivoPdf);

        ResultadoExame resultado = new ResultadoExame();

        resultado.setSolicitacao(solicitacao);
        resultado.setPatologistaResponsavel(patologista);
        resultado.setObservacoes(observacoes);
        resultado.setUrlLaudoPdf(nomeArquivo);

        solicitacao.setStatus(StatusSolicitacao.CONCLUIDO);
        solicitacaoRepository.save(solicitacao);

        return resultadoRepository.save(resultado);
    }
}