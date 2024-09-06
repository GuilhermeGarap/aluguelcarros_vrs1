package com.aluguelcarros_vrs1.domain.aluguel;

import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.domain.cliente.Cliente;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="alugueis")
@Entity(name="aluguel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Aluguel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private Integer dias_alugados;
    private String data_inicio;
    private Boolean ativo;

    @OneToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToOne
    @JoinColumn(name = "carro_id")
    private Carro carro;

    public Aluguel(DadosCadastroAluguel dados) {
        this.dias_alugados = dados.dias_alugados();
        this.data_inicio = dados.data_inicio();
        this.ativo = true;
    }
    
    public void atualizarInformacoes(DadosEditarAluguel dados) {
        if(dados.dias_alugados() != null) {
            this.dias_alugados = dados.dias_alugados();
        }
        if(dados.data_inicio() != null){
            this.data_inicio = dados.data_inicio();
        }
    }

    public void desativar(){
        this.ativo = false;
    }

    public void ativar(){
        this.ativo = true;
    }
}
