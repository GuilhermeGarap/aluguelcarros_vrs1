package com.aluguelcarros_vrs1.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aluguelcarros_vrs1.domain.aluguel.Aluguel;
import com.aluguelcarros_vrs1.domain.aluguel.AluguelRepository;
import com.aluguelcarros_vrs1.domain.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosDetalhamentoAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosEditarAluguel;
import com.aluguelcarros_vrs1.domain.aluguel.DadosListaAluguel;
import com.aluguelcarros_vrs1.domain.carro.CarroRepository;
import com.aluguelcarros_vrs1.domain.cliente.ClienteRepository;
import com.aluguelcarros_vrs1.domainservices.ValidacaoException;
import com.aluguelcarros_vrs1.domainservices.aluguelservices.AluguelService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/aluguel")
@SecurityRequirement(name = "bearer-key")
public class AluguelController {
    
    @Autowired
    private AluguelRepository repository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AluguelService aluguelService;
    
    /**
     * Cadastra um novo aluguel.
     * @param dados Informações do aluguel a ser cadastrado.
     * @param uriBuilder Construtor de URI para a localização do novo recurso.
     * @return Resposta com o aluguel criado e o URI para acesso.
     */
    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroAluguel dados) {
        var dto = aluguelService.cadastrar(dados);
        return ResponseEntity.ok(dto);
    }

    /**
     * Lista todos os aluguéis ativos com paginação.
     * @param paginacao Informações de paginação.
     * @return Página com os aluguéis ativos.
     */
    @GetMapping("/listarAtivos")
    public ResponseEntity<Page<DadosListaAluguel>> listarAtivos(@PageableDefault(size=10) Pageable paginacao) {
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListaAluguel::new);

        return ResponseEntity.ok(page);
    }

    /**
     * Lista todos os aluguéis desativados com paginação.
     * @param paginacao Informações de paginação.
     * @return Página com os aluguéis desativados.
     */
    @GetMapping("/listarDesativados")
    public ResponseEntity<Page<DadosListaAluguel>> listarDesativados(@PageableDefault(size=10) Pageable paginacao) {
        var page = repository.findAllByAtivoFalse(paginacao).map(DadosListaAluguel::new);

        return ResponseEntity.ok(page);
    }

    /**
     * Lista todos os aluguéis com paginação.
     * @param paginacao Informações de paginação.
     * @return Página com todos os aluguéis.
     */
    @GetMapping("/listarTodos")
    public ResponseEntity<Page<DadosListaAluguel>> listarTodos(@PageableDefault(size=10) Pageable paginacao) {
        var page = repository.findAll(paginacao).map(DadosListaAluguel::new);

        return ResponseEntity.ok(page);
    }

    /**
     * Atualiza as informações de um aluguel existente.
     * @param id ID do aluguel a ser atualizado.
     * @param dados Novas informações do aluguel.
     * @return Resposta com as informações atualizadas do aluguel.
     */
    @PutMapping("/editar/{id}")
    @Transactional
    public ResponseEntity atualizar(@PathVariable Long id, @RequestBody DadosEditarAluguel dados) {
        var aluguel = repository.getReferenceById(id);
        aluguel.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoAluguel(aluguel));
    }

    /**
     * Ativa um aluguel desativado.
     * @param id ID do aluguel a ser ativado.
     * @return Resposta com as informações do aluguel ativado ou uma resposta de não modificado.
     */
    @PatchMapping("/ativar/{id}")
    @Transactional
    public ResponseEntity ativar(@PathVariable Long id) {
        var aluguel = repository.getReferenceById(id);
        if (!aluguel.getAtivo()) {
            aluguel.ativar();
            return ResponseEntity.ok(new DadosDetalhamentoAluguel(aluguel));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    /**
     * Busca um aluguel pelo ID.
     * @param id ID do aluguel a ser buscado.
     * @return Resposta com as informações do aluguel.
     */
    @GetMapping("/buscar/{id}")
    public ResponseEntity buscar(@PathVariable Long id) {
        var aluguel = repository.getReferenceById(id);

        return ResponseEntity.ok(new DadosDetalhamentoAluguel(aluguel));
    }

    /**
     * Desativa um aluguel e incrementa a disponibilidade do carro associado.
     * @param id ID do aluguel a ser desativado.
     * @return Resposta com as informações do aluguel desativado.
     */
    @DeleteMapping("/desativar/{id}")
    public ResponseEntity desativar(@PathVariable Long id) {
        try {
            Aluguel aluguel = aluguelService.desativarAluguel(id);
            return ResponseEntity.ok(new DadosDetalhamentoAluguel(aluguel));
        } catch (ValidacaoException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

