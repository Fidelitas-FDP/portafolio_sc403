package domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Data;

/**
 *
 * @author AaCcAio
 */

@Data
@Entity
@Table(name = "factura")

public class Factura implements Serializable {
    
}
