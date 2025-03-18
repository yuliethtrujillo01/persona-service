package co.empresa.productoservice.controllers;

import co.empresa.productoservice.model.entities.Producto;
import co.empresa.productoservice.model.services.IProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/producto-service")
public class ProductoRestController {

    // Inyección de dependencia del servicio que proporciona servicios de CRUD
    private IProductoService productoService;

    // Inyección de dependencia del servicio que proporciona servicios de CRUD
    @Autowired
    public ProductoRestController(IProductoService productoService) {
        this.productoService = productoService;
    }

    // Metodo que retorna todos los productos
    @GetMapping("/productos")
    public List<Producto> getProductos(){
        return productoService.findAll();
    }

    // Metodo que guarda un producto pasandolo por el cuerpo de la petición
    @PostMapping("/productos")
    public Producto save(@RequestBody Producto producto) {
        return productoService.save(producto);
    }

    // Metodo que elimina un producto pasandolo por el cuerpo de la petición
    @DeleteMapping("/productos")
    public void delete(@RequestBody Producto producto){
        productoService.delete(producto);
    }

    // Metodo que actualiza un producto pasandolo por el cuerpo de la petición
    @PutMapping("/productos")
    public Producto update(@RequestBody Producto producto){
        return productoService.update(producto);
    }

    // Metodo que retorna un producto por su id pasado por la URL
    @GetMapping("/productos/{id}")
    public Producto findById(@PathVariable("id") Long id){
        return productoService.findById(id);
    }


}
