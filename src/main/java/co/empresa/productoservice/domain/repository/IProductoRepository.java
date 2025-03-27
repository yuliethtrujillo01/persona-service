package co.empresa.productoservice.domain.repository;

import co.empresa.productoservice.domain.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface que hereda de JpaRepository para realizar las
 * operaciones de CRUD paginacion y ordenamiento sobre la entidad Producto
 */
public interface IProductoRepository extends JpaRepository<Producto, Long> {
}
