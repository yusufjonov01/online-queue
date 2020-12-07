package uz.ecma.queueserver.payload;

import lombok.Data;
import uz.ecma.queueserver.entity.Aware;
import uz.ecma.queueserver.entity.Contact;

import java.util.UUID;

@Data
public class ReqAwareCompany {
    private String awareLink;
    private Contact contact;
    private Aware aware;
}
