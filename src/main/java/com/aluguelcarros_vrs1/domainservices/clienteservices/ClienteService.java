package com.aluguelcarros_vrs1.domainservices.clienteservices;

import java.util.List;
import java.util.stream.Collectors;

import com.aluguelcarros_vrs1.domainservices.ValidacaoException;
import jakarta.validation.ValidationException;
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
            if (clienteRepository.existsByCpf(dados.cpf())) {
                throw new ValidacaoException("Já existe um cliente cadastrado com esse CPF!");
        }
            if (clienteRepository.existsByEmail(dados.email())) {
                throw new ValidacaoException("Já existe um cliente cadastrado com esse email!");
            }
            if (clienteRepository.existsByNome(dados.nome())) {
                throw new ValidacaoException(("Já existe um cliente cadastrado com esse nome!"));
            }
            if (clienteRepository.existsByTelefone(dados.telefone())) {
                throw new ValidacaoException("Já existe um cliente cadastrado com esse telefone!");
            }
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

        if (clienteRepository.existsByNome(dados.nome()) || clienteRepository.existsByNome(cliente.getNome())) {
            throw new ValidacaoException(("Já existe um cliente cadastrado com esse nome ou é o mesmo já cadastrado"));
        }
        if (clienteRepository.existsByTelefone(dados.telefone()) || clienteRepository.existsByNome(cliente.getTelefone())) {
            throw new ValidacaoException("Já existe um cliente cadastrado com esse telefone ou é o mesmo já cadastrado");
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
                .orElseThrow(() -> new ValidacaoException("Cliente não encontrado com o ID: " + id));

        return new DadosDetalhamentoCliente(cliente);
    }
}
