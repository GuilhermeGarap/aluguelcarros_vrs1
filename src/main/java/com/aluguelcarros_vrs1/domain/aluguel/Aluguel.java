package com.aluguelcarros_vrs1.domain.aluguel;

import java.time.LocalDate;

import com.aluguelcarros_vrs1.domain.carro.Carro;
import com.aluguelcarros_vrs1.domain.cliente.Cliente;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "alugueis")
@Entity(name = "aluguel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
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

    public Aluguel(DadosCadastroAluguel dados) {
        this.data_inicio = dados.data_inicio();
        this.data_termino = dados.data_termino();
        this.ativo = true;
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
}
