package uz.ecma.queueserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.ecma.queueserver.entity.template.AbsEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AwareCompany extends AbsEntity {

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Aware aware;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Contact contact;

    @Column(nullable = false)
    private String awareLink;
}
