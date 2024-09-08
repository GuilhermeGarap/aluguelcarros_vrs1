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
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "carros")
@Entity(name = "carro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
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
    private List<Aluguel> alugueis;  // Mudei de @OneToOne para @OneToMany

    public Carro(DadosCadastroCarro dados) {
        this.modelo = dados.modelo();
        this.valor_dia = dados.valor_dia();
        this.unidades = dados.unidades();
        this.ativo = true;
        this.disponivel = dados.unidades();
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
}
