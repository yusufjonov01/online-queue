package uz.ecma.queueserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import uz.ecma.queueserver.entity.template.AbsEntity;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Contact extends AbsEntity {

    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private District district;
    @Column(nullable = false)
    private String address;

    @ElementCollection
    private List<String> phoneNumber;

    private String email;

    private String fax;

    private String lat;

    private String lng;

    @OneToMany(mappedBy = "contact")
    private List<AwareCompany> awareCompanies;
}
