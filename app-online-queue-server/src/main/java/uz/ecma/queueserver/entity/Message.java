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
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Message extends AbsEntity {
    @Column(nullable = false)
    private String messageText;

    private boolean isView;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;
}