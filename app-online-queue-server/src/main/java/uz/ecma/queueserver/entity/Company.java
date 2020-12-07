package uz.ecma.queueserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.ecma.queueserver.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Company extends AbsEntity {
    @Column(nullable = false)
    private String name;

    @OneToOne(optional = false)
    private Contact contact;

    @OneToOne
    private Attachment logo;

    @Column(nullable = false, unique = true)
    private String tin;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Category category;
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<WorkTime> workTimes;
    @JsonIgnore
    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
    private List<Direction> directions;

    private boolean active = false;

    private Double rateAmount;//5+4+2
    private int countRate;//1+1+1
}
