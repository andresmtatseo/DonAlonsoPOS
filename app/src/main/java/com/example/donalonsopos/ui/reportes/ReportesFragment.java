package com.example.donalonsopos.ui.reportes;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.donalonsopos.R;
import com.itextpdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;



public class ReportesFragment extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    Spinner seleccion;
    private Button btnGenerarReporte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reportes);


        seleccion = findViewById(R.id.spinner_r);
        btnGenerarReporte = findViewById(R.id.btnGenerarReporte);

        // Configurar el Spinner con opciones
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.spinner_reportes, android.R.layout.simple_spinner_item);

        seleccion.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Configurar el comportamiento del Spinner

        seleccion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Ocultar todos los filtros
                filterDate.setVisibility(View.GONE);
                filterProduct.setVisibility(View.GONE);
                filterClient.setVisibility(View.GONE); // Mostrar el filtro correspondiente

             String selected = parent.getItemAtPosition(position).toString();
             switch (selected) {
                 case "Clientes": filterClient.setVisibility(View.VISIBLE); break;
                 case "Productos": filterProduct.setVisibility(View.VISIBLE); break;
                 case "Ventas": filterDate.setVisibility(View.VISIBLE); break; }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) { // No hacer nada

            } });


        // Configurar el botón para generar el reporte
        btnGenerarReporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar y solicitar permisos en tiempo de ejecución
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(ReportesFragment.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(ReportesFragment.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                    } else {
                        generatePdf();
                    }
                } else {
                    generatePdf();
                }
            }
        });
    }

    private void generatePdf() {
        // Obtener la selección del spinner
        String select = seleccion.getSelectedItem().toString();

        // Crear el documento PDF
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/report.pdf";
            PdfWriter writer = new PdfWriter(new FileOutputStream(new File(path)));
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            document.add(new Paragraph("Reporte de: " + seleccion));
            // Aquí puedes agregar más contenido al PDF según lo necesites
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generatePdf();
            }
        }
    }

}
