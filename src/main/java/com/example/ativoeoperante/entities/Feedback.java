package com.example.ativoeoperante.entities;

import jakarta.persistence.*;

@Entity
@Table(name= "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fee_id")
    private Long id;
    @Column(name = "fee_texto")
    private String texto;
    @OneToOne
    @JoinColumn(name ="den_id", nullable = false)
    private Orgao orgao;

    public Feedback() {
        this(0L,"",null);
    }

    public Feedback(Long id, String texto, Orgao orgao) {
        this.id = id;
        this.texto = texto;
        this.orgao = orgao;
    }

    public Feedback(String texto, Orgao orgao) {
        this.texto = texto;
        this.orgao = orgao;
    }
}
