package com.example.appgastronomica.services.impl;

import com.example.appgastronomica.configs.EmailConfig;
import com.example.appgastronomica.models.Cliente;
import com.example.appgastronomica.models.Producto;
import com.example.appgastronomica.services.ClienteService;
import com.example.appgastronomica.services.EmailService;
import com.example.appgastronomica.services.ProductoService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService {

    private final ProductoService productoService;
    private final ClienteService clienteService;

    @Autowired
    public EmailServiceImpl(ProductoService productoService, ClienteService clienteService) {
        this.productoService = productoService;
        this.clienteService = clienteService;
    }

    @Override
    public void enviarCatalogoProductos(String email, String nroDoc) {
        Cliente cliente = clienteService.obtenerClientePorNroDoc(nroDoc);

        List<Producto> productos = productoService.obtenerProductos();

        Properties properties = EmailConfig.getProperties();

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(properties.getProperty("mail.smtp.user"), properties.getProperty("mail.smtp.password"));
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.getProperty("mail.smtp.user")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Catálogo de Productos");

            //Construir el contenido HTML del correo con el catálogo de productos y el nombre del cliente
            String htmlContent = buildProductCatalogHtml(productos, cliente.nombreCompleto());
            message.setContent(htmlContent, "text/html");

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo electrónico: " + e.getMessage());
        }
    }

    private String buildProductCatalogHtml(List<Producto> productos, String nombreCompletoCliente) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<!DOCTYPE html>");
        htmlBuilder.append("<html>");
        htmlBuilder.append("<head>");
        htmlBuilder.append("<meta charset=\"UTF-8\">");
        htmlBuilder.append("<title>Catálogo de Productos</title>");
        htmlBuilder.append("<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj\" crossorigin=\"anonymous\">");
        htmlBuilder.append("<style>");
        htmlBuilder.append("body { background-color: #f0f0f0; padding: 20px; }");
        htmlBuilder.append(".product-card { width: 300px; margin: 20px; padding: 20px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }");
        htmlBuilder.append(".product-card img { max-width: 100%; height: auto; margin-bottom: 20px; }");
        htmlBuilder.append("</style>");
        htmlBuilder.append("</head>");
        htmlBuilder.append("<body>");
        htmlBuilder.append("<h1>¡Hola ").append(nombreCompletoCliente).append("! Disfruta de nuestros productos!</h1>");

        // Agregar cada producto al catálogo
        for (Producto producto : productos) {
            if (producto.getStock() >= 1) {
                htmlBuilder.append("<div class=\"product-card\">");
                htmlBuilder.append("<h2>").append(producto.getNombre()).append("</h2>");
                htmlBuilder.append("<img src=\"").append(producto.getImagen()).append("\" alt=\"").append(producto.getNombre()).append("\"/>");
                htmlBuilder.append("<p>Descripción: ").append(producto.getDescripcion()).append("</p>");
                htmlBuilder.append("<p>Precio: ").append(producto.getPrecio()).append("</p>");

                htmlBuilder.append("</div>");
            }
        }

        htmlBuilder.append("</body>");
        htmlBuilder.append("</html>");

        return htmlBuilder.toString();
    }


}
