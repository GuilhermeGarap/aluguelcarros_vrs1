package com.aluguelcarros_vrs1.domainservices.clienteservices;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aluguelcarros_vrs1.domain.cliente.Cliente;
import com.aluguelcarros_vrs1.domain.cliente.ClienteRepository;
import com.aluguelcarros_vrs1.domain.cliente.DadosCadastroCliente;
import com.aluguelcarros_vrs1.domain.cliente.DadosDetalhamentoCliente;
import com.aluguelcarros_vrs1.domain.cliente.DadosEditarCliente;
import com.aluguelcarros_vrs1.domain.cliente.DadosListaCliente;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public DadosDetalhamentoCliente cadastrar(@Valid DadosCadastroCliente dados) {
        var cliente = new Cliente(dados);
        clienteRepository.save(cliente);
        return new DadosDetalhamentoCliente(cliente);
    }

    public List<DadosListaCliente> listarAtivos() {
        return clienteRepository.findAllByAtivoTrue()
            .stream()
            .map(DadosListaCliente::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public DadosDetalhamentoCliente atualizar(Long id, @Valid DadosEditarCliente dados) {
        var cliente = clienteRepository.getReferenceById(id);
        cliente.atualizarInformacoes(dados);
        return new DadosDetalhamentoCliente(cliente);
    }

    @Transactional
    public DadosDetalhamentoCliente desativar(Long id) {
        var cliente = clienteRepository.getReferenceById(id);
        cliente.desativar();
        clienteRepository.save(cliente);
        return new DadosDetalhamentoCliente(cliente);
    }

    @Transactional
    public DadosDetalhamentoCliente ativar(Long id) {
        var cliente = clienteRepository.getReferenceById(id);
        if (!cliente.getAtivo()) {
            cliente.ativar();
            clienteRepository.save(cliente);
            return new DadosDetalhamentoCliente(cliente);
        }
        return null;
    }

    public DadosDetalhamentoCliente buscar(Long id) {
        var cliente = clienteRepository.getReferenceById(id);
        return new DadosDetalhamentoCliente(cliente);
    }
}
