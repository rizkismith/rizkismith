package registrasi.pendaftaran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfFormXObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("record");
    DataObj dataObj = new DataObj();
    EditText et_nama, et_nik, et_hp, et_whatsapp, et_nama_usaha, et_jenis_usaha, et_lainnya, et_email, et_jk;
    long invoiceNo = 0;
    Button btn_lanjutkan;
    SimpleDateFormat datePatternFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callFindViewById();
        callOnclickListener();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invoiceNo = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }

    private void callOnclickListener() {
        btn_lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataObj.invoiceNo = invoiceNo +1;
                dataObj.nama = String.valueOf(et_nama.getText());
                dataObj.nik = String.valueOf(et_nik.getText());
                dataObj.noHp = String.valueOf(et_hp.getText());
                dataObj.noWhatsapp = String.valueOf(et_whatsapp.getText());
                dataObj.namaUsaha = String.valueOf(et_nama_usaha.getText());
                dataObj.jenisUsaha = String.valueOf(et_jenis_usaha.getText());
                dataObj.lainnya = String.valueOf(et_lainnya.getText());
                dataObj.email = String.valueOf(et_email.getText());
                dataObj.jenisKelamin = String.valueOf(et_jk.getText());

                if (dataObj.nama.equals("")) {
                    et_nama.setError("Masukkan Nama Anda");
                    et_nama.requestFocus();
                }else if (dataObj.nik.equals("")) {
                    et_nik.setError("Masukkan Nik Anda");
                    et_nik.requestFocus();
                }else if (dataObj.noHp.equals("")) {
                    et_hp.setError("Masukkan No Hp Anda");
                    et_hp.requestFocus();
                }else if (dataObj.noWhatsapp.equals("")) {
                    et_whatsapp.setError("Masukkan No Whatsapp Anda");
                    et_whatsapp.requestFocus();
                }else if (dataObj.namaUsaha.equals("")) {
                    et_nama_usaha.setError("Masukkan Nama Usaha Anda");
                    et_nama_usaha.requestFocus();
                }else if (dataObj.jenisUsaha.equals("")) {
                    et_jenis_usaha.setError("Masukkan Jenis Usaha Anda");
                    et_jenis_usaha.requestFocus();
                }else if (dataObj.lainnya.equals("")) {
                    et_lainnya.setError("Masukkan Lainnya");
                    et_lainnya.requestFocus();
                }else if (dataObj.email.equals("")) {
                    et_email.setError("Masukkan Email Anda");
                    et_email.requestFocus();
                }else if (dataObj.jenisKelamin.equals("")) {
                    et_jk.setError("Masukkan Jenis Kelamin Anda");
                    et_jk.requestFocus();
                } else{
                    myRef.child(String.valueOf(invoiceNo+1)).setValue(dataObj);
                }
                printPDF();
            }        });
    }

    private void printPDF() {
        PdfDocument myPdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250, 350, 1).create();
        PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
        Canvas canvas = myPage.getCanvas();

        paint.setTextSize(15.5f);
        paint.setColor(Color.rgb(0,50,250));

        canvas.drawText("Pendaftaran Peserta", 20,20, paint);
        paint.setTextSize(8.5f);

        canvas.drawText("NAMA",20,40, paint);
        canvas.drawText(":",90,40, paint);
        canvas.drawText("" +et_nama.getText(),100,40, paint);

        canvas.drawText("NIK",20,55, paint);
        canvas.drawText(":",90,55, paint);
        canvas.drawText("" +et_nik.getText(),100,55, paint);

        canvas.drawText("No Hp",20,70, paint);
        canvas.drawText(":",90,70, paint);
        canvas.drawText("" +et_hp.getText(),100,70, paint);

        canvas.drawText("No Whatsapp",20,85, paint);
        canvas.drawText(":",90,85, paint);
        canvas.drawText("" +et_whatsapp.getText(),100,85, paint);

        canvas.drawText("Nama Usaha",20,100, paint);
        canvas.drawText(":",90,100, paint);
        canvas.drawText("" +et_nama_usaha.getText(),100,100, paint);

        canvas.drawText("Jenis Usaha",20,115, paint);
        canvas.drawText(":",90,115, paint);
        canvas.drawText("" +et_jenis_usaha.getText(),100,115, paint);

        canvas.drawText("Lainnya",20,130, paint);
        canvas.drawText(":",90,130, paint);
        canvas.drawText("" +et_lainnya.getText(),100,130, paint);

        canvas.drawText("Email",20,145, paint);
        canvas.drawText(":",90,145, paint);
        canvas.drawText("" +et_email.getText(),100,145, paint);

        canvas.drawText("Jenis Kelamin",20,160, paint);
        canvas.drawText(":",90,160, paint);
        canvas.drawText("" +et_jk.getText(),100,160, paint);

        canvas.drawText("Date: "+datePatternFormat.format(new Date().getTime()),20, 260, paint);

        myPdfDocument.finishPage(myPage);
        File file = new File(this.getExternalFilesDir("/"),"Peserta.pdf");

        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e){
            e.printStackTrace();
        }
        
        myPdfDocument.close();
    }

    private void callFindViewById() {
        et_nama = findViewById(R.id.et_nama);
        et_nik = findViewById(R.id.et_nik);
        et_hp = findViewById(R.id.et_hp);
        et_whatsapp = findViewById(R.id.et_whatsapp);
        et_nama_usaha = findViewById(R.id.et_nama_usaha);
        et_jenis_usaha = findViewById(R.id.et_jenis_usaha);
        et_lainnya = findViewById(R.id.et_lainnya);
        et_email = findViewById(R.id.et_email);
        et_jk = findViewById(R.id.et_jk);
        btn_lanjutkan = findViewById(R.id.btn_lanjutkan);

    }

}
