@file:OptIn(ExperimentalForeignApi::class)

package org.pascal.ecommerce.utils

import kotlinx.cinterop.*
import org.pascal.ecommerce.data.local.entity.CartEntity
import platform.CoreGraphics.*
import platform.Foundation.*
import platform.UIKit.*

actual fun currentFormattedDate(): String {
    val formatter = NSDateFormatter().apply {
        dateFormat = "yyyyMMdd_HHmmss"
        locale = NSLocale.currentLocale
    }
    return formatter.stringFromDate(NSDate())
}

actual suspend fun generatePdfAndOpen(
    products: List<CartEntity>,
    reportInfo: ReportInfo,
    fileName: String,
    logoBytes: ByteArray?
) {
    val pdfData = createReportPdfBytes(reportInfo, products, logoBytes)

    val tempUrl = NSURL.fileURLWithPath("${NSTemporaryDirectory()}$fileName")
    (pdfData as NSData).writeToURL(tempUrl, true)

    val activityVC = UIActivityViewController(
        activityItems = listOf(tempUrl),
        applicationActivities = null
    )
    activityVC.popoverPresentationController?.sourceView =
        topViewController()?.view

    topViewController()?.presentViewController(activityVC, animated = true, completion = null)
}

actual fun createReportPdfBytes(
    reportInfo: ReportInfo,
    products: List<CartEntity>,
    logoBytes: ByteArray?
): ByteArray {
    val data = NSMutableData()
    memScoped {
        UIGraphicsBeginPDFContextToData(data, CGRectZero.readValue(), null)
        UIGraphicsBeginPDFPage()

        var y = 40.0
        val x = 32.0
        val lineGap = 18.0

        logoBytes?.let {
            val nsData = it.toNSData()
            val img = UIImage(data = nsData)
            img.drawInRect(CGRectMake(x, y, 100.0, 100.0))
        }

        val titleAttrs: Map<Any?, *>? = mapOf(
            NSFontAttributeName to UIFont.boldSystemFontOfSize(18.0)
        )
        val titleX = if (logoBytes != null) x + 120.0 else x
        ("Transaction Report" as NSString)
            .drawAtPoint(CGPointMake(titleX, y + 30.0), withAttributes = titleAttrs)

        y += if (logoBytes != null) 120.0 else 40.0

        val labelAttrs: Map<Any?, *>? = mapOf(
            NSFontAttributeName to UIFont.boldSystemFontOfSize(14.0)
        )
        val bodyAttrs: Map<Any?, *>? = mapOf(
            NSFontAttributeName to UIFont.systemFontOfSize(12.0)
        )

        ("General Information" as NSString).drawAtPoint(CGPointMake(x, y), withAttributes = labelAttrs); y += lineGap
        ("Name: ${reportInfo.name ?: "No Name"}" as NSString).drawAtPoint(CGPointMake(x, y), withAttributes = bodyAttrs); y += lineGap
        ("Email: ${reportInfo.email ?: "No Email"}" as NSString).drawAtPoint(CGPointMake(x, y), withAttributes = bodyAttrs); y += (lineGap + 6.0)

        ("Transaction" as NSString).drawAtPoint(CGPointMake(x, y), withAttributes = labelAttrs); y += lineGap
        var total = 0L
        products.forEach { item ->
            ("Product Name: ${item.name}" as NSString).drawAtPoint(CGPointMake(x, y), withAttributes = bodyAttrs); y += lineGap
            ("Product Qty: ${item.qty}" as NSString).drawAtPoint(CGPointMake(x, y), withAttributes = bodyAttrs); y += lineGap
            item.price?.let { price ->
                val sub = price.toLong() * (item.qty ?: 0)
                total += sub
                ("Subtotal: $sub" as NSString).drawAtPoint(CGPointMake(x, y), withAttributes = bodyAttrs); y += lineGap
            }
            y += 6.0
        }

        y += 10.0
        ("Total Amount" as NSString).drawAtPoint(CGPointMake(x, y), withAttributes = labelAttrs); y += lineGap
        val totalText = if (total > 0) total.toString() else "(no price field in CartEntity)"
        (totalText as NSString).drawAtPoint(CGPointMake(x, y), withAttributes = bodyAttrs)

        UIGraphicsEndPDFContext()
    }
    return data.toByteArray()
}

private fun topViewController(controller: UIViewController? = UIApplication.sharedApplication.keyWindow?.rootViewController): UIViewController? {
    var c = controller
    while (c?.presentedViewController != null) {
        c = c.presentedViewController
    }
    return c
}

private fun NSMutableData.toByteArray(): ByteArray =
    ByteArray(this.length.toInt()).also { arr ->
        memScoped {
            this@toByteArray.getBytes(
                buffer = arr.refTo(0) as COpaquePointer?,
                length = this@toByteArray.length
            )
        }
    }




