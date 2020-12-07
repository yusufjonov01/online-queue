package uz.ecma.queueserver.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.ecma.queueserver.entity.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResCompany {
    private Timestamp createdAt;

    private UUID createdBy;

    private UUID updateBy;

    private Timestamp updatedAt;

    private int countRate;

    private UUID id;

    private String name;

    private Contact contact;

    private Attachment logo;

    private String tin;

    private Category category;

    private List<WorkTime> workTimes;

    private boolean active;

    private Double rate;

    public ResCompany(UUID id, String name, Contact contact, Attachment logo, String tin, Category category, List<WorkTime> workTimes, boolean active, Double rate) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.logo = logo;
        this.tin = tin;
        this.category = category;
        this.workTimes = workTimes;
        this.active = active;
        this.rate = rate;
    }
}
