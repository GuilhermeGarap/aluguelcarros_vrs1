package com.aluguelcarros_vrs1.services;

import java.util.List;
import java.util.stream.Collectors;

import com.aluguelcarros_vrs1.domain.User;
import com.aluguelcarros_vrs1.infra.exception.ErrorDetailsException;
import com.aluguelcarros_vrs1.infra.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.aluguelcarros_vrs1.domain.Cliente;
import com.aluguelcarros_vrs1.repositories.ClienteRepository;
import com.aluguelcarros_vrs1.data.cliente.DadosCadastroCliente;
import com.aluguelcarros_vrs1.data.cliente.DadosDetalhamentoCliente;
import com.aluguelcarros_vrs1.data.cliente.DadosEditarCliente;
import com.aluguelcarros_vrs1.data.cliente.DadosListaCliente;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public DadosDetalhamentoCliente cadastrar(@Valid DadosCadastroCliente dados) {
            if (clienteRepository.existsByCpf(dados.cpf())) {
                throw new ErrorDetailsException("Já existe um cliente cadastrado com esse CPF!");
        }
            if (clienteRepository.existsByEmail(dados.email())) {
                throw new ErrorDetailsException("Já existe um cliente cadastrado com esse email!");
            }
            if (clienteRepository.existsByNome(dados.nome())) {
                throw new ErrorDetailsException(("Já existe um cliente cadastrado com esse nome!"));
            }
            if (clienteRepository.existsByTelefone(dados.telefone())) {
                throw new ErrorDetailsException("Já existe um cliente cadastrado com esse telefone!");
            }
        var cliente = new Cliente(dados);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        cliente.setCreatedBy(user.getUsername());

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

        if (dados.nome() != null
                && !dados.nome().equals(cliente.getNome())
                && clienteRepository.existsByNome(dados.nome())) {
            throw new ErrorDetailsException("Já existe um cliente cadastrado com esse nome!");
        }

        if (dados.telefone() != null
                && !dados.telefone().equals(cliente.getTelefone())
                && clienteRepository.existsByTelefone(dados.telefone())) {
            throw new ErrorDetailsException("Já existe um cliente cadastrado com esse telefone!");
        }

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
        var cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado com o ID: " + id));

        return new DadosDetalhamentoCliente(cliente);
    }
}
