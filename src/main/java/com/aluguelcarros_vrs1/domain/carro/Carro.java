package com.aluguelcarros_vrs1.domain.carro;

import java.util.List;

import com.aluguelcarros_vrs1.domain.aluguel.Aluguel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;


@Table(name = "carros")
@Entity
public class Carro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String modelo;
    private Float valor_dia;
    private Integer unidades;
    private Boolean ativo;
    private Integer disponivel;

    @OneToMany(mappedBy = "carro", cascade = CascadeType.ALL)
    private List<Aluguel> alugueis;

    public Carro() {

    }
    
    public Carro(DadosCadastroCarro dados) {
        this.modelo = dados.modelo();
        this.valor_dia = dados.valor_dia();
        this.unidades = dados.unidades();
        this.ativo = true;
        this.disponivel = dados.unidades();
    }

    public Carro(String modelo, Float valor_dia, Integer unidades, Boolean ativo, Integer disponivel) {
        this.modelo = modelo;
        this.valor_dia = valor_dia;
        this.unidades = unidades;
        this.ativo = ativo;
        this.disponivel = disponivel;
    }

    public Carro(List<Aluguel> alugueis, Boolean ativo, Integer disponivel, Long id, String modelo, Integer unidades, Float valor_dia) {
        this.alugueis = alugueis;
        this.ativo = ativo;
        this.disponivel = disponivel;
        this.id = id;
        this.modelo = modelo;
        this.unidades = unidades;
        this.valor_dia = valor_dia;
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

    public Float getValor_dia() {
        return valor_dia;
    }

    public void setValor_dia(Float valor_dia) {
        this.valor_dia = valor_dia;
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

    public void atualizarInformacoes(DadosEditarCarro dados) {
        if (dados.modelo() != null) {
            this.modelo = dados.modelo();
        }
        if (dados.valor_dia() != null) {
            this.valor_dia = dados.valor_dia();
        }
        if (dados.unidades() != null) {

            int diferencaUnidades = dados.unidades() - this.unidades;
            
            this.unidades = dados.unidades();
    
            this.disponivel += diferencaUnidades;
    
            if (this.disponivel < 0) {
                this.disponivel = 0;
            }
        }
    }

    public void desativar(){
        this.ativo = false;
    }

    public void ativar(){
        this.ativo = true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Carro other = (Carro) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    

}
