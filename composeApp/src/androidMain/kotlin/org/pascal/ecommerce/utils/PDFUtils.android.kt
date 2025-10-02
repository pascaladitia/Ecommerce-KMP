package org.pascal.ecommerce.utils

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import androidx.core.graphics.scale
import org.pascal.ecommerce.ContextUtils
import org.pascal.ecommerce.data.local.entity.CartEntity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun currentFormattedDate(): String {
    val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
    return sdf.format(Date())
}

actual suspend fun generatePdfAndOpen(
    products: List<CartEntity>,
    reportInfo: ReportInfo,
    fileName: String,
    logoBytes: ByteArray?
) {
    try {
        val context = ContextUtils.context
        val pdfBytes = createReportPdfBytes(
            reportInfo = reportInfo,
            products = products,
            logoBytes = logoBytes
        )

        val docsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
            ?: throw IllegalStateException("DIRECTORY_DOCUMENTS not available")
        val outFile = File(docsDir, fileName)
        FileOutputStream(outFile).use { it.write(pdfBytes) }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            outFile
        )

        val viewIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            clipData = ClipData.newRawUri("PDF", uri)
        }

        val chooser = Intent.createChooser(viewIntent, "Open PDF With:").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        context.startActivity(chooser)

    } catch (e: ActivityNotFoundException) {
        Log.e("Tag report", "PDF Viewer not found")
    } catch (e: Exception) {
        Log.e("Tag report", "Failed open PDF : ${e.message}", e)
    }
}


actual fun createReportPdfBytes(
    reportInfo: ReportInfo,
    products: List<CartEntity>,
    logoBytes: ByteArray?
): ByteArray {
    val doc = PdfDocument()

    val pageWidth = 595
    val pageHeight = 842
    val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val page = doc.startPage(pageInfo)
    val canvas = page.canvas

    val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textSize = 18f
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
    }
    val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 14f
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
    }
    val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 12f
        color = Color.BLACK
        textAlign = Paint.Align.LEFT
    }

    var y = 40f
    val xMargin = 32f
    val lineGap = 18f

    logoBytes?.let {
        val bmp = BitmapFactory.decodeByteArray(it, 0, it.size)
        val scaled = bmp.scale(100, 100)
        canvas.drawBitmap(scaled, xMargin, y, null)
    }

    val titleX = if (logoBytes != null) xMargin + 120f else xMargin
    canvas.drawText("Transaction Report", titleX, y + 30f, titlePaint)

    y += if (logoBytes != null) 120f else 40f

    canvas.drawText("General Information", xMargin, y, labelPaint)
    y += lineGap
    canvas.drawText("Name: ${reportInfo.name ?: "No Name"}", xMargin, y, bodyPaint)
    y += lineGap
    canvas.drawText("Email: ${reportInfo.email ?: "No Email"}", xMargin, y, bodyPaint)
    y += (lineGap + 6f)

    canvas.drawText("Transaction", xMargin, y, labelPaint)
    y += lineGap
    var total = 0L
    products.forEach { item ->
        canvas.drawText("Product Name: ${item.name}", xMargin, y, bodyPaint); y += lineGap
        canvas.drawText("Product Qty: ${item.qty}", xMargin, y, bodyPaint); y += lineGap
        item.price?.let { price ->
            val sub = price.toLong() * (item.qty ?: 0)
            total += sub
            canvas.drawText("Subtotal: $sub", xMargin, y, bodyPaint); y += lineGap
        }
        y += 6f
        if (y > pageHeight - 80) {  }
    }

    y += 10f
    canvas.drawText("Total Amount", xMargin, y, labelPaint)
    y += lineGap
    canvas.drawText(
        total.takeIf { it > 0 }?.toString() ?: "(no price field in CartEntity)",
        xMargin, y, bodyPaint
    )

    doc.finishPage(page)

    val out = ByteArrayOutputStream()
    doc.writeTo(out)
    doc.close()
    return out.toByteArray()
}
