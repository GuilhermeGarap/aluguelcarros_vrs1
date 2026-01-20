package com.aluguelcarros_vrs1.domain.aluguel;

import java.time.LocalDate;
import java.util.Objects;

import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.domain.cliente.Cliente;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Table(name = "alugueis")
@Entity
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDate data_inicio;
    private LocalDate data_termino;
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "carro_id")
    private Carro carro;

    public Aluguel(){

    }

    public Aluguel(Long id, LocalDate data_inicio, LocalDate data_termino, Boolean ativo, Cliente cliente,
            Carro carro) {
        this.id = id;
        this.data_inicio = data_inicio;
        this.data_termino = data_termino;
        this.ativo = ativo;
        this.cliente = cliente;
        this.carro = carro;
    }

    public Aluguel(DadosCadastroAluguel dados) {
        this.data_inicio = dados.data_inicio();
        this.data_termino = dados.data_termino();
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(LocalDate data_inicio) {
        this.data_inicio = data_inicio;
    }

    public LocalDate getData_termino() {
        return data_termino;
    }

    public void setData_termino(LocalDate data_termino) {
        this.data_termino = data_termino;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Carro getCarro() {
        return carro;
    }

    public void setCarro(Carro carro) {
        this.carro = carro;
    }

    public void atualizarInformacoes(DadosEditarAluguel dados) {
        if (dados.data_inicio() != null) {
            this.data_inicio = dados.data_inicio();
        }
        if (dados.data_termino() != null) {
            this.data_termino = dados.data_termino();
        }
    }

    public void desativar() {
        this.ativo = false;
    }

    public void ativar() {
        this.ativo = true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final Aluguel other = (Aluguel) obj;
        return Objects.equals(this.id, other.id);
    }

    





}
