package com.example.donalonsopos.ui.reportes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.example.donalonsopos.R;
import com.example.donalonsopos.data.DTO.MovimientoProducto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ReporteMovimientoProducto {

    private final Context context;

    public ReporteMovimientoProducto(Context context) {
        this.context = context;
    }

    public void crearReporteMovimientoProducto(List<MovimientoProducto> movimientos) {
        Document documento = new Document();

        try {
            // Obtener la fecha y hora actual
            String fecha = new java.text.SimpleDateFormat("dd-MM-yyyy").format(new java.util.Date());
            String hora = new java.text.SimpleDateFormat("HH:mm").format(new java.util.Date());
            String horaParaArchivo = hora.replace(":", "_");

            // Directorio de descargas
            File directorioDescargas = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File archivoPDF = new File(directorioDescargas, "ReporteMovimientoProducto_" + fecha + "_Hora" + horaParaArchivo + ".pdf");

            // Crear el archivo PDF
            PdfWriter.getInstance(documento, new FileOutputStream(archivoPDF));
            documento.open();

            // Intentar cargar la imagen (logo)
            try {
                // Cargar el logo desde res/drawable
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.logodonalonso);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image logo = Image.getInstance(stream.toByteArray());
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
            Paragraph titulo = new Paragraph("Heladería Don Alonso\nReporte de Movimiento de Producto\n", boldFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingBefore(10);
            documento.add(titulo);

            // Fecha y hora
            Paragraph fechaHoraParagraph = new Paragraph("Generado el " + fecha + " a las " + hora + "\n", boldFont);
            fechaHoraParagraph.setAlignment(Element.ALIGN_RIGHT);
            fechaHoraParagraph.setSpacingAfter(10);
            documento.add(fechaHoraParagraph);

            // Verificar si hay movimientos
            if (movimientos == null || movimientos.isEmpty()) {
                Paragraph noData = new Paragraph("No se encontraron movimientos para el informe.", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
                noData.setAlignment(Element.ALIGN_CENTER);
                documento.add(noData);
            } else {
                // Crear tabla con 8 columnas
                PdfPTable tabla = new PdfPTable(8);
                tabla.setWidthPercentage(100);
                tabla.setSpacingBefore(10f);
                tabla.setSpacingAfter(10f);

                // Añadir encabezados de la tabla
                agregarCeldaEncabezado(tabla, "ID Movimiento", boldFont);
                agregarCeldaEncabezado(tabla, "ID Producto", boldFont);
                agregarCeldaEncabezado(tabla, "ID Usuario", boldFont);
                agregarCeldaEncabezado(tabla, "Tipo Movimiento", boldFont);
                agregarCeldaEncabezado(tabla, "Referencia", boldFont);
                agregarCeldaEncabezado(tabla, "Cantidad", boldFont);
                agregarCeldaEncabezado(tabla, "Fecha Movimiento", boldFont);
                agregarCeldaEncabezado(tabla, "Descripción", boldFont);

                // Agregar los datos de cada movimiento a la tabla
                for (MovimientoProducto movimiento : movimientos) {
                    tabla.addCell(crearCeldaConBorde(String.valueOf(movimiento.getIdMovimiento()), boldFont));
                    tabla.addCell(crearCeldaConBorde(String.valueOf(movimiento.getIdProducto()), boldFont));
                    tabla.addCell(crearCeldaConBorde(String.valueOf(movimiento.getIdUsuario()), boldFont));
                    tabla.addCell(crearCeldaConBorde(movimiento.getTipoMovimiento(), boldFont));
                    tabla.addCell(crearCeldaConBorde(String.valueOf(movimiento.getReferencia()), boldFont));
                    tabla.addCell(crearCeldaConBorde(String.valueOf(movimiento.getCantidad()), boldFont));
                    tabla.addCell(crearCeldaConBorde(movimiento.getFechaMovimiento(), boldFont));
                    tabla.addCell(crearCeldaConBorde(movimiento.getDescripcion(), boldFont));
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
        celda.setBackgroundColor(BaseColor.LIGHT_GRAY); // Fondo gris claro para encabezado
        celda.setBorderWidth(1.2f); // Grosor del borde
        tabla.addCell(celda);
    }

    private PdfPCell crearCeldaConBorde(String texto, Font font) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, font));
        celda.setBorderWidth(1.2f);
        celda.setPadding(5f);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER); // Centrar texto
        return celda;
    }
}
