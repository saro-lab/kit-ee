package me.saro.kit.i18n

import java.io.File
import java.util.*
import java.util.stream.Stream
import kotlin.collections.LinkedHashMap

class I18n private constructor(
    private val map: Map<String, Map<String, String>>,
    private val support: Set<String>,
    private val locales: List<String>
) {
    fun locale(locales: List<String>): I18n = I18n(map, support, locales.filter { support.contains(it) }.ifEmpty { support.toList() })

    fun locale(locale: String): I18n = locale(listOf(locale))

    fun locale(locale: Locale): I18n = locale(listOf(locale.language))

    fun locales(locales: List<Locale>): I18n = locale(locales.map { it.language })

    fun acceptLanguage(acceptLanguage: String): I18n = locale(acceptLanguage.split(';').map { it.trim() })

    operator fun get(code: String): String {
        val node = map[code]
            ?: return code
        return locales.asSequence().map { node[it] }.firstOrNull { it != null } ?: node.values.firstOrNull() ?: code
    }

    companion object {
        private val matches = Regex("^[a-z\\d_]+$", RegexOption.IGNORE_CASE)

        @JvmStatic
        fun load(fileOrDirectory: File): I18n {
            if (!fileOrDirectory.exists() || !fileOrDirectory.canRead()) {
                throw IllegalArgumentException("I18n["+fileOrDirectory.absolutePath+"] file is not exists or not file or not readable")
            }

            if (fileOrDirectory.isFile) {
                return load(fileOrDirectory.readLines(Charsets.UTF_8))
            } else if (fileOrDirectory.isDirectory) {
                val files: Array<File> = fileOrDirectory.listFiles { it: File -> it.isFile && it.canRead() && it.name.endsWith(".yml") } ?: emptyArray()
                if (files.isEmpty()) {
                    throw IllegalArgumentException("I18n["+fileOrDirectory.absolutePath+"] .yml file is not exists in directory")
                }
                return load(files)
            } else {
                throw IllegalArgumentException("I18n["+fileOrDirectory.absolutePath+"] file is not file or directory")
            }
        }

        fun load(files: Array<File>): I18n {
            return load(Stream.of(*files).flatMap { it.readLines(Charsets.UTF_8).stream() })
        }

        @JvmStatic
        fun load(messages: String): I18n {
            return load(messages.lines())
        }

        @JvmStatic
        fun load(messagesLine: List<String>): I18n {
            return load(messagesLine.stream())
        }

        @JvmStatic
        fun load(messagesLine: Stream<String>): I18n {
            return load(messagesLine, emptyList())
        }

        @JvmStatic
        fun load(messagesLine: Stream<String>, supportLocale: List<String>): I18n {

            val map: LinkedHashMap<String, LinkedHashMap<String, String>> = linkedMapOf()
            var node: LinkedHashMap<String, String>? = null

            val locales = mutableMapOf<String, Int>()

            messagesLine
                .filter { it.isNotBlank() }
                .forEach { line: String ->
                    if (!(line[0] == ' ' || line[0] == '\t')) {
                        val nowCode = line.trim()
                        if (!nowCode.endsWith(':')) {
                            throw IllegalArgumentException("I18n code must end with ':' : $nowCode")
                        }
                        val code = nowCode.substring(0, nowCode.length - 1).trim()

                        node = map[code]

                        if (node == null) {
                            node = linkedMapOf()
                            map[code] = node!!
                        }
                        return@forEach
                    }

                    if (node == null) {
                        throw IllegalArgumentException("I18n code is not defined: $line")
                    }

                    val iof = line.indexOf(':')
                    if (iof == -1) {
                        throw IllegalArgumentException("I18n message must have langCode ex) en: hello : $line")
                    }

                    val langCode = line.substring(0, iof).trim().lowercase()
                    val langValue = line.substring(iof + 1).trim()

                    if (langCode.isBlank() || langValue.isBlank()) {
                        return@forEach
                    }

                    node!![langCode] = langValue
                    locales[langCode] = locales.getOrDefault(langCode, 0) + 1
                }

            val support = locales.entries.sortedByDescending { it.value }.map { it.key }
            val defaultLocales = supportLocale.filter { support.contains(it) }.ifEmpty { support }

            return I18n(map.filter { it.value.isNotEmpty() }, support.toSet(), defaultLocales)
        }
    }
}
