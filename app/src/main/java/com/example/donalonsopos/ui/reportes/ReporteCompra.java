package com.example.donalonsopos.ui.reportes;

import android.content.Context;
import android.os.Environment;

import com.example.donalonsopos.data.DTO.Compra;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReporteCompra {

    private final Context context;

    public ReporteCompra(Context context) {
        this.context = context;
    }

    public void crearInformeCompras(List<Compra> compras) {
        Document documento = new Document();

        try {
            // Obtener la fecha y hora actual y formatearla
            LocalDateTime fechaHoraActual = LocalDateTime.now();
            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm");

            String fecha = fechaHoraActual.format(formatoFecha);
            String hora = fechaHoraActual.format(formatoHora);
            String horaParaArchivo = hora.replace(":", "_");

            // Directorio de descargas
            File directorioDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File archivoPDF = new File(directorioDescargas, "ReporteCompras_" + fecha + "_Hora" + horaParaArchivo + ".pdf");

            // Crear el archivo PDF
            PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
            documento.open();

            // Cargar la imagen desde los recursos
            Image logo = Image.getInstance(context.getResources().openRawResource(R.raw.logodonalonso)); // Ajusta el nombre del recurso
            logo.scaleToFit(200, 95);

            // Crear una tabla para centrar el logo
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            PdfPCell imageCell = new PdfPCell(logo, false);
            imageCell.setBorder(Rectangle.NO_BORDER);
            imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerTable.addCell(imageCell);
            documento.add(headerTable);

            // Fuente para el título
            Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
            Paragraph titulo = new Paragraph("Heladería Don Alonso\nReporte de Compras\n", boldFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingBefore(10);
            documento.add(titulo);

            // Fecha y hora
            Paragraph fechaHoraParagraph = new Paragraph("Generado el " + fecha + " a las " + hora + "\n", boldFont);
            fechaHoraParagraph.setAlignment(Element.ALIGN_RIGHT);
            fechaHoraParagraph.setSpacingAfter(10);
            documento.add(fechaHoraParagraph);

            // Fuente para el contenido
            Font fontTitulo = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font fontContenido = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);

            // Título "Reporte de Compras"
            Paragraph tituloReporte = new Paragraph("Reporte de Compras", fontTitulo);
            tituloReporte.setSpacingBefore(10);
            tituloReporte.setSpacingAfter(10);
            documento.add(tituloReporte);

            // Crear la tabla con los datos
            PdfPTable tabla = new PdfPTable(6); // 6 columnas
            tabla.setWidthPercentage(100);
            tabla.setSpacingBefore(10f);
            tabla.setSpacingAfter(10f);

            // Añadir encabezados
            agregarCeldaEncabezado(tabla, "ID Compra", fontContenido);
            agregarCeldaEncabezado(tabla, "ID Proveedor", fontContenido);
            agregarCeldaEncabezado(tabla, "Fecha Compra", fontContenido);
            agregarCeldaEncabezado(tabla, "Método Pago", fontContenido);
            agregarCeldaEncabezado(tabla, "Nro Factura", fontContenido);
            agregarCeldaEncabezado(tabla, "Total", fontContenido);

            // Agregar los datos de cada compra
            for (Compra compra : compras) {
                tabla.addCell(crearCeldaConBorde(String.valueOf(compra.getIdCompra()), fontContenido));
                tabla.addCell(crearCeldaConBorde(String.valueOf(compra.getIdProveedor()), fontContenido));
                tabla.addCell(crearCeldaConBorde(compra.getFechaCompra(), fontContenido));
                tabla.addCell(crearCeldaConBorde(compra.getMetodoPago(), fontContenido));
                tabla.addCell(crearCeldaConBorde(compra.getNumeroFactura(), fontContenido));
                tabla.addCell(crearCeldaConBorde(String.format("%.2f", compra.getTotal()), fontContenido));
            }

            // Añadir la tabla al documento
            documento.add(tabla);

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            documento.close();
        }
    }

    private void agregarCeldaEncabezado(PdfPTable tabla, String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, font));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(8f);
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY);
        celda.setBorderWidth(1.2f);
        tabla.addCell(celda);
    }

    private PdfPCell crearCeldaConBorde(String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, font));
        celda.setBorderWidth(1.2f);
        return celda;
    }
}
