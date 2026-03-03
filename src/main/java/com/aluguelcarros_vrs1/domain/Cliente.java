package com.aluguelcarros_vrs1.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.aluguelcarros_vrs1.data.cliente.DadosCadastroCliente;
import com.aluguelcarros_vrs1.data.cliente.DadosEditarCliente;

import jakarta.persistence.*;


@Table(name="clientes")
@Entity
public class Cliente implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String telefone;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "cpf", unique = true)
    private String cpf;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    private Endereco endereco;
    private Boolean ativo;

    //Registro
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "created_by")
    private String createdBy;


    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Aluguel> alugueis;
    
    public Cliente () {

    }
    
    public Cliente(DadosCadastroCliente dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cpf = dados.cpf();
        this.dataNascimento = dados.dataNascimento();
        this.endereco = new Endereco(dados.endereco());
        this.ativo = true;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Cliente(Endereco endereco, Long id, String nome, String telefone, String email, String cpf, LocalDate dataNascimento, Boolean ativo, Instant createdAt, Instant updatedAt, String createdBy, List<Aluguel> alugueis) {
        this.endereco = endereco;
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.ativo = ativo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.alugueis = alugueis;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<Aluguel> getAlugueis() {
        return alugueis;
    }

    public void setAlugueis(List<Aluguel> alugueis) {
        this.alugueis = alugueis;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
//
    public void atualizarInformacoes(DadosEditarCliente dados) {
        if(dados.nome() != null) {
            this.nome = dados.nome();
            }
            if(dados.telefone() != null){
                this.telefone = dados.telefone();
            }
            if(dados.endereco() != null) {
                this.endereco.atualizarInformacoes(dados.endereco());
            }
    }

    public void desativar(){
        this.ativo = false;
    }

    public void ativar(){
        this.ativo = true;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cliente cliente)) return false;
        return Objects.equals(id, cliente.id) && Objects.equals(nome, cliente.nome) && Objects.equals(telefone, cliente.telefone) && Objects.equals(email, cliente.email) && Objects.equals(cpf, cliente.cpf) && Objects.equals(dataNascimento, cliente.dataNascimento) && Objects.equals(endereco, cliente.endereco) && Objects.equals(ativo, cliente.ativo) && Objects.equals(createdAt, cliente.createdAt) && Objects.equals(updatedAt, cliente.updatedAt) && Objects.equals(createdBy, cliente.createdBy) && Objects.equals(alugueis, cliente.alugueis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, telefone, email, cpf, dataNascimento, endereco, ativo, createdAt, updatedAt, createdBy, alugueis);
    }
}
