package uz.ecma.queueserver.entity.template;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public abstract class AbsNameEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nameUzl;

    @Column(nullable = false)
    private String nameUzk;

    @Column(nullable = false)
    private String nameRu;

    @Column(nullable = false)
    private String nameEn;
}