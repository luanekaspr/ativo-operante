package com.example.ativoeoperante.entities;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name= "denuncia")
public class Denuncia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "den_id")
    private Long id;
    @Column(name = "den_titulo")
    private String titulo;
    @Column(name = "den_texto")
    private String texto;
    @Column(name = "den_urgencia")
    private int urgencia;
    @ManyToOne
    @JoinColumn(name="org_id", nullable=false)
    private Orgao orgao;
   @Column(name = "den_data", updatable = false)
   private LocalDateTime dataHora;
    @ManyToOne
    @JoinColumn(name = "tip_id",nullable = false)
    private Tipo tipo;
    @ManyToOne
    @JoinColumn(name = "usu_id", nullable = false)
    private Usuario usuario;
    @OneToOne(mappedBy = "denuncia", cascade = CascadeType.ALL, orphanRemoval = true)
    private Feedback feedback;



    public Denuncia() {
        this(null, "", "", 0, null, LocalDateTime.now(), null, null);
    }

    public Denuncia(Long id, String titulo, String texto, int urgencia, Orgao orgao, LocalDateTime dataHora, Tipo tipo, Usuario usuario) {
        this.id = id;
        this.titulo = titulo;
        this.texto = texto;
        this.urgencia = urgencia;
        this.orgao = orgao;
        this.dataHora = dataHora;
        this.tipo = tipo;
        this.usuario = usuario;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getUrgencia() {
        return urgencia;
    }

    public void setUrgencia(int urgencia) {
        this.urgencia = urgencia;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Orgao getOrgao() {
        return orgao;
    }

    public void setOrgao(Orgao orgao) {
        this.orgao = orgao;
    }
}
