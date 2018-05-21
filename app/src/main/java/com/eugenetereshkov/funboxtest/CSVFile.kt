package com.eugenetereshkov.funboxtest

import com.eugenetereshkov.funboxtest.data.entity.Product
import java.io.InputStream

class CSVFile(private var inputStream: InputStream) {

    fun read(): List<Array<String>> {
        val resultList = mutableListOf<Array<String>>()
        inputStream.bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val row = line.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                resultList.add(row)
            }
        }
        return resultList
    }

    companion object {
        fun convertToProduct(data: List<Array<String>>): List<Product> =
                data.map { Product(it[0], it[1].toFloat(), it[2].toInt()) }.toList()
    }
}
