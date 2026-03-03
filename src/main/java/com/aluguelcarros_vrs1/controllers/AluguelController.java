package com.aluguelcarros_vrs1.controllers;

import java.util.List;

import com.aluguelcarros_vrs1.controllers.docs.AluguelControllerDoc;
import com.aluguelcarros_vrs1.services.aluguelservices.AluguelServiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aluguelcarros_vrs1.data.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.data.aluguel.DadosDetalhamentoAluguel;
import com.aluguelcarros_vrs1.data.aluguel.DadosEditarAluguel;
import com.aluguelcarros_vrs1.data.aluguel.DadosListaAluguel;
import com.aluguelcarros_vrs1.services.aluguelservices.AluguelService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/aluguel")
@SecurityRequirement(name = "bearer-key")
public class AluguelController implements AluguelControllerDoc {

    @Autowired
    private AluguelService aluguelService;

    @Autowired
    AluguelServiceStatus aluguelServiceStatus;

    @PostMapping("/cadastrar")
    @Override
    public ResponseEntity<DadosDetalhamentoAluguel> cadastrar(@RequestBody @Valid DadosCadastroAluguel dados) {
        var dto = aluguelService.cadastrar(dados);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/listarAtivos")
    @Override
    public ResponseEntity<List<DadosListaAluguel>> listarAtivos() {
        List<DadosListaAluguel> dadosAlugueisAtivos = aluguelService.listarAtivos();
        return ResponseEntity.ok(dadosAlugueisAtivos);
    }

    @GetMapping("/listarDesativados")
    @Override
    public ResponseEntity<List<DadosListaAluguel>> listarDesativados() {
        List<DadosListaAluguel> dadosAlugueisDesativados = aluguelService.listarDesativados();
        return ResponseEntity.ok(dadosAlugueisDesativados); 
    }

    @GetMapping("/listarTodos")
    @Override
    public ResponseEntity<List<DadosListaAluguel>> listarTodos() {
        List<DadosListaAluguel> dadosTodosAlugueis = aluguelService.listarTodos();
        return ResponseEntity.ok(dadosTodosAlugueis); 
    }

    @PutMapping("/editar/{id}")
    @Override
    public ResponseEntity<DadosDetalhamentoAluguel> atualizar(@PathVariable Long id, @RequestBody DadosEditarAluguel dados) {
        var aluguel = aluguelService.atualizar(id, dados);
        return ResponseEntity.ok(new DadosDetalhamentoAluguel(aluguel));
    }

    @GetMapping("/buscar/{id}")
    @Override
    public ResponseEntity<DadosDetalhamentoAluguel> buscar(@PathVariable Long id) {
        var dto = aluguelService.buscar(id);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/ativar/{id}")
    public ResponseEntity<DadosDetalhamentoAluguel> ativar(@PathVariable Long id) {
        var data = aluguelServiceStatus.ativar(id);
        return ResponseEntity.ok(data);
    }

    @PatchMapping("/desativar/{id}")
    public ResponseEntity<DadosDetalhamentoAluguel> desativar(@PathVariable Long id) {
        var data = aluguelServiceStatus.desativar(id);
        return ResponseEntity.ok(data);
    }

    @PatchMapping("/deixarPendente/{id}")
    public ResponseEntity<DadosDetalhamentoAluguel> deixarPendente(@PathVariable Long id) {
        var data = aluguelServiceStatus.pendente(id);
        return ResponseEntity.ok(data);
    }

    @PatchMapping("/encerrar/{id}")
    public ResponseEntity<DadosDetalhamentoAluguel> encerrar(@PathVariable Long id) {
        var data = aluguelServiceStatus.encerrar(id);
        return ResponseEntity.ok(data);
    }

    @PatchMapping("/cancelar/{id}")
    public ResponseEntity<DadosDetalhamentoAluguel> cancelar(@PathVariable Long id) {
        var data = aluguelServiceStatus.cancelar(id);
        return ResponseEntity.ok(data);
    }



}
