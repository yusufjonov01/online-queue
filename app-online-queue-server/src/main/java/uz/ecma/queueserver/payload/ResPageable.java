package uz.ecma.queueserver.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResPageable {
    private Integer page;
    private Integer size;
    private Integer totalPages;
    private Long totalElements;
    private Object object;
}
