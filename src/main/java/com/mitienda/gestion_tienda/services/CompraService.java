package com.mitienda.gestion_tienda.services;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitienda.gestion_tienda.dtos.CompraDTO;
import com.mitienda.gestion_tienda.dtos.CompraProductoDTO;
import com.mitienda.gestion_tienda.dtos.CompraProductoResponseDTO;
import com.mitienda.gestion_tienda.dtos.CompraResponseDTO;
import com.mitienda.gestion_tienda.entities.Compra;
import com.mitienda.gestion_tienda.entities.CompraProducto;
import com.mitienda.gestion_tienda.entities.Producto;
import com.mitienda.gestion_tienda.entities.Usuario;
import com.mitienda.gestion_tienda.repositories.CompraRepository;
import com.mitienda.gestion_tienda.repositories.ProductoRepository;
import com.mitienda.gestion_tienda.repositories.UsuarioRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraService {
    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<CompraResponseDTO> listarCompras(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            
        List<Compra> compras;
        if (usuario.getRol() == Usuario.Role.ADMIN) {
            compras = compraRepository.findAll();
        } else {
            compras = compraRepository.findByUsuario(usuario);
        }
        
        return compras.stream()
            .map(this::convertToDTO)  // Using your existing conversion method
            .collect(Collectors.toList());
    }

    @Transactional
    public CompraResponseDTO realizarCompra(String email, CompraDTO compraDTO) {
        Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Compra compra = new Compra();
        compra.setUsuario(usuario);
        compra.setFecha(LocalDateTime.now());
        compra.setTotal(BigDecimal.ZERO);

        BigDecimal total = BigDecimal.ZERO;

        for (CompraProductoDTO item : compraDTO.getProductos()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            CompraProducto compraProducto = new CompraProducto();
            compraProducto.setProducto(producto);
            compraProducto.setCantidad(item.getCantidad());
            compraProducto.setSubtotal(
                producto.getPrecio().multiply(BigDecimal.valueOf(item.getCantidad()))
            );
            
            compra.addCompraProducto(compraProducto);
            total = total.add(compraProducto.getSubtotal());
        }

        compra.setTotal(total);
        compra = compraRepository.save(compra);
        
        return convertToDTO(compra);
    }

    private CompraResponseDTO convertToDTO(Compra compra) {
        CompraResponseDTO dto = new CompraResponseDTO();
        dto.setId(compra.getId());
        dto.setUsuarioNombre(compra.getUsuario().getNombre());
        dto.setFecha(compra.getFecha());
        dto.setTotal(compra.getTotal());
        
        dto.setProductos(compra.getProductos().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
            
        return dto;
    }

    private CompraProductoResponseDTO convertToDTO(CompraProducto compraProducto) {
        CompraProductoResponseDTO dto = new CompraProductoResponseDTO();
        dto.setId(compraProducto.getId());
        dto.setProductoNombre(compraProducto.getProducto().getNombre());
        dto.setPrecioUnitario(compraProducto.getProducto().getPrecio());
        dto.setCantidad(compraProducto.getCantidad());
        dto.setSubtotal(compraProducto.getSubtotal());
        return dto;
    }
}