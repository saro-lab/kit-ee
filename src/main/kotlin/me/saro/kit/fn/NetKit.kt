package me.saro.kit.fn

import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress

class NetKit {
    companion object {
        @JvmStatic
        fun isIPv4(inet: InetAddress): Boolean =
            inet is Inet4Address || (inet is Inet6Address && inet.isIPv4CompatibleAddress)

        @JvmStatic
        fun toString(inet: InetAddress): String {
            if (inet is Inet6Address) {
                if (inet.isIPv4CompatibleAddress) {
                    return Inet4Address.getByAddress(inet.address.sliceArray(12..15)).hostAddress
                } else {
                    return inet.hostAddress
                }
            }
            return inet.hostAddress
        }
    }
}