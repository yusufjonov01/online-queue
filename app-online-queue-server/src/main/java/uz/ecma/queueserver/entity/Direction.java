package uz.ecma.queueserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.ecma.queueserver.entity.enums.DirectionDateName;
import uz.ecma.queueserver.entity.template.AbsNameEntity;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Direction extends AbsNameEntity {
    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Company company;
    @JsonIgnore
    @OneToMany(mappedBy = "direction", fetch = FetchType.LAZY)
    private List<OperatorDirection> operatorDirections;
    @JsonIgnore
    @OneToMany(mappedBy = "direction", fetch = FetchType.LAZY)
    private List<WorkTime> workTimes;
    @Column(nullable = false)
    private boolean isAutoTime;

    private Integer time;

    private DirectionDateName directionDateName;

    private boolean active = true;
    private Double rateAmount;//5+4+2
    private int countRate;//1+1+1
}
