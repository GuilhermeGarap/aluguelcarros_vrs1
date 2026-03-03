package com.aluguelcarros_vrs1.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import com.aluguelcarros_vrs1.data.carro.DadosCadastroCarro;
import com.aluguelcarros_vrs1.data.carro.DadosEditarCarro;

import jakarta.persistence.*;


@Table(name = "carros")
@Entity
public class Carro implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String modelo;

    @Column(name = "valor_dia")
    private Float valorDia;
    private Integer unidades;
    private Boolean ativo;
    private Integer disponivel;

    @OneToMany(mappedBy = "carro", cascade = CascadeType.ALL)
    private List<Aluguel> alugueis;

    //Registro
    @JoinColumn(name = "created_at")
    private Instant createdAt;

    @JoinColumn(name = "updated_at")
    private Instant updatedAt;

    @JoinColumn(name = "created_by")
    private String createdBy;

    public Carro() {
    }
    
    public Carro(DadosCadastroCarro dados) {
        this.modelo = dados.modelo();
        this.valorDia = dados.valor_dia();
        this.unidades = dados.unidades();
        this.ativo = true;
        this.disponivel = dados.unidades();
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Carro(Long id, String modelo, Float valorDia, Integer unidades, Boolean ativo, Integer disponivel, List<Aluguel> alugueis, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.modelo = modelo;
        this.valorDia = valorDia;
        this.unidades = unidades;
        this.ativo = ativo;
        this.disponivel = disponivel;
        this.alugueis = alugueis;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Float getValorDia() {
        return valorDia;
    }

    public void setValorDia(Float valor_dia) {
        this.valorDia = valor_dia;
    }

    public Integer getUnidades() {
        return unidades;
    }

    public void setUnidades(Integer unidades) {
        this.unidades = unidades;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Integer disponivel) {
        this.disponivel = disponivel;
    }

    public List<Aluguel> getAlugueis() {
        return alugueis;
    }

    public void setAlugueis(List<Aluguel> alugueis) {
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

    public void atualizarInformacoes(DadosEditarCarro dados) {
        if (dados.modelo() != null) {
            this.modelo = dados.modelo();
            this.updatedAt = Instant.now();
        }
        if (dados.valor_dia() != null) {
            this.valorDia = dados.valor_dia();
            this.updatedAt = Instant.now();
        }
        if (dados.unidades() != null) {
            int diferencaUnidades = dados.unidades() - this.unidades;
            
            this.unidades = dados.unidades();
    
            this.disponivel += diferencaUnidades;
    
            if (this.disponivel < 0) {
                this.disponivel = 0;
            }
            this.updatedAt = Instant.now();
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
        if (!(o instanceof Carro carro)) return false;
        return Objects.equals(id, carro.id) && Objects.equals(modelo, carro.modelo) && Objects.equals(valorDia, carro.valorDia) && Objects.equals(unidades, carro.unidades) && Objects.equals(ativo, carro.ativo) && Objects.equals(disponivel, carro.disponivel) && Objects.equals(alugueis, carro.alugueis) && Objects.equals(createdAt, carro.createdAt) && Objects.equals(updatedAt, carro.updatedAt) && Objects.equals(createdBy, carro.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, modelo, valorDia, unidades, ativo, disponivel, alugueis, createdAt, updatedAt, createdBy);
    }
}
