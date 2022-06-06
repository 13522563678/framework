package com.kcwl.framework.utils;



import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;



/**
 * @author 姚华成
 * @date 2017-10-16
 */
public final class NetworkUtil {

    private NetworkUtil() {
    }

    public static String getHostIp() {
        return getHostIpV4();
    }

    public static String getHostIpV4() {
        try {
            return getHostIpV4Address().getHostAddress();
        } catch (SocketException e) {
            return "127.0.0.1";
        }
    }

    public static String getHostIpV6() {
        try {
            return getHostIpV6Address().getHostAddress();
        } catch (SocketException e) {
            return "unknown";
        }
    }

    public static Inet4Address getHostIpV4Address() throws SocketException {
        return (Inet4Address) getHostIpAddress(Inet4Address.class);
    }

    public static Inet6Address getHostIpV6Address() throws SocketException {
        return (Inet6Address) getHostIpAddress(Inet6Address.class);
    }

    private static <T extends InetAddress> InetAddress getHostIpAddress(Class<T> ipType) throws SocketException {
        return getHostIpAddress(new AddressSelectionCondition() {
            @Override
            public boolean isAcceptableAddress(InetAddress address) {
                return address.getClass().equals(ipType);
            }
        });
    }

    /**
     * 获取程序主机的IP地址，能够解决多网卡、虚拟网卡、loopback设备等问题。<P>
     * 这是从HBase的Addressing.Java中复制的方法，
     *
     * @param condition
     * @return
     * @throws SocketException
     */
    public static InetAddress getHostIpAddress(AddressSelectionCondition condition) throws
            SocketException {
        // Before we connect somewhere, we cannot be sure about what we'd be bound to; however,
        // we only connect when the message where client ID is, is long constructed. Thus,
        // just use whichever IP address we can find.
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        if (interfaces != null) {
            while (interfaces.hasMoreElements()) {
                NetworkInterface current = interfaces.nextElement();
                if (!current.isUp() || current.isLoopback() || current.isVirtual()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = current.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr.isLoopbackAddress()) {
                        continue;
                    }
                    if (condition.isAcceptableAddress(addr)) {
                        return addr;
                    }
                }
            }
        }
        throw new SocketException("Can't get our ip address, interfaces are: " + interfaces);
    }

    public interface AddressSelectionCondition {
        /**
         * 判断是否是能够接受的网络地址
         *
         * @param address 网络地址
         * @return 是否
         */
        boolean isAcceptableAddress(InetAddress address);
    }
}
