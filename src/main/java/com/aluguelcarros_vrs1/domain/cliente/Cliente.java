package com.aluguelcarros_vrs1.domain.cliente;

import java.util.List;
import java.util.Objects;

import com.aluguelcarros_vrs1.domain.aluguel.Aluguel;
import com.aluguelcarros_vrs1.domain.endereco.Endereco;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Table(name="clientes")
@Entity
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String telefone;
    private String email;
    private String cpf;
    private Endereco endereco;
    private Boolean ativo;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Aluguel> alugueis;
    
    public Cliente () {

    }

    
    public Cliente(DadosCadastroCliente dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cpf = dados.cpf();
        this.endereco = new Endereco(dados.endereco());
        this.ativo = true;
    }

    public Cliente(Boolean ativo, String cpf, String email, Endereco endereco, Long id, String nome, String telefone) {
        this.ativo = ativo;
        this.cpf = cpf;
        this.email = email;
        this.endereco = endereco;
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
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


    public boolean verificadorCpf(String cpf) {
        CPFValidator cpfValidator = new CPFValidator();
    try {
        cpfValidator.assertValid(cpf);
        return true;
    } catch (InvalidStateException e) {
        return false;
    }
}

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cliente other = (Cliente) obj;
        return Objects.equals(this.id, other.id);
    }

    


}
