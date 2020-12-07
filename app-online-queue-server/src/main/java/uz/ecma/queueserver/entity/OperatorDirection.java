package uz.ecma.queueserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.ecma.queueserver.entity.template.AbsEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.criteria.CriteriaBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class OperatorDirection extends AbsEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Direction direction;
    private boolean active;
}
