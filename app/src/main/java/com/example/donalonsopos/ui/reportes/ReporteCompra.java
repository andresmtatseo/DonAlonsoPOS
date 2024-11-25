package com.example.donalonsopos.ui.reportes;

import android.content.Context;
import android.media.Image;
import android.os.Environment;

import com.example.donalonsopos.R;
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
            // Obtener la fecha y hora actual
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

            // Intentar cargar la imagen
            Image logo;
            try {

                Image logo = Image.getInstance(context.getResources().openRawResource(R.drawable.logodonalonso));
                logo.scaleToFit(200, 95);

                PdfPTable headerTable = new PdfPTable(1);
                headerTable.setWidthPercentage(100);
                PdfPCell imageCell = new PdfPCell(logo, false);
                imageCell.setBorder(Rectangle.NO_BORDER);
                imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerTable.addCell(imageCell);
                documento.add(headerTable);
            } catch (Exception e) {
                e.printStackTrace();
                Paragraph noLogo = new Paragraph("Logo no disponible.\n", new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC));
                noLogo.setAlignment(Element.ALIGN_CENTER);
                documento.add(noLogo);
            }

            // Título
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

            // Verificar si hay datos
            if (compras == null || compras.isEmpty()) {
                Paragraph noData = new Paragraph("No se encontraron datos de compras para el informe.", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
                noData.setAlignment(Element.ALIGN_CENTER);
                documento.add(noData);
            } else {
                // Crear tabla con datos
                PdfPTable tabla = new PdfPTable(6);
                tabla.setWidthPercentage(100);
                tabla.setSpacingBefore(10f);
                tabla.setSpacingAfter(10f);

                agregarCeldaEncabezado(tabla, "ID Compra", boldFont);
                agregarCeldaEncabezado(tabla, "ID Proveedor", boldFont);
                agregarCeldaEncabezado(tabla, "Método Pago", boldFont);
                agregarCeldaEncabezado(tabla, "Fecha Compra", boldFont);
                agregarCeldaEncabezado(tabla, "Nro Factura", boldFont);
                agregarCeldaEncabezado(tabla, "Total", boldFont);

                for (Compra compra : compras) {
                    tabla.addCell(crearCeldaConBorde(String.valueOf(compra.getIdCompra()), boldFont));
                    tabla.addCell(crearCeldaConBorde(String.valueOf(compra.getIdProveedor()), boldFont));
                    tabla.addCell(crearCeldaConBorde(compra.getFechaCompra(), boldFont));
                    tabla.addCell(crearCeldaConBorde(compra.getMetodoPago(), boldFont));
                    tabla.addCell(crearCeldaConBorde(compra.getNumeroFactura(), boldFont));
                    tabla.addCell(crearCeldaConBorde(String.format("%.2f", compra.getTotal()), boldFont));
                }

                documento.add(tabla);
            }
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
        celda.setPadding(5f);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return celda;
    }
}
