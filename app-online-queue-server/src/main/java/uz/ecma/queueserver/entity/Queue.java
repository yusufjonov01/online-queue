package uz.ecma.queueserver.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.ecma.queueserver.entity.enums.QueueStatus;
import uz.ecma.queueserver.entity.template.AbsEntity;
import javax.persistence.*;
import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Queue extends AbsEntity{
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private User client;

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private Direction direction;

    @ManyToOne(fetch = FetchType.LAZY)
    private User operator;

    @Enumerated(EnumType.STRING)
    private QueueStatus status;

    private Timestamp startTime;

    private Timestamp finishTime;

    private Integer queueNumber;
}
