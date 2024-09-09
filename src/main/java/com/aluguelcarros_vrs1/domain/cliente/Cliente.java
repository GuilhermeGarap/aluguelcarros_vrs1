package com.aluguelcarros_vrs1.domain.cliente;

import com.aluguelcarros_vrs1.domain.aluguel.Aluguel;
import com.aluguelcarros_vrs1.domain.endereco.Endereco;

import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="clientes")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
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

    @OneToOne(mappedBy= "cliente", cascade = CascadeType.ALL)
    private Aluguel aluguel;
    

    public Cliente(DadosCadastroCliente dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.cpf = dados.cpf();
        this.endereco = new Endereco(dados.endereco());
        this.ativo = true;
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
}
