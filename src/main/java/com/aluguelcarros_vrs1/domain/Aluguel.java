package com.aluguelcarros_vrs1.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.aluguelcarros_vrs1.data.aluguel.DadosCadastroAluguel;
import com.aluguelcarros_vrs1.data.aluguel.DadosEditarAluguel;

import com.aluguelcarros_vrs1.infra.exception.ErrorDetailsException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Table(name = "alugueis")
@Entity
public class Aluguel implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    //Classe
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_inicio")
    private LocalDate dataInicio;

    @Column(name = "data_termino")
    private LocalDate dataTermino;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "carro_id")
    private Carro carro;

    @Enumerated(EnumType.STRING)
    @Column(name = "aluguel_status", columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<AluguelStatus> aluguelStatus;

    public enum AluguelStatus {
        ATIVO,
        INATIVO,
        PENDENTE,
        ENCERRADO,
        CANCELADO;

    }

    //Registro
    @JoinColumn(name = "created_at")
    private Instant createdAt;

    @JoinColumn(name = "updated_at")
    private Instant updatedAt;

    @JoinColumn(name = "created_by")
    private String createdBy;


    public Aluguel(){

    }

    public Aluguel(Long id, LocalDate dataInicio, LocalDate dataTermino, Cliente cliente, Carro carro, List<AluguelStatus> aluguelStatus, Instant createdAt, Instant updatedAt, String createdBy) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataTermino = dataTermino;
        this.cliente = cliente;
        this.carro = carro;
        this.aluguelStatus = aluguelStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
    }

    public Aluguel(Long id, LocalDate dataInicio, LocalDate dataTermino, Cliente cliente, Carro carro, List<AluguelStatus> aluguelStatus, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataTermino = dataTermino;
        this.cliente = cliente;
        this.carro = carro;
        this.aluguelStatus = aluguelStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Aluguel(LocalDate dataInicio, Long id, LocalDate dataTermino, Cliente cliente, Carro carro, List<AluguelStatus> aluguelStatus, Instant createdAt, Instant updatedAt, String createdBy) {
        this.dataInicio = dataInicio;
        this.id = id;
        this.dataTermino = dataTermino;
        this.cliente = cliente;
        this.carro = carro;
        this.aluguelStatus = aluguelStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
    }

    public Aluguel(DadosCadastroAluguel dados) {
        this.dataInicio = dados.dataInicio();
        this.dataTermino = dados.dataTermino();
        if (dados.dataInicio().equals(LocalDate.now())) {
            this.aluguelStatus = new ArrayList<>(List.of(AluguelStatus.ATIVO));
        } else {
            this.aluguelStatus = new ArrayList<>(List.of(AluguelStatus.PENDENTE));
        }
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public void atualizarInformacoes(DadosEditarAluguel dados) {
        if (dados.dataInicio() != null) {
            this.dataInicio = dados.dataInicio();
            this.updatedAt = Instant.now();
        }
        if (dados.dataTermino() != null) {
            this.dataTermino = dados.dataTermino();
            this.updatedAt = Instant.now();
        }
    }

    public void ativar() {
        this.aluguelStatus.removeIf(s ->
                s == AluguelStatus.INATIVO ||
                s == AluguelStatus.CANCELADO ||
                s == AluguelStatus.ENCERRADO
        );
        if (this.aluguelStatus.contains(AluguelStatus.ATIVO)) {
            throw new ErrorDetailsException("Não é possível ativar um aluguel já ativo!");
        }
        this.aluguelStatus.add(AluguelStatus.ATIVO);

        this.updatedAt = Instant.now();
    }

    public void desativar() {
        this.aluguelStatus.removeIf(s ->
                s == AluguelStatus.PENDENTE ||
                s == AluguelStatus.ATIVO
        );
        if (this.aluguelStatus.contains(AluguelStatus.INATIVO)) {
            throw new ErrorDetailsException("Não é possível desativar um aluguel já desativado!");
        }
        this.aluguelStatus.add(AluguelStatus.INATIVO);

        this.updatedAt = Instant.now();
    }

    public void pendente() {
        if (this.aluguelStatus.contains(AluguelStatus.PENDENTE)) {
            throw new ErrorDetailsException("Não é possível deixar pendente um aluguel já pendente");
        }
        if (!this.aluguelStatus.contains(AluguelStatus.ATIVO)) {
            ativar();
        }
        this.aluguelStatus.add(AluguelStatus.PENDENTE);

        this.updatedAt = Instant.now();
    }

    public void encerrar() {
        if (this.aluguelStatus.contains(AluguelStatus.ENCERRADO)) {
            throw new ErrorDetailsException("Não é possível encerrar um aluguel já encerrado");
        }
        if (!this.aluguelStatus.contains(AluguelStatus.INATIVO)) {
            desativar();
        }
        this.aluguelStatus.add(AluguelStatus.ENCERRADO);

        this.updatedAt = Instant.now();
    }

    public void cancelar() {
        if (this.aluguelStatus.contains(AluguelStatus.CANCELADO)) {
            throw new ErrorDetailsException("Não é possível cancelar um aluguel já cancelado");
        }
        if (!this.aluguelStatus.contains(AluguelStatus.INATIVO)) {
            desativar();
        }
        this.aluguelStatus.add(AluguelStatus.CANCELADO);

        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<AluguelStatus> getAluguelStatus() {
        return aluguelStatus;
    }

    public void setAluguelStatus(List<AluguelStatus> aluguelStatus) {
        this.aluguelStatus = aluguelStatus;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Aluguel aluguel)) return false;
        return Objects.equals(id, aluguel.id) && Objects.equals(dataInicio, aluguel.dataInicio) && Objects.equals(dataTermino, aluguel.dataTermino) && Objects.equals(cliente, aluguel.cliente) && Objects.equals(carro, aluguel.carro) && Objects.equals(aluguelStatus, aluguel.aluguelStatus) && Objects.equals(createdAt, aluguel.createdAt) && Objects.equals(updatedAt, aluguel.updatedAt) && Objects.equals(createdBy, aluguel.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dataInicio, dataTermino, cliente, carro, aluguelStatus, createdAt, updatedAt, createdBy);
    }
}
